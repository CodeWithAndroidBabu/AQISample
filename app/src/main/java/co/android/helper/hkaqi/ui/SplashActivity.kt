package co.android.helper.hkaqi.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import co.android.helper.hkaqi.BaseActivity
import co.android.helper.hkaqi.R
import co.android.helper.hkaqi.databinding.ActivitySplashBinding
import co.android.helper.hkaqi.ui.home.map.HomeMapsActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
      /*  performTranslationYAnimation()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, HomeMapsActivity::class.java)
            startActivity(intent)
            finish()
        }, 2 * 1000)*/
        //Glide.with(this).load(R.drawable.logo).into(DrawableImageViewTarget(binding.animationView))
    }

    private fun performTranslationYAnimation() {
       // binding.cv.animate().translationY(0f).duration = 1500
    }
}