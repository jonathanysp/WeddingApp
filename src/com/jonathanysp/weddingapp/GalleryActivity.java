package com.jonathanysp.weddingapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jonathanysp.tools.HttpPostHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class GalleryActivity extends Activity {
	
	private ImageCache cache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//setup cache
		cache = new ImageCache(4*1024*1024);
		
		URL url;
		String[] links = null;
		try {
			url = new URL(tools.SERVER + "website/mobile/mobileImageArray.php");
			HttpURLConnection get = (HttpURLConnection) url.openConnection();
			get.connect();
			InputStream is = get.getInputStream();
			byte[] buffer = new byte[1024];
			StringBuffer sb = new StringBuffer();
			int read = is.read(buffer);
			while(read != -1){
				sb.append(new String(buffer, 0, read));
				read = is.read(buffer);
			}
			is.close();
			String msg = sb.toString();
			JSONArray array = new JSONArray(msg);
			links = new String[array.length()];
			for(int i = 0; i < array.length(); i++){
				links[i] = array.getJSONObject(i).getString("filename");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final GridView gv = (GridView) findViewById(R.id.gridView1);
		gv.setAdapter(new GalleryAdapter(links));
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				// TODO Auto-generated method stub
				//Log.d("http", (String) gv.getAdapter().getItem(pos));
				Intent i = new Intent(GalleryActivity.this, ImageActivity.class);
				i.putExtra("name", (String) gv.getAdapter().getItem(pos));
				startActivity(i);
			}
		});
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_gallery, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class downloadImage extends AsyncTask<String, Integer, Bitmap>{
		
		ImageView imageV;
		
		public downloadImage(ImageView view){
			imageV = view;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			String token = tools.getToken(GalleryActivity.this);
			URL url = null;
			Bitmap bm = null;
			try {
				url = new URL(tools.getImageUrl(token, params[0], "thumb"));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HttpURLConnection get = null;
			
			try {
				get = (HttpURLConnection) url.openConnection();
				get.connect();
				InputStream is = get.getInputStream();
				if(get.getHeaderField("Content-Type").equals("text/html")){
					byte[] buffer = new byte[1024];
					int read = is.read(buffer);
					String msg = new String(buffer, 0, read);
					if(msg.equals(tools.TOKEN_ERROR)){
					}
					is.close();
				} else {
					bm = BitmapFactory.decodeStream(is);
					cache.put(params[0], bm);
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				get.disconnect();
			}
			
			return bm;
			
			/*
			try {
				String token = tools.getToken(GalleryActivity.this);
				//URL url = new URL(tools.getImageUrl(token, "20130107_002702.jpg", "full"));
				URL url = new URL(tools.getImageUrl(token, params[0], "thumb"));
				HttpURLConnection get = (HttpURLConnection) url.openConnection();
				get.setDoInput(true);
				get.connect();
				if(get.getHeaderField("Content-Type").equals("text/html")){
					InputStream input = get.getInputStream();
					byte[] buffer = new byte[1024];
					int read = input.read(buffer);
					String msg = new String(buffer, 0, read);
					Log.d("http", msg);
					if(msg.equals(tools.TOKEN_ERROR)){
						return null;
					}
				}
				InputStream is = get.getInputStream();
				Bitmap bm = BitmapFactory.decodeStream(is);
				is.close();
				get.disconnect();
				cache.put(params[0], bm);
				return bm;
			} catch (MalformedURLException e) {
				Log.d("http", "mue");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("http", "ioe");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null; */
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			imageV.setImageBitmap(result);
		}
		
	}
	
	private class GalleryAdapter extends BaseAdapter{
		
		String[] items;
		
		public GalleryAdapter(String[] links){
			items = links;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return items[arg0];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View grid;
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater)GalleryActivity.this.getSystemService
					      (Context.LAYOUT_INFLATER_SERVICE);
				grid = new View(GalleryActivity.this);
				grid = inflater.inflate(R.layout.image_item, null); 
			}else{
				grid = (View)convertView; 
			}
			
			ImageView imageView = (ImageView)grid.findViewById(R.id.Image);
			Bitmap fromCache = cache.get(items[position]);
			if(fromCache != null){
				imageView.setImageBitmap(fromCache);
			} else {
				new downloadImage(imageView).execute(items[position]);
			}
			return grid;
		}
	}
}
