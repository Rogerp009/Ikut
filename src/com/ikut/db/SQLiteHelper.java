package com.ikut.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ikut.utils.Constant;

public class SQLiteHelper extends SQLiteOpenHelper{
	
	/**  sql */
	private String sql = "CREATE TABLE "
			+Constant.TABLE_MENSSEGER+ " ( " 
			+Constant.ID_PHONE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
			+ Constant.HORA + " TEXT NOT NULL, " 
			+ Constant.FECHA_ENVIADO +" TEXT NOT NULL, " 
			+ Constant.ASUNTO + " TEXT NOT NULL, " 
			+ Constant.CONTENIDO + " TEXT NOT NULL, "
			+ Constant.EMAIL + " TEXT NOT NULL, "
			+ Constant.KEY_NOMBRE + " TEXT NOT NULL " + " ) ";	
	
	private String sql2 = "CREATE TABLE "
			+Constant.TABLE_MENSSEGER_USER+ " ( " 
			+Constant.ID_PHONE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
			+ Constant.HORA + " TEXT NOT NULL, " 
			+ Constant.FECHA_ENVIADO +" TEXT NOT NULL, " 
			+ Constant.ASUNTO + " TEXT NOT NULL, " 
			+ Constant.CONTENIDO + " TEXT NOT NULL, "
			+ Constant.EMAIL + " TEXT NOT NULL, "
			+ Constant.KEY_NOMBRE + " TEXT NOT NULL " + " ) ";	
	
	public SQLiteHelper(Context context) {		
		super(context, Constant.DATABASE, null, Constant.VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		 db.execSQL(sql);
		 db.execSQL(sql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,  int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (newVersion > oldVersion	){
			//elimina tabla
			db.execSQL( "DROP TABLE IF EXISTS " + sql);
			db.execSQL( "DROP TABLE IF EXISTS " + sql2);
			onCreate(db);
		}		
	}

}//end class
