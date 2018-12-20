package com.amperas17.mangaedenapp.utils

import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

//use before setContentView()
fun AppCompatActivity.makeFullScreen() {
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

fun AppCompatActivity.hideStatusBar() {
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}