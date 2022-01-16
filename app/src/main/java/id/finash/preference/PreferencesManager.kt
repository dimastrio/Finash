package id.finash.preference

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val PREF_NAME = "finash.pref"
    private val sharedpref: SharedPreferences
    val editor: SharedPreferences.Editor

    init {
        sharedpref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedpref.edit()
    }

    fun put(key: String, value: String){
        editor.putString(key, value)
            .apply()
    }

    fun put(key: String, value: Int){
        editor.putInt(key, value)
            .apply()
    }

    fun getString(key: String): String? {
        return sharedpref.getString(key, "")
    }

    fun getInt(key: String): Int? {
        return sharedpref.getInt(key, 0)
    }

    fun clear(){
        editor.putInt("pref_is_login", 0)
        editor.clear()
            .apply()
    }





}