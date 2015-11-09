package com.ikut.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScheduleReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(getClass().getCanonicalName(), "onReceive");
		context.stopService(new Intent(context, GCMIntentService.class));
		context.sendBroadcast(new Intent("com.ikut.change.login"));
	}
}//end class