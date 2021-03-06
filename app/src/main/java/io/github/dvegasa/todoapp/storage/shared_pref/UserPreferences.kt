package io.github.dvegasa.todoapp.storage.shared_pref

import android.content.Context
import androidx.preference.PreferenceManager
import io.github.dvegasa.todoapp.screens.preferences.PREF_KEY_AUTOSAVE_EVERY_N_SYMBOLS
import io.github.dvegasa.todoapp.screens.preferences.PREF_KEY_PREVIEW_LIMIT

/**
 * 22.08.2019
 */
class UserPreferences(val context_: Context) {
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context_)

    fun getPreviewBodySymbolsLimit(): Int {
        val str = sharedPref.getString(PREF_KEY_PREVIEW_LIMIT, "limit100")
        return when (str) {
            "limit25" -> 25
            "limit50" -> 50
            "limit100" -> 100
            "limit150" -> 150
            "limit250" -> 250
            "limit350" -> 350
            "limit450" -> 450
            else -> 100
        }
    }

    fun getAutoSaveEveryNSymbols(): Int {
        val str = sharedPref.getString(PREF_KEY_AUTOSAVE_EVERY_N_SYMBOLS, "saveEvery50")
        return when (str) {
            "saveEvery5" -> 5
            "saveEvery10" -> 10
            "saveEvery20" -> 20
            "saveEvery50" -> 50
            "saveEvery75" -> 75
            "saveEvery100" -> 100
            else -> 50
        }

    }
}

