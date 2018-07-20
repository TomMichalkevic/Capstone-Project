package com.tomasmichalkevic.seevilnius.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM place ORDER BY visited_time")
    LiveData<List<PlaceEntry>> loadAllPlaces();

    @Query("SELECT * FROM place ORDER BY visited_time DESC")
    List<PlaceEntry> loadAllPlacesAsList();

    @Insert
    void insertPlace(PlaceEntry taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlace(PlaceEntry placeEntry);

    @Delete
    void deletePlace(PlaceEntry placeEntry);

    @Query("SELECT * FROM place WHERE id = :id")
    LiveData<PlaceEntry> loadPlaceById(int id);

    @Query("SELECT * FROM place WHERE place_id = :place_id")
    LiveData<PlaceEntry> loadPlaceByPlaceId(String place_id);

    @Query("SELECT * FROM place WHERE place_id = :place_id")
    Cursor loadPlaceByPlaceIdWithProvider(String place_id);
}
