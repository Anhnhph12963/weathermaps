package com.example.weathermaps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermaps.adapter.QualityWeatherAdapter
import com.example.weathermaps.databinding.FragmentMapsBinding
import com.example.weathermaps.model.CommentQualityModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import org.json.JSONException
import java.io.IOException
import java.util.Random

class MapsFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private lateinit var mMap: GoogleMap
    private var binding: FragmentMapsBinding? = null
    private var mapFragment: SupportMapFragment? = null
    var diameter = 100
    var currentLocation: Location? = null
    private val FINE_PERMISSION_CODE = 1
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var mLastLocation: Location
    private var mCurrLocationMarker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest

    var lstCommentModel: List<CommentQualityModel>? = null
    private var latLngArrayList = mutableListOf<LatLng>()
    private var locationNameArraylist = mutableListOf<String>()

    private val callbackSearchView = OnMapReadyCallback { googleMap ->
        mMap = googleMap
    }


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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLastlocation()

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

        binding?.imgLocation?.setOnClickListener {
            onMyLocationButtonClick()
        }
        searchMapss()
    }

    private fun searchMapss() {
        binding?.apply {
            searchMaps.setOnQueryTextListener(object : OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val location: String = searchMaps.query.toString()
                    var addressList: List<Address>? = null
                    if (location != null || location == "") {

                        val geoCoder = Geocoder(requireContext())
                        try {
                            addressList = geoCoder.getFromLocationName(location, 1)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        if (addressList?.size != 0) {
                            Toast.makeText(
                                requireContext(),
                                "Location :$location",
                                Toast.LENGTH_SHORT
                            ).show()
                            val address = addressList!![0]
                            val latLng = LatLng(address.latitude, address.longitude)
                            mMap.addMarker(MarkerOptions().position(latLng).title(location))
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30f))
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Địa chỉ :$location không có trên bản đồ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
            mapFragment?.getMapAsync(callbackSearchView)
        }
    }

    private fun getLastlocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_PERMISSION_CODE
            )
            return
        }
        val task: Task<Location> = mFusedLocationClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                mapFragment?.getMapAsync(callback)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//            ){
//                buildGoogleApiClient()
//                mMap!!.isMyLocationEnabled = true
//            }
//        }else{
//            buildGoogleApiClient()
//            mMap!!.isMyLocationEnabled = true
//        }

//    private fun buildGoogleApiClient() {
//        mGoogleApiClient = GoogleApiClient.Builder(this)
//            .addConnectionCallbacks(this)
//            .addOnConnectionFailedListener(this)
//            .addApi(LocationServices.API).build()
//        mGoogleApiClient!!.connect()
//    }

        val conf = Bitmap.Config.ARGB_8888
        val bmp = Bitmap.createBitmap(diameter, diameter, conf)

        for (i in latLngArrayList.indices) {
            val number: Int = i + 1
            makeColorPonint(number, bmp)
            mMap.addMarker(
                MarkerOptions().position(latLngArrayList[i])
                    .title("Marker in " + locationNameArraylist.get(i))
                    .icon(BitmapDescriptorFactory.fromBitmap(bmp))
            )
            // below line is use to move camera.
        }
        val latLng = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
        val marker: Marker = mMap.addMarker(
            MarkerOptions().position(latLng).title("vi2tri1")
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
        )!!
        val id = marker.id
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30f))
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = false

        binding.apply {

            mMap.setOnMarkerClickListener {
                val dialog = Dialog(requireContext())
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window!!.attributes)
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.dialog_quality_weather)
                dialog.window!!.attributes = layoutParams
                addListCommentDialogQW()
                //                locationDetailses.add(new LocationDetails(marker.getId(), location_right, location_left, city, Double.parseDouble(deg), Double.parseDouble(speed)));
//                for (int i = 0; i<locationDetailses.size(); i++) {
//                    //matching id so, alert dialog can show specific data
//                    if (marker.getId().equals(locationDetailses.get(i).getMarkerID())){
//                        builder.setTitle("City: "+locationDetailses.get(i).getCity());
//                        builder.setMessage("Wind Speed: "+locationDetailses.get(i).getSpeed()+"\n"+"Degree: "+locationDetailses.get(i).getDeg()+"\n"+"We can plant WindMill here");
//                    }}
                val recyclerView1 = dialog.findViewById<RecyclerView>(R.id.ryc_comment_quality)
                val adapter = QualityWeatherAdapter(lstCommentModel)
                recyclerView1.adapter = adapter
                adapter.notifyDataSetChanged()

                val img_news = dialog.findViewById<ImageView>(R.id.img_news)
                img_news.setImageResource(R.drawable.abc)
                val tv_number_IAQ = dialog.findViewById<TextView>(R.id.tv_number_IAQ)
                tv_number_IAQ.text = "1000"
                val tv_number_date = dialog.findViewById<TextView>(R.id.tv_number_date)
                tv_number_date.text = "chiều"
                val tv_number_humidity = dialog.findViewById<TextView>(R.id.tv_number_humidity)
                tv_number_humidity.text = "100%"
                val tv_number_temperature =
                    dialog.findViewById<TextView>(R.id.tv_number_temperature)
                tv_number_temperature.text = "29°C"
                val tv_number_evaluate = dialog.findViewById<TextView>(R.id.tv_number_evaluate)
                tv_number_evaluate.text = "Có thể có bão to"
                val tv_number_news = dialog.findViewById<TextView>(R.id.tv_number_news)
                tv_number_news.text = "Sóng thần sắp đến"
                val tv_see = dialog.findViewById<TextView>(R.id.tv_see)
                tv_see.setOnClickListener(View.OnClickListener { })


//                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
                dialog.show()
                false
            }
        }
        // tô màu vùng
        mMap.uiSettings.isZoomControlsEnabled = true
        val madrid = LatLng(20.957326389031053, 105.75617530946106)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 3f))
        try {
            val geoJsonLayer = GeoJsonLayer(mMap, R.color.es_geojson, requireContext())
            val style: GeoJsonPolygonStyle = geoJsonLayer.defaultPolygonStyle
            style.fillColor = Color.MAGENTA
            style.strokeColor = Color.MAGENTA
            style.strokeWidth = 1f
            geoJsonLayer.addLayerToMap()
        } catch (ex: IOException) {
            Log.e("IOException", ex.localizedMessage)
        } catch (ex: JSONException) {
            Log.e("JSONException", ex.localizedMessage)
        }
    }

    @Override
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastlocation()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Bạn cần bật quyền location cho ứng dụng này !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onMyLocationClick(locationOnClick: Location) {
        currentLocation = locationOnClick
    }

    override fun onMyLocationButtonClick(): Boolean {
        currentLocation = mMap.myLocation //Lấy za vị trí hiện tại
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30f))
        return false
    }

    private fun makeColorPonint(number: Int, bmp: Bitmap) {
        val canvas = Canvas(bmp)
        val color = Paint()
        val text = Paint()
        text.color = Color.BLUE
        text.textSize = 50f
        text.strokeWidth = 5f
        text.textAlign = Paint.Align.CENTER
        val xPos = canvas.width / 2
        val yPos = (canvas.height / 2 - (text.descent() + text.ascent()) / 2).toInt()
        color.color = getRandomColor()
        canvas.drawCircle(
            (diameter / 2).toFloat(),
            (diameter / 2).toFloat(),
            (diameter / 2).toFloat(),
            color
        )
        canvas.drawText("" + number, xPos.toFloat(), yPos.toFloat(), text)
    }


    private fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    private fun addListCommentDialogQW() {
        lstCommentModel = ArrayList<CommentQualityModel>()
        (lstCommentModel as ArrayList<CommentQualityModel>).add(
            CommentQualityModel(
                "hello1",
                "hello2",
                "10/02/2001"
            )
        )
        (lstCommentModel as ArrayList<CommentQualityModel>).add(
            CommentQualityModel(
                "hello1",
                "hello2",
                "10/02/2001"
            )
        )
        (lstCommentModel as ArrayList<CommentQualityModel>).add(
            CommentQualityModel(
                "hello1",
                "hello2",
                "10/02/2001"
            )
        )
        (lstCommentModel as ArrayList<CommentQualityModel>).add(
            CommentQualityModel(
                "hello1",
                "hello2",
                "10/02/2001"
            )
        )
        (lstCommentModel as ArrayList<CommentQualityModel>).add(
            CommentQualityModel(
                "hello1",
                "hello2",
                "10/02/2001"
            )
        )
        (lstCommentModel as ArrayList<CommentQualityModel>).add(
            CommentQualityModel(
                "hello1",
                "hello2",
                "10/02/2001"
            )
        )
        (lstCommentModel as ArrayList<CommentQualityModel>).add(
            CommentQualityModel(
                "hello1",
                "hello2",
                "10/02/2001"
            )
        )
        (lstCommentModel as ArrayList<CommentQualityModel>).add(
            CommentQualityModel(
                "hello1",
                "hello2",
                "10/02/2001"
            )
        )
    }
}
