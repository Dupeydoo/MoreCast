package com.exeter.ecm2425.morecast.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


public class ErrorDialog {

    private String message;
    private String title;

    public ErrorDialog(String message, String title) {
        this.message = message;
        this.title = title;
    }

    public void showDialog(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
