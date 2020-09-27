package cz.minarik.base.common.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.koin.core.KoinComponent

abstract class PrefManager(context: Context) : KoinComponent {
    val shared: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
}