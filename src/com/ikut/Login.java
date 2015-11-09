package com.ikut;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.espiandev.showcaseview.ShowcaseView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ikut.R.color;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Functions;
import com.ikut.utils.Letters;
import com.ikut.utils.Menus;
import com.ikut.utils.Preferences;
import com.ikut.web.ServerFunctions;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Login extends Activity {
	
	/** var */
	/** var */
    private String TAG = getClass().getName();    
    private AccountAuthenticatorResponse response = null;
    private AccountManager accountManager = null;
    private final int REQ_SIGNUP = 1;
	private EditText mEmailView = null;
	private EditText mPasswordView = null;
	private UserLoginTask mAuthTask = null;
	private String mEmail = "";	// Values for email and password at the time of the login attempt.
	private String mPassword = "";
	private View mLoginFormView = null;
	private View mLoginStatusView = null;
	private TextView creating_account;
	private JSONObject jObj = null;
    
	ShowcaseView sv;
	Preferences preferences = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		preferences = new Preferences(getApplicationContext());
		preferences.setTour(true);	
		if(!checkPlayServices()){dialogPlayServices();}
		/**
        if(preferences.getShowCase() == false){
            preferences.setShowCase(true); 
    		ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();		       	    
            co.hideOnClickOutside = false;
            co.fadeInDuration = 500;
            co.fadeOutDuration = 500;                    
            sv = ShowcaseView.insertShowcaseView(R.id.creating_account, this, "Cuenta nueva", "Para difrutar de nueastro servicios por favor cree una cuenta", co);
            sv.setAlpha(0.9f); 	                 
        }
        */
	    accountManager = AccountManager.get(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	if(extras.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE) != null){
        		response = getIntent().getExtras().getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        	}else{
    			if(getIntent().getExtras().getString(Constant.KEY_SUCCESS).equals(Constant.KEY_SUCCESS)){			
    				Toast.makeText(getApplicationContext(), "Inicie Sessíon", Toast.LENGTH_LONG).show();
    			}else{
    				if(extras.getParcelable(AccountManager.KEY_ERROR_MESSAGE)){
    					Toast.makeText(getApplicationContext(), "Inicie Sessíon", Toast.LENGTH_LONG).show();
    				}else{
    					if(extras.getParcelable("ERROR_CODE_CANCELED").equals("true")){
    						Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
    					}
    				}
    			}
        	}
        }
        
        findViewById(Menus.CREATING_ACCOUNT).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Login.this, RegisterAccount.class).putExtra("Show", "true"));
			}
		});
        
       
		/** View */	
		mEmailView = (EditText)findViewById(com.ikut.utils.Menus.EMAIL);
		mPasswordView = (EditText)findViewById(com.ikut.utils.Menus.PASSWORD);			
		mLoginFormView = findViewById(com.ikut.utils.Menus.LOGIN_FORM);
		mLoginStatusView = findViewById(com.ikut.utils.Menus.LOGIN_STATUS);		
		
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				// TODO Auto-generated method stub
				if(id == R.id.login || id == EditorInfo.IME_NULL){
					if(Functions.isConnectingToInternet(getApplicationContext())){
						AddAccount();
						return true;	
					}else{
						showMessage(getResources().getString(R.string.verifique_conexion), Style.INFO);
					}			
				}//end if
				return false;
			}
		});
		
		/** account */
		creating_account = (TextView) findViewById(Menus.CREATING_ACCOUNT);
		creating_account.setTypeface(Letters.ROBOTOLIGHT(this));
		findViewById(Menus.CREATING_ACCOUNT).setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				creating_account.setTextColor(color.primary);
                Intent signup = new Intent(getApplicationContext(), RegisterAccount.class);
                startActivityForResult(signup, REQ_SIGNUP);
				Log.i(TAG, "CREATE_ACCOUNTS onClick");		
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);				
			}

		});		
		
		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Functions.isConnectingToInternet(getApplicationContext())){
					AddAccount();
				}else{
					showMessage(getResources().getString(R.string.verifique_conexion), Style.INFO);	
				}		         				
			}
		});
		
		jObj = new JSONObject();        
	}//end 

/** ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||  */	

	private void AddAccount() {
		// TODO Auto-generated method stub
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		
		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		
		Log.i(Constant.TAG, mEmail+" "+mPassword);
		
		boolean cancel = false;
		View focusView = null;
		
		/**Check for a valid password */
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		/** Check for a valid email address */
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}		
		
		/** validate account */
		if (AccountUtils.getUserAccount(getApplicationContext(), mEmail) != null) {
		    showMessage(getResources().getString(R.string.warning_account_already_exists), Style.INFO);
		    return;
		 }		
		
		/** validate account */
		if (AccountUtils.getUserAccount(getApplicationContext(), mEmail) != null) {
		    showMessage(getResources().getString(R.string.warning_account_already_exists), Style.INFO);
		    return;
		 }		
		
		if (cancel) {
			focusView.requestFocus();
		} else {
			if(Functions.isOnline(this)){
				showProgress(true);//mostrar el AsyncTask
				mAuthTask = new UserLoginTask();
				mAuthTask.execute((Void) null);
			}else{
				showMessage(getResources().getString(R.string.error_ocurrido), Style.ALERT);	
				Log.i(Constant.TAG, "isOnline = false");
			}
		}			
	}	
	
	/** anim and show cargando and gone form*/
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {//anim
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
	}//end method
	
	public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

		/** var */
		JSONObject json = new JSONObject();
		
		@Override
		protected Integer doInBackground(Void... params){
			// TODO: attempt authentication against a network service.
			
			Log.e(Constant.JSON_TAG, "Estoy en doInBackground");
			try {
				if(Functions.isConnectingToInternet(Login.this) && Functions.isOnline(Login.this)){
					while (Functions.isOnline(Login.this)) {					
						json = ServerFunctions.loginUser(mEmail, mPassword);
						Log.e(TAG, json.toString());					
						if(json.get(Constant.KEY_ERROR).equals("-1") ||json ==  null ||json.length() == 0 || json.isNull(Constant.KEY_SUCCESS)){
							return 1;//error sel servidor o interno
						}else{
							if(json.getString(Constant.KEY_SUCCESS) != null){
								String res = json.getString(Constant.KEY_SUCCESS); 
								if(Integer.parseInt(res) == 1){
									jObj = json;
									return 2;// success
								}else{
									return 3;//password and email incorrecto
								}								
							}else{
								return 1;//error sel servidor o interno
							}
						}
					}//while
				}//end conection
			
			} catch (Exception e) {
				// TODO: handle exception
				return 4;//error sel servidor o interno
			}
			
			return 0;
		}//doInBackground

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			 mAuthTask = null;
			showProgress(false);
			
			switch (result) {
			case 0:
				showMessage("Ah ocurrido un error", Style.ALERT);
				break;
			case 1:
				showMessage(getResources().getString(R.string.error_ocurrido), Style.ALERT);
				break;
			case 2:
				SaveAccount();
				break;
			case 3:
				showMessage(getResources().getString(R.string.error_email_password), Style.ALERT);
				break;
			case 4:
				showMessage("Ah ocurrido un error", Style.ALERT);
				break;				
			default:
				break;
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			mAuthTask = null;
			mAuthTask.cancel(true);
			showProgress(false);				
		}				
	}//end class
		
	private void SaveAccount(){
		Log.w(TAG, "SaveAccotun "+ jObj.toString());
		Bundle b = new Bundle();//local
		Preferences preferences = new Preferences(getApplicationContext());
		//String encryptedPassword = "";
		
		try {
			
			JSONObject json_user = jObj.getJSONObject("user");		
			Account newUserAccount = new Account(mEmail, getResources().getString(R.string.account_type));
			boolean accountCreated = accountManager.addAccountExplicitly(newUserAccount, mPassword, null);
			String estado = json_user.getString(Constant.KEY_ESTADO);
			String fecha = json_user.getString(Constant.KEY_FECHA_REGISTRO);
			String fecha_modificacion = json_user.getString(Constant.KEY_FECHA_MODIFICACION);
			String nombre = json_user.getString(Constant.KEY_NOMBRE);
			String telefono = json_user.getString(Constant.KEY_TELEFONO);
			String gcm_regid = json_user.getString("gcm_regid");
			
			if (accountCreated) {
				accountManager.setUserData(newUserAccount, Constant.KEY_ESTADO, estado);
				accountManager.setUserData(newUserAccount, Constant.KEY_FECHA_REGISTRO, fecha);
				accountManager.setUserData(newUserAccount, Constant.KEY_FECHA_MODIFICACION, fecha_modificacion);
				accountManager.setUserData(newUserAccount, Constant.KEY_NOMBRE, nombre);
				accountManager.setUserData(newUserAccount, Constant.KEY_TELEFONO, telefono);
				accountManager.setUserData(newUserAccount, Constant.GOOGLE_CLOUD_MESSENGER, gcm_regid);				
				preferences.setEmail(mEmail);
				preferences.setEstado(estado);
				preferences.setGCM_ID(gcm_regid);
				preferences.setNombre(nombre);
				preferences.setPhone(telefono);
				preferences.setModificacion(fecha_modificacion);
				preferences.setRegistro(fecha);
				preferences.setFlat(true);
				
				if (response != null) {
                    Bundle result = new Bundle();
                    result.putString(AccountManager.KEY_ACCOUNT_NAME, mEmail);
                    result.putString(AccountManager.KEY_ACCOUNT_TYPE, getResources().getString(R.string.account_type));
                    response.onResult(result);
				}
			}
			
			Log.i(Constant.JSON_TAG, json_user.getString(Constant.KEY_NOMBRE));
			
			Intent i = new Intent();
			i.putExtras(b);
			i.setClass(Login.this, UserAccount.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			Log.v(TAG, preferences.toString());
			
			mPasswordView.setText("");
			mEmailView.setText("");			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Crouton.cancelAllCroutons();
	}	
	
	private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, Constant.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }//end method	
	
	private void dialogPlayServices(){//show		
	     final AlertDialog.Builder dialogo = new AlertDialog.Builder(this);  
	     dialogo.setTitle("Este dispositivo no es compatible con Google Play Services.");  
	     dialogo.setMessage(getResources().getString(R.string.title_dialog_close));            
	     dialogo.setCancelable(false);  
	     dialogo.setPositiveButton(getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {  
	    	 public void onClick(DialogInterface dialogo1, int id) {  
	    		 goToWeb("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=es_419");
	         	}  
	       	});  
	        
	     dialogo.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {  
	    	 public void onClick(DialogInterface dialogo1, int id) {
	    		 finish();
	         }  
	     });
	     
	     dialogo.show();	        
	}//end method
	
	private void goToWeb(String pagina){//Llamar a una pagina web
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pagina));
		startActivity(intent);
		
	}//end method		
	
	public void showMessage(String message, Style style){
		if(Login.this != null){
			Crouton.showText(Login.this, message, style);
		}
	}//end method
	
	protected void setupActionBar(){
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.icon_second);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);
	}
}//end class
