package com.example.ukradlimirower;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public abstract class BaseActivity extends Activity {
	
	public void showLogin() {
		Intent intent = new Intent(null, LoginActivity.class);
		startActivity(intent);
	}

	public void showSignin() {
		Intent intent = new Intent(null, SignUpActivity.class);
		startActivity(intent);
	}

	public void showHome() {
		Intent intent = new Intent(null, HomeActivity.class);
		startActivity(intent);
	}
	
	public void showSignUp() {
		Intent intent = new Intent(null, SignUpActivity.class);
		startActivity(intent);
	}
	
	public void showNewBicycle() {
		Intent intent = new Intent(null, NewBicycleActivity.class);
		startActivity(intent);
	}
	
	protected void StoreApiKey(String apiKey) {
		String FILENAME = "APIKEY";
		
		File f = new File(FILENAME);
		
		FileOutputStream fos;
		
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(apiKey.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
