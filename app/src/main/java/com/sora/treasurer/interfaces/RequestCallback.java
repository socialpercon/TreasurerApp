package com.sora.treasurer.interfaces;

import com.android.volley.VolleyError;

public interface RequestCallback {

    void onResponse(Object response);
    void onErrorResponse(VolleyError error);
}
