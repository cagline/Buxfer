package com.cagline.buxfer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cagline.buxfer.helpers.APIRequestTask;
import com.cagline.buxfer.helpers.ConnectionDetector;

public class MainActivity extends Activity {
	
    // Connection detector class
    ConnectionDetector cd;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button btnLogin = (Button) findViewById(R.id.btnLogin);

		/*  
		 * Login button listener 
		 */
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
            public void onClick(View v) {
            	
            	final TextView textLoginStatus = (TextView) findViewById(R.id.txtVLoginStatus);
            	EditText textFUsername = (EditText) findViewById(R.id.txtFUsername);
            	EditText textFPassword = (EditText) findViewById(R.id.txtFPassword);
            	
            	String requestURL = "https://www.buxfer.com/api/login.json";
            	
            	List<NameValuePair> requestParams = new ArrayList<NameValuePair>(2);
            	requestParams.add(new BasicNameValuePair("userid", textFUsername.getText().toString()));
            	requestParams.add(new BasicNameValuePair("password", textFPassword.getText().toString()));
            	
            	// creating connection detector class instance
                cd = new ConnectionDetector(getApplicationContext());
                
				if (cd.isConnectingToInternet()) {
 
					/*
					 * API Request initialize for Authenticate Buxfer User.
					 */
					APIRequestTask requestTask = new APIRequestTask(MainActivity.this, textLoginStatus, requestURL, requestParams){
						
						@SuppressLint("ShowToast")
						@Override
						public void getResponse(JSONObject getJSONResponse) {
							
							JSONObject response;
							String status;
							String token;
							try {
								response =   (JSONObject) getJSONResponse.get("response");
								status = ((JSONObject) response).getString("status");
								token = ((JSONObject) response).getString("token");
								
								if(status.equalsIgnoreCase("OK")){
									//login Successfully !
									Intent transactionIntent = new Intent(MainActivity.this, TransactionActivity.class);
									transactionIntent.putExtra("TOKEN", token);
									MainActivity.this.startActivity(transactionIntent);
									//MainActivity.this.finish();
								}else{
									//login fail
									textLoginStatus.setText("Fail ...!");
									Toast.makeText(getApplicationContext(), "Login Fail...!", Toast.LENGTH_LONG).show();
								}
							} catch (Exception e) {
								//Response fail 
								textLoginStatus.setText("Problem with API respnse!");
							}
						}
						
					};
					requestTask.execute("");
					/*
					 * End > API Request initialize for Authenticate Buxfer User.
					 */
				} else {
					
					Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
				 
				}
            }
        });
		/*  
		 * End > Login button listener 
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

}
