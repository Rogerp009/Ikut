package com.ikut.fragment;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.ikut.db.SQLite;
import com.ikut.list.AdapteMessenge;
import com.ikut.list.ContentList;
import com.ikut.list.ContentList.Messenger;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Functions;
import com.ikut.utils.Letters;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class FragmentListMessenger extends Fragment{

	/** method return fragment*/
	public static FragmentListMessenger newInstance() {
		FragmentListMessenger fragment = new FragmentListMessenger();
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
	private View viewContainer;
	View linea = null;
	private TextView text = null;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		view =  inflater.inflate(R.layout.fragment_list_messenger, container, false);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
		linea = view.findViewById(R.id.no_exist);
		text = (TextView)view.findViewById(R.id.textView1);
		text.setTypeface(Letters.ROBOTOLIGHT(getActivity()));
		
		sqlite = new SQLite(getActivity());
		sqlite.abrir();
		ContentList.ITEMS.clear();
		
		listView = (ListView) view.findViewById( R.id.list_messenge);
		viewContainer = view.findViewById(R.id.undobar);
		listMsg =  new ArrayList<Messenger>();
		Cursor cursor = sqlite.getMessengers();
		ArrayList<String> listData = sqlite.getListMessenger(cursor);
		if( listData.size() == 0 ){
			showProgress(true);
		}
		
		if(cursor != null){
			if(cursor.moveToFirst()){
				do {			
					String[] t = cursor.getString(2).split("-");
					ContentList.addItem(new Messenger (cursor.getInt(0), cursor.getString(1), Functions.SplipFecha(cursor.getString(2))+" "+t[0], cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6),R.drawable.flat_ile ));			
				} while (cursor.moveToNext());
			}				
		}
		
		//listMsg = new ArrayList<Messenger>(new HashSet<Messenger>(ContentList.ITEMS));//no repetir elementos		
		adaptador = new AdapteMessenge(getActivity(), ContentList.ITEMS);
		listView.setAdapter(adaptador);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				getActivity().sendBroadcast(new Intent("com.ikut.showBotton_yes"));
				Bundle argument = new Bundle();
				argument.putInt("ID", ContentList.ITEMS.get(position).id);
				argument.putInt("FRAGMENT", 2);
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
				*/
			}
		});
		
		//Deslizar item para borrarlo
		SwipeListViewTouchListener touchListener = new SwipeListViewTouchListener(listView, new SwipeListViewTouchListener.OnSwipeCallback() {
			@Override
			public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
				//Aqui ponemos lo que hara el programa cuando deslizamos un item ha la izquierda
				int i = ContentList.ITEMS.get(reverseSortedPositions[0]).id;
				ContentList.ITEMS.remove((reverseSortedPositions[0]));
				ContentList.removeItem(i);
				Log.d(SAVE_MESSENGER, Integer.toString(i));
				if(ContentList.ITEMS.size() == 0){
					showProgress(true);
				}
			}

			@Override
			public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
				//Aqui ponemos lo que hara el programa cuando deslizamos un item ha la derecha
				int i = ContentList.ITEMS.get(reverseSortedPositions[0]).id;
				ContentList.ITEMS.remove((reverseSortedPositions[0]));
				ContentList.removeItem(i);
				Log.d(SAVE_MESSENGER, Integer.toString(i));
				if(ContentList.ITEMS.size() == 0){
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

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void showUndo(final View viewContainer) {
		    viewContainer.setVisibility(View.VISIBLE);
		    viewContainer.setAlpha(1);
		    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
		    	viewContainer.animate().alpha(0.4f).setDuration(5000).setListener(new AnimatorListenerAdapter() {

					@Override
					public void onAnimationEnd(Animator animation) {
						// TODO Auto-generated method stub
						 viewContainer.setVisibility(View.GONE);
					}
				});
		    	
		    }else{
			    viewContainer.animate().alpha(0.4f).setDuration(5000).withEndAction(new Runnable() {
		          @Override
		          public void run() {
		            viewContainer.setVisibility(View.GONE);
		          }
		        });		    	
		    }

	  }
	  
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		  showUndo(viewContainer);
	    return true;
	  }	  
	  
		/** anim and show cargando and gone form*/
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
		
		// CLEAR BACK STACK.
		private void clearBackStack() {
		    final FragmentManager fragmentManager = getFragmentManager();
		    while (fragmentManager.getBackStackEntryCount() != 0) {
		    	fragmentManager.popBackStackImmediate();
		    }
		}		
}//end class

