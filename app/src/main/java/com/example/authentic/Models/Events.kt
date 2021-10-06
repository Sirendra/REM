package com.example.authentic.Models

//collect of Events data
data class Events (val JPEG:String,val Venue:String,val date:String ,val desc:String,val distance:Double,
                   val latitude:String,val longitude:String,val name:String,val time: String){
    constructor(): this("","","","",0.0,"","","","")
}