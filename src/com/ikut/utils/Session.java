package com.ikut.utils;

import android.content.Context;

public class Session {
	    
    public static boolean SessionExistsAccount(Context context, String email) {
        // Check if the user account exists
        if (AccountUtils.getUserAccount(context, email) == null) {
        	return false;
        }
        return true;
    }   
    
    public static boolean SessionExistsPreference(Context context) {
        Preferences preferences = new Preferences(context);

        // Check if the user name exists in the Shared preferences
        String email = preferences.getEmail();
        if (email == null || email.equals(Preferences.DEFAULT_EMAIL)) {
            return false;
        }
        return true;
    }       
    
    public static boolean SessionExistsPreferenceAccount(Context context){
        Preferences preferences = new Preferences(context);
        String email2 = preferences.getEmail();    	
    	if(SessionExistsAccount(context, email2) && SessionExistsPreference(context)){
    		return true;
    	}else{
    		return false;
    	}
    }
}//end class
