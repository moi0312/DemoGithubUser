package com.edlo.mydemoapp.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class LoadingDialogFragment(val dialogBuilder: () -> AlertDialog): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return dialogBuilder()
    }

}