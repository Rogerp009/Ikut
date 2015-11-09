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

import com.ikut.ContainerFragment;
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

public class FragmentPasswordChange extends Fragment{

	/** method return fragment*/
	public static FragmentPasswordChange newInstance() {
		FragmentPasswordChange fragment = new FragmentPasswordChange();
		return fragment;		
	}//end method
		
    /** View */
	private EditText mPasswordView = null;
	private EditText mPassword_confirmarView = null;
	private EditText mOld_Password = null;
	private TextView mTitleView = null;
	private TextView mForgetPass = null;
	
	private View mStatusView = null;
	private TextView status_conectando = null;
	
	/** String */
	private String mPassword = "";
	private String mPassword_Confirmar = "";	
	private String mPassword_Old = "";
	private Button boton = null;
	private Preferences preferences = null;
    private String TAG = getClass().getName();
    
    View view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		view =  inflater.inflate(R.layout.fragment_password_change, container, false);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
		
		boton = (Button)view.findViewById(R.id.btn_change_pass);
		mPassword_confirmarView = (EditText)view.findViewById(R.id.password_confirma_change);
		mTitleView  = (TextView)view.findViewById(R.id.change_pass);
		mOld_Password = (EditText)view.findViewById(R.id.old_password);
		mTitleView.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		mPasswordView = (EditText)view.findViewById(R.id.password_change);
		mForgetPass = (TextView)view.findViewById(R.id.olvido_pass);
		mStatusView = view.findViewById(R.id.status_change_password);
		status_conectando = (TextView)view.findViewById(R.id.text_conectando);
		status_conectando.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		mForgetPass.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		
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
		mPassword_confirmarView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
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
		
		/** Buton */
		view.findViewById(R.id.btn_change_pass).setOnClickListener(new View.OnClickListener() {
			
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
		
		mForgetPass.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Functions.isConnectingToInternet(getActivity())){
					Intent i = new Intent(getActivity(), IkutUpload.class);
					i.setAction(Constant.FORGET_PASSWORD);
					getActivity().startService(i);				
					showProgress(true);
					boton.setEnabled(false);
					mPasswordView.setEnabled(false);
					mPassword_confirmarView.setEnabled(false);
					mOld_Password.setEnabled(false);					
				}else{
					showMessage(getResources().getString(R.string.verifique_conexion), Style.INFO);
				}
								
			}
		});
		
		return view;

	}//onCreateView
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(receiver);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter inf = new IntentFilter();
		inf.addAction(Constant.PASSWORD_RECONET);
		inf.addAction(Constant.OK);
		inf.addAction(Constant.FORGET_PASSWORD_OK);
		getActivity().registerReceiver(receiver, inf);		
		
		if(!AccountUtils.getAccounts(getActivity())){
			startActivity(new Intent(getActivity(), Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
			getActivity().overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
			getActivity().finish();			
		}else{
			Log.d(Constant.TAG, "Exist Account");
		}		
	}

	private void ChangePassword() throws Exception {
		// TODO Auto-generated method stub
		/** reset errors */
		preferences = new Preferences(getActivity());	
		
		mPasswordView.setError(null);
		mPassword_confirmarView.setError(null);
		mOld_Password.setError(null);
		
		/** convert string */
		mPassword = mPasswordView.getText().toString();
		mPassword_Confirmar = mPassword_confirmarView.getText().toString();
		mPassword_Old = mOld_Password.getText().toString();
		
		Log.d(TAG, mPassword+" "+mPassword_Confirmar);
		
		boolean cancel = false;
		View focusView = null;
		
		/**Check for a valid password confir */
		if (TextUtils.isEmpty(mPassword_Confirmar)) {
			mPassword_confirmarView.setError(getString(R.string.error_field_required));
			focusView = mPassword_confirmarView;
			cancel = true;
		} else if (mPassword.length() < 5) {
			mPassword_confirmarView.setError(getString(R.string.error_invalid_password));
			focusView = mPassword_confirmarView;
			cancel = true;
		}else if(!mPassword_Confirmar.equals(mPassword)){
			mPassword_confirmarView.setError(getString(R.string.error_invalid_pasword_dont_match));
			focusView = mPassword_confirmarView;
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

		String passOld = AccountUtils.getPassword(getActivity(), preferences.getEmail());
		if(preferences.getEmail() != null){
			if(!passOld.equals(mPassword_Old)){
				mOld_Password.setError("Contraseña actual incorrecta");
				focusView = mOld_Password;
				cancel = true;
			}
		}		
		
		if (cancel) {
			focusView.requestFocus();
		} else {
			Log.d(TAG, "Enviar Broadcast");
			Intent i = new Intent(getActivity(), IkutUpload.class);
			i.setAction(Constant.CHANGE_PASSWORD);
			getActivity().startService(i);
			preferences.setPassMoment(mPassword);
			showProgress(true);
			boton.setEnabled(false);
			mPasswordView.setEnabled(false);
			mPassword_confirmarView.setEnabled(false);
			mOld_Password.setEnabled(false);
			

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
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context ctx, Intent intent) {
			// TODO Auto-generated method stub
			if(intent != null){
				if(intent.getAction().equals(Constant.PASSWORD_RECONET) == true){
					getActivity().stopService(new Intent(getActivity(), IkutUpload.class));
					showMessage(getResources().getString(R.string.error_ocurrido), Style.ALERT);
					showProgress(false);
					mPasswordView.setEnabled(true);
					mPassword_confirmarView.setEnabled(true);
					mOld_Password.setEnabled(true);					
				}else{
					if(intent.getAction().equals(Constant.OK) == true){
						getActivity().stopService(new Intent(getActivity(), IkutUpload.class));
						AccountUtils.setPasswordNew(getActivity(), preferences.getEmail(), mPassword);
						try {
							preferences.setPassMoment("");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						getActivity().finish();
					}else{
						if(intent.getAction().equals(Constant.FORGET_PASSWORD_OK) == true){
							Log.d(TAG, "Preferece Pass");
							Intent i = new Intent(getActivity(), ContainerFragment.class);
							i.putExtra(Constant.ID_SELECT_FRAGMENT, Constant.FRAGMENT_FORGET_PASSWORD);
							getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
							startActivity(i);		
							getActivity().finish();
						}
					}
				}
			}
		}
	};		
	
}//end class
