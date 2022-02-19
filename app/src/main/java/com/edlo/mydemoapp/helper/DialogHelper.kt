package com.edlo.mydemoapp.helper

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.edlo.mydemoapp.R
import com.edlo.mydemoapp.ui.dialog.LoadingDialogFragment
import javax.inject.Inject

class DialogHelper @Inject constructor() {

    companion object {
        const val PROGRESS_DIALOG_TAG = "PROGRESS_DIALOG_ALERT_TAG"
    }

    fun showProgressDialog(activity: AppCompatActivity, @LayoutRes resId: Int = R.layout.dialog_loading) {
        var fragManager = activity.supportFragmentManager
        val dialogFragment = fragManager.findFragmentByTag(PROGRESS_DIALOG_TAG)
        if (dialogFragment != null) {
            return
        }

        val fragment = LoadingDialogFragment {
            val alertDialog = AlertDialog.Builder(activity, R.style.ProgressDialog)
                .setView(resId)
                .create()
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.setOnCancelListener { dialogInterface: DialogInterface? -> }
            alertDialog
        }
        fragment.isCancelable = false
        fragment.show(fragManager, PROGRESS_DIALOG_TAG)
    }

    fun hideProgressDialog(activity: AppCompatActivity) {
        var fragManager = activity.supportFragmentManager
        fragManager.findFragmentByTag(PROGRESS_DIALOG_TAG)?.let {
            (it as DialogFragment).dismiss()
        }
    }
}