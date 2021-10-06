package com.example.authentic

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    //declaration to use the firebase authentication
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth=FirebaseAuth.getInstance()
        dialog= ProgressDialog(this)
        regBtn.setOnClickListener {//to register the users
            register()
        }

        backBtn2.setOnClickListener {//to move back to the login page, just in case
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun register() {
        var email = userText1.text.toString() //store the input
        var pass = passText1.text.toString() //store the input
        var cpass = passText2.text.toString() //store the input

        //check the possible condition of user errors
        if (email.isEmpty()) {
            userText1.error = "Please enter your Email"
            return
        }
        if (pass.isEmpty()) {
            passText1.error = "Please enter a password"
            return}
        if (cpass.isEmpty()) {
            passText2.error = "Please confirm your password"
            return
        }
        if (cpass != pass) {
            Toast.makeText(this, "Password is Not Matched", Toast.LENGTH_SHORT).show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userText1.error = "Invalid Email Format"
            return
        }
        if (pass.length < 5) {
            passText1.error = "Password should be greater than 5 Characters"
            return
        }

        //check whether the checkBox is tick or not, if not user won't be able to register
        if(!checkBox.isChecked){
            Toast.makeText(this, "Agree & Read TOS", Toast.LENGTH_SHORT).show()
            return
        }
        //set the dialog running to the user when it creating account to the firebase authentication
        dialog.setMessage("..Creating Account..")
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)

        auth.createUserWithEmailAndPassword(email, pass) //function to create the account by passing the value that we collected
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    dialog.cancel()//stop the dialog running when created account in the authentication successfully
                    Toast.makeText(this, "Account Created", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)//back to the login page
                    startActivity(intent)
                    finish()
                } else {
                    dialog.cancel()
                    Toast.makeText(this, "Failed to create Account", Toast.LENGTH_LONG).show()
                }
            })
    }
}


