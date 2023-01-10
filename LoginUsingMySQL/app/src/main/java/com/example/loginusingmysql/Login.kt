package com.example.loginusingmysql

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginusingmysql.databinding.ActivityLoginBinding
import com.vishnusivadas.advanced_httpurlconnection.PutData


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val textInputEditTextUsername = binding.username
        val textInputEditTextPassword = binding.password
        val buttonLogin = binding.buttonLogin
        val progressBar = binding.progress

        buttonLogin.setOnClickListener {
            val username: String = textInputEditTextUsername.text.toString()
            val password: String = textInputEditTextPassword.text.toString()

            if(!username.equals("") && !password.equals("")) {
                progressBar.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    val field = arrayOfNulls<String>(2)
                    field[0] = "username"
                    field[1] = "password"

                    //Creating array for data
                    val data = arrayOfNulls<String>(2)
                    data[0] = username
                    data[1] = password
                    val putData = PutData(
                        "http://192.168.1.99/LoginRegister/login.php",
                        "POST",
                        field,
                        data
                    )
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar.visibility = View.GONE

                            val result = putData.result
                            if(result.equals("Login Success")){
                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, Wellcome::class.java)
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