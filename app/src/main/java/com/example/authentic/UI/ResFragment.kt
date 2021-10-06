package com.example.authentic.UI

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.authentic.Logout
import com.example.authentic.MapsActivity
import com.example.authentic.Models.Restaurants
import com.example.authentic.R
import com.example.authentic.adapter.ItemAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.res_fragment.*

class ResFragment : Fragment() ,SensorEventListener{

    companion object {
        fun newInstance() = ResFragment()
    }
    //declare global variable to use
    lateinit var dataReference: FirebaseFirestore
    lateinit var objList: MutableList<Restaurants>//send list from the database

    private var sensorManager: SensorManager? = null//use in accerelometer
    private var lastUpdate: Long = 0
    private lateinit var main: Logout
    private lateinit var currentLatLon: LatLng //keep current location
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataReference = FirebaseFirestore.getInstance()
        objList = mutableListOf()
        main = requireActivity() as Logout
        lastUpdate = System.currentTimeMillis()
        sensorManager = main.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val root = inflater.inflate(R.layout.amenity_fragment, container, false)
        //The line below use to get the data and show data on the list and sort data by name
        val db = dataReference.collection("restaurant")//restaurant is the collection name in firestore
        db.orderBy("name").get()//ordering ...
            .addOnSuccessListener { snapShot ->//this means if read is successful then this data will be loaded to snapshot
                if (snapShot != null) {
                    objList.clear()
                   objList = snapShot.toObjects(Restaurants::class.java)
                    //collect the data as same type in database

                    //call restaurant(item) adapter using row.xml to represent each row in the list
                    val adapter = activity?.applicationContext?.let {
                        ItemAdapter(
                            it,
                            R.layout.row,
                            objList,currentLatLon
                        )
                    }
                    listView.adapter = adapter
                }

            }//in case it fails, it will toast failed
            .addOnFailureListener { exception ->
                Log.d(
                    "FirebaseError",
                    "Fail:",
                    exception
                )//this is kind a debugger to check whether working correctly or not
                Toast.makeText(
                    activity?.applicationContext,
                    "Fail to read message",
                    Toast.LENGTH_SHORT
                ).show()

            }
        locationManager = main.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                //get GPS data and store in currentLatLon variable
                currentLatLon = LatLng(location.latitude, location.longitude)
            }

            override fun onProviderDisabled(provider: String) {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        requestLocation()
        return root
    }

    private fun requestLocation() {//To access user's location
        if (context?.applicationContext?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 10)
            }
            return
        }
        locationManager.requestLocationUpdates("gps", 5000, 0f, locationListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            10 -> requestLocation()//requestCode to be 10, have to be match
            else -> Toast.makeText(
                activity,
                "Do not nothing (becuz the requestCode != 10)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
        Log.i("GPS Status","pause")
        sensorManager!!.unregisterListener(this)
    }



    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event)
        }

    }

    //Accelerometer
    private fun getAccelerometer(event: SensorEvent) {
        val values = event.values
        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accel = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)
        val actualTime = System.currentTimeMillis()
        if (accel > 2) {
            if (actualTime - lastUpdate < 100) {
                return
            } else {
                lastUpdate = actualTime
                val db = dataReference.collection("restaurant")//initialize database to call the "restaurant"
                Toast.makeText(activity, "Refreshed!", Toast.LENGTH_LONG).show()
                db.orderBy("distance").get()////sort ordering by distance
                    .addOnSuccessListener { snapShot ->//this means if read is successful then this data will be loaded to snapshot
                        if (snapShot != null) {
                            objList.clear()
                            objList = snapShot.toObjects(Restaurants::class.java)
                            val adapter = activity?.applicationContext?.let {
                                ItemAdapter(
                                    it,
                                    R.layout.row,
                                    objList,
                                    currentLatLon,true
                                )
                            }
                            listView.adapter = adapter
                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.d(
                            "FirebaseError",
                            "Fail:",
                            exception
                        )//this is kind a debugger to check whether working correctly or not
                        Toast.makeText(
                            activity?.applicationContext,
                            "Fail to read message",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }
    override fun onStart() {
        super.onStart()

        listView.setOnItemClickListener { adapterView, view, position, id ->
            val res = objList!![position]//pass the position
            gotoMap(res)//pass res to gotoMap function
        }
    }

    private fun gotoMap(res: Restaurants) {//receive res
        var intent = Intent(activity, MapsActivity::class.java)//change activity to show map to user
        intent.putExtra("selRoute", res.name)
        intent.putExtra("point1lat", res.latitude)
        intent.putExtra("point1lon", res.longitude)
        startActivity(intent)
    }
}
