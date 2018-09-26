package com.hittareviews;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestData {

    private static String JSON_URL = "https://api.hitta.se/search/v7/app/company/ctyfiintu";
    private static String SAVE_REVIEW_URL = "https://test.hitta.se/reviews/v1/company";

    private static RequestData mInstance;
    private RequestQueue requestQueue;

    private RequestData(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized RequestData getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestData(context);
        }

        return mInstance;
    }

    public void getData(final Response.Listener<String> onSuccess, final Response.ErrorListener onError) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                JSON_URL,
                null,
                response -> {
                    try {
                        // Get the displayName from the JSON - this wasn't parsed in pojo objects cause for this case we are only using the displayName
                        String name = ((JSONObject) (
                                response.getJSONObject("result")
                                        .getJSONObject("companies")
                                        .getJSONArray("company")
                                        .get(0)))
                                .getString("displayName");
                        onSuccess.onResponse(name);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                    onError.onErrorResponse(error);
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void sendReviewRequest(String userName, String comment, int rating, final Response.Listener<String> onSuccess, final Response.ErrorListener onError) {

        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("userName", userName);
            jsonBody.put("comment", comment);
            jsonBody.put("companyId", "ctyfiintu");
            jsonBody.put("score", rating);

            CustomJsonObjectRequest jsonOblect = new CustomJsonObjectRequest(Request.Method.POST, SAVE_REVIEW_URL, jsonBody,
                    response -> onSuccess.onResponse(response != null ? response.toString() : ""),
                    error -> {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                        onError.onErrorResponse(error);
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("X-HITTA-DEVICE-NAME", "MOBILE_WEB");
                    headers.put("X-HITTA-SHARED-IDENTIFIER", "15188693697264027");
                    return headers;
                }
            };
            requestQueue.add(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
