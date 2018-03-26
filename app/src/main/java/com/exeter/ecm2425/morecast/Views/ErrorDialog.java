package com.exeter.ecm2425.morecast.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Allows Activites to display error alerts when something
 * goes wrong.
 *
 * @author 640010970
 * @version 1.0.0
 */
public class ErrorDialog {

    private String message;
    private String title;

    /**
     * Create a dialog with a message and title.
     * @param message The error message.
     * @param title The title of the error.
     */
    public ErrorDialog(String message, String title) {
        this.message = message;
        this.title = title;
    }

    /**
     * Show the error dialog.
     * @param context The context to display the dialog on.
     */
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
