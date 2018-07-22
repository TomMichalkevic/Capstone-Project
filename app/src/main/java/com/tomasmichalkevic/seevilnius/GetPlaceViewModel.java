package com.tomasmichalkevic.seevilnius;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.tomasmichalkevic.seevilnius.data.db.AppDatabase;
import com.tomasmichalkevic.seevilnius.data.db.PlaceEntry;

public class GetPlaceViewModel extends ViewModel {

    private LiveData<PlaceEntry> place;

    public GetPlaceViewModel(AppDatabase database, String placeID) {
        place = database.placeDao().loadPlaceByPlaceId(placeID);
    }

    public LiveData<PlaceEntry> getPlace() {
        return place;
    }

}
