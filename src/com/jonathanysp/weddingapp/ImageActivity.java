package com.jonathanysp.weddingapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.support.v4.app.NavUtils;

public class ImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		String name = getIntent().getExtras().getString("name");
		if(name == null){
			finish();
		}
		new downloadImage((ImageView) findViewById(R.id.imageView1)).execute(name);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_image, menu);
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
			try {
				String token = tools.getToken(ImageActivity.this);
				//URL url = new URL(tools.getImageUrl(token, "20130107_002702.jpg", "full"));
				URL url = new URL(tools.getImageUrl(token, params[0], "web"));
				HttpURLConnection get = (HttpURLConnection) url.openConnection();
				get.setDoInput(true);
				get.connect();
				if(get.getHeaderField("Content-Type").equals("text/html")){
					InputStream input = get.getInputStream();
					byte[] buffer = new byte[1024];
					int read = input.read(buffer);
					String msg = new String(buffer, 0, read);
					Log.d("http", msg);
				}
				InputStream is = get.getInputStream();
				Bitmap bm = BitmapFactory.decodeStream(is);
				is.close();
				get.disconnect();
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
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			imageV.setImageBitmap(result);
		}
	}
}
