package com.cagline.buxfer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cagline.buxfer.helpers.APIRequestTask;
import com.cagline.buxfer.helpers.ConnectionDetector;

public class AddTransactionActivity extends Activity {
	
	// Connection detector class
    ConnectionDetector cd;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_transaction);

		final TextView txtVStatus = (TextView) findViewById(R.id.txtVRequestStatus);
        final RadioGroup rdogrpTransactionType = (RadioGroup) findViewById(R.id.rdogrpTransactionType);
 
     	final TextView txtFTransaction = (TextView) findViewById(R.id.txtFTransaction);
		final TextView txtFAmount = (TextView) findViewById(R.id.txtFAmount);
		
		final Spinner spinSTag = (Spinner) findViewById(R.id.spinSTag);
		final Spinner spinSAccount = (Spinner) findViewById(R.id.spinSAccount);
		final Spinner spinSAccount2 = (Spinner) findViewById(R.id.spinSAccount2);
		spinSAccount2.setVisibility(View.GONE);
		
		rdogrpTransactionType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
					Log.d("chk", "id" + checkedId);
					RadioButton rdobtnTransactionType = (RadioButton) findViewById(checkedId);
					String strTransactionType = (String) rdobtnTransactionType.getText().toString();
                if (strTransactionType.equalsIgnoreCase("Transfer")) {
                	spinSAccount2.setVisibility(View.VISIBLE);
                }else{
                	spinSAccount2.setVisibility(View.GONE);
                }

            }

        });
		
		/* get list of Tags
		 * set list of tags to spinSTag
		 */
		String requestURL = "https://www.buxfer.com/api/tags.json";
		List<NameValuePair> requestParams = new ArrayList<NameValuePair>(2);
		Intent i = getIntent();
		String token = i.getStringExtra("TOKEN");
		if (token != null) {
			requestParams.add(new BasicNameValuePair("token", token));
		}
		
		// creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
        
		if (cd.isConnectingToInternet()) {
			
	    	/*
	    	 * API Request initialize for get All Tags.
	    	 */
			APIRequestTask tagRequestTask = new APIRequestTask(this, txtVStatus, requestURL, requestParams) {
	
				@Override
				public void getResponse(JSONObject getJSONResponse) {
					JSONObject response;
					String status;
	 
					try {
						response = (JSONObject) getJSONResponse	.get("response");
						status = ((JSONObject) response).getString("status");
	 
	
						if (status.equalsIgnoreCase("OK")) {
	
							JSONArray  transactions = response.getJSONArray("tags"); 
	
							ArrayList<String> items = new ArrayList<String>();
	
							JSONObject json_data;
	
							for(int i=0; i < transactions.length() ; i++) {
							    json_data = transactions.getJSONObject(i);
							    JSONObject keyTransaction = (JSONObject) json_data.get("key-tag");
							    String description= (String) ((JSONObject) keyTransaction).getString("name");
							    items.add(description);
							}
	
							ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddTransactionActivity.this, R.layout.spinner_item, items);
							dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spinSTag.setAdapter(dataAdapter);
	
							Toast.makeText(getApplicationContext(),	"Tags has been fetched!", Toast.LENGTH_LONG).show();
	
						} else {
							// Adding Transaction fail
							txtVStatus.setText("Fail ...!");
							Toast.makeText(getApplicationContext(), "Login Fail...!", Toast.LENGTH_LONG).show();
	
						}
					} catch (Exception e) {
						// Response fail
	
						txtVStatus.setText("Problem with API response! "+ e.getMessage());
						Toast.makeText(getApplicationContext(),"Problem with API response!", Toast.LENGTH_LONG).show();
					}
				}
			};
			tagRequestTask.execute("");
			/*
	    	 * End > API Request initialize for get All Tags.
	    	 */

    	

	    	/*
	    	 * API Request initialize for get All Accounts.
	    	 */
			requestURL = "https://www.buxfer.com/api/accounts.json";
			APIRequestTask accountRequestTask = new APIRequestTask(this, txtVStatus, requestURL, requestParams) {
	
				@Override
				public void getResponse(JSONObject getJSONResponse) {
					JSONObject response;
					String status;
	 
					try {
						response = (JSONObject) getJSONResponse	.get("response");
						status = ((JSONObject) response).getString("status");
	 
	
						if (status.equalsIgnoreCase("OK")) {
	
							JSONArray  transactions = response.getJSONArray("accounts"); 
	
							ArrayList<String> items = new ArrayList<String>();
	
							JSONObject json_data;
	
							for(int i=0; i < transactions.length() ; i++) {
							    json_data = transactions.getJSONObject(i);
							    JSONObject keyTransaction = (JSONObject) json_data.get("key-account");
							    String description= (String) ((JSONObject) keyTransaction).getString("name");
							    items.add(description);
							}
	
							ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddTransactionActivity.this, R.layout.spinner_item, items);
							dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spinSAccount.setAdapter(dataAdapter);
							spinSAccount2.setAdapter(dataAdapter);
	
							Toast.makeText(getApplicationContext(),	"Accounts has been fetched!", Toast.LENGTH_LONG).show();
	
						} else {
							// Adding Transaction fail
							txtVStatus.setText("Fail ...!");
							Toast.makeText(getApplicationContext(), "Login Fail...!", Toast.LENGTH_LONG).show();
	
						}
					} catch (Exception e) {
						// Response fail
	
						txtVStatus.setText("Problem with API response! "+ e.getMessage());
						Toast.makeText(getApplicationContext(),"Problem with API response!", Toast.LENGTH_LONG).show();
					}
				}
			};
			accountRequestTask.execute("");
			/*
	    	 * End > API Request initialize for get All Accounts.
	    	 */
		} else {
			
					Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
				 
		}
		
		/*  
		 * Add Transaction button listener for start sending API request 
		 */
		final Button btnAddTransaction = (Button) findViewById(R.id.btnAddTransaction);
		btnAddTransaction.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				String requestURL = "https://www.buxfer.com/api/add_transaction.json";
				List<NameValuePair> requestParams = new ArrayList<NameValuePair>(2);
				Intent i = getIntent();
				String token = i.getStringExtra("TOKEN");

				if (token != null) {
					
					requestParams.add(new BasicNameValuePair("token", token));
					requestParams.add(new BasicNameValuePair("format", "sms"));
					
					String strTransaction = (String) txtFTransaction.getText().toString();
					String strAmount = (String) txtFAmount.getText().toString();
					String strTag = (String) spinSTag.getSelectedItem().toString();
					String strAccount = (String) spinSAccount.getSelectedItem().toString();

					int selectedId = rdogrpTransactionType.getCheckedRadioButtonId();
				    RadioButton rdobtnTransactionType = (RadioButton) findViewById(selectedId);
					String strTransactionType = (String) rdobtnTransactionType.getText().toString();
					
					if(strTransactionType.equalsIgnoreCase("Expense")){
						requestParams.add(new BasicNameValuePair("text",strTransaction+" "+strAmount+" tags:"+strTag+" acct:"+strAccount));
					}else if(strTransactionType.equalsIgnoreCase("Income")){
						requestParams.add(new BasicNameValuePair("text",strTransaction+" +"+strAmount+" tags:"+strTag+" acct:"+strAccount));
					}else if(strTransactionType.equalsIgnoreCase("Transfer")){
						String strAccount2 = (String) spinSAccount2.getSelectedItem().toString();
						requestParams.add(new BasicNameValuePair("text",strTransaction+" "+strAmount+" tags:"+strTag+" acct:"+strAccount+","+strAccount2));
					}
		 
 
				}

		    	/*
		    	 * API Request initialize for Add New Transaction.
		    	 */
				APIRequestTask requestTask = new APIRequestTask(AddTransactionActivity.this, txtVStatus, requestURL, requestParams) {

					@Override
					public void getResponse(JSONObject getJSONResponse) {

						JSONObject response;
						String status;
						// String numTransactions;

						try {
							response = (JSONObject) getJSONResponse.get("response");
							status = ((JSONObject) response).getString("status");
							// numTransactions = ((JSONObject)
							// response).getString("numTransactions");

							if (status.equalsIgnoreCase("OK")) {
								// login Successfully !
								txtVStatus.setText("Successfully added!");
								Toast.makeText(getApplicationContext(),	"Successfully added!", Toast.LENGTH_LONG).show();

							} else {
								// login fail

								txtVStatus.setText("Fail ...!");
								Toast.makeText(getApplicationContext(), "Login Fail...!", Toast.LENGTH_LONG).show();

							}
						} catch (Exception e) {
							// Response fail

							txtVStatus.setText("Problem with API response!"+ e.getMessage());
							Toast.makeText(getApplicationContext(),"Problem with API response!", Toast.LENGTH_LONG).show();
						}
					}

				};
				requestTask.execute("");
		    	/*
		    	 * End > API Request initialize for Add New Transaction.
		    	 */
			} 
		});
		/*  
		 * End > Add Transaction button listener for start sending API request 
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_transactionx, menu);
		return true;
	}

}
