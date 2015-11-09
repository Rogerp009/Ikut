package com.ikut;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ikut.db.SQLite;
import com.ikut.fragment.FragmentListMessenger;
import com.ikut.fragment.FragmentListMessengerUser;
import com.ikut.navigation.NavigationAdapter;
import com.ikut.navigation.NavigationList;
import com.ikut.service.IkutService;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Functions;
import com.ikut.utils.Menus;
import com.ikut.utils.Preferences;
import com.ikut.utils.Utils;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class UserAccount extends FragmentActivity implements View.OnClickListener{
	
	public static Context context;
	
	/** var */
	private FloatingButton addMessenger = null;
	private Preferences preferences = null;
	
	/** drawer */
    private int lastPosition = 0;
	private ListView listDrawer = null;    
	private int counterItemDownloads = 0;
	private DrawerLayout layoutDrawer = null;		
	private LinearLayout linearDrawer = null;
	private RelativeLayout userDrawer = null;
	private NavigationAdapter navigationAdapter = null;
	private ActionBarDrawerToggleCompat drawerToggle = null;	
	
	/** extras */
	private TextView title = null;
	private TextView sub_title = null;
	String mStatus, email = "";
	int select = 0;
	boolean hideMenus = true; //show overflow icon
	public static final TouchEffect TOUCH = new TouchEffect();
	private Menu optionsMenu = null;
	private Menu mMenu = null;
	private SQLite sqlite = null;
	private Cursor cursor = null;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		IkutService.actionStart(getApplicationContext());
		loadData();
		
		sqlite = new SQLite(getApplicationContext());
		sqlite.abrir();
		cursor = sqlite.getMessengerUser();

	}

	private void loadData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_account);
		
		getActionBar().setSubtitle("Inicio");
		context = UserAccount.this;
		preferences = new Preferences(getApplicationContext());
		if(!checkPlayServices()){dialogPlayServices();}
		checkSession();

		
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getBoolean(AccountManager.KEY_BOOLEAN_RESULT) == true){
            	showMessage("Solo se admite una cuenta en Ikut", Style.INFO);
            }
        }
		/** view or asing*/    
        listDrawer = (ListView) findViewById(R.id.listDrawer);        
		linearDrawer = (LinearLayout) findViewById(R.id.linearDrawer);		
		layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);			
		userDrawer = (RelativeLayout) findViewById(R.id.userDrawer);		
		title = (TextView)findViewById(R.id.tituloDrawer);
		sub_title = (TextView)findViewById(R.id.sub_title);
		
		/** header drawer*/
		if(preferences != null){
			try {
				title.setText(preferences.getNombre());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sub_title.setText(preferences.getEmail());
			email = preferences.getEmail();
			sub_title.setTextColor(getResources().getColor(R.color.primary));
		}
		
		if (listDrawer != null) {
			navigationAdapter = NavigationList.getNavigationAdapter(this);
		}
		
		userDrawer.setOnClickListener(userOnClick); 
		listDrawer.setAdapter(navigationAdapter);
		listDrawer.setOnItemClickListener(new DrawerItemClickListener());
		drawerToggle = new ActionBarDrawerToggleCompat(this, layoutDrawer);		
		layoutDrawer.setDrawerListener(drawerToggle);       		
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		if (savedInstanceState != null) { 			
			setLastPosition(savedInstanceState.getInt(Constant.LAST_POSITION)); 				
			if (lastPosition < 5){
				navigationAdapter.resetarCheck();			
				navigationAdapter.setChecked(lastPosition, true);
			}    	
			
	    }else{
	    	setLastPosition(lastPosition); 
	    	setFragmentList(lastPosition);	    	
	    }	
		
		addMessenger = new FloatingButton.Builder(this)//floating bottom
    		.withDrawable(getResources().getDrawable(R.drawable.ic_action_new))//icon
    		.withButtonColor(getResources().getColor(R.color.primary))//color
    		.withGravity(Gravity.BOTTOM | Gravity.RIGHT)//position
    		.withMargins(0, 0, 12, 12)//margin    		
    		.create();//create			
		addMessenger.setOnTouchListener(TOUCH);
		addMessenger.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(UserAccount.this, ContainerFragment.class).
						putExtra(Constant.ID_SELECT_FRAGMENT, Constant.FRAGMENT_SEND_MESSENGER));
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					
			};
		});
		
	}//end onCreate

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub		
		super.onSaveInstanceState(outState);		
		outState.putInt(Constant.LAST_POSITION, lastPosition);					
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);	     
	    drawerToggle.syncState();	
	 }	
	
	public void setTitleActionBar(CharSequence information) {//informacao    	
    	getActionBar().setTitle(information);
    }	
	
	public void setSubtitleActionBar(CharSequence information) {    	
    	getActionBar().setSubtitle(information);
    }	

	public void setIconActionBar(int icon) {    	
    	getActionBar().setIcon(icon);
    }	
	
	public void setLastPosition(int position){		
		this.lastPosition = position;
	}	
	
	
	public void showButton(final boolean show){
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
			addMessenger.setVisibility(View.VISIBLE);
			addMessenger.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							addMessenger.setVisibility(show ? View.VISIBLE: View.GONE);
						}
					});			
			
				
	}//end method showButton
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);		
	}
	
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {          	        	
	    	setLastPosition(position);        	
	    	setFragmentList(lastPosition);	    	
	    	layoutDrawer.closeDrawer(linearDrawer);	    	
        }
    }	
    
	private OnClickListener userOnClick = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			layoutDrawer.closeDrawer(linearDrawer);
		}
	};
			
	private void setFragmentList(int position){
		
		FragmentManager fragmentManager = getSupportFragmentManager();		
		switch (position) {
		case Constant.FRAGMENT_LIST_MENSSAGER:
			getActionBar().setSubtitle("Notificaciones");
			fragmentManager.beginTransaction().setCustomAnimations(R.animator.anim_in_pop, R.animator.anim_out_pop).replace(R.id.container, FragmentListMessenger.newInstance()).commit();				
			//select = 1;
			break;
			
		case Constant.FRAGMENT_LIST_MESSENGER_USER:
			getActionBar().setSubtitle("Mis notificaciones");		
			fragmentManager.beginTransaction().setCustomAnimations(R.animator.anim_in_pop, R.animator.anim_out_pop).replace(R.id.container, FragmentListMessengerUser.newInstance()).commit();
			//select = 2;
			break;
			
		case Constant.FRAGMENT_SETTING:
			getActionBar().setSubtitle("Inicio");
			layoutDrawer.closeDrawer(linearDrawer);
			Intent intent = new Intent(this, ContainerFragment.class);
			intent.putExtra(Constant.ID_SELECT_FRAGMENT, Constant.FRAGMENT_SETTING);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			startActivity(intent);		
			break;	
				
		case 3:
			getActionBar().setSubtitle("Inicio");			
			sendEmail();
			break;			
		default:
			break;
		}
		
		if (position <= 4){
			navigationAdapter.resetarCheck();			
			navigationAdapter.setChecked(position, true);
		}
	}	
	
    
	private class ActionBarDrawerToggleCompat extends ActionBarDrawerToggle {


		/** contructor */
		public ActionBarDrawerToggleCompat(Activity mActivity, DrawerLayout mDrawerLayout){
			super(mActivity, mDrawerLayout, R.drawable.ic_action_navigation_drawer, R.string.drawer_open, R.string.drawer_close);
		}
	
		@Override
		public void onDrawerOpened(View drawerView) {	
			navigationAdapter.notifyDataSetChanged();			
			invalidateOptionsMenu();
			hideMenus = false;
			getActionBar().setTitle("Cuenta de usuario");
			showButton(false);//show boton
		}	
		
		@Override
		public void onDrawerClosed(View view) {			
			invalidateOptionsMenu();
			hideMenus = true;
			switch (select) {
			case 1:
				getActionBar().setTitle("Inicio");
				break;
			case 2:
				getActionBar().setTitle("Tu mensajes");
				break;
			default:
				break;
			}
			showButton(true);//ocultar boton
		}
	
	}//class
	
	public void setTitleFragments(int position){	
		setIconActionBar(Utils.iconNavigation[position]);
		setSubtitleActionBar(Utils.getTitleItem(UserAccount.this, position));				
	}

	public int getCounterItemDownloads() {
		return counterItemDownloads;
	}

	public void setCounterItemDownloads(int value) {
		this.counterItemDownloads = value;
	}
	
    
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		showButton(true);//show boton	
		android.app.FragmentManager fm = getFragmentManager();
		android.app.FragmentTransaction ft = fm.beginTransaction();
		Log.i(Constant.TAG, fm.getBackStackEntryCount() + "Back");
		if (fm.getBackStackEntryCount() > 1) {          		
		    fm.popBackStack();
		    ft.setCustomAnimations(R.animator.anim_in_pop, R.animator.anim_out_pop).commit(); 		   
		}
			
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}//end click
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
		
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {  

        if (drawerToggle.onOptionsItemSelected(item)) {
              return true;
        }		
		switch (item.getItemId()) {	
		
			case Menus.HOME:
					if (layoutDrawer.isDrawerOpen(linearDrawer)) {
						layoutDrawer.closeDrawer(linearDrawer);
					} else {
						layoutDrawer.openDrawer(linearDrawer);
					}
				return true;	
				
			case R.id.progress:
				setRefreshActionButtonState(true);
			return true;			
		default:
			return super.onOptionsItemSelected(item);			
		}	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 this.optionsMenu = menu;
		 getMenuInflater().inflate(R.menu.user_account, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
		IntentFilter filter = new IntentFilter();
		filter.addAction(IkutService.ACTION_REFLESH_STOP);
		filter.addAction("com.ikut.showBotton_yes");
		registerReceiver(receiver, filter);
		
		if(!AccountUtils.getAccounts(getApplicationContext())){
			startActivity(new Intent(getApplicationContext(), Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
			overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
			finish();			
		}else{
			Log.d(Constant.TAG, "Exist Account");
		}
	}


	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context ctx, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(IkutService.ACTION_REFLESH_STOP) == true){
				setRefreshActionButtonState(false);
			}else{
				if(intent.getAction().equals("com.ikut.showBotton_yes") == true){
					showButton(false);//show boton
				}
			}
		}
	};	
	
	public void setRefreshActionButtonState(final boolean refreshing) {
	    if (optionsMenu != null) {
	        final MenuItem refreshItem = optionsMenu.findItem(R.id.progress);
	        if (refreshItem != null) {
	            if (refreshing) {
	                refreshItem.setActionView(R.layout.download_messenger);
	                sendBroadcast(new Intent(IkutService.ACTION_REFLESH));
	            } else {
	            	refreshItem.isEnabled();
	                refreshItem.setActionView(null);
	            }
	        }
	    }	
	}

	private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, Constant.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(Constant.TAG, "This device is not supported.");
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
	
	public void checkSession(){
		if(Functions.Session(getApplicationContext()) != 4){
            AccountUtils.removeAccount(getApplicationContext());
            finish();
            startActivity(new Intent(this, Login.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));			
		}		
	}
	
	public void showMessage(String message, Style style){
		if(UserAccount.this != null){
			Crouton.showText(UserAccount.this, message, style);
		}
	}//end method
	
	public void sendEmail(){		
		String asunto = "Ayuda Ikut";
		String mensaje = "";
		
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		String[] to = {"rogerp.009@gmail.com"};
		String[] cc = {"rogerp.009@yahoo.com"};
		emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
		emailIntent.putExtra(Intent.EXTRA_CC, cc);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
		emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
		emailIntent.setType("message/rfc822");
		startActivity(Intent.createChooser(emailIntent, "Email "));		
	}
	
}//end class
