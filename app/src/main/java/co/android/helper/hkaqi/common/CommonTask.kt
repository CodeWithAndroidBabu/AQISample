package co.android.helper.hkaqi.common

import android.content.res.Resources


object CommonTask {
    fun getScreenHeight(ratio: Int = 1) =  Resources.getSystem().displayMetrics.density/ratio
}