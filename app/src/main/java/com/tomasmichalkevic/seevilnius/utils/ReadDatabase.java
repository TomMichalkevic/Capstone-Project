package com.tomasmichalkevic.seevilnius.utils;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.tomasmichalkevic.seevilnius.data.db.AppDatabase;
import com.tomasmichalkevic.seevilnius.data.db.PlaceDao;
import com.tomasmichalkevic.seevilnius.data.db.PlaceEntry;

import java.util.List;

public class ReadDatabase extends AsyncTask<Context, Void, List<PlaceEntry>> {

    @Override
    protected List<PlaceEntry> doInBackground(Context... contexts) {
        AppDatabase appDatabase = Room.databaseBuilder(contexts[0], AppDatabase.class, "place").build();
        PlaceDao dao = appDatabase.placeDao();
        return dao.loadAllPlacesAsList();
    }

    @Override
    protected void onPostExecute(List<PlaceEntry> placeEntries) {
        super.onPostExecute(placeEntries);
    }
}
