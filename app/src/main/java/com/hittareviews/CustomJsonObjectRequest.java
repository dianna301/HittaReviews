package com.hittareviews;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * this is a custom class created for treating the response of the post request
 * - for our particular save_review req the server response is 201, but the response is empty and by default the JsonObjectRequest treats this as an error response
 */
public class CustomJsonObjectRequest extends JsonObjectRequest {

    public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            JSONObject result = null;

            if (jsonString.length() > 0)
                result = new JSONObject(jsonString);

            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}