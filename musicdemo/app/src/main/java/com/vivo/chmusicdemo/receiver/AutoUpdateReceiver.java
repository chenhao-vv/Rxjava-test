package com.vivo.chmusicdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vivo.chmusicdemo.service.AutoUpdateService;

public class AutoUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, AutoUpdateService.class);
        try {
            context.startService(intent1);
        } catch(IllegalStateException e) {
            e.printStackTrace();
        }

    }
}
