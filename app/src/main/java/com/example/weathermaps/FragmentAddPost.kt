package com.example.weathermaps

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermaps.adapter.PostsAddAdapter
import com.example.weathermaps.databinding.FragmentAddPostBinding
import com.example.weathermaps.databinding.ItemRycDetailImageBinding
import com.example.weathermaps.model.PostModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.UploadTask.TaskSnapshot
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


class FragmentAddPost : Fragment() {

    private var binding: FragmentAddPostBinding? = null
  //  private lateinit var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<PostModel, PostsAddAdapter>
    private lateinit var firebaseRecyclerOptions: FirebaseRecyclerOptions<PostModel>
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var uri: Uri
    private lateinit var itemRycDetailImageBinding: ItemRycDetailImageBinding

    private lateinit var date: Date
    private lateinit var simpleDateFormat: SimpleDateFormat


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        databaseReference = FirebaseDatabase.getInstance().reference.child("User")
        databaseReference = FirebaseDatabase.getInstance().reference.child("Posts")
        databaseReference = FirebaseDatabase.getInstance().getReference("Comment")
        storageReference = FirebaseStorage.getInstance().reference.child("PostImage")
        recyclerView.layoutManager.toString()


     //  AddPost()
    }

//    override fun onStart() {
//        super.onStart()
//        if (firebaseUser == null)
//            findNavController().navigate(R.id.fragmentLogin)
//        else {
//            databaseReference.child(firebaseUser.uid)
//                .addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        if (snapshot.exists()) {
//                            val usernameV = snapshot.child("username").value.toString()
//
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Toast.makeText(requireContext(), "Lỗi", Toast.LENGTH_SHORT).show()
//                    }
//                })
//        }
//    }
//
//    private fun LoadPost() {
//        firebaseRecyclerOptions = FirebaseRecyclerOptions.Builder<PostModel>()
//            .setQuery(databaseReference, PostModel::class.java)
//            .build()
//        firebaseRecyclerAdapter =
//            object : FirebaseRecyclerAdapter<PostModel, PostsAddAdapter>(firebaseRecyclerOptions) {
//                override fun onCreateViewHolder(
//                    parent: ViewGroup,
//                    viewType: Int
//                ): PostsAddAdapter {
//                    TODO("Not yet implemented")
//                }
//
//            }
//
//    }
//
//    private fun AddPost() {
//        binding?.apply {
//            btnSave.apply {
//                if (edtTitle.text.isNullOrEmpty())
//                    Toast.makeText(
//                        requireContext(),
//                        "vui lòng nhập dữ liệu",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                else if (edtContent.text.isNullOrEmpty())
//                    Toast.makeText(
//                        requireContext(),
//                        "vui lòng nhập dữ liệu",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                else if (spnLocation.isSelected)
//                    Toast.makeText(
//                        requireContext(),
//                        "vui lòng chọn địa điểm",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                else if (spnDate.isSelected)
//                    Toast.makeText(
//                        requireContext(),
//                        "vui lòng chọn thời gian",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                else if (rycImage.isSelected)
//                    Toast.makeText(
//                        requireContext(),
//                        "vui lòng chọn hình ảnh",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                else {
//                    Toast.makeText(requireContext(), "thanh cong", Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(R.id.fragmentPost)
//                }
//                val date = Date()
//                val simpleDateFormat = SimpleDateFormat("dd-MM-yyy hh:mm:ss")
//                val strDate = simpleDateFormat.format(date)
//
//                storageReference.child(firebaseUser.uid + strDate).putFile(uri)
//                    .addOnCompleteListener() {
//                        fun onComplete(task: Task<UploadTask.TaskSnapshot?>) {
//                            if (task.isSuccessful) {
//                                storageReference.child(firebaseUser.uid + strDate).downloadUrl.addOnSuccessListener { uri ->
//                                    val hashMap: HashMap<*, *> = HashMap<Any?, Any?>()
//                                    hashMap["datePost"] = strDate
//                                    hashMap["postImage"] = uri.toString()
//                                    hashMap["postTitle"] = PostTitle
//                                    hashMap["userProfile"] = ImageUrlV
//                                    hashMap["userName"] = usernameV
//                                    databasePost.child(firebaseUser.uid + strDate).setValue(hashMap)
//                                        .addOnCompleteListener(object :
//                                            OnCompleteListener<Any?> {
//                                            fun onComplete(task: Task<*>) {
//                                                if (task.isSuccessful) {
//                                                    progressDialog.dismiss()
//                                                    Toast.makeText(
//                                                        this@MainActivity,
//                                                        "Đăng lên thành công",
//                                                        Toast.LENGTH_SHORT
//                                                    ).show()
//                                                    img_post.setImageResource(R.drawable.ic_image)
//                                                    edt_post.setText("")
//                                                } else {
//                                                    progressDialog.dismiss()
//                                                    Toast.makeText(
//                                                        this@MainActivity,
//                                                        "" + task.exception.toString(),
//                                                        Toast.LENGTH_SHORT
//                                                    ).show()
//                                                }
//                                            }
//                                        })
//                                }
//                            } else {
//                                progressDialog.dismiss()
//                                Toast.makeText(
//                                    this@MainActivity,
//                                    "" + task.exception.toString(),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    }
//            }
//        }
//
//        private fun caculateTimeAgo(datePost: String): String? {
//            val sdf = SimpleDateFormat("dd-MM-yyy hh:mm:ss")
//            sdf.timeZone = TimeZone.getTimeZone("GMT")
//            try {
//                val time = sdf.parse(datePost).time
//                val now = System.currentTimeMillis()
//                val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
//                return ago.toString() + ""
//            } catch (e: ParseException) {
//                e.printStackTrace()
//            }
//            return ""
//        }
//
//        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//            super.onActivityResult(requestCode, resultCode, data)
//            if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null)
//                uri = data.data!!
//            //  imgDetailImage.setImageURI(uri)
//        }
//    }
}