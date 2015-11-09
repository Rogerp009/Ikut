package com.ikut.list;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikut.R;
import com.ikut.list.ContentList.Messenger;
import com.ikut.utils.Letters;

public class AdapteMessenge extends BaseAdapter{

    protected Context ctx;
	protected ArrayList<Messenger> items;

	public AdapteMessenge(Context context, ArrayList<Messenger> messenge) {
		// TODO Auto-generated constructor stub
		this.ctx = context;
        this.items = messenge;
	}
	
	@Override
	public int getCount() {
	
		return items.size();
	}
	 
	@Override
	public Object getItem(int position) {
	
		return items.get(position);
	}
	 
	@Override
	public long getItemId(int position) {
	
		return items.get(position).getId();
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();	 
		Messenger msg = items.get(position);
		
		View view = convertView;	

		if(convertView == null){
			LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.item_list, null);
		}

		holder.img = (ImageView) view.findViewById(R.id.img_list);
		holder.asunto = (TextView) view.findViewById(R.id.text_list_messege);
		holder.contenido = (TextView) view.findViewById(R.id.subtitle_list_messenge);		
		holder.fecha = (TextView) view.findViewById(R.id.text_list_fecha);
		
		holder.img.setImageResource(msg.getImageID());
		holder.asunto.setText(msg.getAsunto());
		holder.contenido.setText(msg.getContenido());
		holder.fecha.setText(msg.getFecha());
				
		holder.fecha.setTypeface(Letters.ROBOTOLIGHT(ctx));
		holder.asunto.setTypeface(Letters.ROBOTOMEDIUM(ctx));
		holder.contenido.setTypeface(Letters.ROBOTOLIGHT(ctx));
		return view;
	}

	public static class ViewHolder {
		public ImageView img = null;		
		public TextView asunto = null;
		public TextView contenido = null;
		public TextView fecha = null;
	}	
}
