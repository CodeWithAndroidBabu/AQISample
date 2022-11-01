package co.android.helper.hkaqi.utils

import android.app.Activity
import android.content.Context
import android.view.View
import okhttp3.MediaType
import okhttp3.RequestBody

fun Context.pxToDp(px: Int): Float {
    return (px / resources.displayMetrics.density)
}

fun Activity.getScreenHeight(){

}

fun View.makeInvisible(shouldInvisible: Boolean){
    visibility = if(shouldInvisible) View.INVISIBLE else View.VISIBLE
}

fun View.makeVisible(shouldVisibilityGone: Boolean){
    visibility = if(shouldVisibilityGone) View.GONE else View.VISIBLE
}
/*
fun String.createRequestBody(): RequestBody = RequestBody.create(MediaType.parse("text/plain"), this)*/
