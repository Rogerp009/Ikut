package com.ikut.service;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ikut.Login;
import com.ikut.R;
import com.ikut.UserAccount;
import com.ikut.db.SQLite;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Functions;
import com.ikut.utils.Preferences;

public class GCMIntentService extends IntentService{

	private static final String TAG = "GCMIntentService";
	public static final int notifyID = 9001;
	NotificationCompat.Builder builder;
	String nombre = ""; 
	String hora = ""; 
	String email = ""; 
	String fecha= ""; 
	String message = "";
	private String asunto;
	SQLite sqlite = null;
    NotificationManager mNotificationManager;
    Intent resultIntent = null;
    PendingIntent resultPendingIntent = null;
    public static int numMessages = 0;
    long[] pattern = null;
    Uri defaultSound = null;    
    Preferences preferences = null;
	 public GCMIntentService() {
		super(TAG);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		preferences = new Preferences(getApplicationContext());
		
		resultIntent = new Intent(this, UserAccount.class);
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);	    
	    
		sqlite = new SQLite(getApplicationContext());
		sqlite.abrir();	    
		if(!preferences.getVibrate()){
		    pattern = new long[]{1000, 500, 1000};
		}else{
		    pattern = new long[]{0,0,0};
		}
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		if(AccountUtils.getAccounts(getApplicationContext())){
			if(!preferences.getNotification()){
				if (!extras.isEmpty()) {
					if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
						Log.d("Send error: " , extras.toString());
					} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
						Log.d("Deleted messages on server: " , extras.toString());
					} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
						Log.i(TAG, "Extras"+ extras.toString());						
						asunto = extras.getString("asunto");
						nombre = extras.getString("nombre");
						hora = extras.getString("hora");
						email = extras.getString("email");
						fecha = extras.getString("fecha");
						message = extras.getString("message");
						if(sqlite.addMessenger(hora, Functions.getDate(), asunto, message, email, nombre)){
							sendNotification(); //When Message is received normally from GCM Cloud Server
							Log.d("store Sqlite", "BIEN GCMIntentService");
						}else{
							Log.e("store Sqlite", "MAL GCMIntentService");
						}					
						
					}
				}
				GcmBroadcastReceiver.completeWakefulIntent(intent);						
			}
		}else{
			goToMain();
		}	
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sqlite.cerrar();
	}

	private void sendNotification() {
	    resultIntent.setAction(Intent.ACTION_MAIN);
	    resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);
		numMessages = 0;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            Notification_API_OLD();
        }else{
        	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            	Notification();
        	}
        }
        
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void Notification() {
		// TODO Auto-generated method stub     
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.ic_launcher)
    	        .setContentTitle(nombre)
    	        .setNumber(numMessages)
    	        .setLights(0xffff00, 2000, 2000)
    	        .setVibrate(pattern)
    	        .setSound(defaultSound)
    	        .setContentIntent(resultPendingIntent)
    	        .setContentText(asunto); //.setContentInfo(contentTitle);
        
		NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
		bigTextStyle.bigText(message);
		bigTextStyle.setSummaryText(hora);    	
		mBuilder.setStyle(bigTextStyle);        
    	mBuilder.setAutoCancel(true);
    	
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    	stackBuilder.addParentStack(UserAccount.class);
    	stackBuilder.addNextIntent(resultIntent);    	
    	PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
    	mBuilder.setContentIntent(resultPendingIntent);
    	mNotificationManager.notify(1, mBuilder.build());
	}

	private void Notification_API_OLD() {
		// TODO Auto-generated method stub
		 NotificationCompat.Builder mNotifyBuilder;	   
	     mNotifyBuilder = new NotificationCompat.Builder(this)
	     .setContentTitle(asunto)
	     .setContentText(message)
	     .setSmallIcon(R.drawable.ic_launcher);
	      // Set pending intent
	     mNotifyBuilder.setContentIntent(resultPendingIntent);	        
	        // Set the content for Notification
	        mNotifyBuilder.setVibrate(pattern);
	        mNotifyBuilder.setLights(0xffff00, 2000, 2000);
	        mNotifyBuilder.setSound(defaultSound);
	        mNotifyBuilder.setNumber(numMessages);
	        mNotifyBuilder.setContentTitle(nombre);
	        mNotifyBuilder.setContentText(message);
	        // Set autocancel
	        mNotifyBuilder.setAutoCancel(true);
	        // Post a notification
	        
	    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	    	stackBuilder.addParentStack(UserAccount.class);
	    	stackBuilder.addNextIntent(resultIntent);    	
	    	PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
	    	mNotifyBuilder.setContentIntent(resultPendingIntent);	        
	        mNotificationManager.notify(notifyID, mNotifyBuilder.build());		
    }//end method	
	
	
	private void goToMain() {//ir a login o account user
		// TODO Auto-generated method stub
		TimerTask task = new TimerTask() {//instances timer
			
			@Override
			public void run() {
				// TODO Auto-generated method stub				
				startActivity(new Intent(getApplicationContext(), Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
			}//end run
		};//end TimerTask
				
		Timer timer = new Timer();//process 2 seconds		
		timer.schedule(task, Constant.SPLASH_SCREEN_DELAY);		
	}	
}//end class
