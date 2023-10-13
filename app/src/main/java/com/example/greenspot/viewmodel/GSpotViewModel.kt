package com.example.greenspot.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.greenspot.model.GSpot
import com.example.greenspot.data.GreenSpotDatabase
import com.example.greenspot.repository.GSpotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GSpotViewModel(application: Application): AndroidViewModel(application) {
   val readAllGSpot: LiveData<List<GSpot>>
    private val repository: GSpotRepository

    init {
        val gSpotDao = GreenSpotDatabase.getDatabase(application).gSpotDao()
        repository = GSpotRepository(gSpotDao)
        readAllGSpot = repository.readAllGSpot
    }

    fun addGSpot(gSpot: GSpot){
        viewModelScope.launch (Dispatchers.IO){
            repository.addGSpot(gSpot)
        }
    }
    fun updateNote(gSpot: GSpot){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateNote(gSpot)
        }
    }
    fun deleteSpot(gSpot: GSpot){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteGSpot(gSpot)
        }
    }
     fun deleteAllNotes(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllGSpot()
        }
    }

}