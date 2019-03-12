package com.wangkeke.virtualapkhost;

import android.content.Context;
import android.widget.Toast;

public class ShowUtils {

    public static void showToast(Context context,String tip){
        Toast.makeText(context, ""+tip, Toast.LENGTH_SHORT).show();
    }
}
