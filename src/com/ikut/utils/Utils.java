package com.ikut.utils;

import android.content.Context;

import com.ikut.R;

public class Utils {
	
	//Set all the navigation icons and always to set "zero 0" for the item is a category
	public static int[] iconNavigation = new int[] { 
			0,
			0,
			R.drawable.ic_action_settings,
			R.drawable.ic_action_help};	
	
	//get title of the item navigation
	public static String getTitleItem(Context context, int posicion){		
		String[] titulos = context.getResources().getStringArray(R.array.nav_menu_items);  
		return titulos[posicion];
	} 
	
}//end class
