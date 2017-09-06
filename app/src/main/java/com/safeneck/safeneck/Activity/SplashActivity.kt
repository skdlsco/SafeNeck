package com.safeneck.safeneck.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.safeneck.safeneck.R
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        anim_alpha_to_0()
        splash_register.setOnClickListener {
            startActivity<RegisterActivity>()
        }
        splash_login.setOnClickListener {
            startActivity<LoginActivity>()
        }
    }

    private fun anim_alpha_to_0() {
        val anim: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha_to_0)
        anim.setAnimationListener(animationListener)
        splash_container_text.startAnimation(anim)
    }

    private fun anim_alpha_to_1() {
        val anim: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha_to_1)
        anim.setAnimationListener(animationListener)
        splash_container_button.startAnimation(anim)
        anim.reset()
    }

    private val animationListener: Animation.AnimationListener = object : Animation.AnimationListener {
        var isAnimated = false
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            if (!isAnimated) {
                splash_container_text.visibility = View.GONE
                splash_container_button.visibility = View.VISIBLE
                anim_alpha_to_1()
                isAnimated = true
            }

        }

        override fun onAnimationStart(p0: Animation?) {
        }
    }
}