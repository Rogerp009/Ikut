package com.ikut.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ikut.utils.Constant;

public class SQLite {

	/** var */
	private SQLiteHelper sqliteHelper = null;
	private SQLiteDatabase db = null;	
	
	/** Constructor de clase */
	public SQLite(Context context){
		sqliteHelper = new SQLiteHelper(context);
	}	
	
	/** Abre conexion a base de datos */
	public void abrir(){
		Log.i("SQLite", "Se abre conexion a la base de datos " + sqliteHelper.getDatabaseName() );
		db = sqliteHelper.getWritableDatabase(); 	
		
	}//end method
	
	/** Cierra conexion a la base de datos */
	public void cerrar(){
		Log.i("SQLite", "Se cierra conexion a la base de datos " + sqliteHelper.getDatabaseName() );
		sqliteHelper.close();		
	}//end method	
	
	public boolean addMessenger(String hora, String fecha_enviado, String asunto, String contenido, String email, String nombre){
		if(nombre.length() > 0 ){
			ContentValues contentValues = new ContentValues();
			contentValues.put(Constant.HORA, hora);
			contentValues.put(Constant.FECHA_ENVIADO, fecha_enviado);				
			contentValues.put(Constant.ASUNTO, asunto);
			contentValues.put(Constant.CONTENIDO, contenido);
			contentValues.put(Constant.EMAIL, email);
			contentValues.put(Constant.KEY_NOMBRE, nombre);				
			Log.i("SQLite", "Add Messenger " );
			return ( db.insert(Constant.TABLE_MENSSEGER , null, contentValues ) != -1 ) ? true : false;					
		}else
			return false;	
	}//end
	
	public boolean addMessengerUser(String hora, String fecha_enviado, String asunto, String contenido, String email, String nombre){
		if(nombre.length() > 0 ){
			ContentValues contentValues = new ContentValues();
			contentValues.put(Constant.HORA, hora);
			contentValues.put(Constant.FECHA_ENVIADO, fecha_enviado);				
			contentValues.put(Constant.ASUNTO, asunto);
			contentValues.put(Constant.CONTENIDO, contenido);
			contentValues.put(Constant.EMAIL, email);
			contentValues.put(Constant.KEY_NOMBRE, nombre);				
			Log.i("SQLite", "Add Messenger User" );
			return ( db.insert(Constant.TABLE_MENSSEGER_USER , null, contentValues ) != -1 ) ? true : false;					
		}else
			return false;	
	}//end
	
	public boolean deleteMessenger(int id){
		//table , whereClause, whereArgs
		return  (db.delete(Constant.TABLE_MENSSEGER, Constant.ID_PHONE + " = " + id ,  null) > 0) ? true : false;	
	}	

	public void getIdMessenger(int id){
		//table , whereClause, whereArgs
			db.query(Constant.TABLE_MENSSEGER ,	new String[]{Constant.ID_PHONE}, Constant.ID_PHONE + " = " + id , null, null, null, null);		
		 
	}	
	
	public boolean deleteMessengerUser(int id){
		//table , whereClause, whereArgs
		return  (db.delete(Constant.TABLE_MENSSEGER_USER, Constant.ID_PHONE + " = " + id ,  null) > 0) ? true : false;	
	}	
	
	public Cursor getMessengers(){
		return db.query(Constant.TABLE_MENSSEGER, new String[]{
				Constant.ID_PHONE,
				Constant.HORA,
				Constant.FECHA_ENVIADO,
				Constant.ASUNTO,
				Constant.CONTENIDO,
				Constant.EMAIL,
				Constant.KEY_NOMBRE}, null, null, null, null, Constant.ID_PHONE+" DESC");
	}
	
	public Cursor getMessenger( int id ){
		return db.query(Constant.TABLE_MENSSEGER ,				
					new String[]{
					Constant.HORA ,
					Constant.FECHA_ENVIADO,
					Constant.ASUNTO,
					Constant.CONTENIDO,
					Constant.EMAIL,
					Constant.KEY_NOMBRE}, Constant.ID_PHONE + " = " + id , null, null, null, null);
	}	
	
	public Cursor getMessengerUser(){
		return db.query(Constant.TABLE_MENSSEGER_USER, new String[]{
					Constant.ID_PHONE,
					Constant.HORA,
					Constant.FECHA_ENVIADO,
					Constant.ASUNTO,
					Constant.CONTENIDO,
					Constant.EMAIL,
					Constant.KEY_NOMBRE}, null, null, null, null, Constant.ID_PHONE+" DESC");
	}	
	
	
	public ArrayList<String> getListMessenger(Cursor cursor){
		ArrayList<String> list = new ArrayList<String>();
		String item = "";
			if(cursor.moveToFirst() ){
				do{
					item += "ID: [" + cursor.getInt(0) + "]\r\n";
					item += "Hora: " + cursor.getString(1) + "\r\n";
					item += "Fecha: " + cursor.getString(2) + "\r\n";
					item += "Asunto: " + cursor.getString(3) + "\r\n";
					item += "Contenido: " + cursor.getString(4) + "\r\n";
					item += "Email: " + cursor.getString(5) + "\r\n";
					item += "Nombre: " + cursor.getString(6) + "";
					list.add(item);
					item = "";		         
				} while ( cursor.moveToNext() );
			}		
		return list;		
	}	
}//end class
