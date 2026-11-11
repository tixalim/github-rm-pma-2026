package com.example.myapp008bdatastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

// DataStore musí být vytvořený jen jednou pro celou aplikaci, proto je tady mimo třídy.
// Používají ho oba fragmenty.
val Context.dataStore by preferencesDataStore(name = "hracsky_profil")

// Klíče (na rozdíl od SharedPreferences mají pevně daný typ)
val JMENO_KEY = stringPreferencesKey("JMENO")
val HARDCORE_KEY = booleanPreferencesKey("HARDCORE")