package com.ikut.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Functions;
import com.ikut.utils.Preferences;


public class ServerFunctions {
	
	/**
	 * function make Login Request
	 * @param email
	 * @param password
	 * */
	public static JSONObject loginUser(String email, String password){//check login
		// Building Parameters		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = null;
		JSONObject json2 = new JSONObject();
		Log.e(Constant.TAG_WEB_FUNCTIONS, "JSONObject loginUser");	
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", Constant.LOGINT_TAG));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		json = jsonParser.getJSONFromUrl(Constant.LOGIN_URL, params);
		if(json ==  null || json.toString().equals("") || json.toString().equals("[]") || json.length() == 0){
			try {
				return json2.put(Constant.KEY_ERROR, "-1");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			return json;
		}	
		
		return json;
	}//end method
	
	/**
	 * function make Register Request
	 * @param email
	 * @param password
	 * */	
	public static JSONObject registerUser(Context ctx, String name, String email, String password, String phone, String id){
		// Building Parameters
		Log.d("registerUser", name+" "+email+" "+password);
		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = null;
		JSONObject json2 = new JSONObject();
		
		try {
            GCMRegistrar.setRegisteredOnServer(ctx, false);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", Constant.REGISTER_TAG));
			params.add(new BasicNameValuePair("nombre", name));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("regId", id));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("telefono", phone));
			params.add(new BasicNameValuePair("fecha_registro", Functions.getDate()));
			params.add(new BasicNameValuePair("fecha_modificacion", Functions.getDate()));
			params.add(new BasicNameValuePair("estado", "Activo"));			
			// getting JSON Object
			json = jsonParser.getJSONFromUrl(Constant.REGISTER_URL, params);
			
			if(json ==  null || json.toString().equals("") || json.toString().equals("[]") || json.length() == 0){
				return json2.put(Constant.KEY_ERROR, "-1");
			}else{
				return json;
			}	
		
		} catch (Exception e) {
			// TODO: handle exception
			try {
				json.put(Constant.KEY_ERROR, "-1");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// return json
		return json;
	}	
	
	/**
	 * function make Register Request
	 * @param email
	 * @param estado
	 * */		
	public static JSONObject changeStatus(String email, String status){	
		// Building Parameters		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = null;
		JSONObject json2 = new JSONObject();
		
			Log.e(Constant.TAG_WEB_FUNCTIONS, "JSONObject changeStatus");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "changeStatus"));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("estado", status));
			json = jsonParser.getJSONFromUrl(Constant.LOGIN_URL, params);
			if(json ==  null || json.toString().equals("") || json.toString().equals("[]") || json.length() == 0){
				try {
					return json2.put(Constant.KEY_ERROR, "-1");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return json;
			}
			return json;		
	}
		
	
	public static JSONObject changePass(String email, String pass){	
		// Building Parameters		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = null;
		JSONObject json2 = new JSONObject();
		
			Log.e(Constant.TAG_WEB_FUNCTIONS, "JSONObject changePassword");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "changePassword"));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("password", pass));
			params.add(new BasicNameValuePair("fecha_modificacion", Functions.getDate()));
			json = jsonParser.getJSONFromUrl(Constant.LOGIN_URL, params);
			if(json ==  null || json.toString().equals("") || json.toString().equals("[]") || json.length() == 0){
				try {
					return json2.put(Constant.KEY_ERROR, "-1");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return json;
			}
			return json;		
	}	
	
	public static JSONObject changePhone(String email, String phone){	
		// Building Parameters		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = null;
		JSONObject json2 = new JSONObject();
		
			Log.e(Constant.TAG_WEB_FUNCTIONS, "JSONObject changePhone");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "changePhone"));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("telefono", phone));
			params.add(new BasicNameValuePair("fecha_modificacion", Functions.getDate()));
			json = jsonParser.getJSONFromUrl(Constant.LOGIN_URL, params);
			if(json ==  null || json.toString().equals("") || json.toString().equals("[]") || json.length() == 0){
				try {
					return json2.put(Constant.KEY_ERROR, "-1");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return json;
			}
			return json;		
	}
	
	public static JSONObject downloadMessenger(Context ctx){	
		// Building Parameters		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = null;
		JSONObject json2 = new JSONObject();
		Preferences preferences = new Preferences(ctx);
		
			Log.e(Constant.TAG_WEB_FUNCTIONS, "JSONObject downloadMessenger");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "downloadMessenger"));
			params.add(new BasicNameValuePair("email", preferences.getEmail()));
			json = jsonParser.getJSONFromUrl(Constant.LOGIN_URL, params);
			if(json ==  null || json.toString().equals("") || json.toString().equals("[]") || json.length() == 0){
				try {
					return json2.put(Constant.KEY_ERROR, "-1");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return json;
			}
			return json;		
	}	
	
	public static JSONObject storeMessenger(Context ctx, String asunto, String contenido){	
		// Building Parameters		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = null;
		JSONObject json2 = new JSONObject();
		Preferences preferences = new Preferences(ctx);
		Log.e(Constant.TAG_WEB_FUNCTIONS, "JSONObject storeMessenger");
			
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "storeMessenger"));
		params.add(new BasicNameValuePair("email", preferences.getEmail()));	
		params.add(new BasicNameValuePair("nombre", preferences.getNombre()));
		params.add(new BasicNameValuePair("hora", Functions.getHour()));
		params.add(new BasicNameValuePair("fecha_enviado", Functions.getDate()));					
		params.add(new BasicNameValuePair("asunto", asunto));
		params.add(new BasicNameValuePair("contenido", contenido));
		params.add(new BasicNameValuePair("borrar", "true"));
		params.add(new BasicNameValuePair("regId", AccountUtils.getAccountGCM(ctx, preferences.getEmail())));

		Log.d("DATA", params.toString());
			
		json = jsonParser.getJSONFromUrl(Constant.LOGIN_URL, params);
		if(json ==  null || json.toString().equals("") || json.toString().equals("[]") || json.length() == 0){
			try {
				return json2.put(Constant.KEY_ERROR, "-1");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{		
			return json;
		}
		return json;		
	}	
	
	public static JSONObject downloadMessengerHour(Context ctx){	
		// Building Parameters		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = null;
		JSONObject json2 = new JSONObject();
		Preferences preferences = new Preferences(ctx);
		
			Log.e(Constant.TAG_WEB_FUNCTIONS, "JSONObject downloadMessengerHour");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "downloadMessengerHour"));
			params.add(new BasicNameValuePair("email", preferences.getEmail()));			
			params.add(new BasicNameValuePair("hora", Functions.getHour()));

			Log.d("DATA", params.toString());
			
			json = jsonParser.getJSONFromUrl(Constant.LOGIN_URL, params);
			if(json ==  null || json.toString().equals("") || json.toString().equals("[]") || json.length() == 0){
				try {
					return json2.put(Constant.KEY_ERROR, "-1");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return json;
			}
			return json;		
	}
	
	public static JSONObject checkMessenger(Context ctx){	
		// Building Parameters		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = null;
		JSONObject json2 = new JSONObject();
		Preferences preferences = new Preferences(ctx);
		
			Log.e(Constant.TAG_WEB_FUNCTIONS, "JSONObject MessengerExisted");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "MessengerExisted"));
			params.add(new BasicNameValuePair("email", preferences.getEmail()));
			params.add(new BasicNameValuePair("t_hora", Functions.getHora()));
			params.add(new BasicNameValuePair("t_minuto", Functions.getMinuto()));	
			params.add(new BasicNameValuePair("fecha_enviado", Functions.getDate()));

			Log.d("DATA", params.toString());
			
			json = jsonParser.getJSONFromUrl(Constant.LOGIN_URL, params);
			if(json ==  null || json.toString().equals("") || json.toString().equals("[]") || json.length() == 0){
				try {
					return json2.put(Constant.KEY_ERROR, "-1");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return json;
			}
			return json;		
	}	
	
	public static JSONObject MessengerExistedDownload(Context ctx){	
		// Building Parameters		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = null;
		JSONObject json2 = new JSONObject();
		Preferences preferences = new Preferences(ctx);
		
			Log.e(Constant.TAG_WEB_FUNCTIONS, "JSONObject MessengerExisted");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "MessengerExisted"));
			params.add(new BasicNameValuePair("email", preferences.getEmail()));
			params.add(new BasicNameValuePair("t_hora", Functions.getHora()));	
			params.add(new BasicNameValuePair("hora", Functions.getHour()));

			Log.d("DATA", params.toString());
			
			json = jsonParser.getJSONFromUrl(Constant.LOGIN_URL, params);
			if(json ==  null || json.toString().equals("") || json.toString().equals("[]") || json.length() == 0){
				try {
					return json2.put(Constant.KEY_ERROR, "-1");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return json;
			}
			return json;		
	}
	
	public static JSONObject forgetPass(Context ctx){	
		// Building Parameters		
		JSONParser jsonParser = new JSONParser();
		JSONObject json = null;
		JSONObject json2 = new JSONObject();
		Preferences preferences = new Preferences(ctx);
			Log.e(Constant.TAG_WEB_FUNCTIONS, "JSONObject forgetPass");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "forgetPassword"));
			params.add(new BasicNameValuePair("email", preferences.getEmail()));
			params.add(new BasicNameValuePair("fecha_modificacion", Functions.getDate()));
			json = jsonParser.getJSONFromUrl(Constant.LOGIN_URL, params);
			if(json ==  null || json.toString().equals("") || json.toString().equals("[]") || json.length() == 0){
				try {
					return json2.put(Constant.KEY_ERROR, "-1");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return json;
			}
			return json;		
	}	
}//end class
