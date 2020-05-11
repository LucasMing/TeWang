package com.qing.tewang.api.exception;


import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import retrofit2.HttpException;

/**
 * Created by wuliao on 2018/4/17.
 */

public abstract class ExceptionObserver<T> implements Observer<T> {


    @Override
    public void onError(Throwable t) {
        if (t instanceof HttpException) {
            HttpException result = (HttpException) t;
            String body = "";
            try {
                body = new String(result.response().errorBody().bytes(), "utf-8");
                JSONObject object = new JSONObject(body);
                String message = object.getJSONArray(object.names().getString(0)).getString(0);
                onError(new ApiException(t, message));
            } catch (Exception ee) {
                JSONObject object = null;
                try {
                    object = new JSONObject(body);
                    String message = object.getString(object.names().getString(0));
                    onError(new ApiException(t, message));
                } catch (JSONException e) {
                    onError(new ApiException(t, result.message()));
                }
            }
        } else {
            onError(new ApiException(t, t.getMessage()));
        }

    }

    public abstract void onError(ApiException ex);

    public void onNotFound() {

    }

}
