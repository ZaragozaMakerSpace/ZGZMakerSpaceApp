package com.zms.zgzmakerspace.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class FirebaseViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirebaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FirebaseViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {
    val makerspaceIsOpen = mutableStateOf(false)
    private val database = Firebase.database

    init {
        // InitializeApp is initialized in MainActivity since onCreate method.
        // FirebaseApp.initializeApp(application)
        setupListeners()
    }

    private fun setupListeners() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("ZMSDoor")
        try {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(Boolean::class.java)?.let { data ->
                        makerspaceIsOpen.value = data
                    } ?: run {
                        Log.i("ERROR Firebase", "Expected a Boolean but found something else.")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("ERROR FireBase", error.message)
                }
            })
        } catch (e: Exception) {
            Log.e("Firebase Setup Error", "Failed to set up Firebase listener", e)
        }
    }

    fun writeOnFirebase() {
        val dbRef = database.reference
        dbRef.setValue("Open")
    }
}