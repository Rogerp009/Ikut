package com.ikut;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ikut.fragment.FragmentDetailMessenge;
import com.ikut.fragment.FragmentForgetPassword;
import com.ikut.fragment.FragmentPassword;
import com.ikut.fragment.FragmentPasswordChange;
import com.ikut.fragment.FragmentPhoneChange;
import com.ikut.fragment.FragmentSendMessenger;
import com.ikut.fragment.FragmentSettings;
import com.ikut.list.ContentList;
import com.ikut.service.IkutUpload;
import com.ikut.utils.AccountUtils;
import com.ikut.utils.Constant;
import com.ikut.utils.Functions;
import com.ikut.utils.Menus;

public class ContainerFragment extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);
		
		if(!checkPlayServices()){dialogPlayServices();}
		checkSession();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);//show up
		getActionBar().setTitle(getResources().getString(R.string.nav_bar_messenger_send));
		
		if (savedInstanceState == null) {	
			if(getIntent().getExtras() != null){
				if(getIntent().getExtras().getInt(Constant.ID_SELECT_FRAGMENT) == Constant.FRAGMENT_SEND_MESSENGER){
					if(getIntent().getExtras().containsKey(Constant.CONTENIDO) && getIntent().getExtras().containsKey(Constant.ASUNTO)){
						Bundle b = new Bundle();
						b.putString(Constant.CONTENIDO, getIntent().getExtras().getString(Constant.CONTENIDO));
						b.putString(Constant.ASUNTO, getIntent().getExtras().getString(Constant.ASUNTO));
						
						FragmentSendMessenger f = new FragmentSendMessenger();
						f.setArguments(b);
						getSupportFragmentManager().
						beginTransaction().						
						add(R.id.detail_container_fragment, f).
						setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).
						commit();											
					}else{
						getSupportFragmentManager().
						beginTransaction().
						add(R.id.detail_container_fragment, FragmentSendMessenger.newInstance()).
						setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).
						commit();						
					}
				}else{
					if(getIntent().getExtras().getInt(Constant.ID_SELECT_FRAGMENT) == Constant.FRAGMENT_SETTING){
						FragmentSettings fragment = new FragmentSettings();
						getFragmentManager().beginTransaction().add(R.id.detail_container_fragment, fragment).commit();
						getActionBar().setTitle("Configuración");
						
					}else{
						if(getIntent().getExtras().getInt(Constant.ID_SELECT_FRAGMENT) == Constant.FRAGMENT_PASSWORD_CHANGE){
							getSupportFragmentManager().
							beginTransaction().
							replace(R.id.detail_container_fragment, FragmentPasswordChange.newInstance()).
							setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).							
							commit();
							getActionBar().setTitle("Cambiar Contraseña");
						}else{
							if(getIntent().getExtras().getInt(Constant.ID_SELECT_FRAGMENT) == Constant.FRAGMENT_PHONE_CHANGE){
								getSupportFragmentManager().
								beginTransaction().
								replace(R.id.detail_container_fragment, FragmentPhoneChange.newInstance()).
								setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).
								commit();
								getActionBar().setTitle("Cambiar Telefono");
							}else{
								if(getIntent().getExtras().getInt(Constant.ID_SELECT_FRAGMENT) == Constant.FRAGMENT_FORGET_PASSWORD){
									getSupportFragmentManager().
									beginTransaction().
									replace(R.id.detail_container_fragment, FragmentForgetPassword.newInstance()).
									setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).
									commit();
									getActionBar().setTitle("Codigo de verificación");									
								}else{
									if(getIntent().getExtras().getInt(Constant.ID_SELECT_FRAGMENT) == Constant.FRAGMENT_PASSWORD){
										getSupportFragmentManager().
										beginTransaction().
										replace(R.id.detail_container_fragment, FragmentPassword.newInstance()).
										setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).
										commit();
										getActionBar().setTitle("Cambiar contraseña");											
									}else{
										if(getIntent().getExtras().getInt(Constant.ID_SELECT_FRAGMENT) == Constant.FRAGMENT_DETAIL_MESSENGER){
											Bundle argument = new Bundle();
											argument.putInt("ID", getIntent().getExtras().getInt("ID"));
											argument.putInt("FRAGMENT", getIntent().getExtras().getInt("FRAGMENT"));
											FragmentDetailMessenge fragment = new FragmentDetailMessenge();
											fragment.setArguments(argument);											
											getSupportFragmentManager().
											beginTransaction().						
											add(R.id.detail_container_fragment, fragment).
											setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).
											commit();											
										}
									}
								}
							}							
						}						
					}
				}				
			}else{
				Log.e(Constant.TAG, getIntent().getExtras().toString());
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menus.HOME:
			stopService(new Intent(ContainerFragment.this, IkutUpload.class));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		sendBroadcast(new Intent("com.ikut.stop"));
		stopService(new Intent(ContainerFragment.this, IkutUpload.class));
		finish();
	}
	
	public void doNegativeClick() {
		// TODO Auto-generated method stub
		Log.i("FragmentSetting", "Negativo click!");
	}

	public void doPositiveClick() {
		// TODO Auto-generated method stub
		 Log.i("FragmentSetting", "Positivo click!");
	}	
	
	public void checkSession(){
		if(Functions.Session(getApplicationContext()) != 4){
            AccountUtils.removeAccount(getApplicationContext());
            finish();
            startActivity(new Intent(this, Login.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));			
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
}
