package com.ikut.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikut.ContainerFragment;
import com.ikut.Login;
import com.ikut.R;
import com.ikut.TouchEffect;
import com.ikut.db.SQLite;
import com.ikut.list.ContentList;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Letters;
import com.ikut.utils.Preferences;

public class FragmentDetailMessenge extends Fragment implements View.OnClickListener{

	/** method return fragment*/
	public static FragmentDetailMessenge newInstance() {
		FragmentDetailMessenge fragment = new FragmentDetailMessenge();
		return fragment;		
	}//end method
	
	public static final String POP_BACK_STACK = "com.ikut.pop_stack";
	
	private ImageView delete = null;
	private ImageView reply = null;
	public static final TouchEffect TOUCH = new TouchEffect();
	private View view;
	private int id = 0;
	private TextView nombre = null;
	private TextView email = null;
	private TextView asunto = null;
	private TextView contenido = null;
	private TextView fecha = null;
	private TextView hora = null;
	private ContentList.Messenger mItem;
	private SQLite sqlite = null;
	private int fragment = 0;
	
	String mAsunto = "";
	String mContenido = "";
	Preferences preferences = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		view =  inflater.inflate(R.layout.fragment_detail_messenge, container, false);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
		preferences = new Preferences(getActivity());
		sqlite = new SQLite(getActivity());
		sqlite.abrir();
				
		nombre = (TextView)view.findViewById(R.id.nombre);
		email = (TextView)view.findViewById(R.id.email);
		asunto = (TextView)view.findViewById(R.id.asunto);
		contenido = (TextView)view.findViewById(R.id.contenido);
		fecha = (TextView)view.findViewById(R.id.fecha);
		hora = (TextView)view.findViewById(R.id.hora);
		
		if(getArguments().containsKey("FRAGMENT") == true){
			if(getArguments().getInt("FRAGMENT") == 1){
				if(getArguments().containsKey("ID")){
					fragment = 1;
					mItem = ContentList.ITEM_MAP_USER.get(getArguments().getInt("ID"));
				}				
			}else{
				if(getArguments().containsKey("ID")){
					fragment = 2;
					mItem = ContentList.ITEM_MAP.get(getArguments().getInt("ID"));
				}				
			}
		}
		
		if(mItem != null){
			nombre.setText(mItem.nombre);
			asunto.setText(mItem.asunto);
			email.setText(mItem.email);
			contenido.setText(mItem.contenido);
			fecha.setText(mItem.fecha);
			hora.setText(mItem.hora);
			id = mItem.id;				
		}
		
		mAsunto = mItem.asunto;
		mContenido = mItem.contenido;
	    
		nombre.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		email.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		asunto.setTypeface(Letters.ROBOTOMEDIUM(getActivity()));
		contenido.setTypeface(Letters.ROBOTOREGULAR(getActivity()));
		fecha.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		hora.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		
		delete = (ImageView) view.findViewById(R.id.delete_messenger);
		delete.setOnTouchListener(TOUCH);
	
		reply = (ImageView) view.findViewById(R.id.reply_messenger);
		reply.setOnTouchListener(TOUCH);
		
		delete.setOnClickListener(this);
		reply.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.delete_messenger:
				if(id > 0){
					if(fragment == 1){
						sqlite.deleteMessengerUser(id);
						FragmentManager fm = getFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Log.i(Constant.TAG, fm.getBackStackEntryCount() + "Detail Manager Back");
						if (fm.getBackStackEntryCount() > 0) {          		
						    fm.popBackStack();
						    ft.setCustomAnimations(R.animator.anim_in_pop, R.animator.anim_out_pop).
						    replace(R.id.container, FragmentListMessengerUser.newInstance()).
						    commit();					    
						}						
					}else{
						sqlite.deleteMessenger(id);
						FragmentManager fm = getFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Log.i(Constant.TAG, fm.getBackStackEntryCount() + "Detail Manager Back");
						if (fm.getBackStackEntryCount() > 0) {          		
						    fm.popBackStack();
						    ft.setCustomAnimations(R.animator.anim_in_pop, R.animator.anim_out_pop).
						    replace(R.id.container, FragmentListMessenger.newInstance()).
						    commit();					    
						}						
					}
									
				}
			break;

		case R.id.reply_messenger:
			FragmentManager fm = getFragmentManager();
			Log.i(Constant.TAG, fm.getBackStackEntryCount() + "Detail Manager Back");
			if (fm.getBackStackEntryCount() > 0) {          		
			    fm.popBackStack();
				startActivity(new Intent(getActivity(), ContainerFragment.class).
						putExtra(Constant.ID_SELECT_FRAGMENT, Constant.FRAGMENT_SEND_MESSENGER).
						putExtra(Constant.CONTENIDO, mContenido).
						putExtra(Constant.ASUNTO, mAsunto));
				getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);				    
			}				
			break;			
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sqlite.cerrar();
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
	
}//end class
