package com.example.weathermaps

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.hotspot2.pps.Credential
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weathermaps.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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

class FragmentLogin : Fragment() {
    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInAccount: GoogleSignInAccount

    //    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var googleAuthProvider: GoogleAuthProvider
    private lateinit var credential: Credential
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var googleSignInClient: GoogleSignInClient

    private var binding: FragmentLoginBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().getReference("User")

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
                                    databaseReference.child(auth.uid.toString())
                                        .addValueEventListener(object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                if (!snapshot.exists()) {
                                                    var bundle = Bundle()
                                                    bundle.putString("imageLink","https://img.freepik.com/free-photo/white-cloud-blue-sky_74190-7709.jpg")
                                                    bundle.putString("name", "Name")
                                                    findNavController().navigate(R.id.fragmentProfile2, bundle)
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Tạo tài khoản thành công",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    findNavController().navigate(R.id.blankFragment)
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Đăng nhập thành công",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                            }
                                        })
                                } else {
                                    Toast.makeText(requireContext(), "Bạn chưa check verified trong mail", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(
                                    requireContext(),
                                    "Thông tin tài khoản không đúng !",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                }
            }

            btnDk.setOnClickListener {
                findNavController().navigate(R.id.fragmentLogin)
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
            googleSignInClient.signOut();
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

    private fun updateUI(googleSignInAccount: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "login success ", Toast.LENGTH_SHORT).show()

                databaseReference.child(auth.uid.toString())
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                var bundle = Bundle()
                                val url = googleSignInAccount.photoUrl.toString()
                                bundle.putString("imageLink", url)
                                bundle.putString("name", googleSignInAccount.displayName)
                                findNavController().navigate(R.id.action_fragmentLogin_to_fragmentProfile2, bundle)
                                Toast.makeText(
                                    requireContext(),
                                    "Tạo tài khoản thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                findNavController().navigate(R.id.mapsFragment)
                                Toast.makeText(
                                    requireContext(),
                                    "Đăng nhập thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            } else {
                Toast.makeText(
                    requireContext(),
                    "Đăng nhập thất bại ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}

