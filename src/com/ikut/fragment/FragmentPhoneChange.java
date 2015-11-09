package com.ikut.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ikut.Login;
import com.ikut.R;
import com.ikut.service.IkutUpload;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Functions;
import com.ikut.utils.Letters;
import com.ikut.utils.Preferences;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class FragmentPhoneChange extends Fragment{

	/** method return fragment*/
	public static FragmentPhoneChange newInstance() {
		FragmentPhoneChange fragment = new FragmentPhoneChange();
		return fragment;		
	}//end method

    /** View */
	private EditText mPhoneView = null;
	private TextView mTitleView = null;
	TextView registrando = null;
	private View mStatusView = null;
	private TextView status_conectando = null;
		
	/** String */
	private Button btn = null;
	private String mPhone = "";
	
	private Preferences preferences = null;
    private String TAG = getClass().getName();    
    
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		view =  inflater.inflate(R.layout.fragment_phone_change, container, false);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
		preferences = new Preferences(getActivity());
		mPhoneView = (EditText)view.findViewById(R.id.phone);
		mTitleView = (TextView)view.findViewById(R.id.change_phone);
		mStatusView = view.findViewById(R.id.status_change_password);
		status_conectando = (TextView)view.findViewById(R.id.text_conectando);
		status_conectando.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		btn = (Button)view.findViewById(R.id.btn_save_phone);
		mTitleView.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
	
		
		view.findViewById(R.id.btn_save_phone).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(Functions.isConnectingToInternet(getActivity())){
					ChangePhone();
				}
			}
		});
		return view;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(receiverPhone);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter inf = new IntentFilter();
		inf.addAction(Constant.PHONE_RECONET);
		inf.addAction(Constant.OK);
		getActivity().registerReceiver(receiverPhone, inf);		
		
		if(!AccountUtils.getAccounts(getActivity())){
			startActivity(new Intent(getActivity(), Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
			getActivity().overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
			getActivity().finish();			
		}else{
			Log.d(Constant.TAG, "Exist Account");
		}		
	}	
	
	private void ChangePhone() {
		// TODO Auto-generated method stub
		mPhoneView.setError(null);
		
		mPhone = mPhoneView.getText().toString();
		boolean cancel = false;
		View focusView = null;
		
		
		if(TextUtils.isEmpty(mPhone)){
			mPhoneView.setError("Campo no puede ir vacio");
			focusView = mPhoneView;
			cancel = true;			
		}else{
			if(!Functions.isValidPhoneNumber(mPhone)){
				mPhoneView.setError("Telefono invalido");
				focusView = mPhoneView;
				cancel = true;				
			}else{
				if(mPhone.length() != 11){
					mPhoneView.setError("Telefono invalido");
					focusView = mPhoneView;
					cancel = true;				
				}				
			}
		}
		
		if (cancel) {
			focusView.requestFocus();
		} else {
			Log.d(TAG, "Llamar IkutUpload");			
			Intent i = new Intent(getActivity(), IkutUpload.class);
			i.setAction(Constant.CHANGE_PHONE);			
			i.putExtra(Constant.KEY_TELEFONO, mPhone);
			getActivity().startService(i);
			preferences.setServerPhone(1);
			showProgress(true);
			btn.setEnabled(false);
			mPhoneView.setEnabled(false);
		}			
	}
	
	public void showMessage(String message, Style style){
		if(getActivity() != null)
			Crouton.showText(getActivity(), message, style);
	}
	
	/** anim and show cargando and gone form*/
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {//anim
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
			mStatusView.setVisibility(View.VISIBLE);
			mStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});			
	}//end method	
	
	private BroadcastReceiver receiverPhone = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context ctx, Intent intent) {
			// TODO Auto-generated method stub
			if(intent != null){
				if(intent.getAction().equals(Constant.PHONE_RECONET) == true){
					getActivity().stopService(new Intent(getActivity(), IkutUpload.class));
					showMessage(getResources().getString(R.string.error_ocurrido), Style.ALERT);
					showProgress(false);
					mPhoneView.setEnabled(true);
					btn.setEnabled(true);				
				}else{
					if(intent.getAction().equals(Constant.OK) == true){
						getActivity().stopService(new Intent(getActivity(), IkutUpload.class));
						AccountUtils.setPasswordNew(getActivity(), preferences.getEmail(), mPhone);
						try {
							preferences.setPhone(mPhone);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						preferences.setServerPhone(0);
						getActivity().finish();
					}
				}
			}
		}
	};		
}
