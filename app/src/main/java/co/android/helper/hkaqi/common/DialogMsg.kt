package co.android.helper.hkaqi.common

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogMsg {
    fun showMsg(
        context: Context,
        title: String = "MOE AQI",
        msg: String,
        onPositiveClick: () -> Unit
    ) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(title)
            setMessage(msg)
        }.also {
            it.setPositiveButton(
                "OK"
            ) { p0, p1 ->
                onPositiveClick.invoke()
            }
            it.show()
        }
    }
}
