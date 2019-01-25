package com.android.net;

import android.text.TextUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CookieStringRequest extends StringRequest {

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";

    public CookieStringRequest(int method, String url,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        //return super.parseNetworkResponse(response);

        String parsed;
        try {

            Map<String, String> headers=response.headers;

            if(headers!=null&&headers.containsKey(SET_COOKIE_KEY)){
                String cookie=headers.get(SET_COOKIE_KEY);

                if(!TextUtils.isEmpty(cookie)){
                    // TODO: 将cookie存到本地，如Sharepreference
                    //LogUtils.log("headers:"+cookie);
                    NetConfig.Cookie = cookie;
                }
            }

            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        //return super.getHeaders();

        Map<String, String> headers=super.getHeaders();

        if(headers==null||headers.equals(Collections.emptyMap())){
            headers=new HashMap<>();
        }
        // TODO: 从本地获取到cookie，并把cookie添加到header中
        headers.put(COOKIE_KEY,NetConfig.Cookie);

        return headers;
    }
}

