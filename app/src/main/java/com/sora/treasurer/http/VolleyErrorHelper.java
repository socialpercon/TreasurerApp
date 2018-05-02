package com.sora.treasurer.http;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rupertdurano on 30/01/2018.
 */

public class VolleyErrorHelper {
    private VolleyErrorHelper() {}

    /**
     *
     * @param error
     * @param context
     * @return Returns appropriate message which is to be displayed to the user against
     * the specified error object.
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return "Connection timed out.";
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);
        } else if (isNetworkProblem(error)) {
            return "No internet connection";
        }
        return "Connection error.";
    }

    /**
     *
     * @param error
     * @param context
     * @return Return generic message for errors
     */
    public static String getErrorType(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return "Connection Timeout";
        } else if (error instanceof ServerError) {
            return "Something wrong with the query or server.";
        } else if (error instanceof AuthFailureError) {
            return "Authentication failed.";
        } else if (error instanceof NetworkError) {
            return "Network failed.";
        } else if (error instanceof NoConnectionError) {
            return "No internet connection";
        } else if (error instanceof ParseError) {
            return "Error parsing data.";
        }
        return "Connection error.";
    }

    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server
     *
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock
     * message or to show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 401:
                    try {
                        // server might return error like this { "error":
                        // "Some error occured" }
                        // Use "Gson" to parse the result
                        HashMap<String, String> result = new Gson().fromJson(new String(response.data),
                                new TypeToken<Map<String, String>>() { }.getType());

                        if (result != null && result.containsKey("error")) {
                            return result.get("error");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request
                    return error.getMessage();

                default:
                    return "Something wrong with the query.";
            }
        }
        return "Connection error.";
    }
}
