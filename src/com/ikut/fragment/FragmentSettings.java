package com.ikut.fragment;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.ikut.ContainerFragment;
import com.ikut.Login;
import com.ikut.R;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Preferences;

public class FragmentSettings extends PreferenceFragment {
	
	private static final boolean ALWAYS_SIMPLE_PREFS = false;
    private String TAG = getClass().getName();    
	private Preference password = null;
	private Preference telefono = null;
	private Preference fecha = null;
	private Preference version = null; 
	private Preference dispositivo = null;
	PackageInfo pinfo = null;
	AccountManager accountManager = null;
	Preferences pref = null;
	String pass, pass2 = "";
	CheckBoxPreference new_messenger = null;
	CheckBoxPreference vibrate = null;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		getActivity().getActionBar().setTitle("Configuración");
		pref = new Preferences(getActivity());
		pass =  AccountUtils.getPassword(getActivity(), pref.getEmail());
		try {
			pinfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString() + "Package");
			e.printStackTrace();
		}
		// Device model
		String versionName = pinfo.versionName;
		new_messenger = (CheckBoxPreference)findPreference("pref_Notifications_new_message");
		vibrate = (CheckBoxPreference)findPreference("pref_Notifications_new_message_vibrate");
		
		/** asing preferences */
		password = findPreference("prefPassword");
		telefono = findPreference("prefPhone");
		fecha = findPreference("prefFecha");
		dispositivo = findPreference("prefDispositivo");
		version = findPreference("prefVersion");
		for (int i = 0; i < pass.length(); i++) {
			pass2 += "*";
		}
		
		try {
			if(pref.getPhone() == null || pref.getPhone().length() == 0 || pref.getPhone() == ""){
				telefono.setSummary("No tiene telefono registrado");
			}else{
				telefono.setSummary(pref.getPhone());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		password.setSummary(pass2);
		fecha.setSummary(pref.getRegistro());
		version.setSummary(versionName);
		dispositivo.setSummary(android.os.Build.MANUFACTURER+", "+android.os.Build.MODEL);		
		password.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Preferece Pass");
				Intent intent = new Intent(getActivity(), ContainerFragment.class);
				intent.putExtra(Constant.ID_SELECT_FRAGMENT, Constant.FRAGMENT_PASSWORD_CHANGE);
				getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				startActivity(intent);	
				return true;
			}
		});
		
		telefono.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Preference Phone");
				Intent intent = new Intent(getActivity(), ContainerFragment.class);
				intent.putExtra(Constant.ID_SELECT_FRAGMENT, Constant.FRAGMENT_PHONE_CHANGE);
				getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				startActivity(intent);				
				return true;
			}
		});
				
		new_messenger.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object value) {
				// TODO Auto-generated method stub
				 if(value instanceof Boolean){
					 Boolean boolVal = (Boolean)value;
					 pref.setNotification(boolVal);
				 }
				return true;
			}
		});
		
		vibrate.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object value) {
				// TODO Auto-generated method stub
				 if(value instanceof Boolean){
					 Boolean boolVal = (Boolean)value;
					 pref.setVibrate(boolVal);
				 }
				return true;
			}
		});		
	}
	
	/** tablet 10" */
	/** {@inheritDoc} */
	public boolean onIsMultiPane() {
		return isXLargeTablet(getActivity()) && !isSimplePreferences(getActivity());
	}
	
	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}
	
	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB || !isXLargeTablet(context);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!AccountUtils.getAccounts(getActivity())){
			startActivity(new Intent(getActivity(), Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
			getActivity().overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
			getActivity().finish();			
		}else{
			Log.d(Constant.TAG, "Exist Account");
		}		
	}	
}
