package com.example.summerpractics.storage

import android.content.Context

const val PREF_NAME = "theme"
const val THEME_ID = "theme_id"
const val ITEM_ID = "item_id"

class SharedPreference(ctx: Context) {

    var data = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveThemeId(themeId: Int) {
        data.edit().putInt(THEME_ID, themeId).apply()
    }

    fun getThemeId(): Int {
        return data.getInt(THEME_ID, 0)
    }

    fun saveCheckedItem(item: Int) {
        data.edit().putInt(ITEM_ID, item).apply()
    }

    fun getCheckedItem(): Int {
        return data.getInt(ITEM_ID, 0)
    }

}