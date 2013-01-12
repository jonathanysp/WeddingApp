package com.jonathanysp.weddingapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.jonathanysp.tools.HttpPostHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	EditText email_et;
	EditText password_et;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences settings = getSharedPreferences("login", 0);
		if(settings.getString("token", null) != null){
			Log.d("jonathan", "token already stored");
			gotoMenu();
		}
		
		Button b = (Button) findViewById(R.id.button1);
		email_et = (EditText) findViewById(R.id.email);
		password_et = (EditText) findViewById(R.id.password);
		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String e = email_et.getText().toString();
				String p = password_et.getText().toString();
				
				//bypass login
				e = "jonathanysp@gmail.com";
				p = "jonathan";
				
				new LoginAsync().execute(e,p);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
	}
	
	public void gotoMenu(){
		Intent i = new Intent(MainActivity.this, MenuActivity.class);
		startActivity(i);
	}
	
	private class LoginAsync extends AsyncTask<String, Integer, String>{
		
		@Override
		protected String doInBackground(String... params) {
			
			String response = "ERROR";
			try {
				HttpPostHelper post = new HttpPostHelper(new URL(tools.SERVER + "website/mobile/mobileLogin.php"));
				post.sendKeyValuePair("email", params[0]);
				post.sendKeyValuePair("password", params[1]);
				byte[] br = post.close();
				response = new String(br);
				Log.d("http", response);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return response.trim();
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result.equals(tools.LOGIN_ERROR)){
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Log In Error");
				builder.setMessage("Incorrect email/password");
				builder.setNeutralButton("Ok", new Dialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				AlertDialog ad = builder.create();
				ad.show();
			} else if(result.equals("ERROR")){
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Network Error");
				builder.setMessage("Please make sure you are connected to the internet");
				builder.setNeutralButton("Ok", new Dialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						//Intent i = new Intent(MainActivity.this, MenuActivity.class);
						//startActivity(i);
					}
				});
				AlertDialog ad = builder.create();
				ad.show();
			} else {
				//TODO store response token
				Log.d("jonathan", "login ok, storing token: " + result);
				SharedPreferences settings = getSharedPreferences("login", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("token", result);
				editor.commit();
				gotoMenu();
			}
		}
	}
}
