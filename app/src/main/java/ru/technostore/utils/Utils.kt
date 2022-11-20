package ru.technostore.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.technostore.R
import ru.technostore.utils.Utils.toast

object Utils {

    fun Activity.toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun inProcess(context: Context) {
        Toast.makeText(context, R.string.str_in_process,Toast.LENGTH_SHORT).show()
    }
}