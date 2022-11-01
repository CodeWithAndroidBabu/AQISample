package co.android.helper.hkaqi.common

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.TranslateAnimation


object ViewAnimationUtils {
    fun expandOrCollapse(v: View, exp_or_colpse: String) {
        var anim: TranslateAnimation? = null
        if (exp_or_colpse == "expand") {
            anim = TranslateAnimation(0.0f, 0.0f, (-v.height).toFloat(), 0.0f)
            v.visibility = View.VISIBLE
        } else {
            anim = TranslateAnimation(0.0f, 0.0f, 0.0f, (-v.height).toFloat())
            val collapselistener: AnimationListener = object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    v.visibility = View.GONE
                }
            }
            anim.setAnimationListener(collapselistener)
        }

        // To Collapse
        //
        anim.duration = 300
        anim.interpolator = AccelerateInterpolator(0.5f)
        v.startAnimation(anim)
    }
}