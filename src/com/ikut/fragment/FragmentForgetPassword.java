package com.ikut.fragment;

import android.content.Intent;
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

import com.ikut.ContainerFragment;
import com.ikut.Login;
import com.ikut.R;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Letters;
import com.ikut.utils.Preferences;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class FragmentForgetPassword extends Fragment{
	
	/** method return fragment*/
	public static FragmentForgetPassword newInstance() {
		FragmentForgetPassword fragment = new FragmentForgetPassword();
		return fragment;		
	}//end method
		
	private TextView text_code = null;
	private EditText mCode = null;
	private Button btn = null;
	private View view = null;
	private String codigo = "";
	private String codigo_pref = "";
	Preferences preferences = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view =  inflater.inflate(R.layout.fragment_forget_password, container, false);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
		preferences = new Preferences(getActivity());
		showMessage("Verfique su correo electronico", Style.CONFIRM);
		codigo_pref = preferences.getCode();
		text_code = (TextView)view.findViewById(R.id.code_verification);
		mCode  = (EditText)view.findViewById(R.id.code);
		btn  = (Button)view.findViewById(R.id.code_btn);
		
		text_code.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Reset errors.
				mCode.setError(null);
				
				boolean cancel = false;
				View focusView = null;
				codigo = mCode.getText().toString();
				
				/**Check for a valid password */
				if (TextUtils.isEmpty(codigo)) {
					mCode.setError(getString(R.string.error_field_required));
					focusView = mCode;
					cancel = true;
				} else if (codigo.length() < 4) {
					mCode.setError("Codigo muy corto");
					focusView = mCode;
					cancel = true;
				}
				
				if (cancel) {
					focusView.requestFocus();
				} else {
					if(codigo.length() == 4){
						if(!codigo_pref.equals("")){
							if(codigo_pref.equals(codigo)){
								Intent i = new Intent(getActivity(), ContainerFragment.class);
								i.putExtra(Constant.ID_SELECT_FRAGMENT, Constant.FRAGMENT_PASSWORD);
								getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
								startActivity(i);
								getActivity().finish();
							}else{
								showMessage("Verifique codigo de activacíon", Style.ALERT);
							}
						}else{
							showMessage(getResources().getString(R.string.error_ocurrido), Style.ALERT);	
						}
					}else{
						showMessage(getResources().getString(R.string.error_ocurrido), Style.ALERT);	
					}
				}				
			}
		});
		return view;
	}

	public void showMessage(String message, Style style){
		if(getActivity() != null){
			Crouton.showText(getActivity(), message, style);
		}
	}//end method
	
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
