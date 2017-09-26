package com.safeneck.safeneck.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.safeneck.safeneck.Models.User
import com.safeneck.safeneck.R
import com.safeneck.safeneck.Utils.DataManager
import com.safeneck.safeneck.Utils.NetworkHelper
import com.safeneck.safeneck.Utils.RetrofitInterface
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn_back.setOnClickListener {
            onBackPressed()
        }

        login_container.setOnClickListener {
            if (isRequestOk() && NetworkHelper.returnNetworkState(this)) {
                val id = login_edit_id.text.toString()
                val password = login_edit_password.text.toString()
                NetworkHelper.networkInstance.userLogin(id, password).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>?, response: Response<User>?) {
                        if (response!!.code() == 200) {
                            if (response.body()!!.status == 200) {
                                val dataManager = DataManager(this@LoginActivity)
                                dataManager.token = (response.body()!!.data.token)
                                startActivity<MainActivity>()
                                return
                            }

                        }
                        Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }

    private fun isRequestOk(): Boolean {
        val id = login_edit_id.text.toString()
        val password = login_edit_password.text.toString()
        if (id == "") {
            Toast.makeText(applicationContext, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password == "") {
            Toast.makeText(applicationContext, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
