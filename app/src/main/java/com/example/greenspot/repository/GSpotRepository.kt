package com.example.greenspot.repository

import androidx.lifecycle.LiveData
import com.example.greenspot.model.GSpot
import com.example.greenspot.data.GreenSpotDao


class GSpotRepository(private val gSpotDao: GreenSpotDao) {

    val readAllGSpot: LiveData<List<GSpot>> = gSpotDao.readAllGSpot()

      fun addGSpot(gSpot: GSpot){
          gSpotDao.addGSpot(gSpot)
    }
    fun updateNote(gSpot: GSpot){
        gSpotDao.updateGSpot(gSpot)
    }
     fun deleteGSpot(gSpot: GSpot){
         gSpotDao.deleteGSpot(gSpot)
    }
  fun deleteAllGSpot(){
      gSpotDao.deleteAllNotes()
    }
}