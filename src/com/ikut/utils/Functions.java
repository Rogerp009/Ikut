package com.ikut.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class Functions {

	/** method */
	public static boolean checkLetters(String str) {//validate texto
		boolean respuesta = false;
		if ((str).matches("([a-z]|[A-Z]|\\s)+")) {
			respuesta = true;
		}
		return respuesta;
		
	}//end method 		
	
	public static String getDatePhone(){
	    Calendar cal = new GregorianCalendar();
	    Date date = cal.getTime();
	    SimpleDateFormat vn = new SimpleDateFormat("yyyy-MM-dd");
	    String formatteDate = vn.format(date);
	    return formatteDate;
	    
	}//end method	
	
	public static boolean OnlineAndConecting(Context ctx){		
		return Functions.isOnline(ctx) && Functions.isConnectingToInternet(ctx) ? true : false;		
	}
	public static String getDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateandTime = sdf.format(new Date());
	    return currentDateandTime;
	    
	}//end method	
		
	public static String getHour(){
		SimpleDateFormat hour = new SimpleDateFormat("hh-mm-ss a");
		String currentDateandTime = hour.format(new Date());
	    return currentDateandTime;	    
	}//end method	
	
	public static String getHora(){
		SimpleDateFormat hour = new SimpleDateFormat("hh");
		String currentDateandTime = hour.format(new Date());
	    return currentDateandTime;	    
	}//end method
	
	public static String getMinuto(){
		SimpleDateFormat hour = new SimpleDateFormat("mm");
		String currentDateandTime = hour.format(new Date());
	    return currentDateandTime;	    
	}//end method
	
	public static String getHourDate(){
		SimpleDateFormat hour = new SimpleDateFormat("yy-MM-dd hh-mm-ss a");
		String currentDateandTime = hour.format(new Date());
	    return currentDateandTime;
	    
	}//end method	
	
	/** hay internet? */
	public static boolean isOnline(Context ctx) {
	    ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	    
	}//end 	
	

	/** converti string y limpiar*/
	public static String convertString(InputStream is){
		String json = "";
		String line = "";
		
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);//convertir respuesta a string
			StringBuilder sb = new StringBuilder();
			
			Log.e(Constant.TAG_JSONPARSER, "BufferedReader " + line);
			
			while ((line = reader.readLine()) != null) {//convertir y limpiar
				sb.append(line + "\n");
			}//end while
			
			is.close();
			json = sb.toString();
			
			Log.e(Constant.JSON_TAG, json);
			
		} catch (Exception e) {
			
			Log.e(Constant.TAG_BUFFER_ERROR, "Error convertir resultado " + e.toString());		
		}		
		
		return json;
		
	}//end method convet	
	
    /**
     * Checking for all possible internet providers
     * **/
    public static boolean isConnectingToInternet(Context ctx){
        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        	if (connectivity != null){
        		NetworkInfo[] info = connectivity.getAllNetworkInfo();
        		if (info != null)
        			for (int i = 0; i < info.length; i++)
        				if (info[i].getState() == NetworkInfo.State.CONNECTED){
        					return true;
        				}
        	}
        	return false;
    }	
    

    public static final boolean isValidPhoneNumber(CharSequence target) {  
        return android.util.Patterns.PHONE.matcher(target).matches();   
    }    

    public static int Session(Context ctx){    	
		String email = AccountUtils.getEmail(ctx);

    	if(!Session.SessionExistsAccount(ctx, email) && !Session.SessionExistsPreference(ctx)){//no exit las 2
    		return 1;
    	}else{
    		if(Session.SessionExistsAccount(ctx, email) && !Session.SessionExistsPreference(ctx)){//exite accoeunt pero no la preferencia
    			return 2;
    		}else{
    			if(!Session.SessionExistsAccount(ctx, email)){//no existe la cuenta pero si la pref
    				return 3;
    			}else{
    				if(Session.SessionExistsAccount(ctx, email) && Session.SessionExistsPreference(ctx)){//existe la preferencia y la cuenta
    					return 4;
    				}else{
    					return 0;
    				}
    			}
    		}
    	}
    }//end method    

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }    
    
	/** method */
	public static String SplipFecha(String str) {//validate texto
		String respuesta = "";
		String fecha[];
		fecha = str.split("-");
		if(fecha[1].equals("1") || fecha[1].equals("01")){
			respuesta = fecha[2]+" "+"ene";
		}else{
			if(fecha[1].equals("2") || fecha[1].equals("02")){
				respuesta = fecha[2]+" "+"feb";	
			}else{
				if(fecha[1].equals("3")  || fecha[1].equals("03")){
					respuesta = fecha[2]+" "+"mar";
				}else{
					if(fecha[1].equals("4")  || fecha[1].equals("04")){
						respuesta = fecha[2]+" "+"abr";
					}else{
						if(fecha[1].equals("5")  || fecha[1].equals("05")){
							respuesta = fecha[2]+" "+"may";
						}else{
							if(fecha[1].equals("6")  || fecha[1].equals("06")){
								respuesta = fecha[2]+" "+"jun";
							}else{
								if(fecha[1].equals("7")  || fecha[1].equals("07")){
									respuesta = fecha[2]+" "+"jul";
								}else{
									if(fecha[1].equals("8")  || fecha[1].equals("08")){
										respuesta = fecha[2]+" "+"ago";
									}else{
										if(fecha[1].equals("9")  || fecha[1].equals("09")){
											respuesta = fecha[2]+" "+"sep";
										}else{
											if(fecha[1].equals("10")){
												respuesta = fecha[2]+" "+"oct";
											}else{
												if(fecha[1].equals("11")){
													respuesta = fecha[2]+" "+"nov";
												}else{
													if(fecha[1].equals("12")){
														respuesta = fecha[2]+" "+"dic";
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return respuesta;
		
	}//end method    
}//enc class
