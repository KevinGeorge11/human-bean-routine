package com.example.human_bean_routine.Puzzles;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

// Code sourced from https://developer.android.com/training/volley/requestqueue

public class APISingleton {
    private static APISingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private APISingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized APISingleton getInstance(Context context) {
        if (instance == null) {
            instance = new APISingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

