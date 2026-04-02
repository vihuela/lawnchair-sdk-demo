package com.example.lawnchairsdkdemo

import android.content.Context
import app.lawnchair.sdk.LawnchairOperations

object DemoDesktopItems {
    const val ENTRY_1_ID = "demo_entry_1"
    const val ENTRY_2_ID = "demo_entry_2"

    fun removeEntry1(context: Context): Boolean {
        return LawnchairOperations.removeDesktopItem(context, ENTRY_1_ID)
    }
}
