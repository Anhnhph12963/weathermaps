package com.example.weathermaps

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermaps.databinding.FragmentAddPostBinding
import com.example.weathermaps.model.PostModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class FragmentAddPost : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var binding: FragmentAddPostBinding? = null
    private lateinit var firebaseRecyclerOptions: FirebaseRecyclerOptions<PostModel>
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReferencePost: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var uri: Uri
    private lateinit var calendar: Calendar
 //   private lateinit var progressDialog: ProgressDialog

    private var linkImage: String = ""
    private var latLngArrayList = mutableListOf<LatLng>()
    private var locationNameArraylist = mutableListOf<String>()


    private lateinit var date: Date
    private lateinit var simpleDateFormat: SimpleDateFormat

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0


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
        databaseReference = FirebaseDatabase.getInstance().reference.child("User")
        databaseReferencePost = FirebaseDatabase.getInstance().reference.child("Posts")
        storageReference = FirebaseStorage.getInstance().reference.child("image")
        diadiem()
    }

    private fun diadiem() {

        var thanhxuan = LatLng(20.996002041937803, 105.8097114468855)
        var caugiay = LatLng(21.036958484112965, 105.79056623615422)
        var hadong = LatLng(20.95724543050617, 105.75632759998462)
        var mydinh = LatLng(21.02353706410797, 105.77322652116061)
        var thanhxuan1 = LatLng(21.01415087541889, 105.74054497999343)
        var caugiay1 = LatLng(21.011666998362294, 105.7452659889851)
        var hadong1 = LatLng(21.006659048378612, 105.74470811982496)
        var mydinh1 = LatLng(20.99836560774121, 105.73676918002924)
        var thanhxuan2 = LatLng(21.00297269601158, 105.73316401537109)
        var caugiay2 = LatLng(20.999487182804028, 105.7337221918186)
        var hadong2 = LatLng(20.994799623547337, 105.73316465907484)
        var mydinh2 = LatLng(20.98805745970365, 105.76109438711876)
        var thanhxuan3 = LatLng(21.017835495765652, 105.78426644792702)
        var caugiay3 = LatLng(21.007325463847934, 105.77885067545006)
        var hadong3 = LatLng(21.041013043984737, 105.79472862600348)
        var mydinh3 = LatLng(21.031783713413795, 105.80584405810978)
        var thanhxuan4 = LatLng(21.037307833187484, 105.7953783954595)
        var caugiay4 = LatLng(21.027808838487825, 105.7911204871416)
        var hadong4 = LatLng(21.005643375787688, 105.79783263179253)
        var mydinh4 = LatLng(21.03582494708833, 105.83659092065274)
        var thanhxuan5 = LatLng(21.028078120798806, 105.83615869624975)
        var caugiay5 = LatLng(21.02410457222415, 105.85795366275453)
        var hadong5 = LatLng(20.98148428702097, 105.87182773449652)
        var mydinh5 = LatLng(20.956608715699733, 105.8883628794604)
        var thanhxuan6 = LatLng(21.13360891560745, 105.77364318540234)
        var caugiay6 = LatLng(20.990760029737444, 105.83722413137573)
        var hadong6 = LatLng(20.961577927780898, 105.87286745032998)
        var mydinh6 = LatLng(21.01744074505883, 105.81960346072589)
        var thanhxuan7 = LatLng(21.055973124862845, 105.79780763130194)
        var caugiay7 = LatLng(21.064662760505943, 105.78835176658349)
        var hadong7 = LatLng(21.072341445280227, 105.7739856630961)
        var mydinh7 = LatLng(21.05260424176765, 105.77796431658712)

        latLngArrayList = mutableListOf<LatLng>()
        locationNameArraylist = mutableListOf<String>()
        latLngArrayList.add(thanhxuan)
        locationNameArraylist.add("Thanh xuân")
        latLngArrayList.add(caugiay)
        locationNameArraylist.add("Cầu Giấy")
        latLngArrayList.add(hadong)
        locationNameArraylist.add("Hà Đông")
        latLngArrayList.add(mydinh)
        locationNameArraylist.add("Mỹ Đình")
        latLngArrayList.add(thanhxuan1)
        locationNameArraylist.add("Thanh xuân")
        latLngArrayList.add(caugiay1)
        locationNameArraylist.add("Cầu Giấy")
        latLngArrayList.add(hadong1)
        locationNameArraylist.add("Hà Đông")
        latLngArrayList.add(mydinh1)
        locationNameArraylist.add("Mỹ Đình")
        latLngArrayList.add(thanhxuan2)
        locationNameArraylist.add("Thanh xuân")
        latLngArrayList.add(caugiay2)
        locationNameArraylist.add("Cầu Giấy")
        latLngArrayList.add(hadong2)
        locationNameArraylist.add("Hà Đông")
        latLngArrayList.add(mydinh2)
        locationNameArraylist.add("Mỹ Đình")
        latLngArrayList.add(thanhxuan3)
        locationNameArraylist.add("Thanh xuân")
        latLngArrayList.add(caugiay3)
        locationNameArraylist.add("Cầu Giấy")
        latLngArrayList.add(hadong3)
        locationNameArraylist.add("Hà Đông")
        latLngArrayList.add(mydinh3)
        locationNameArraylist.add("Mỹ Đình")
        latLngArrayList.add(thanhxuan4)
        locationNameArraylist.add("Thanh xuân")
        latLngArrayList.add(caugiay4)
        locationNameArraylist.add("Cầu Giấy")
        latLngArrayList.add(hadong4)
        locationNameArraylist.add("Hà Đông")
        latLngArrayList.add(mydinh4)
        locationNameArraylist.add("Mỹ Đình")
        latLngArrayList.add(thanhxuan5)
        locationNameArraylist.add("Thanh xuân")
        latLngArrayList.add(caugiay5)
        locationNameArraylist.add("Cầu Giấy")
        latLngArrayList.add(hadong5)
        locationNameArraylist.add("Hà Đông")
        latLngArrayList.add(mydinh5)
        locationNameArraylist.add("Mỹ Đình")
        latLngArrayList.add(thanhxuan6)
        locationNameArraylist.add("Thanh xuân")
        latLngArrayList.add(caugiay6)
        locationNameArraylist.add("Cầu Giấy")
        latLngArrayList.add(hadong6)
        locationNameArraylist.add("Hà Đông")
        latLngArrayList.add(mydinh6)
        locationNameArraylist.add("Mỹ Đình")
        latLngArrayList.add(thanhxuan7)
        locationNameArraylist.add("Thanh xuân")
        latLngArrayList.add(caugiay7)
        locationNameArraylist.add("Cầu Giấy")
        latLngArrayList.add(hadong7)
        locationNameArraylist.add("Hà Đông")
        latLngArrayList.add(mydinh7)
        locationNameArraylist.add("Mỹ Đình")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            locationNameArraylist
        )
        binding?.spnLocation?.adapter = adapter
        onclickTime()
        binding?.imgAddimage?.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launcher.launch(intent)
        }
            SaveData()


    }
    //postmodel   -  gettitile,getcontent,getlocation,gettime(calander),getimage(listimage)(string,)r
    private fun SaveData() {
        binding?.apply {
            btnSave.setOnClickListener {
                if (edtTitle.text.isNullOrEmpty()) {
                    edtTitle.error = "NULL"
                } else if (edtContent.text.isNullOrEmpty()) {
                    edtContent.error = "NULL"
                } else {
                    val hashMap = HashMap<String, Any>()
                    hashMap.put("title", edtTitle.text.toString())
                    hashMap.put("location", spnLocation.selectedItem.toString())
                    hashMap.put("date", spnDate.text.toString())
                    hashMap.put("imagePost", linkImage)
                    databaseReferencePost.push().setValue(hashMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                            //    progressDialog?.dismiss()
                                Toast.makeText(
                                    requireContext(),
                                    "Setup profile complete",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }.addOnFailureListener { e ->
                       //     progressDialog?.dismiss()
                            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }


                }
            }
        }
    }


    private fun onclickTime() {
        binding?.apply {
            spnDate.setOnClickListener {
                val calendar: Calendar = Calendar.getInstance()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(requireContext(), this@FragmentAddPost, year, month, day)
                datePickerDialog.show()
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireContext(), this@FragmentAddPost, hour, minute,
            DateFormat.is24HourFormat(requireContext())
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        binding?.spnDate?.text =
            "Year:  $myYear  Month: $myMonth  Day: $myDay  Hour: $myHour Minute: $myMinute"
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                binding?.imgAddimage?.setImageURI(result.data?.data)
                storageReference.child(firebaseAuth.uid!!).putFile(result.data?.data!!)
                    .addOnCompleteListener(object :
                        MediaPlayer.OnCompletionListener,
                        OnCompleteListener<UploadTask.TaskSnapshot> {
                        override fun onCompletion(mp: MediaPlayer?) {
                            TODO("Not yet implemented")
                        }

                        override fun onComplete(task: Task<UploadTask.TaskSnapshot>) {
                            if (task.isSuccessful) {
                                storageReference.child(firebaseAuth.currentUser?.uid!!).downloadUrl.addOnCompleteListener {
                                    linkImage = it.result.toString()
                                }
                            }
                        }
                    })
            }
        }

}