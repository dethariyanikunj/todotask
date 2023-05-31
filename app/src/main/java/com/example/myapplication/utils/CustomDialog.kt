package com.example.myapplication.utils

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CustomDialog(
    private val title: String,
    private val message: String,
    private val positiveButton: String,
    private val negativeButton: String,
    private val dialogListener: DialogInterface.OnClickListener
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialogRounded)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton, dialogListener)
            .setNegativeButton(negativeButton, null)
            .create()
    }
}