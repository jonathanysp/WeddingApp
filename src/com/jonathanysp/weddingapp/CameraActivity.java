package com.jonathanysp.weddingapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.jonathanysp.tools.HttpPostHelper;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class CameraActivity extends Activity {
	
	Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		ImageView iv = (ImageView) findViewById(R.id.imageView1);
		final EditText et = (EditText) findViewById(R.id.editText1);
		final File file = new File(getIntent().getExtras().getString("path"));
		Uri uri = Uri.fromFile(file);
		iv.setImageURI(uri);
		
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Button b = (Button) findViewById(R.id.UploadButton);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String caption = et.getText().toString();
				SharedPreferences settings = getSharedPreferences("login", 0);
				String token = settings.getString("token", null);
				String filepath = file.getPath();
				new UploadFileAsync().execute(token, caption, filepath);
				Intent i = new Intent(CameraActivity.this, MenuActivity.class);
				startActivity(i);
			}
		});
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_camera, menu);
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
	
	private class UploadFileAsync extends AsyncTask<String, Integer, String>{
		
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 * should take in
		 * -token
		 * -image
		 * -caption
		 * -location?
		 */

		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL(tools.SERVER + "website/mobile/mobileUpload.php");
				HttpPostHelper post = new HttpPostHelper(url);
				post.sendKeyValuePair("token", params[0]);
				post.sendKeyValuePair("caption", params[1]);
				post.sendFile("photo", new File(params[2]));
				byte[] res = post.close();
				Log.d("http", new String(res));
				return "SUCCESS";
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				Log.d("http", "mue");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d("http", "ioe");
				e.printStackTrace();
			}
			return "ERROR";
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Toast toast = Toast.makeText(CameraActivity.this, result, Toast.LENGTH_SHORT);
			toast.show();
		}
		
	}

}
