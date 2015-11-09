package com.ikut.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ikut.Login;
import com.ikut.R;
import com.ikut.service.IkutUpload;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Functions;
import com.ikut.utils.Letters;
import com.ikut.utils.Menus;
import com.ikut.utils.Preferences;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class FragmentSendMessenger extends Fragment{
	
	/** method return fragment*/
	public static FragmentSendMessenger newInstance() {
		FragmentSendMessenger fragment = new FragmentSendMessenger();
		return fragment;		
	}//end method
	
    private String TAG = getClass().getName();
    
	/** var */
	View view = null;
	private TextView mViewEmailUser = null;
	private TextView mViewDe = null;
	private EditText  mViewAsunto = null;
	private EditText mViewContenido = null;	
	
	private TextView mLabelViewAsunto = null;
	private TextView mLavelViewContenido = null;
	
	//Font
	static Typeface ROBOTOLIGHT = null;
	static Typeface ROBOTO_MEDIUM = null;
	static Typeface ROBOTO_REGULAR = null;
	
	private String mAsunto = "";
	private String mContenido = "";
	
	private Preferences preferences = null;
	private ProgressDialog pd = null;
	
	private void intancesViewAldd() {
		// TODO Auto-generated method stub
		/** view or asing */

		mViewEmailUser = (TextView)view.findViewById(Menus.EMAIL_TEXT_MESSENGER);
		mViewDe  = (TextView)view.findViewById(Menus.DE_MESSENGER);
		mLabelViewAsunto   = (TextView)view.findViewById(Menus.LABEL_ASUNTO_MESSENGER);
		mViewAsunto    = (EditText)view.findViewById(Menus.EDIT_ASUNTO);
		mLavelViewContenido = (TextView)view.findViewById(Menus.LABEL_CONTENIDO_MESSENGER);
		mViewContenido= (EditText)view.findViewById(Menus.EDIT_CONTENIDO);
				
		
	}//end method
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		view =  inflater.inflate(R.layout.fragment_send_messenger, container, false);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
		getActivity().getActionBar().setTitle(getResources().getText(R.string.nav_bar_messenger_send));
		preferences =  new Preferences(getActivity());
		
		/** intancesViewAldd*/
		intancesViewAldd();
		
		/** argunment of intent*/
		if(preferences != null){
			mViewEmailUser.setText(preferences.getEmail());
			mViewEmailUser.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
			mViewDe.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		}else{
			Log.e(TAG, "Preference null" + preferences.toString());
		}

		if(getArguments() != null){
			if(getArguments().containsKey(Constant.CONTENIDO) && getArguments().containsKey(Constant.ASUNTO)){
				mContenido = getArguments().getString(Constant.CONTENIDO);
				mAsunto = getArguments().getString(Constant.ASUNTO);
			}
		}
				
		mViewAsunto.setText(mAsunto);
		mViewContenido.setText(mContenido);
		
		
		/** EditText*/
		mViewContenido.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(v.getId() == Menus.EDIT_CONTENIDO){
					mLavelViewContenido.setText("Contenido");	
					mLavelViewContenido.setTextColor(Color.rgb(235, 98, 62));
					mLavelViewContenido.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
					mViewContenido.setTypeface(Letters.ROBOTOREGULAR(getActivity()));
				}
			}
		});	
	
		mViewAsunto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(v.getId() == Menus.EDIT_ASUNTO){
					mLabelViewAsunto.setText("Asunto");					
					mLabelViewAsunto.setTextColor(Color.rgb(235, 98, 62));
					mLabelViewAsunto.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
					mViewAsunto.setTypeface(Letters.ROBOTOMEDIUM(getActivity()));
				}
			}
		});
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	private void validateMessenger() {
		// TODO Auto-generated method stub
		mViewAsunto.setError(null);
		
		/** convert to string */
		mAsunto = mViewAsunto.getText().toString();
		mContenido = mViewContenido.getText().toString();
		
		boolean cancel = false;
		View focusView = null;
		
		Log.i(Constant.TAG, mAsunto+" "+mContenido);
		System.out.println(mAsunto+" "+mContenido);
		
		if (TextUtils.isEmpty(mContenido)) {
			mViewContenido.setError(getString(R.string.error_contenido_invalid));
			focusView = mViewContenido;
			cancel = true;
		} else if (mContenido.length() < 3) {
			mViewContenido.setError(getString(R.string.error_contenido_invalid_shot));
			focusView = mViewContenido;
			cancel = true;
		}else{
			if(mContenido.length() > 140){
				mViewContenido.setError("Solo se acepta 140 caracteres");
				showMessage("Solo se acepta 160 caracteres", Style.INFO);
				focusView = mViewContenido;
				cancel = true;				
			}
		}	
		
		/**Check for a valid asunto */
		if (TextUtils.isEmpty(mAsunto)) {
			mViewAsunto.setError(getString(R.string.error_asunto_invalid));
			focusView = mViewAsunto;
			cancel = true;
		} else if (mAsunto.length() < 4) {
			mViewAsunto.setError(getString(R.string.error_asunto_invalid_short));
			focusView = mViewAsunto;
			cancel = true;
		}else{
			if(mAsunto.length() > 40){
				mViewContenido.setError("Solo se acepta 40 caracteres");
				//showMessage("Solo se acepta 160 caracteres", Style.INFO);
				focusView = mViewContenido;
				cancel = true;				
			}
		}		
		
		if (cancel) {
			focusView.requestFocus();
		} else {
			if(checkPlayServices()){
				JSONObject json = new JSONObject();
				Intent i = new Intent(getActivity(), IkutUpload.class);
				try {
					json.put(Constant.ASUNTO, mAsunto);
					json.put(Constant.CONTENIDO, mContenido);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i.putExtra(Constant.JSON_TAG, json.toString());
				i.setAction(Constant.SEND);
				showProgress(true);
				getActivity().startService(i);				
			}else{
            	showMessage("Este dispositivo no es compatible.", Style.ALERT);
                Log.i(TAG, "This device is not supported.");				
			}
		}		
	}//method	
	
	/** Menu */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.send_messenger, menu);
		menu.findItem(Menus.SEND_MESSENGER).setVisible(true);
		
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if(id== Menus.SEND_MESSENGER){
			if(Functions.isConnectingToInternet(getActivity())){
				validateMessenger();
			}			
		}
		return true;
	}	
	
	@Override
	public void onDestroy() {
    	super.onDestroy();
    	if (pd != null) {
			pd.dismiss();
		}
    }
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(receiverMessenger);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mViewContenido.getWindowToken(), 0);	
        inputMethodManager.hideSoftInputFromWindow(mViewAsunto.getWindowToken(), 0);
        
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.MESSENGER_SEND);
		filter.addAction(Constant.OK);	
		filter.addAction("com.ikut.stop");
		getActivity().registerReceiver(receiverMessenger, filter);
		
		if(!AccountUtils.getAccounts(getActivity())){
			startActivity(new Intent(getActivity(), Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
			getActivity().overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
			getActivity().finish();			
		}else{
			Log.d(Constant.TAG, "Exist Account");
		}		
	}

	private BroadcastReceiver receiverMessenger = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context ctx, Intent intent) {
			// TODO Auto-generated method stub
			if(intent != null){
				if(intent.getAction().equals(Constant.MESSENGER_SEND) == true){
					getActivity().stopService(new Intent(getActivity(), IkutUpload.class));
					showMessage(getResources().getString(R.string.error_ocurrido), Style.ALERT);		
					pd.cancel();
					pd.dismiss();	
				}else{
					if(intent.getAction().equals(Constant.OK) == true){
						getActivity().stopService(new Intent(getActivity(), IkutUpload.class));
						pd.cancel();
						pd.dismiss();	
						getActivity().finish();
					}else{
						if(intent.getAction().equals("com.ikut.stop") == true){
							getActivity().stopService(new Intent(getActivity(), IkutUpload.class));
							showMessage(getResources().getString(R.string.error_ocurrido), Style.ALERT);
							pd.cancel();
							pd.dismiss();	
						}
					}					
				}
			}
		}
	};
	
	public void showProgress(boolean show){
		pd = new ProgressDialog(getActivity());
		pd.setTitle("Conectando con el servidor...");
		pd.setMessage("Espere por favor...");
		pd.setCancelable(false);
		pd.setIndeterminate(true);			
		pd.show();
	}
	
	public void showMessage(String message, Style style){
		if(getActivity() != null)
			Crouton.showText(getActivity(), message, style);
	}	
	
	private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), Constant.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
            	showMessage("Este dispositivo no es compatible.", Style.ALERT);
                Log.i(TAG, "This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        return true;
    }//end method		
}//end class

