package com.google.ar.core.examples.java.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class DialogNoAnswer {

    AlertDialog.Builder ad;

    public DialogNoAnswer(Context context){
        ad=new AlertDialog.Builder(context);

        ad.setMessage("답을 입력하세요!").setNeutralButton("확인",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

            }

        }).create();
    }

    public void show(){
        ad.show();
    }
}
