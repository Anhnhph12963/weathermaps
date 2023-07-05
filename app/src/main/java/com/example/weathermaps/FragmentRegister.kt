package com.example.weathermaps

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.weathermaps.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FragmentRegister : Fragment() {
    private lateinit var auth: FirebaseAuth

    private var binding: FragmentRegisterBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        onClickView()

    }

    private fun onClickView() {
        binding?.apply {
            btnRegister.setOnClickListener { view ->
                if (edtUsername.text.isNullOrEmpty()) {
                    edtUsername.error = "Bạn cần nhập Username"
                } else if (edtPassword.text.isNullOrEmpty()) {
                    edtPassword.error = "Bạn cần nhập Pass"
                } else if (edtPassword.text!!.length < 5) {
                    edtPassword.error = "Mật khẩu phải có 6 kí tự"
                } else if (edtConfirmPassword.text?.equals(edtPassword.text.toString()) == true) {
                    edtConfirmPassword.error = "Mật khẩu không trùng lặp"
                } else {
                    auth.createUserWithEmailAndPassword(
                        edtUsername.text.toString(),
                        edtPassword.text.toString()
                    )
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                sendEmailVerification()
                                findNavController().navigate(R.id.fragmentLogin)
                                Toast.makeText(
                                    requireContext(),
                                    "dang ky thanh cong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    requireContext(),
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()

                            }
                        }
                }
            }
            btnExit.setOnClickListener { view ->
                findNavController().popBackStack()
            }


        }
    }

    private fun sendEmailVerification() {
        //get instance of firebase auth
        val firebaseAuth = FirebaseAuth.getInstance()
        //get current user
        val firebaseUser = firebaseAuth.currentUser

        //send email verification
        firebaseUser!!.sendEmailVerification()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Đã gửi verified", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Failed to send due to " + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}