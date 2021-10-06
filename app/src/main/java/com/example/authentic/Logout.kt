package com.example.authentic

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.example.authentic.UI.AmenityFragment
import com.example.authentic.UI.EventsFragment
import com.example.authentic.UI.ProfileFragment
import com.example.authentic.UI.ResFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class Logout : AppCompatActivity() {
    //Declaration of using the ChipNavigationBar for showing the restaurant, events and-
    //-amenities + user profile on the bottom
    lateinit var bottomNav: ChipNavigationBar
    val fragment = ResFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        bottomNav=findViewById(R.id.nav_view)
        openMainFragment()//open restaurant fragment as a main fragment(after login)


        bottomNav.setItemSelected(R.id.menu_res)
        bottomNav.setOnItemSelectedListener {
            when (it) {

                R.id.menu_res -> {//detect if user click on the ChipView of restaurant
                    openMainFragment()
                }
                R.id.menu_events -> {//detect if user click on the ChipView of Event
                    supportActionBar?.show()
                    supportActionBar?.setTitle(
                        Html.fromHtml("<font color='#FFFFFF'>" +
                                "\t".repeat(17) +
                                "Event</font>"))
                    val eventsFragment = EventsFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, eventsFragment).commit()

                }
                R.id.menu_amenity -> {//detect if user click on the ChipView of amenities
                    supportActionBar?.setTitle(
                        Html.fromHtml("<font color='#FFFFFF'>" +
                                "\t".repeat(17) +
                                "Amenity</font>"))
                    supportActionBar?.show()
                    val amenityFragment = AmenityFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, amenityFragment).commit()
                }

                R.id.menu_profile -> { //detect if user click on the ChipView of user profile
                        val profileFragment = ProfileFragment()
                        supportActionBar?.hide()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment, profileFragment).commit()
                }
            }
        }
    }

    private fun openMainFragment() { //main after login
        supportActionBar?.setTitle(
            Html.fromHtml("<font color='#FFFFFF'>" +
                    "\t".repeat(17) +
                    "Restaurants</font>"))
        supportActionBar?.show()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.commit()
    }
}

