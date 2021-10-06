package com.example.authentic.Models

//collect of Restaurants data
data class Restaurants (val JPEG:String,val category:String,val distance:Double ,val latitude:String,val longitude:String,val name:String,val rating: String,val telephone:String){
    constructor(): this("","",0.0,"","","","","")
}