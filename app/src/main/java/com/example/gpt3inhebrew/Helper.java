package com.example.gpt3inhebrew;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Helper
{
    //place your openai api key here
    public static String open_ai_api_key = "";

    //place your azure translation api key here
    public static String azure_translation_api_key = "";

    //place your azure resource region here
    public static String azure_translation_resource_location = "";

    public static void showError(String message, Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("שגיאה")
                .setMessage(message)
                .setPositiveButton("בסדר", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
}
