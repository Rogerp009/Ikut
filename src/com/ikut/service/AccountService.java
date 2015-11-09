package com.ikut.service;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ikut.Login;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Preferences;

public class AccountService extends Service {

    private static final String LOG_TAG = AccountService.class.getSimpleName();

    private static AccountAuthenticatorImpl sAccountAuthenticator = null;

    public AccountService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(AccountService.LOG_TAG, "Binding the service");
        IBinder ret = null;
        if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
            ret = getAuthenticator().getIBinder();
            return ret;
        }else{
        	Log.e("v", "v");
        	return null;
        }
        
    }

    private AccountAuthenticatorImpl getAuthenticator() {
        if (AccountService.sAccountAuthenticator == null) {
            AccountService.sAccountAuthenticator = new AccountAuthenticatorImpl(this);
        }

        return AccountService.sAccountAuthenticator;
    }

    private static class AccountAuthenticatorImpl extends AbstractAccountAuthenticator {

    	/** var */
        private final Context ctx;
        Bundle bundle = null;
        public AccountAuthenticatorImpl(Context context) {
            super(context);
            this.ctx = context;
            bundle = new Bundle();
        }

        @SuppressLint("HandlerLeak")
		@Override
        public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
            Log.d(AccountService.LOG_TAG, "Add Account" +"");
                  
            if(AccountUtils.getAccounts(ctx) == true){
            	bundle.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
                return bundle;                          
            }else{
                Log.d(AccountService.LOG_TAG, "The auth token type is " + authTokenType);
                Intent i = new Intent(ctx, Login.class);
                i.setAction(Constant.ADD_ACCOUNT_TYPE);
                i.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
                i.putExtra(Constant.AUTH_TOKEN_TYPE, authTokenType);
                bundle.putParcelable(AccountManager.KEY_INTENT, i);
                return bundle;
            }
        }

        @Override
		public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account) throws NetworkErrorException {
			// TODO Auto-generated method stub
        	Preferences preferences = new Preferences(ctx);
        	String email = preferences.getEmail();        	  
        	 if (!TextUtils.isEmpty(email)) {
        	    //mApplication.removeUserData();
        	 }
        	  
        	  Bundle result = new Bundle();
        	  result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
        	  return result;
		}

		@Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse arg0, Account arg1,
                Bundle arg2) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse arg0, String arg1) {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse arg0, Account arg1,
                String arg2, Bundle arg3) throws NetworkErrorException {
            Bundle bundle = new Bundle();
            
            if(AccountUtils.getAccounts(ctx) == true){
            	Toast.makeText(ctx, "Solo se admite una cuenta Ikut", Toast.LENGTH_SHORT).show();
            	bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "Solo se admite una cuenta Ikut");
            	return bundle;
            }
            return null;
        }

        @Override
        public String getAuthTokenLabel(String arg0) {
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse arg0, Account arg1,
                String[] arg2) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse arg0, Account arg1,
                String arg2, Bundle arg3) throws NetworkErrorException {
            return null;
        }     
    }
    
}

