package com.exeter.ecm2425.morecast.API;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.exeter.ecm2425.morecast.Views.ErrorDialog;

/**
 * The child class of APIResultReceiver that allows communication
 * between an Activity and the APIService.
 *
 * @author 640010970
 * @version 1.0.0
 */
public class APIResultReceiver extends ResultReceiver {
    private Receiver apiReceiver;

    /**
     * Constructor for the receiver.
     * @param handler Facilitates sending of Messages and Runnables
     *                involved with threads.
     */
    public APIResultReceiver(Handler handler) {
        super(handler);
    }

    /**
     * The interface an Activity must implement to be able to communicate
     * with the APIService.
     */
    public interface Receiver {
        /**
         * Event method in response to the Activity receiving results
         * from the IntentService.
         * @param resultCode The result code signifying if the IntentService
         *                   is running, finished, or encountered an error.
         * @param resultData The bundle of result data sent by the IntentService.
         */
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    /**
     * Sets the APIResultReceiver.
     * @param receiver The receiber object to set.
     */
    public void setReceiver(Receiver receiver) {
        this.apiReceiver = receiver;
    }

    /**
     * Override of result event from the parent Receiver. Calls the interface
     * onReceiveResult method to a code and data.
     * @param resultCode The result code signifying if the IntentService
     *                   is running, finished, or encountered an error.
     * @param resultData The bundle of result data sent by the IntentService.
     */
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(apiReceiver != null) {
            apiReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
