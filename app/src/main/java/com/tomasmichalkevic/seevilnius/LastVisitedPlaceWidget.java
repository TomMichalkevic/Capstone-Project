package com.tomasmichalkevic.seevilnius;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import com.tomasmichalkevic.seevilnius.data.db.PlaceEntry;
import com.tomasmichalkevic.seevilnius.utils.ReadDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

//Reused instructions from https://medium.com/android-bits/android-widgets-ad3d166458d3

public class LastVisitedPlaceWidget extends AppWidgetProvider {

    private PendingIntent service;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent i = new Intent(context, WidgetUpdateService.class);

        if (service == null) {
            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        Objects.requireNonNull(manager).setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60000, service);
        //if you need to call your service less than 60 sec
        //answer is here:
        //http://stackoverflow.com/questions/29998313/how-to-run-background-service-after-every-5-sec-not-working-in-android-5-1
        Log.d("UpdatingWidget: ","onUpdate");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("UpdatingWidget: ","onReceive");
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.d("UpdatingWidget: ","onAppWidgetOptionsChanged");

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d("UpdatingWidget: ","onDeleted");

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d("UpdatingWidget: ","onEnabled");

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d("UpdatingWidget: ","onDisabled");

    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
        Log.d("UpdatingWidget: ","onRestored");

    }

    public static class WidgetUpdateService extends Service {

        WidgetUpdateService() {
            super();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            //Cursor cursor = getContentResolver().query(BASE_CONTENT_URI, null, null, null, null);
            String lastUpdate = "";
            List<PlaceEntry> placeEntryList = new ArrayList<>();
            try {
                placeEntryList.addAll(new ReadDatabase().execute(getApplicationContext()).get());
            } catch (InterruptedException | ExecutionException e) {
                Log.e("CURSOR", "onStartCommand: ", e);
            }
            if(placeEntryList.size()>0){
                lastUpdate = placeEntryList.get(0).getAddress();
            }
            // Reaches the view on widget and displays the number
            RemoteViews view = new RemoteViews(getPackageName(), R.layout.last_visited_place_widget);
            view.setTextViewText(R.id.last_visited_tv, lastUpdate);
            ComponentName theWidget = new ComponentName(this, LastVisitedPlaceWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(theWidget, view);
            Log.i("stuff", "onStartCommand: ");
//            if(cursor!=null){
//                cursor.close();
//            }
            return super.onStartCommand(intent, flags, startId);
        }
    }
}
