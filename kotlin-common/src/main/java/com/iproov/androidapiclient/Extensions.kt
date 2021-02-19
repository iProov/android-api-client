package com.iproov.androidapiclient

import android.content.Context
import android.util.Log
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

fun String.datetime(): String =

    SimpleDateFormat("D.HHmmssSSS").format(Date()).let { str ->
        "$this$str"
    }


fun String.jsonFile(context: Context): String? =

    try {
        with(context.getAssets().open(this)) {
            ByteArray(available()).let { buffer ->
                read(buffer)
                close()
                String(buffer, Charset.defaultCharset())
            }
        }

    } catch (ex: IOException) {
        ex.printStackTrace()
        null
    }

 fun <K: Any, V: Any> Map<K, V>.merge(to: Map<K, V>?) = run {
    val result = mutableMapOf<K, V>()
    this.forEach{ result[it.key] = it.value }
    to?.forEach{ result[it.key] = it.value }
    result
}


