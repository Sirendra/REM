package com.example.authentic.adapter

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.authentic.Models.Amenity
import com.example.authentic.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore


class AmenityAdapter(val mContext: Context, val layoutResId:Int, val messageList:List<Amenity>, val currentLatLon:LatLng=LatLng(0.0,0.0),val type:Boolean=false)
    : ArrayAdapter<Amenity>(mContext,layoutResId,messageList) {
    //declare database
    lateinit var dataReference: FirebaseFirestore
    //This is short Version if we have just have to show small data
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val view: View = layoutInflater.inflate(layoutResId, null)
        val nameText = view.findViewById<TextView>(R.id.textView)
        val catText = view.findViewById<TextView>(R.id.textView1)
        val disTextView = view.findViewById<TextView>(R.id.dis)
        val tel = view.findViewById<TextView>(R.id.vv12)
        val imageTextView = view.findViewById<ImageView>(R.id.imageShow)

        //val RatTextView=view.findViewById<TextView>(R.id.ratView)

        val msg = messageList[position]
        val image = msg.JPEG //get the image name from msg (database)

        //to assign the picture to each amenities by using switch case
        var result = when (image) {
            "apic1" -> R.drawable.apic1
            "apic2" -> R.drawable.apic2
            "apic3" -> R.drawable.apic3
            "apic4" -> R.drawable.apic4
            else -> R.drawable.apic5
        }

            val loc1 = Location("")//GPS location
            loc1.setLatitude(currentLatLon.latitude)
            loc1.setLongitude(currentLatLon.longitude)

            val loc2 = Location("")//That specific place in the list
            loc2.setLatitude(msg.latitude.toDouble())
            loc2.setLongitude(msg.longitude.toDouble())


            val distanceInMeters: Float =
                loc1.distanceTo(loc2) //to compare the user location to the amenities' location
            var distanceInKm = String.format("%.2f", (distanceInMeters / 1000)).toFloat()
            if (type) {
                disTextView.text = distanceInKm.toString() + "km"
            }

            dataReference = FirebaseFirestore.getInstance()
            val db = dataReference.collection("Event")
            val AmeName = msg.JPEG
            //use the image to find document ID to change the "distance" value in database
            var docPath = when (AmeName) {
                "apic1" -> "1"
                "apic2" -> "2"
                "apic3" -> "3"
                "apic4" -> "4"
                else -> "5"
            }
            //update "distance" in the database (firestore)
            db.document(docPath).update("distance", distanceInKm)


            //set to show to user
            imageTextView.setImageResource(result)
            nameText.text = msg.name
            tel.text = "Telephone: " + msg.telephone
            catText.text = msg.category

            return view
        }

        override fun getCount(): Int {
            return messageList.size
        }
    }