package com.example.weathermaps

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weathermaps.databinding.DialogUpdateProfileBinding
import com.example.weathermaps.databinding.FragmentLoginBinding
import com.example.weathermaps.model.User
import com.example.weathermaps.model.UserSave
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

class FragmentLogin : Fragment() {
    private var PREFERENCE_FILE_KEY = "SavePreference"
    private lateinit var auth: FirebaseAuth
    private var linkImage: String =
        "https://img.freepik.com/free-photo/white-cloud-blue-sky_74190-7709.jpg"
    private var nameUser = ""
    private lateinit var storageReference: StorageReference
    private var imageview: CircleImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    var imageUri: Intent ? = null
    private var binding: FragmentLoginBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        storageReference = FirebaseStorage.getInstance().reference.child("image")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        onClickView()
    }

    private fun onClickView() {
        binding?.apply {
            btnLogin.setOnClickListener { view ->
                if (edtUsername.text.isNullOrEmpty()) {
                    edtUsername.error = "Bạn cần nhập Username"
                } else if (edtPassword.text.isNullOrEmpty()) {
                    edtPassword.error = "Bạn cần nhập Pass"
                } else {
                    auth.signInWithEmailAndPassword(
                        edtUsername.text.toString(),
                        edtPassword.text.toString()
                    )
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                if (auth.currentUser!!.isEmailVerified) {
                                    checkInformationUser()
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Bạn chưa check verified trong mail",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(
                                    requireContext(),
                                    "Thông tin tài khoản không đúng !",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }

            btnDk.setOnClickListener {
                findNavController().navigate(R.id.fragmentRegister)
            }
            imgGoogle.setOnClickListener {
                signInGoogle()
            }
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun signInGoogle() {
        binding.apply {
            googleSignInClient.signOut()
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }
    private fun checkInformationUser() {
        databaseReference.child(auth.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        showAlertDialogButtonClicked()
                    } else {
                        view?.post {
                            val userGet: User = snapshot.getValue(User::class.java)!!
                            val sharedPref: SharedPreferences = context!!.getSharedPreferences(PREFERENCE_FILE_KEY, MODE_PRIVATE)
                            val user = UserSave(username = userGet.username, imageLink = userGet.profileImage, sharedPref)
                            user.Save()
                            findNavController().navigate(R.id.action_fragmentLogin_to_blankFragment)
                            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun showAlertDialogButtonClicked() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Thêm thông tin !")
        val bindingAlert: DialogUpdateProfileBinding =
            DialogUpdateProfileBinding.inflate(LayoutInflater.from(context))
        builder.setView(bindingAlert.root)
        val dialog: AlertDialog = builder.create()
        bindingAlert.edtUsername.setText(nameUser)
        Picasso.get().load(linkImage).fit().into(bindingAlert.imgProfile)
        imageview = bindingAlert.imgProfile
        bindingAlert.imgProfile.setOnClickListener { viewImg ->
            choosePhotoFromGallary()
        }
        bindingAlert.btnSave.setOnClickListener {
            addInformationUser(
                linkImage = linkImage,
                bindingAlert.edtUsername,
                bindingAlert.edtOld,
                bindingAlert.edtLocation,
                bindingAlert.edtPhone
            )
            dialog.dismiss()
        }
        bindingAlert.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun updateUI(googleSignInAccount: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "login success ", Toast.LENGTH_SHORT).show()
                linkImage = googleSignInAccount.photoUrl.toString()
                nameUser = googleSignInAccount.displayName.toString()
                checkInformationUser()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Đăng nhập thất bại ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("IntentReset")
    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY)
    }
    private fun saveImage(data: Intent?) {
        storageReference.child(auth.uid.toString()).putFile(data?.data!!)
            .addOnCompleteListener(object :
                    MediaPlayer.OnCompletionListener,
                    OnCompleteListener<UploadTask.TaskSnapshot> {
                    override fun onCompletion(mp: MediaPlayer?) {
                    }

                    override fun onComplete(task: Task<UploadTask.TaskSnapshot>) {
                        if (task.isSuccessful) {
                            storageReference.child(auth.uid.toString()).downloadUrl.addOnCompleteListener {
                                linkImage = it.result.toString()
                            }
                        }
                    }
                })
    }
    private fun addInformationUser(
        linkImage: String,
        edtUsername: EditText,
        edtOld: EditText,
        edtLocation: EditText,
        edtPhone: EditText
    ) {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val username = edtUsername.text.toString()
        val old = edtOld.text.toString()
        val location = edtLocation.text.toString()
        val phone = edtPhone.text.toString()

        if (username.isEmpty() || username.length < 3) {
            edtUsername.error = " không hợp lệ"
        } else if (old.isEmpty()) {
            edtOld.error = "không được để trống"
        } else if (location.isEmpty()) {
            edtLocation.error = "không được để trống"
        } else if (phone.isEmpty() || phone.length <= 9) {
            edtPhone.error = "không được hợp lệ"
        } else if (linkImage.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT)
                .show()
        } else {
            val hashMap = HashMap<String, Any>()
            hashMap.put("username", username)
            hashMap.put("old", old)
            hashMap.put("location", location)
            hashMap.put("phone", phone)
            hashMap.put("profileImage", linkImage)
            hashMap.put("status", "offline")
            databaseReference.child(firebaseUser.uid).setValue(hashMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        view?.post {
                            val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREFERENCE_FILE_KEY, MODE_PRIVATE)
                            val user = UserSave(username = username, imageLink = linkImage, sharedPref)
                            user.Save()
                            findNavController().navigate(R.id.action_fragmentLogin_to_blankFragment)
                            Toast.makeText(
                                context,
                                "Setup profile complete",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Setup profile fail",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                try {
                    saveImage(data = data)
                    Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
                    imageview!!.setImageURI(data.data)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == CAMERA) {
                imageUri = data
                Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
