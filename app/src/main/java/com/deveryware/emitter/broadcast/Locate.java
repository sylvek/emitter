/**
 *
 * Copyright (C) 2011 Deveryware S.A. All Rights Reserved.
 *  
 * @author sylvek
 *
 * Created on 8 mai 2011
 *
 */
package com.deveryware.emitter.broadcast;

import com.deveryware.emitter.Constants;
import com.deveryware.library.GiftClient;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * @author sylvek
 * 
 */
public class Locate extends BroadcastReceiver {

    private static final long DEFAULT_DELAY = 1000L;

    public static final String MESSAGE = "com.deveryware.emitter.LOCATE";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i(Constants.NAME, "phone is locating ...");

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent answer = new Intent(context, Answer.class);

        final String id = intent.getStringExtra(GiftClient.CMD_ID);
        answer.putExtra(GiftClient.CMD_ID, id);
        answer.putExtra(GiftClient.CMD_ACK, 0 /* 1 si erreur */);

        final String[] params = intent.getStringArrayExtra(GiftClient.CMD_PARAMS);
        long delay = DEFAULT_DELAY;
        if (params != null && params.length == 1) {
            try {
                delay = Long.valueOf(params[0]);
            } catch (NumberFormatException e) {
                Log.e(Constants.NAME, "unable to parse delay : " + params[0]);
            }
        } else {
            Log.w(Constants.NAME, "no delay found. We use the default value : " + DEFAULT_DELAY);
        }

        final PendingIntent answerIt = PendingIntent.getBroadcast(context, 0, answer, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, answerIt);
        Log.i(Constants.NAME, "locating in " + delay + " ms.");
    }
}
