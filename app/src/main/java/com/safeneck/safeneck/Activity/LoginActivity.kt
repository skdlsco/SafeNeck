package com.safeneck.safeneck.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.safeneck.safeneck.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_container.setOnClickListener {
            startActivity<FirstLoginActivity>()
        }
    }
}
