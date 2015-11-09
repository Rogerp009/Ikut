package com.ikut;

import java.io.IOException;

import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ikut.utils.Constant;
import com.ikut.utils.Functions;
import com.ikut.utils.Letters;
import com.ikut.utils.Menus;
import com.ikut.utils.Preferences;
import com.ikut.web.ServerFunctions;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class RegisterAccount extends FragmentActivity {
	
	/** var */
    private String TAG = getClass().getName();
    /** View */
	private EditText mPasswordView = null;
	private EditText mPassword_confirView = null;	
	private View mRegisterFormView = null;
	private View mRegisterStatusView = null;	
	private EditText mNameView = null;
	private EditText mEmailView = null;
	private EditText mPhoneView = null;
	private CheckBox mCheckTerminoView = null;			
	private TextView mTerminoView = null;
	private TextView mRegistrar_ikutView = null;
	TextView registrando = null;
	
	/** String */
	private String mName = "";
	private String mPhone = "";
	private String mEmail = "";
	private String mPassword = "";
	private String mPassword_Confirmar = "";	
	
	/** terminos*/
	private TextView termino = null;
	private TextView termino2 = null;
	private TextView termino3 = null;
	private TextView termino4 = null;
	private TextView termino5 = null;
	private TextView termino6 = null;
	private TextView termino7 = null;
	private TextView termino8 = null;
	private TextView termino9 = null;
	private TextView termino10 = null;
	private TextView termino11 = null;
	private TextView termino12 = null;
	private TextView termino13 = null;
	private TextView termino14 = null;
	private TextView termino15 = null;
	private TextView termino16 = null;
	private TextView termino17 = null;
	private TextView termino18 = null;	
	
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
	private GoogleCloudMessaging gcm = null;
    private Context context = null;
	private RegisterTask task = null;
	private String regId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_account);
		
		context = getApplicationContext();
		
		if(!checkPlayServices()){dialogPlayServices();}	
		getActionBar().setDisplayHomeAsUpEnabled(true);//show up
		Preferences preferences = new Preferences(getApplicationContext());
		preferences.setTour(true);		
		
		/** Gone */
		mRegisterFormView = findViewById(Menus.REGISTER_FORM);
		mRegisterStatusView = findViewById(Menus.REGISTER_STATUS);		
		mNameView = (EditText)findViewById(Menus.NAME);
		mEmailView = (EditText)findViewById(Menus.EMAIL_REGISTER);
		mPasswordView = (EditText)findViewById(Menus.PASSWORD_REGISTER);
		mPassword_confirView = (EditText)findViewById(Menus.PASSWORD_CONFIR);
		mCheckTerminoView = (CheckBox)findViewById(Menus.CHECK_TERM);
		mTerminoView = (TextView) findViewById(R.id.text_termino);
		mRegistrar_ikutView =  (TextView) findViewById(R.id.register_ikut);	
		mPhoneView = (EditText)findViewById(R.id.phone);
		
		/** Pass */
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				// TODO Auto-generated method stub
				if(id == EditorInfo.IME_NULL){
					if(Functions.isConnectingToInternet(getApplicationContext())){
						createAccount();
						return true;	
					}else{
						showMessage(getResources().getString(R.string.verifique_conexion), Style.INFO);
					}			
				}//end if
				return false;
			}
		});//end mPasswordView
		
		/** Pass Confir*/
		mPassword_confirView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				// TODO Auto-generated method stub
				if(id == EditorInfo.IME_NULL){
					if(Functions.isConnectingToInternet(getApplicationContext())){
						createAccount();
						return true;	
					}else{
						showMessage(getResources().getString(R.string.verifique_conexion), Style.INFO);
					}			
				}//end if
				return false;
			}
		
		});
		
		/** Buton */
		findViewById(Menus.CREATE_ACCOUNTS).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Functions.isConnectingToInternet(getApplicationContext())){
					createAccount();	
				}else{
					showMessage(getResources().getString(R.string.verifique_conexion), Style.INFO);
				}					
			}
		});
		
		mTerminoView.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext()));
		mRegistrar_ikutView.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext()));
		registrando = (TextView) findViewById(R.id.register_status_message);
		registrando.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext()));
		
		mTerminoView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		
		GCMRegistrar.checkDevice(this);
	    GCMRegistrar.checkManifest(this);
	    
        if (checkPlayServices()) {
        	if(gcm == null){
                gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                regId = getRegistrationId(context);       		
        	}
            if (regId.isEmpty()) {
                //registerInBackground();
            	Log.d(TAG, "Id Vacio");
            }else{
            	Toast.makeText(getApplicationContext(), regId, Toast.LENGTH_LONG).show();
            }
        }
	}//end onCreate
	

	private void createAccount() {
		// TODO Auto-generated method stub
		if (task != null) {
			return;
		}
		/** reset errors */		
		mNameView.setError(null);
		mEmailView.setError(null);
		
		mPasswordView.setError(null);
		mPassword_confirView.setError(null);
		
		/** convert string */
		mName = mNameView.getText().toString();
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mPassword_Confirmar = mPassword_confirView.getText().toString();
		mPhone = mPhoneView.getText().toString();
		
		Log.d(TAG, mName+" "+mEmail+" "+mPassword+" "+mPassword_Confirmar);
		
		boolean cancel = false;
		View focusView = null;
		
		/**Check for a valid password */
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 5) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		/**Check for a valid password confir */
		if (TextUtils.isEmpty(mPassword_Confirmar)) {
			mPassword_confirView.setError(getString(R.string.error_field_required));
			focusView = mPassword_confirView;
			cancel = true;
		} else if (mPassword.length() < 5) {
			mPassword_confirView.setError(getString(R.string.error_invalid_password));
			focusView = mPassword_confirView;
			cancel = true;
		}else if(!mPassword_Confirmar.equals(mPassword)){
			mPassword_confirView.setError(getString(R.string.error_invalid_pasword_dont_match));
			focusView = mPassword_confirView;
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
		
		/** Check for a valid name */
		if (TextUtils.isEmpty(mName)) {
			mNameView.setError(getString(R.string.error_field_required));
			focusView = mNameView;
			cancel = true;
		} else if (!Functions.checkLetters(mName)) {			
			mNameView.setError(getString(R.string.error_invalid_name));
			focusView = mNameView;
			cancel = true;
		}else if(mName.length() < 4){
			mNameView.setError(getString(R.string.error_invalid_name_short));
			focusView = mNameView;
			cancel = true;			
		}	
		
		if (cancel) {
			focusView.requestFocus();
		} else {
			if(Functions.isOnline(this) && checkPlayServices()){				
				if (mCheckTerminoView.isChecked()) {
					if(checkPlayServices()){
						showProgress(true);//mostrar el AsyncTask
						task = new RegisterTask();
						task.execute((Void) null);						
					}
						
				}else{	
		        	showMessage("Tienes que aceptar nuestro terminos y servicios", Style.ALERT);
				}				
			}else{
				showMessage(getResources().getString(R.string.error_ocurrido), Style.ALERT);
				Log.d(TAG, "isOnline = false");				
			}
		}		
	}//method	
	
	public class RegisterTask extends AsyncTask<Void, Void, Integer>{
		
		/** var */		
		JSONObject json = new JSONObject();
		
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.e(TAG, "doInBackground");
            Log.d(TAG, regId);
			try {
				regId = gcm.register(Constant.SENDER_ID);
	            // Device is already registered on GCM, check server.
	            if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
	            	showMessage("Repetido", Style.CONFIRM);
	            	return 0;
	            }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}             
			if(!regId.equals("")){
				try {
					while (Functions.isOnline(getApplicationContext())) {
						if(Functions.isConnectingToInternet(getApplicationContext()) && Functions.isOnline(getApplicationContext())){
							json = ServerFunctions.registerUser(getApplicationContext(), mName, mEmail, mPassword, mPhone, regId);
							Log.e(TAG, json.toString());					
							if(json.get(Constant.KEY_ERROR).equals("-1") || json ==  null || json.length() == 0 || json.isNull(Constant.KEY_SUCCESS)){
								return 1;//error sel servidor o interno
							}else{
								if(json.getString(Constant.KEY_SUCCESS) != null){
									String res = json.getString(Constant.KEY_SUCCESS); 
									if(Integer.parseInt(res) == 1){	
										return 2;//todo esta bien				
									}else{
										if(json.getString(Constant.KEY_ERROR).equals("2") || json.isNull(Constant.KEY_ERROR) == false || json.isNull("error_msg") == false){
											return 3;//el usuario existe
										}
									}								
								}						
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString() + json.toString());		
					return 4;
				}					
			}else{
				return 0;
			}			
			
			Log.e(TAG, json.toString());
			return 1;	
		}


		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			task = null;			
			showProgress(false);
			
			Bundle p = new Bundle();
			p.putString(Constant.KEY_SUCCESS, Constant.KEY_SUCCESS);
			Intent i = new Intent(getApplicationContext(), Login.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtras(p);
			switch (result) {
			case 0:
				showMessage("Ah ocurrido un error en Ikut comuniquese con el Administrador", Style.ALERT);
				break;
			case 1:
				showMessage(getResources().getString(R.string.error_ocurrido), Style.ALERT);
				break;
			case 2:
				showMessage("Usuario registrado correctamente", Style.CONFIRM);
				startActivity(i);																
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();				
				break;
			case 3:
				showMessage(getResources().getString(R.string.error_invalid_user_exist), Style.INFO);
				break;
			case 4:
				showMessage("Ah ocurrido un error", Style.ALERT);
				break;				
			default:
				break;
			}
			Log.e(TAG, result.toString());
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			task = null;
			showProgress(false);			
		}		
	}//end class	
		
	/** anim and show cargando and gone form*/
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mRegisterStatusView.setVisibility(View.VISIBLE);
			mRegisterStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mRegisterFormView.setVisibility(View.VISIBLE);
			mRegisterFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mRegisterStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}//show
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menus.HOME:
			NavUtils.navigateUpTo(this, new Intent(this, Login.class));
			setResult(RESULT_CANCELED);
	        GCMRegistrar.onDestroy(this);
			Crouton.cancelAllCroutons();
			if (task != null) {
				task = null;
				task.cancel(true);
			}			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(RESULT_CANCELED);
        GCMRegistrar.onDestroy(this);
		Crouton.cancelAllCroutons();
		if (task != null) {
			task = null;
			task.cancel(true);
		}		
		finish();
	}	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Crouton.cancelAllCroutons();
	}

	public void showMessage(String message, Style style){
		if(RegisterAccount.this != null)
			Crouton.showText(RegisterAccount.this, message, style);
	}
	
	public void showDialog(){
		
		// Rescatamos el layout creado para el prompt
		LayoutInflater li = LayoutInflater.from(this);
		View prompt = li.inflate(R.layout.termino_servicios, null);	
		// Creamos un constructor de Alert Dialog y le añadimos nuestro layout al cuadro de dialogo
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(prompt);	
		alertDialogBuilder.setTitle("Términos y servicios");
		
		termino = (TextView) prompt.findViewById(R.id.termino1_title); 
		termino2 = (TextView) prompt.findViewById(R.id.termino2); 
		termino3 = (TextView) prompt.findViewById(R.id.termino3_title); 
		termino4 = (TextView) prompt.findViewById(R.id.termino4); 
		termino5 = (TextView) prompt.findViewById(R.id.termino5); 
		termino6 = (TextView) prompt.findViewById(R.id.termino6_title); 
		termino7 = (TextView) prompt.findViewById(R.id.termino7); 
		termino8 = (TextView) prompt.findViewById(R.id.termino8); 
		termino9 = (TextView) prompt.findViewById(R.id.termino9); 
		termino10 = (TextView) prompt.findViewById(R.id.termino10); 
		termino11 = (TextView) prompt.findViewById(R.id.termino11); 
		termino12 = (TextView) prompt.findViewById(R.id.termino12_title); 
		termino13 = (TextView) prompt.findViewById(R.id.termino13); 
		termino14 = (TextView) prompt.findViewById(R.id.termino14); 
		termino15 = (TextView) prompt.findViewById(R.id.termino15); 
		termino16 = (TextView) prompt.findViewById(R.id.termino16); 
		termino17 = (TextView) prompt.findViewById(R.id.termino17); 
		termino18 = (TextView) prompt.findViewById(R.id.termino18);
		
		termino.setTypeface(Letters.ROBOTOREGULAR(getApplicationContext())); 
		termino2.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino3.setTypeface(Letters.ROBOTOREGULAR(getApplicationContext())); 
		termino4.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino5.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino6.setTypeface(Letters.ROBOTOREGULAR(getApplicationContext())); 
		termino7.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino8.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino9.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino10.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino11.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino12.setTypeface(Letters.ROBOTOREGULAR(getApplicationContext())); 
		termino13.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino14.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino15.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino16.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino17.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 
		termino18.setTypeface(Letters.ROBOTOLIGHT(getApplicationContext())); 	
		
		termino13.setTextColor(Color.RED); 
		termino14.setTextColor(Color.RED); 
		termino15.setTextColor(Color.RED); 
		termino16.setTextColor(Color.RED); 
		termino17.setTextColor(Color.RED); 
		termino18.setTextColor(Color.RED); 	
		
		// Creamos un AlertDialog y lo mostramos
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();		
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
	
	private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = Functions.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
	
	private SharedPreferences getGcmPreferences(Context context) {
        return getSharedPreferences(RegisterAccount.class.getSimpleName(),Context.MODE_PRIVATE);
    }
			
}//end class
