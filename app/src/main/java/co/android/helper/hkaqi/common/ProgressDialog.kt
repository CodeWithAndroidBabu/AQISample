package co.android.helper.hkaqi.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import co.android.helper.hkaqi.R

class ProgressDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_progress_dialog)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun show() {
        if(!isShowing){
            super.show()
        }
    }

    override fun dismiss() {
        if(isShowing){
            super.dismiss()
        }
    }
}