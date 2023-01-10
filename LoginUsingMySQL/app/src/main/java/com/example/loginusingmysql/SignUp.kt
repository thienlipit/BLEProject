package com.example.loginusingmysql

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.example.loginusingmysql.databinding.ActivitySignUpBinding
import com.vishnusivadas.advanced_httpurlconnection.PutData

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val textInputEditTextUsername = binding.username
        val textInputEditTextFullname = binding.fullname
        val textInputEditTextEmail= binding.email
        val textInputEditTextPassword = binding.password
        val buttonSignup = binding.buttonSignUp
        val textViewLogin = binding.loginText
        val progressBar = binding.progress

        textViewLogin.setOnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        buttonSignup.setOnClickListener {
            val fullname: String = textInputEditTextFullname.text.toString()
            val email: String = textInputEditTextEmail.text.toString()
            val username: String = textInputEditTextUsername.text.toString()
            val password: String = textInputEditTextPassword.text.toString()

            if(!fullname.equals("") && !email.equals("") && !username.equals("") && !password.equals("")) {
                progressBar.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    val field = arrayOfNulls<String>(4)
                    field[0] = "fullname"
                    field[1] = "username"
                    field[2] = "password"
                    field[3] = "email"
                    //Creating array for data
                    val data = arrayOfNulls<String>(4)
                    data[0] = fullname
                    data[1] = username
                    data[2] = password
                    data[3] = email
                    val putData = PutData(
                        "http://192.168.1.99/LoginRegister/signup.php",
                        "POST",
                        field,
                        data
                    )
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar.visibility = View.GONE

                            val result = putData.result
                            if(result.equals("Sign Up Success")){
                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, Login::class.java)
                                startActivity(intent)
                                finish()

                            }
                            else {
                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                            }
                            //End ProgressBar (Set visibility to GONE)
//                    Log.i("PutData", result)
                        }
                    }
                    //End Write and Read data with URL
                })
            }
            else Toast.makeText(applicationContext, "All field required", Toast.LENGTH_SHORT).show()
        }

    }
}