package com.jonathanysp.weddingapp;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MenuAdapter extends BaseAdapter {
	
	int numberOfItems = 5;
	String[] items = new String[numberOfItems];
	Bitmap[] icons = new Bitmap[numberOfItems];
	Context context;
	
	public MenuAdapter(Context c){
		context = c;
		
		items[0] = "Events";
		items[1] = "Camera";
		items[2] = "Gallery";
		items[3] = "Directions";
		items[4] = "Log Out";
		
		for(int i = 0; i < numberOfItems; i++){
			icons[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View grid;
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			grid = new View(context);
			grid = inflater.inflate(R.layout.menu_item, null); 
		}else{
			grid = (View)convertView; 
		}

		ImageView imageView = (ImageView)grid.findViewById(R.id.imageView1);
		imageView.setImageBitmap(icons[position]);
		TextView textView = (TextView)grid.findViewById(R.id.textView1);
		textView.setText(items[position]);
		    
		return grid;
	}
	
}
