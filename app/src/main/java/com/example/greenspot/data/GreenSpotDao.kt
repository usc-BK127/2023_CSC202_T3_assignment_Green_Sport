package com.example.greenspot.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.greenspot.model.GSpot

@Dao
interface GreenSpotDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun addGSpot(gSpot: GSpot)

    @Query("SELECT * FROM g_spot_table ORDER BY id ASC")
     fun readAllGSpot() : LiveData<List<GSpot>>

     @Update
     fun updateGSpot(gSpot: GSpot)

     @Delete
     fun deleteGSpot(gSpot: GSpot)

     @Query("DELETE FROM g_spot_table")
   fun deleteAllNotes()
}