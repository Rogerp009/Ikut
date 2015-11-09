package com.ikut.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ikut.UserAccount;
import com.ikut.db.SQLite;

@SuppressLint("UseSparseArrays")
public class ContentList {
	
	/** -- -- -- -- -- -- --  OTHER -- -- -- -- -- -- -- -- -- -- -- */
	public static ArrayList<Messenger> ITEMS = new ArrayList<Messenger>();//OTHER
	public static Map<Integer, Messenger> ITEM_MAP = new HashMap<Integer, Messenger>();
	
	public static void addItem(Messenger item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}	
	public static void removeItem(int id) {
		SQLite sqlite = new SQLite(UserAccount.context);
		sqlite.abrir();
		sqlite.deleteMessenger(id);
		Log.d("CONTENTLIST", "ELIMINAR");
		sqlite.cerrar();
	}	
	
	/** -- -- -- -- -- -- -- USER -- -- -- -- -- -- -- -- -- -- -- */	
	public static ArrayList<Messenger> ITEMS_USER = new ArrayList<Messenger>();//USER
	public static Map<Integer, Messenger> ITEM_MAP_USER = new HashMap<Integer, Messenger>();
	
	public static void addItemUser(Messenger item) {
		ITEMS_USER.add(item);
		ITEM_MAP_USER.put(item.id, item);
	}	
	public static void removeItemUser(int id) {
		SQLite sqlite = new SQLite(UserAccount.context);
		sqlite.abrir();
		sqlite.deleteMessengerUser(id);
		Log.d("CONTENTLIST", "ELIMINAR");
		sqlite.cerrar();
	}	
	
	
	public static class Messenger{
		public String asunto;
		public int image;
		public String contenido;
		public int id;
		public String fecha;
		public String hora;
		public String nombre;
		public String email;
		
		public Messenger(int id, String hora, String fecha, String asunto, String contenido, String email, String nombre, int image) {
			this.asunto = asunto;
			this.image = image;
			this.contenido = contenido;
			this.id = id;
			this.fecha = fecha;
			this.hora = hora;
			this.nombre = nombre;
			this.email = email;
		}
	
		public String getAsunto() {
			return asunto;
		}
	
		public void setAsunto(String asunto) {
			this.asunto = asunto;
		}
	
		public String getFecha() {
			return fecha;
		}
	
		public void setFecha(String fecha) {
			this.fecha = fecha;
		}
		
		public int getImageID() {
			return image;
		}
	
		public void setmage(int image) {
			this.image = image;
		}	
		
		public String getContenido() {
			return contenido;
		}
	
		public void setContenido(String contenido) {
			this.contenido = contenido;
		}
		
		public int getId() {
			return id;
		}
		 
		public void setId(int id) {
			this.id = id;
		}
		
		public String getNombre() {
			return nombre;
		}
	
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}		
		
		public String getHora() {
			return hora;
		}
	
		public void setHora(String hora) {
			this.hora = hora;
		}	
		
		public String getEmail() {
			return email;
		}
	
		public void setEmail(String email) {
			this.email = email;
		}		
	}
}
