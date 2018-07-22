package com.tomasmichalkevic.seevilnius;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tomasmichalkevic.seevilnius.data.db.AppDatabase;
import com.tomasmichalkevic.seevilnius.data.db.PlaceEntry;

import java.util.List;

public class DetailsViewModel extends AndroidViewModel{

    private LiveData<List<PlaceEntry>> places;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.i("STUFF", "DetailsViewModel:");
        places = database.placeDao().loadAllPlaces();
    }

    public LiveData<List<PlaceEntry>> getTasks() {
        return places;
    }

}
