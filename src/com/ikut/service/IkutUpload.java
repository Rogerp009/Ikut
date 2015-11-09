package com.ikut.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.ikut.db.SQLite;
import com.ikut.utils.Constant;
import com.ikut.utils.Functions;
import com.ikut.utils.Preferences;
import com.ikut.web.ServerFunctions;

public class IkutUpload extends IntentService{
	
    public IkutUpload() {
		super("IntenService");
		// TODO Auto-generated constructor stub
	}
	private String TAG = getClass().getName();    
    private String pass = "";
    private String email = "";	
    private String phone = "";
    private String contenido = "";
    private String asunto = "";

    private String data = "";
    private Preferences pref = null;
    JSONObject json = null;
    JSONObject jsonReplace = null;
    int status = 0;
    int status2 = 0;
    int status3 = 0;
    int status4 = 0;
    int status5 = 0;
    JSONObject j =  null;
    
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		json = new JSONObject();
		jsonReplace = new JSONObject();
		pref = new Preferences(getApplicationContext());	
		email = pref.getEmail();
		try {
			pass = pref.getPassMoment();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		if(intent != null){
			if(intent.getAction().equals(Constant.CHANGE_PASSWORD) == true){
				try {
					status = changePassword();
					if(status < 0){
						getApplicationContext().sendBroadcast(new Intent(Constant.PASSWORD_RECONET));
					}else{
						getApplicationContext().sendBroadcast(new Intent(Constant.OK));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else{
				if(intent.getAction().equals(Constant.CHANGE_PHONE) == true){
					phone = intent.getExtras().getString(Constant.KEY_TELEFONO);
					try {
						status2 = changePhone();
						if(status2 < 0){
							getApplicationContext().sendBroadcast(new Intent(Constant.PHONE_RECONET));
						}else{
							getApplicationContext().sendBroadcast(new Intent(Constant.OK));
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}else{
					if(intent.getAction().equals(Constant.SEND)){
						data = intent.getExtras().getString(Constant.JSON_TAG);
						Log.d(TAG, data);
						try {
							j = new JSONObject(data);
							asunto = j.getString(Constant.ASUNTO);
							contenido  = j.getString(Constant.CONTENIDO);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						try {
							status3 = SendMessenger();
							if(status3 < 0){
								getApplicationContext().sendBroadcast(new Intent(Constant.MESSENGER_SEND));
							}else{
								getApplicationContext().sendBroadcast(new Intent(Constant.OK));
							}
						} catch (Exception e) {
							// TODO: handle exception
						}						
					}else{
						if(intent.getAction().equals(Constant.FORGET_PASSWORD) == true){
							try {
								status4 = forgetPassword();
								if(status4 < 0){
									getApplicationContext().sendBroadcast(new Intent(Constant.PASSWORD_RECONET));
								}else{
									getApplicationContext().sendBroadcast(new Intent(Constant.FORGET_PASSWORD_OK));
								}
							} catch (Exception e) {
								// TODO: handle exception
							}							
						}
					}
				}
			}			
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(new Intent(getApplicationContext(), IkutUpload.class));
	}
		
	public int changePassword(){

		Log.e(TAG, "Ikut doInBackground");
		try {
			if(Functions.isConnectingToInternet(getApplicationContext()) && Functions.isOnline(getApplicationContext())){
				while (Functions.isOnline(getApplicationContext()) && Functions.isConnectingToInternet(getApplicationContext())) {					
					json = ServerFunctions.changePass(email, pass);
					Log.e(TAG, json.toString()+ " " + "setPassword()");					
					if(json.get(Constant.KEY_ERROR).equals("-1") || json.isNull(Constant.KEY_SUCCESS) || json == null){
						return -1;
					}else{
						if(json.getString(Constant.KEY_SUCCESS) != null){
							String res = json.getString(Constant.KEY_SUCCESS); 
							if(Integer.parseInt(res) == 1){
								return 1;// success
							}else{
								return -1;//password and email incorrecto
							}								
						}else{
							return -1;//error sel servidor o interno
						}
					}
				}//while					
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString()+" "+ Constant.TRY_CATCH);
			return -1;//error sel servidor o interno				
		}
		return -1;		
	}
	
	public int changePhone(){
		Log.e(TAG, "Ikut doInBackground");
		try {
			if(Functions.isConnectingToInternet(getApplicationContext()) && Functions.isOnline(getApplicationContext())){
				while (Functions.isOnline(getApplicationContext()) && Functions.isConnectingToInternet(getApplicationContext())) {					
					json = ServerFunctions.changePhone(email, phone);
					Log.e(TAG, json.toString()+ " " + "setPhone()");					
					if(json.get(Constant.KEY_ERROR).equals("-1") || json.isNull(Constant.KEY_SUCCESS) || json == null){
						return -1;
					}else{
						if(json.getString(Constant.KEY_SUCCESS) != null){
							String res = json.getString(Constant.KEY_SUCCESS); 
							if(Integer.parseInt(res) == 1){
								return 1;// success
							}else{
								return -1;//password and email incorrecto
							}								
						}else{
							return -1;//error sel servidor o interno
						}
					}
				}//while					
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString()+" "+ Constant.TRY_CATCH);
			return -1;//error sel servidor o interno				
		}
		return -1;		
	}
	
	public int SendMessenger(){
		SQLite sqlite = new SQLite(getApplicationContext());
		sqlite.abrir();
		Preferences preferences = new Preferences(getApplicationContext());
		Log.e(TAG, "Ikut doInBackground");
		try {
			if(Functions.isConnectingToInternet(getApplicationContext()) && Functions.isOnline(getApplicationContext())){
				while (Functions.isOnline(getApplicationContext()) && Functions.isConnectingToInternet(getApplicationContext())) {					
					json = ServerFunctions.storeMessenger(getApplicationContext(), asunto, contenido);					
					if(json.get(Constant.KEY_ERROR).equals("-1") || json.isNull(Constant.KEY_SUCCESS) || json == null){
						return -1;
					}else{
						if(json.getString(Constant.KEY_SUCCESS) != null){
							String res = json.getString(Constant.KEY_SUCCESS); 
							if(Integer.parseInt(res) == 1){							
								if(sqlite.addMessengerUser(Functions.getHour(), Functions.getDate(), asunto, contenido, preferences.getEmail(), preferences.getNombre())){
									//getApplicationContext().sendBroadcast(new Intent("com.ikut.REFRESH"));
									Log.d("store Sqlite", "BIEN IkutUpload");
								}else{
									Log.e("store Sqlite", "MAL IkutUpload");
								}	
								sqlite.cerrar();
								return 1;// success
							}else{
								return -1;//password and email incorrecto
							}								
						}else{
							return -1;//error sel servidor o interno
						}
					}
				}//while					
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString()+" "+ Constant.TRY_CATCH);
			return -1;//error sel servidor o interno				
		}
		return -1;		
	}	
	
	
	public int forgetPassword(){
		Log.e(TAG, "Ikut doInBackground");
		Preferences preferences = new Preferences(getApplicationContext());
		String code = "";
		try {
			if(Functions.isConnectingToInternet(getApplicationContext()) && Functions.isOnline(getApplicationContext())){
				while (Functions.isOnline(getApplicationContext()) && Functions.isConnectingToInternet(getApplicationContext())) {					
					json = ServerFunctions.forgetPass(getApplicationContext());
					Log.e(TAG, json.toString()+ " " + "forgetPassword()");					
					if(json.get(Constant.KEY_ERROR).equals("-1") || json.isNull(Constant.KEY_SUCCESS) || json == null){
						return -1;
					}else{
						if(json.getString(Constant.KEY_SUCCESS) != null){
							String res = json.getString(Constant.KEY_SUCCESS); 
							if(Integer.parseInt(res) == 1){
								code = json.getString("codigo");
								preferences.setCode(code);
								return 1;// success
							}else{
								return -1;//password and email incorrecto
							}								
						}else{
							return -1;//error sel servidor o interno
						}
					}
				}//while					
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString()+" "+ Constant.TRY_CATCH);
			return -1;//error sel servidor o interno				
		}
		return -1;		
	}	
	
}//end class
