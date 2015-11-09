package com.ikut.fragment;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ikut.ContainerFragment;
import com.ikut.Login;
import com.ikut.R;
import com.ikut.SwipeListViewTouchListener;
import com.ikut.TouchEffect;
import com.ikut.db.SQLite;
import com.ikut.list.AdapteMessenge;
import com.ikut.list.ContentList;
import com.ikut.list.ContentList.Messenger;
import com.ikut.service.IkutService;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Functions;
import com.ikut.utils.Letters;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class FragmentListMessengerUser extends Fragment{

	/** method return fragment*/
	public static FragmentListMessengerUser newInstance() {
		FragmentListMessengerUser fragment = new FragmentListMessengerUser();
		return fragment;		
	}//end method
	private SQLite sqlite = null;
	private ListView listView;
	private AdapteMessenge adaptador ;	
	public static String SAVE_MESSENGER = "save_menssenger";
	ArrayList<Messenger> listMsg;
	private View view;
	String nombre = ""; 
	String hora = ""; 
	String email = ""; 
	String fecha= ""; 
	String message = "";	
	String asunto = "";
	private TextView text = null;
	private View linea;
	public static final TouchEffect TOUCH = new TouchEffect();
	ArrayList<String> listData = null;
	/** Constructor */
	public FragmentListMessengerUser(){
		
	}	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
        // Open the default realm ones for the UI thread.
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		view =  inflater.inflate(R.layout.fragment_list_messenger, container, false);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
		linea = view.findViewById(R.id.no_exist);
		text = (TextView)view.findViewById(R.id.textView1);
		text.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		listView = (ListView) view.findViewById( R.id.list_messenge);
		
		ContentList.ITEMS_USER.clear();
		sqlite = new SQLite(getActivity());
		sqlite.abrir();		
		
		listMsg =  new ArrayList<Messenger>();
		Cursor cursor = sqlite.getMessengerUser();
		listData = sqlite.getListMessenger(cursor);
		if( listData.size() == 0 ){
			showProgress(true);
		}
		
		if(cursor != null){
			if(cursor.moveToFirst()){
				do {			
					String[] t = cursor.getString(2).split("-");
					ContentList.addItemUser(new Messenger (cursor.getInt(0), cursor.getString(1), Functions.SplipFecha(cursor.getString(2))+" "+t[0], cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6),R.drawable.flat_ile ));				
				} while (cursor.moveToNext());
			}				
		}
		
		//listMsg = new ArrayList<Messenger>(new HashSet<Messenger>(ContentList.ITEMS_USER));//no repetir elementos
		adaptador = new AdapteMessenge(getActivity(), ContentList.ITEMS_USER);
		listView.setAdapter(adaptador);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub				
				Bundle argument = new Bundle();
				argument.putInt("ID", ContentList.ITEMS_USER.get(position).id);
				argument.putInt("FRAGMENT", 1);
				startActivity(new Intent(getActivity(), ContainerFragment.class).
						putExtras(argument).
						putExtra(Constant.ID_SELECT_FRAGMENT, Constant.FRAGMENT_DETAIL_MESSENGER));
				
				/**
				FragmentDetailMessenge fragment = new FragmentDetailMessenge();
				fragment.setArguments(argument);
				getFragmentManager().
				beginTransaction().
				setCustomAnimations(R.animator.anim_in_pop, R.animator.anim_out_pop, R.animator.anim_in_pop, R.animator.anim_out_pop).
				addToBackStack(null).
				replace(R.id.container, fragment).
				commit();
				getActivity().sendBroadcast(new Intent("com.ikut.showBotton_yes"));
				*/
			}
		});
		
		//Deslizar item para borrarlo
		SwipeListViewTouchListener touchListener = new SwipeListViewTouchListener(listView, new SwipeListViewTouchListener.OnSwipeCallback() {
			@Override
			public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
				//Aqui ponemos lo que hara el programa cuando deslizamos un item ha la izquierda
				int i = ContentList.ITEMS_USER.get(reverseSortedPositions[0]).id;
				ContentList.ITEMS_USER.remove((reverseSortedPositions[0]));
				ContentList.removeItemUser(i);
				Log.d(SAVE_MESSENGER, Integer.toString(i));
				if(ContentList.ITEMS_USER.size() == 0){
					showProgress(true);
				}
			}

			@Override
			public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
				//Aqui ponemos lo que hara el programa cuando deslizamos un item ha la derecha
				int i = ContentList.ITEMS_USER.get(reverseSortedPositions[0]).id;
				ContentList.ITEMS_USER.remove((reverseSortedPositions[0]));
				ContentList.removeItemUser(i);
				Log.d(SAVE_MESSENGER, Integer.toString(i));
				if(ContentList.ITEMS_USER.size() == 0){
					showProgress(true);
				}
			}
		},true, false);
		
		//Escuchadores del listView
		listView.setOnTouchListener(touchListener);
		listView.setOnScrollListener(touchListener.makeScrollListener());			
		return view;
	}
    
	public void showMessage(String message, Style style){
		if(getActivity() != null){
			Crouton.showText(getActivity(), message, style);
		}
	}//end method
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {//anim
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			linea.setVisibility(View.VISIBLE);
			linea.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							linea.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

	}//end method 	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(showList);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(IkutService.ACTION_MAIN);
		getActivity().registerReceiver(showList, filter);
		
		if(!AccountUtils.getAccounts(getActivity())){
			startActivity(new Intent(getActivity(), Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
			getActivity().overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
			getActivity().finish();			
		}else{
			Log.d(Constant.TAG, "Exist Account");
		}
		
	}

	private BroadcastReceiver showList = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context ctx, Intent intent) {
			// TODO Auto-generated method stub
			if (intent != null) {
				
			}
		}
	};	
}//end class
	