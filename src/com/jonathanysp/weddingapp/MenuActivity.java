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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MenuActivity extends Activity {

	Uri uri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
        GridView gridView = (GridView)findViewById(R.id.gridView1);
        
        MenuAdapter adapter = new MenuAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				if(pos == 1){
					Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					uri = tools.getOutputMediaFileUri();
					i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(i, 100);
				}
				if(pos == 2){
					Intent i = new Intent(MenuActivity.this, GalleryActivity.class);
					startActivity(i);
				}
				if(pos == 4){
					//clear saved token
					SharedPreferences settings = getSharedPreferences("login", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("token", null);
					editor.commit();
					Intent i = new Intent(MenuActivity.this, MainActivity.class);
					startActivity(i);
					finish();
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == 100){
			if(resultCode == Activity.RESULT_OK){
				//new UploadFileAsync().execute(new File(uri.getPath()));
				Intent i = new Intent(MenuActivity.this, CameraActivity.class);
				i.putExtra("path", uri.getPath());
				startActivity(i);
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		if(uri != null){
			outState.putString("uriString", uri.getPath());
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		String path = savedInstanceState.getString("uriString");
		uri = new Uri.Builder().path(path).build();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}
	
	private class UploadFileAsync extends AsyncTask<File, Integer, String>{

		@Override
		protected String doInBackground(File... params) {
			try {
				URL url = new URL("http://10.0.2.2/post.php");
				HttpPostHelper post = new HttpPostHelper(url);
				post.sendKeyValuePair("data", "yay");
				post.sendFile("file", params[0]);
				byte[] res = post.close();
				Log.d("http", new String(res));
				return "SUCCESS";
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "ERROR";
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Toast toast = Toast.makeText(MenuActivity.this, result, Toast.LENGTH_SHORT);
			toast.show();
		}
		
	}
	
}
