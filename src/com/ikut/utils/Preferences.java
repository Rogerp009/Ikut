package com.ikut.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
	
    private static final String FILE_NAME = "com.ikut.preferences";
    private static final String EMAIL_KEY = "email";
    public static final String DEFAULT_EMAIL = "";
    public static final String DEFAULT_NAME = "";
    public static final String DEFAULT_ESTADO = "";
    public static final String DEFAULT_PHONE = "";
	public static String DEFAULT_REGISTRO = "";
	public static String DEFAULT_MODIFICACION= "";
	
    public static final boolean DEFAULT_FLAT = false;
    public static final String FLAT = "flat";
    
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Preferences.FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    
    /** email */
    public String getEmail() {
        String username = sharedPreferences.getString(Preferences.EMAIL_KEY, DEFAULT_EMAIL);
        return username;
    }

    public void setEmail(String phone) {
        editor.putString(Preferences.EMAIL_KEY, phone);
        editor.commit();
    }

    /** email */
    public String getPhone() {
        String phone = sharedPreferences.getString(Constant.KEY_TELEFONO, DEFAULT_PHONE);
        return phone;
    }

    public void setPhone(String phone) {
        editor.putString(Constant.KEY_TELEFONO, phone);
        editor.commit();
    }
    
    public String getNombre() {
        String name = sharedPreferences.getString(Constant.KEY_NOMBRE, DEFAULT_NAME);
        return name;
    }

    public void setNombre(String name) {
        editor.putString(Constant.KEY_NOMBRE, name);
        editor.commit();
    }
    
    public String getEstado() {
        String status = sharedPreferences.getString(Constant.KEY_ESTADO, DEFAULT_ESTADO);
        return status;
    }

    
    public void setEstado(String status) {
        editor.putString(Constant.KEY_ESTADO, status);
        editor.commit();
    }    
    
    
    public void setFlat(boolean flat) {
        editor.putBoolean(Preferences.FLAT, flat);
        editor.commit();
    }
    
    public boolean getFlat() {
    	boolean flat = sharedPreferences.getBoolean(Preferences.FLAT, DEFAULT_FLAT);
        return flat;
    }    

    public String getRegistro() {
        String registro = sharedPreferences.getString(Constant.KEY_FECHA_REGISTRO, DEFAULT_REGISTRO);
        return registro;
    }

    public void setRegistro(String registro) {
        editor.putString(Constant.KEY_FECHA_REGISTRO, registro);
        editor.commit();
    }    
    
    public String getModificacion() {
        String phone = sharedPreferences.getString(Constant.KEY_FECHA_MODIFICACION, DEFAULT_MODIFICACION);
        return phone;
    }

    public void setModificacion(String modificacion) {
        editor.putString(Constant.KEY_FECHA_MODIFICACION, modificacion);
        editor.commit();
    }    
    
    public String getGCM_ID() {
        String id = sharedPreferences.getString("GCM_ID", "");
        return id;
    }

    public void setGCM_ID(String id) {
        editor.putString("GCM_ID", id);
        editor.commit();
    }        
    /** |||||||||||||||||||||| extras |||||||||||||||||||||||||| */       
    public int getServerPass() {
        int change = sharedPreferences.getInt("change_password", 0);
        return change;
    }

    public void setServerPass(int change) {
        editor.putInt("change_password", change);
        editor.commit();
    }     
    
    public int getServerPhone() {
        int phone = sharedPreferences.getInt("change_phone", 0);
        return phone;
    }

    public void setServerPhone(int phone) {
        editor.putInt("change_phone", phone);
        editor.commit();
    }        
    
    public String getPassMoment() {
        String status = sharedPreferences.getString("MOMENT", "");
        return status;
    }

    
    public void setPassMoment(String moment) {
        editor.putString("MOMENT", moment);
        editor.commit();
    }        
    
    /** error */
    public int getFlatError() {
        int error = sharedPreferences.getInt("ERROR", 0);
        return error;
    }

    
    public void setFlatError(int flat) {
        editor.putInt("ERROR", flat);
        editor.commit();
    }  
    
    /** error */
    public boolean getShowCase() {
        boolean ShowCase = sharedPreferences.getBoolean("ShowCase", false);
        return ShowCase;
    }

    
    public void setShowCase(boolean ShowCase) {
        editor.putBoolean("ShowCase", ShowCase);
        editor.commit();
    }
    
    /** notification */
    public boolean getVibrate() {
    	boolean vb = sharedPreferences.getBoolean("Vibrar", false);
        return vb;
    }
    
    public void setVibrate(boolean flat) {
        editor.putBoolean("Vibrar", flat);
        editor.commit();
    }      
    
    public String getSound() {
    	String sn = sharedPreferences.getString("Sound", "");
        return sn;
    }
    
    public void setSound(String flat) {
        editor.putString("Sound", "");
        editor.commit();
    } 
    
    public boolean getNotification() {
    	boolean nt = sharedPreferences.getBoolean("Notification", false);
        return nt;
    }
    
    public void setNotification(boolean flat) {
        editor.putBoolean("Notification", flat);
        editor.commit();
    }   
    
    public boolean getTour() {
    	boolean nt = sharedPreferences.getBoolean("TOUR", false);
        return nt;
    }
    
    public void setTour(boolean tour) {
        editor.putBoolean("TOUR", tour);
        editor.commit();
    }      

    
    public String getCode() {
        String code = sharedPreferences.getString("CODE", "");
        return code;
    }
    public void setCode(String code) {
        editor.putString("CODE", code);
        editor.commit();
    }       
    
    public void clear() {
        editor.clear();
        editor.commit();
    }    
}//end class
