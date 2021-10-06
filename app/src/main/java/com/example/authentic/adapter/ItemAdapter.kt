package com.example.authentic.adapter
import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.example.authentic.Models.Restaurants
import com.example.authentic.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore

class ItemAdapter(val mContext:Context, val layoutResId:Int, val messageList:List<Restaurants>, val currentLatLon: LatLng = LatLng(0.0,0.0), val type:Boolean=false)
    :ArrayAdapter<Restaurants>(mContext,layoutResId,messageList) {
    //declare database
    lateinit var dataReference: FirebaseFirestore
    //This is short Version if we have just have to show small data
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater=LayoutInflater.from(mContext)
        val view:View=layoutInflater.inflate(layoutResId,null)

        val nameText=view.findViewById<TextView>(R.id.textView)
        val categoryText=view.findViewById<TextView>(R.id.textView1)
        val disTextView=view.findViewById<TextView>(R.id.dis)
        val imageTextView=view.findViewById<ImageView>(R.id.imageShow)
        val desc = view.findViewById<TextView>(R.id.vv12)
        val rate = view.findViewById<RatingBar>(R.id.ratingBar2)//call rating bar

        val msg = messageList[position]
        val image=msg.JPEG//get the image name from msg (database)

        //to assign the picture to each restaurants by using switch case
        var result=when (image) {
            "pic1" -> R.drawable.pic1
            "pic2" -> R.drawable.pic2
            "pic3"-> R.drawable.pic3
            "pic4"-> R.drawable.pic4
            "pic5"-> R.drawable.pic5
            "pic6"-> R.drawable.pic6
            "pic7"-> R.drawable.pic7
            "pic8" -> R.drawable.pic8
            "pic9" -> R.drawable.pic9
            "pic10" -> R.drawable.pic10
            "pic11"-> R.drawable.pic11
            "pic12" -> R.drawable.pic12
            "pic13"-> R.drawable.pic13
            "pic14"-> R.drawable.pic14
            else -> R.drawable.pic15
        }

            val loc1 = Location("")//GPS location
            loc1.setLatitude(currentLatLon.latitude)
            loc1.setLongitude(currentLatLon.longitude)

            val loc2 = Location("")//That specific place in the list
            loc2.setLatitude(msg.latitude.toDouble())
            loc2.setLongitude(msg.longitude.toDouble())

            val distanceInMeters: Float = loc1.distanceTo(loc2) //to compare the user location to the restaurant's location
            var distanceInKm = String.format("%.2f", (distanceInMeters / 1000)).toFloat()
        if (type) {
            disTextView.text = distanceInKm.toString() + "km"}

            dataReference = FirebaseFirestore.getInstance()
            val db = dataReference.collection("restaurant")
            val ResName = msg.JPEG
            //use the image to find document ID to change the "distance" value in database
            var docPath = when (ResName) {
                "pic7" -> "BuphpkdgzI93FBjxi59h"
                "pic2" -> "CJWhezVIQi3gpXJea0iR"
                "pic1"-> "JRIYpX5QftvpvM88bu58"
                "pic12"-> "KnCZ3Nz6cYcpuUWr4DTk"
                "pic10"-> "LaMvwz0Q11pT4KeEJEG5"
                "pic6"-> "PXcJzzFpu2m8MNc6RnfS"
                "pic8"-> "RJLWG1Xu96GE3aUsSOTT"
                "pic14" -> "UsTukhmhhd9Bj0F7lCZc"
                "pic11" -> "ZxKtCDFvDTdeIFgW5m3N"
                "pic4" -> "clk9xeJDKMWSaUwUGqNi"
                "pic9"-> "e2AYGhTjmf9PZHT65Qbg"
                "pic3" -> "ffg3SrHQxcI5PkFApK8i"
                "pic15"-> "tbOuHHnibisEtZR95L38"
                else -> "yAzuHoCmiTRvNxnNmi4l"
            }//update "distance" in the database (firestore)
            db.document(docPath).update("distance", (distanceInKm))


        //set to show to user
        imageTextView.setImageResource(result)
        nameText.text=msg.name
        categoryText.text=msg.category
        desc.text = "("+msg.rating+")"
        rate.rating = msg.rating.toDouble().toFloat()
        return view
    }
    override fun getCount(): Int {
        return messageList.size
    }
}