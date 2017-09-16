package com.safeneck.safeneck.Activity

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import com.safeneck.safeneck.R
import kotlinx.android.synthetic.main.activity_first_login.*
import org.jetbrains.anko.startActivity
import android.widget.Spinner
import com.safeneck.safeneck.SpinnerAgeAdapter
import com.safeneck.safeneck.databinding.ActivityFirstLoginBinding

class FirstLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_login)

        val dataBinding: ActivityFirstLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_first_login)
        val gender = Gender(this)

        dataBinding.gender = gender
        firstLogin_container.setOnClickListener {
            startActivity<MainActivity>()
        }

        val age = ArrayList<String>()
        (1..100).mapTo(age) { "" + it }

        val adapter = SpinnerAgeAdapter(age, this)
        firstLogin_spinner_age.adapter = adapter

        val popup = Spinner::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true

        // Get private mPopup member variable and try cast to ListPopupWindow
        val popupWindow = popup.get(firstLogin_spinner_age) as android.widget.ListPopupWindow

        // Set popupWindow height to 500px
        popupWindow.height = 250

        firstLogin_spinner_age_img.setOnClickListener {
            firstLogin_spinner_age.performClick()
        }

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
