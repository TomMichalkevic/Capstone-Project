package com.tomasmichalkevic.seevilnius;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.tomasmichalkevic.seevilnius.data.db.AppDatabase;

public class GetPlaceViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase database;
    private final String placeID;

    public GetPlaceViewModelFactory(AppDatabase database, String placeID) {
        this.database = database;
        this.placeID = placeID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new GetPlaceViewModel(database, placeID);
    }
}
