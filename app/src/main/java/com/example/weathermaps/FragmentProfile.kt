package com.example.weathermaps

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.weathermaps.databinding.FragmentProfileBinding
import com.example.weathermaps.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

class FragmentProfile : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    lateinit var storageReference: StorageReference
    private var progressDialog: ProgressDialog? = null
    private var linkImage: String =
        "https://img.freepik.com/free-photo/white-cloud-blue-sky_74190-7709.jpg"
    private var binding: FragmentProfileBinding? = null
    private var imageUri: Intent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        progressDialog = ProgressDialog(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference.child(/* pathString = */ "User")
        storageReference = FirebaseStorage.getInstance().reference.child("image")
        binding?.apply {
            databaseReference.child(firebaseUser.uid).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val userModel = it.result.getValue(User::class.java)
                    if (userModel != null) {
                        Picasso.get().load(userModel.profileImage).fit().into(imgProfile)
                        edtUsername.setText(userModel.username)
                        edtLocation.setText(userModel.location)
                        edtOld.setText(userModel.old)
                        edtPhone.setText(userModel.phone)
                    }
                }
            }
            Picasso.get().load(linkImage).fit().into(imgProfile)
            imgProfile.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                launcher.launch(intent)
            }
            btnSave.setOnClickListener {
                SaveData()
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                imageUri = result.data
                binding?.imgProfile?.setImageURI(imageUri?.data)
                storageReference.child(firebaseUser.uid).putFile(imageUri?.data!!)
                    .addOnCompleteListener(object :
                            OnCompletionListener,
                            OnCompleteListener<UploadTask.TaskSnapshot> {
                            override fun onCompletion(mp: MediaPlayer?) {
                                TODO("Not yet implemented")
                            }

                            override fun onComplete(task: Task<UploadTask.TaskSnapshot>) {
                                if (task.isSuccessful) {
                                    storageReference.child(firebaseUser.uid).downloadUrl.addOnCompleteListener {
                                        linkImage = it.result.toString()
                                    }
                                }
                            }
                        })
            }
        }

    private fun SaveData() {
        binding?.apply {
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
                progressDialog?.setTitle("add setup profile")
                progressDialog?.setCanceledOnTouchOutside(false)
                progressDialog?.show()

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
                            progressDialog?.dismiss()
                            Toast.makeText(
                                requireContext(),
                                "Setup profile complete",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }.addOnFailureListener { e ->
                        progressDialog?.dismiss()
                        Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
