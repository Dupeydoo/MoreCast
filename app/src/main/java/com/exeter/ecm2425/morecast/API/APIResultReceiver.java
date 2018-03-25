package com.exeter.ecm2425.morecast.API;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.exeter.ecm2425.morecast.Views.ErrorDialog;


public class APIResultReceiver extends ResultReceiver {
    private Receiver apiReceiver;

    public APIResultReceiver(Handler handler) {
        super(handler);
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    public void setReceiver(Receiver receiver) {
        this.apiReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(apiReceiver != null) {
            apiReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
