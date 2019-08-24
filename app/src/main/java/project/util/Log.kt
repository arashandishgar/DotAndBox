package project.util

import android.util.Log

fun Any?.log( tag:String="TEST"){
    Log.i(tag,this.toString());
}