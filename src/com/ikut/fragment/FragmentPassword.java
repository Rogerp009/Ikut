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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
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

public class FragmentPassword extends Fragment{
	/** method return fragment*/
	public static FragmentPassword newInstance() {
		FragmentPassword fragment = new FragmentPassword();
		return fragment;		
	}//end method

	private TextView text_code = null;
	private TextView text_title = null;
	private EditText mPasswordView = null;
	private EditText mPasswordConfirmarView = null;
	private Button btn = null;
	Preferences preferences = null;	
	private View view;
	private String mPassword = "";
	private String mPassword_Confirmar = "";	
	private View mStatusView = null;
	private TextView status_conectando = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view =  inflater.inflate(R.layout.fragment_password, container, false);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
		
		preferences = new Preferences(getActivity());
		text_code = (TextView)view.findViewById(R.id.code_verification);
		text_title  = (TextView)view.findViewById(R.id.change_pass);
		mPasswordView  = (EditText)view.findViewById(R.id.password_change);
		mPasswordConfirmarView  = (EditText)view.findViewById(R.id.password_confirma_change);
		btn  = (Button)view.findViewById(R.id.btn_change_pass);
		
		text_code.setText("Codigo de Verificación: "+preferences.getCode());
		text_title.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		text_code.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		
		mStatusView = view.findViewById(R.id.status_change_password);
		status_conectando = (TextView)view.findViewById(R.id.text_conectando);
		status_conectando.setTypeface(Letters.ROBOTOLIGHT(getActivity()));		
		/** Pass */
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				// TODO Auto-generated method stub
				if(id == EditorInfo.IME_NULL){
					if(Functions.isConnectingToInternet(getActivity())){
						try {
							ChangePassword();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return true;	
					}else{
						showMessage(getResources().getString(R.string.verifique_conexion), Style.INFO);
					}			
				}//end if
				return false;
			}

		});//end mPasswordView

		/** Pass Confir*/
		mPasswordConfirmarView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				// TODO Auto-generated method stub
				if(id == EditorInfo.IME_NULL){
					if(Functions.isConnectingToInternet(getActivity())){
						try {
							ChangePassword();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}				
						return true;	
					}else{
						showMessage(getResources().getString(R.string.verifique_conexion), Style.INFO);
					}			
				}//end if
				return false;
			}
		
		});			
		
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Functions.isConnectingToInternet(getActivity())){
					try {
						ChangePassword();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}else{
					showMessage(getResources().getString(R.string.verifique_conexion), Style.INFO);
				}				
			}
		});
		
		return view;
	}
	

	private void ChangePassword() {
		// TODO Auto-generated method stub
		mPasswordView.setError(null);
		mPasswordConfirmarView.setError(null);
		
		/** convert string */
		mPassword = mPasswordView.getText().toString();
		mPassword_Confirmar = mPasswordConfirmarView.getText().toString();
		
		boolean cancel = false;
		View focusView = null;
		
		/**Check for a valid password confir */
		if (TextUtils.isEmpty(mPassword_Confirmar)) {
			mPasswordConfirmarView.setError(getString(R.string.error_field_required));
			focusView = mPasswordConfirmarView;
			cancel = true;
		} else if (mPassword.length() < 5) {
			mPasswordConfirmarView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordConfirmarView;
			cancel = true;
		}else if(!mPassword_Confirmar.equals(mPassword)){
			mPasswordConfirmarView.setError(getString(R.string.error_invalid_pasword_dont_match));
			focusView = mPasswordConfirmarView;
			cancel = true;			
		}
		
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
		
		if (cancel) {
			focusView.requestFocus();
		} else {
			Intent i = new Intent(getActivity(), IkutUpload.class);
			i.setAction(Constant.CHANGE_PASSWORD);
			getActivity().startService(i);
			preferences.setPassMoment(mPassword);
			showProgress(true);
			btn.setEnabled(false);
			mPasswordView.setEnabled(false);
			mPasswordConfirmarView.setEnabled(false);
		}		
	}	
	
	public void showMessage(String message, Style style){
		if(getActivity() != null){
			Crouton.showText(getActivity(), message, style);
		}
	}//end method
	
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(receiverPass);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter inf = new IntentFilter();
		inf.addAction(Constant.PASSWORD_RECONET);
		inf.addAction(Constant.OK);
		inf.addAction(Constant.FORGET_PASSWORD_OK);
		getActivity().registerReceiver(receiverPass, inf);
		
		if(!AccountUtils.getAccounts(getActivity())){
			startActivity(new Intent(getActivity(), Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
			getActivity().overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
			getActivity().finish();			
		}else{
			Log.d(Constant.TAG, "Exist Account");
		}		
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
	
	private BroadcastReceiver receiverPass = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context ctx, Intent intent) {
			// TODO Auto-generated method stub
			if(intent != null){
				if(intent.getAction().equals(Constant.PASSWORD_RECONET) == true){
					getActivity().stopService(new Intent(getActivity(), IkutUpload.class));
					showMessage(getResources().getString(R.string.error_ocurrido), Style.ALERT);
					showProgress(false);
					mPasswordView.setEnabled(true);
					mPasswordConfirmarView.setEnabled(true);
				}else{
					if(intent.getAction().equals(Constant.OK) == true){
						getActivity().stopService(new Intent(getActivity(), IkutUpload.class));
						AccountUtils.setPasswordNew(getActivity(), preferences.getEmail(), mPassword);
						preferences.setCode("");
						try {
							preferences.setPassMoment("");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						getActivity().finish();
					}
				}
			}
		}
	};		
}
