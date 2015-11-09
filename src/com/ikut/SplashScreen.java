package com.ikut;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ikut.R.color;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Preferences;
import com.ikut.utils.Session;

public class SplashScreen extends Activity {

	String email = "";
	Preferences preferences = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//orientation postrait and navigation not
		setContentView(R.layout.splash_screen);//adapte layout

		preferences = new Preferences(getApplicationContext());
		email = AccountUtils.getEmail(getApplicationContext());
		 
		if(!preferences.getTour()){
			// TODO Auto-generated method stub
			if(!Session.SessionExistsPreference(getApplicationContext()) && !AccountUtils.getAccounts(getApplicationContext())){
				goToTour();
			}else{
				if(!Session.SessionExistsPreference(getApplicationContext()) && AccountUtils.getAccounts(getApplicationContext())){
					preferences.setEmail(email);
					preferences.setFlat(true);
					preferences.setEstado(AccountUtils.getAccountStatus(getApplicationContext(), email));
					preferences.setNombre(AccountUtils.getAccountName(getApplicationContext(), email));
					preferences.setGCM_ID(AccountUtils.getAccountGCM(getApplicationContext(), email));
					Log.d("PREFERENCE = false", preferences.toString()+" "+AccountUtils.getAccountName(getApplicationContext(), email));
					startActivity(new Intent(SplashScreen.this, UserAccount.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
					overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
					finish();					
				}else{
					if((Session.SessionExistsPreference(getApplicationContext()) && !AccountUtils.getAccounts(getApplicationContext()))){
						goToMain();
					}
				}
			}
		}else{
			if(preferences.getTour()){
				if(!Session.SessionExistsPreference(getApplicationContext()) && !AccountUtils.getAccounts(getApplicationContext())){
					preferences.clear();
					goToMain();						
				}else{
					if(!Session.SessionExistsPreference(getApplicationContext()) && AccountUtils.getAccounts(getApplicationContext())){
						preferences.setEmail(email);
						preferences.setFlat(true);
						preferences.setEstado(AccountUtils.getAccountStatus(getApplicationContext(), email));
						preferences.setNombre(AccountUtils.getAccountName(getApplicationContext(), email));
						preferences.setGCM_ID(AccountUtils.getAccountGCM(getApplicationContext(), email));
						Log.d("PREFERENCE = false", preferences.toString()+" "+AccountUtils.getAccountName(getApplicationContext(), email));
						startActivity(new Intent(SplashScreen.this, UserAccount.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
						overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
						finish();							
					}else{
						if(Session.SessionExistsPreference(getApplicationContext()) && !AccountUtils.getAccounts(getApplicationContext())){
							preferences.clear();
							goToMain();										
						}else{
							if(Session.SessionExistsPreference(getApplicationContext()) && AccountUtils.getAccounts(getApplicationContext())){
								startActivity(new Intent(SplashScreen.this, UserAccount.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
								overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
								finish();									
							}else{
								startActivity(new Intent(SplashScreen.this, UserAccount.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
								overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
								finish();	
							}
						}
					}
				}
			}
		}
	}//onCreate


	private void goToMain() {//ir a login o account user
		// TODO Auto-generated method stub
		TimerTask task = new TimerTask() {//instances timer
			
			@Override
			public void run() {
				// TODO Auto-generated method stub				
				startActivity(new Intent(SplashScreen.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
				overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
				finish();
				
			}//end run
		};//end TimerTask
				
		Timer timer = new Timer();//process 2 seconds		
		timer.schedule(task, Constant.SPLASH_SCREEN_DELAY);		
	}
	
	private void goToTour() {//ir a login o account user
		// TODO Auto-generated method stub
		TimerTask task = new TimerTask() {//instances timer
			
			@Override
			public void run() {
				// TODO Auto-generated method stub				
				startActivity(new Intent(SplashScreen.this, Tour.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));//call fragmentActivity				
				overridePendingTransition(R.animator.anim_in_pop, R.animator.anim_out_pop);
				finish();
				
			}//end run
		};//end TimerTask
				
		Timer timer = new Timer();//process 2 seconds		
		timer.schedule(task, Constant.SPLASH_SCREEN_DELAY);		
	}
		
	@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}//end method	
	
	
	public void SystemBarTintManager(){ //system bar trasnluci		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setNavigationBarTintResource(color.primary);
			tintManager.setStatusBarTintResource(color.primary);
		}		
	}//end method
}//end class
