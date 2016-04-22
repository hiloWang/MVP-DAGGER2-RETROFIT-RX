package com.github.hilo.util;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

public class ToastUtils {

    private Context context;

    @Inject
    public ToastUtils(Context context) {
        this.context = context;
    }

    public void makeText(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void makeText(String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }
}
