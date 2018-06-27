package com.tomasmichalkevic.seevilnius.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkRequestQueue {

    private static NetworkRequestQueue instance;
    private Context context;
    private RequestQueue requestQueue;

    private NetworkRequestQueue(Context context){
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public static synchronized NetworkRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkRequestQueue(context);
        }
        return instance;
    }

}
