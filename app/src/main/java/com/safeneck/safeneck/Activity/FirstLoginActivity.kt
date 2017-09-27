package com.safeneck.safeneck.Activity

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.ListPopupWindow
import com.safeneck.safeneck.R
import kotlinx.android.synthetic.main.activity_first_login.*
import org.jetbrains.anko.startActivity
import android.widget.Spinner
import android.widget.Toast
import com.safeneck.safeneck.Adapter.SpinnerAgeAdapter
import com.safeneck.safeneck.Adapter.SpinnerWorkAdapter
import com.safeneck.safeneck.Utils.NetworkHelper
import com.safeneck.safeneck.databinding.ActivityFirstLoginBinding
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_login)

        val dataBinding: ActivityFirstLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_first_login)
        val gender = Gender(this)

        dataBinding.gender = gender

        firstLogin_container.setOnClickListener {
            finishAffinity()
            startActivity<TutorialActivity>()
        }

        val age = ArrayList<String>()
        (1..6).mapTo(age) { "" + (it * 10) }

        val ageAdapter = SpinnerAgeAdapter(age, this)
        firstLogin_spinner_age.adapter = ageAdapter

        val agePopup = Spinner::class.java.getDeclaredField("mPopup")
        agePopup.isAccessible = true

        val agePopupWindow = agePopup.get(firstLogin_spinner_age) as android.widget.ListPopupWindow

        agePopupWindow.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics).toInt()


        val work = ArrayList<String>()
        work.add("직장인")
        work.add("초등학생")
        work.add("중학생")
        work.add("고등학생")
        work.add("대학생")
        work.add("일반인")

        val workAdapter = SpinnerWorkAdapter(work, this)
        firstLogin_spinner_job.adapter = workAdapter

        val workPopup = Spinner::class.java.getDeclaredField("mPopup")
        workPopup.isAccessible = true
        val workPopupWindow = workPopup.get(firstLogin_spinner_job) as ListPopupWindow
        workPopupWindow.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics).toInt()
        firstLogin_spinner_job_container.setOnClickListener {
            firstLogin_spinner_job.performClick()
        }

        firstLogin_spinner_age_container.setOnClickListener {
            firstLogin_spinner_age.performClick()
        }

        firstLogin_container.setOnClickListener {
            if (isRequestOk() && NetworkHelper.returnNetworkState(this)) {
                val token = intent.getStringExtra("token")
                val age = firstLogin_spinner_age.selectedItem.toString()
                val work = when (firstLogin_spinner_job.selectedItem.toString()) {
                    "초등학생" -> "학생"
                    "중학생" -> "학생"
                    "고등학생" -> "학생"
                    else -> firstLogin_spinner_job.selectedItem.toString()
                }

                val sex: String = if (gender.gender == 0)
                    "여자"
                else
                    "남자"
                NetworkHelper.networkInstance.userInit(token, "" + age, sex, work).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        if (response!!.code() == 200) {
                            val json = JSONObject("" + response.body()!!.string())
                            val status = json.getInt("status")
                            if (status == 200) {
                                startActivity<TutorialActivity>()
                                return
                            }
                        }
                        Toast.makeText(applicationContext, "오류", Toast.LENGTH_SHORT).show()
                        Toast.makeText(applicationContext, "오류", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Toast.makeText(applicationContext, "오류", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }

    private fun isRequestOk(): Boolean {
        return true
    }

    class Gender(val context: Context) : BaseObservable() {
        // 0 = female, 1 = male
        var gender = 0

        fun onClick(gender: Int) {
            this.gender = gender
            notifyChange()
        }

        fun getElevation(gender: Int): Float = when (gender) {
            this.gender -> TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, context.resources.displayMetrics)
            else -> 0f
        }

        fun getTextColor(gender: Int): Int = when (gender) {
            this.gender -> context.resources.getColor(R.color.colorWhite)
            else -> context.resources.getColor(R.color.colorPrimaryDark)
        }

        fun getBackGround(gender: Int): Drawable = when (gender) {
            this.gender -> context.resources.getDrawable(R.drawable.box_solid_gradient)
            else -> context.resources.getDrawable(R.drawable.box_stroke_mint)
        }
    }
}
