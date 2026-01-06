package com.example.mylist

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.core.content.edit

@Serializable
data class Person(
    val name: String,
    val description: String
)

class PersonRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyListPrefs", Context.MODE_PRIVATE)

    private val json = Json { ignoreUnknownKeys = true }

    companion object {
        private const val KEY_PERSONS = "persons_list"
    }

    fun savePersons(persons: List<Pair<String, String>>) {
        val personList = persons.map { Person(it.first, it.second) }
        val jsonString = json.encodeToString(personList)
        sharedPreferences.edit { putString(KEY_PERSONS, jsonString) }
    }

    fun loadPersons(): SnapshotStateList<Pair<String, String>> {
        val jsonString = sharedPreferences.getString(KEY_PERSONS, null)
        val persons = if (jsonString != null) {
            try {
                json.decodeFromString<List<Person>>(jsonString)
            } catch (_: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }

        return mutableStateListOf<Pair<String, String>>().apply {
            addAll(persons.map { Pair(it.name, it.description) })
        }
    }
}
