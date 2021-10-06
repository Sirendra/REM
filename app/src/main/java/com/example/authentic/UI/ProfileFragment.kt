package com.example.authentic.UI

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.authentic.MainActivity
import com.example.authentic.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }
    private lateinit var auth: FirebaseAuth
    lateinit var fm: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.profile_fragment, container, false)
        return root
    }

    override fun onStart() {//use to sign out from the application
        super.onStart()
        auth= FirebaseAuth.getInstance()
        val user= auth.currentUser
        signoutBtn.setOnClickListener {
            auth.signOut()
            Toast.makeText(activity,"Signed Out",Toast.LENGTH_SHORT).show()
            val intent=Intent(activity?.applicationContext,MainActivity::class.java)
            startActivity(intent)
        }
        if (user != null) {
            eTxt.text=user.email
        }
    }

}