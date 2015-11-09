package com.ikut.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;

import com.ikut.db.SQLite;

public class IkutService extends Service{

	public static final String 	DEBUG_TAG = "IkutService"; // Debug TAG
	private static final String ACTION_START 	= DEBUG_TAG + ".START"; // Action to start
	private static final String ACTION_STOP		= DEBUG_TAG + ".STOP"; // Action to stop
	public static final String ACTION_REFLESH = DEBUG_TAG + ".REFHESH"; // Action to reconnect	
	public static final String ACTION_REFLESH_STOP = DEBUG_TAG + ".REFHESH_STOP"; // Action to reconnect	
	public static final String ACTION_UPDATE = DEBUG_TAG + ".UPDATE"; // Action to reconnect	
	public static final String ACTION_MAIN = DEBUG_TAG + ".MAIN_LIST"; // Action to reconnect	
	
	private SQLite sqlite = null;
	Cursor cursor = null;

	@Override
	public IBinder onBind(Intent arg0) {return null;}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public static void actionStart(Context ctx){
		Intent i = new Intent(ctx, IkutService.class);
		i.setAction(ACTION_START);
		ctx.startService(i);		
	}
	
	public static void actionStop(Context ctx){
		Intent i = new Intent(ctx, IkutService.class);
		i.setAction(ACTION_STOP);
		ctx.startService(i);		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);		
		if(intent != null){
			if(intent.getAction().equals(ACTION_START)){
				start();
			}else{
				if(intent.getAction().equals(ACTION_STOP)){
					stop();
				}else{
					if(intent.getAction().equals(ACTION_UPDATE)){
						update();
					}
				}
			}
		}else{
			start();
		}
		return Service.START_STICKY;
	}

	private synchronized void start() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_REFLESH);
		registerReceiver(Mutex, filter);
	}
	
	private synchronized void stop() {
		unregisterReceiver(Mutex);
	}	
	
	private synchronized void update() {
		// TODO Auto-generated method stub
		
	}
	
	private BroadcastReceiver Mutex = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context ctx, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(ACTION_REFLESH)){
				
			}
		}
	};	
}//end class
