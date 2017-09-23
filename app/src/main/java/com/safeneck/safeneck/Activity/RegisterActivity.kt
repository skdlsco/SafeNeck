package com.safeneck.safeneck.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.safeneck.safeneck.Models.User
import com.safeneck.safeneck.R
import com.safeneck.safeneck.Utils.NetworkHelper
import com.safeneck.safeneck.Utils.RetrofitInterface
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register_btn_back.setOnClickListener {
            onBackPressed()
        }

        register_container.setOnClickListener {
            val id = register_edit_id.text.toString()
            val password = register_edit_password.text.toString()
            val name = register_edit_name.text.toString()

            if (isRequestOK() && NetworkHelper.returnNetworkState(this)) {
                NetworkHelper.networkInstance.userRegister(id, password, name).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>?, response: Response<User>?) {
                        if (response!!.code() == 200) {
                            Log.d("status", "" + response.body()!!.status)
                            if (response.body()!!.status == 200) {
                                val intent = Intent(this@RegisterActivity, FirstLoginActivity::class.java)
                                intent.putExtra("token", response.body()!!.data.token)
                                startActivity(intent)
                                return
                            }
                        }
                        Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }

    private fun isRequestOK(): Boolean {
        val id = register_edit_id.text.toString()
        val password = register_edit_password.text.toString()
        val rePassword = register_edit_rePassword.text.toString()
        val name = register_edit_name.text.toString()
        if (name == "") {
            Toast.makeText(applicationContext, "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        if (id == "") {
            Toast.makeText(applicationContext, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password == "") {
            Toast.makeText(applicationContext, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != rePassword) {
            Toast.makeText(applicationContext, "비밀번호 재입력이 같지 않습니다.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
