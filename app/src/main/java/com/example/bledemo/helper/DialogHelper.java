package com.example.bledemo.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

public class DialogHelper {
    public static AlertDialog getInputDialog(
            Context context,
            String title,
            String posTitle,
            String negTitle,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener
            ) 
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(posTitle, positiveListener);
        builder.setNegativeButton(negTitle, negativeListener);
        return builder.create();
    }
}
