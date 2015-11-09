package com.ikut.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import com.ikut.R;

public class AccountUtils {

    /**
     * The tag used for log
     */
    private static final String LOG_TAG = AccountUtils.class.getSimpleName();
    public static Account getUserAccount(Context context, String email) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(context.getResources().getString(R.string.account_type));
        for (Account account : accounts) {
            if (account.name.equalsIgnoreCase(email)) {
            	return account;
            }
        }

        return null;
    }

    
    public static boolean getAccounts(Context context) {
    	AccountManager accountManager = AccountManager.get(context);
    	Account[] accounts = accountManager.getAccountsByType(context.getResources().getString(R.string.account_type));
    	if(accounts.length > 0 ){
    		return true;
    	}else{
    		return false;
    	}
    }  
    
    public static String getEmail(Context context) {
    	AccountManager accountManager = AccountManager.get(context);
    	Account[] accounts = accountManager.getAccountsByType(context.getResources().getString(R.string.account_type));
        for (Account account : accounts) {
            if (account != null) {            
            	return account.name;
            }else{
            	return null;
            }
        }
		return null;
    }    
    
    public static String getPassword(Context context, String email) {
    	AccountManager accountManager = AccountManager.get(context);
    	Account account = AccountUtils.getUserAccount(context, email);
    	String pass = accountManager.getPassword(account);
		return pass;
    }            

    public static String getPasswordyByUserEmail(Context context, String email) {
        AccountManager accountManager = AccountManager.get(context);
        String encryptedPassword;
        String decryptedPassword = "";

        Account account = AccountUtils.getUserAccount(context, email);
        if (account != null) {
            encryptedPassword = accountManager.getPassword(account);
            try {
                decryptedPassword = Security.decrypt(encryptedPassword);
                Log.d("Desctr", decryptedPassword);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getLocalizedMessage(), e);
            }
        }

        return decryptedPassword;
    }
    public static String getPassworEmail(Context context, String email) {
        AccountManager accountManager = AccountManager.get(context);
        String encryptedPassword = "";
        Account account = getUserAccount(context, email);
        encryptedPassword = accountManager.getPassword(account);
        return encryptedPassword;
    }
    /**
     * Set the encrypted password in the user account. If the account doesn't exist
     * before, it creates a new one.
     * @param password The password to be stored in the account manager
     * @param username The user name associated with the password
     * @return true if the password has been set
     *         false otherwise
     */
    public static boolean setPasswordByUserName(Context context, String email, String password) {
        String accountType = context.getResources().getString(R.string.account_type);
        AccountManager accountManager = AccountManager.get(context);

        // Encrypt the password
        try {
            String encryptedPassword = Security.encryptToHex(password);
            Account account = getUserAccount(context, email);
            if (account != null) {
                // Check if the old password is the same as the new one
                String oldPassword = accountManager.getPassword(account);
                if (!oldPassword.equalsIgnoreCase(encryptedPassword)) {
                    accountManager.setPassword(account, encryptedPassword);
                }
            }
            // If the account doesn't exist before, create it.
            else {
                Account newUserAccount = new Account(email, accountType);
                accountManager.addAccountExplicitly(newUserAccount, encryptedPassword, null);
            }
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getLocalizedMessage(), e);
            return false;
        }
    }
    
    public static void removeAccount(Context context) {
    	Preferences preferences = new Preferences(context);
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(context.getResources().getString(R.string.account_type));
        for (Account account : accounts) {
        	preferences.clear();
            accountManager.removeAccount(account, null, null);
        }
    }   
    
    public static String getAccountStatus(Context context, String email) {
    	AccountManager accountManager = AccountManager.get(context);
    	Account account = AccountUtils.getUserAccount(context, email);
    	String status = accountManager.getUserData(account, Constant.KEY_ESTADO);
    	return status;
    } 
    
    public static String getAccountName(Context context, String email) {
    	AccountManager accountManager = AccountManager.get(context);
    	Account account = AccountUtils.getUserAccount(context, email);
    	String name = accountManager.getUserData(account, Constant.KEY_NOMBRE);
    	return name;
    }       
    
    public static String getAccountGCM(Context context, String email) {
    	AccountManager accountManager = AccountManager.get(context);
    	Account account = AccountUtils.getUserAccount(context, email);
    	String id = accountManager.getUserData(account, Constant.GOOGLE_CLOUD_MESSENGER);
    	return id;
    }         
    public static boolean setPasswordNew(Context context, String email, String new_password) {
        String accountType = context.getResources().getString(R.string.account_type);
        AccountManager accountManager = AccountManager.get(context);

        // Encrypt the password
            Account account = AccountUtils.getUserAccount(context, email);
            if (account != null) {
                // Check if the old password is the same as the new one
            	accountManager.clearPassword(account);
                accountManager.setPassword(account, new_password);             
            }
            // If the account doesn't exist before, create it.
            else {
                Account newUserAccount = new Account(email, accountType);
                accountManager.addAccountExplicitly(newUserAccount, new_password, null);
            }
            return true;
   
    }    
    
    public static boolean setPhoneNew(Context context, String email, String phone) {
        AccountManager accountManager = AccountManager.get(context);
        Preferences p = new Preferences(context);
        // Encrypt the password
            Account account = AccountUtils.getUserAccount(context, email);
            if (account != null) {
                // Check if the old password is the same as the new one
            	accountManager.setUserData(account, Constant.KEY_TELEFONO, phone);
                p.setPhone(phone);
                return true;
            }else{
            	return false;
            }
    }        
   
}// end class
