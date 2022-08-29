package com.example.fullsteam.animation

import android.view.View
import android.view.animation.Animation

fun View.startAnimation(animation: Animation, onComplete: () -> Unit){
    animation.setAnimationListener(object :Animation.AnimationListener{
        override fun onAnimationStart(p0: Animation?) {
            TODO("Not yet implemented")
        }

        override fun onAnimationEnd(p0: Animation?) {
            TODO("Not yet implemented")
        }

        override fun onAnimationRepeat(p0: Animation?) {
            TODO("Not yet implemented")
        }

    })
}