package com.example.weathermaps

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.weathermaps.databinding.ActivityMainBinding
import com.example.weathermaps.model.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var user: User? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var binding: ActivityMainBinding
    private lateinit var intent: Intent
    private var mAuthListener: AuthStateListener? = null
    var fragmentManager: FragmentManager? = null
    var fragmentTransaction: FragmentTransaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        // add bottom nav
        binding.apply {
            // thêm
            setSupportActionBar(appbar.appbar)
            supportActionBar!!.title = "App weather"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
            val navController = findNavController(R.id.nav_host_fragment)

            navView.setupWithNavController(navController)
//
            if (FirebaseAuth.getInstance().currentUser != null) {
//                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnCompleteListener {
//                    if (it.isSuccessful){
//                        user = it.result.getValue(User::class.java)
//                    }else{
//                        Log.d("TAG", "${it.exception?.message}")
//                    }
//                }
//               if(user!=null){
//                   Log.d("ABCD", "initView: "+user!!.profileImage.toString())
//                    navView.getHeaderView(0).findViewById<TextView>(R.id.tv_username).text = user!!.username
//                    val imageView  = navView.getHeaderView(0).findViewById<CircleImageView>(R.id.profileimage)
//                    Picasso.get().load(user!!.profileImage).fit().into(imageView)
//                }
            }

            // hide bottom nav when fragment not need
            navController.let {
                navController.addOnDestinationChangedListener { _, des, _ ->
                    if (des.id == R.id.fragmentLogin || des.id == R.id.fragmentRegister || des.id == R.id.fragmentSignout) {
                        binding.appbar.appbar.visibility = View.GONE
                        binding.drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    } else {
                        binding.appbar.appbar.visibility = View.VISIBLE
                        binding.drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    }
                }
            }
        }
    }

//

    override fun onBackPressed() {
        if (binding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerlayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fragmentLogin -> {
                findNavController(R.id.fragmentLogin)
                Firebase.auth.signOut()
                this.finish()
            }

            R.id.blankFragment -> {
                Toast.makeText(this@MainActivity, "blankFragment", Toast.LENGTH_SHORT).show()
                findNavController(R.id.blankFragment)
            }

            R.id.mapsFragment -> {
                Toast.makeText(this@MainActivity, "mapsFragment", Toast.LENGTH_SHORT).show()
                findNavController(R.id.mapsFragment)
            }

            R.id.fragmentProfile2 -> {
                Toast.makeText(this@MainActivity, "Profile", Toast.LENGTH_SHORT).show()
                findNavController(R.id.fragmentProfile2)
            }

            R.id.fragmentPost -> {
                Toast.makeText(this@MainActivity, "fragmentPost", Toast.LENGTH_SHORT).show()
                findNavController(R.id.fragmentPost)
            }

            R.id.fragmenthoidap -> {
                findNavController(R.id.fragmenthoidap)
            }

            R.id.fragmentPostName -> {
                findNavController(R.id.fragmentPostName)
            }
        }
        binding.drawerlayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.drawerlayout.openDrawer(GravityCompat.START)
            }
            R.id.fragmentSignout -> {
                val alert: AlertDialog = AlertDialog.Builder(this).create()
                alert.setTitle("Logout")
                alert.setMessage("Are you sure you want to logout?")
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { dialog, which ->
                    CoroutineScope(Dispatchers.IO).launch {
                        withContext(Dispatchers.Main) {
                            Firebase.auth.signOut()
                        }
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    dialog.dismiss()
                    startActivity(intent)
                }
                alert.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, which ->
                    dialog.dismiss()
                }
                alert.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
