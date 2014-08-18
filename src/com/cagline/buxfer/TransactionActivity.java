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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cagline.buxfer.helpers.APIRequestTask;
import com.cagline.buxfer.helpers.ConnectionDetector;
import com.cagline.buxfer.helpers.ListItem;

public class TransactionActivity extends Activity {
	
    // Connection detector class
    ConnectionDetector cd;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);

    	final TextView txtVStatus = (TextView) findViewById(R.id.txtVRequestStatus); 
    	List<NameValuePair> requestParams = new ArrayList<NameValuePair>(2);
    	
    	String requestURL = "https://www.buxfer.com/api/transactions.json";
    	
		Intent i= getIntent();
		String token = i.getStringExtra("TOKEN");
		 
		if(token != null)
        {
            requestParams.add(new BasicNameValuePair("token", token ));
        }
		// creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
        
		if (cd.isConnectingToInternet()) {
	    	/*
	    	 * API Request initialize for get Recent Transactions.
	    	 */
		
	    	APIRequestTask requestTask = new APIRequestTask(this, txtVStatus, requestURL, requestParams){
	
				@Override
				public void getResponse(JSONObject getJSONResponse) {
					
					JSONObject response;
					String status;
					String numTransactions; 
					
					ListView listVTransactions = (ListView) findViewById(R.id.listVTransactions);
	 
					try {
						response =   (JSONObject) getJSONResponse.get("response");
						status =  ((JSONObject) response).getString("status");
						numTransactions = ((JSONObject) response).getString("numTransactions");
	
						if(status.equalsIgnoreCase("OK")){
							//login Successfully !
	
							//txtVStatus.setText(numTransactions);
	
							JSONArray  transactions = response.getJSONArray("transactions"); 
	
							ArrayList<String> transactionTitle = new ArrayList<String>();
							ArrayList<String> transactionAmount = new ArrayList<String>();
							ArrayList<String> transactionTags = new ArrayList<String>();
							ArrayList<String> transactionDate = new ArrayList<String>();
							ArrayList<String> transactionType = new ArrayList<String>();
	
							JSONObject json_data;
	
							for(int i=0; i < transactions.length() ; i++) {
							    json_data = transactions.getJSONObject(i);
						 
							    JSONObject keyTransaction = (JSONObject) json_data.get("key-transaction");
							    
							    String description= (String) ((JSONObject) keyTransaction).getString("description");
							    String amount= (String) ((JSONObject) keyTransaction).getString("amount");
							    String tags= (String) ((JSONObject) keyTransaction).getString("tags");
							    String date= (String) ((JSONObject) keyTransaction).getString("date");
							    String type= (String) ((JSONObject) keyTransaction).getString("type");
	
							    transactionTitle.add(description);
							    transactionAmount.add(amount);
							    transactionTags.add(tags);
							    transactionDate.add(date);
							    transactionType.add(type);
							    
	
							}
	 
							ListItem mArrayAdapter = new ListItem(TransactionActivity.this, transactionTitle, transactionAmount, transactionTags, transactionDate, transactionType);
							
	//						ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(TransactionActivity.this, R.layout.list_item, items);
							
							
							listVTransactions.setAdapter(mArrayAdapter);
							txtVStatus.setText("Results ("+numTransactions+")");
							Toast.makeText(getApplicationContext(), "Transactions has been fetched!", Toast.LENGTH_LONG).show();
	 
						}else{
							//login fail
	
							txtVStatus.setText("Fail ...!");
							Toast.makeText(getApplicationContext(), "Login Fail...!", Toast.LENGTH_LONG).show();
	
						}
					} catch (Exception e) {
						//Response fail 
	
						txtVStatus.setText("Problem with API response!"+e.getMessage());
						Toast.makeText(getApplicationContext(), "Problem with API response!", Toast.LENGTH_LONG).show();
					}
				}
	
	    	};
	    	requestTask.execute("");
	    	/*
	    	 * End > API Request initialize for get Recent Transactions.
	    	 */
		} else {
			
			Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
		 
		}
    	/*
    	 * Add Transaction button listener for jump to Add Transaction activate 
    	 **/
    	final Button btnAddTransaction = (Button) findViewById(R.id.btnAddTransaction);
    	btnAddTransaction.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i= getIntent();
				String token = i.getStringExtra("TOKEN");
				Intent addTransactionIntent = new Intent(TransactionActivity.this, AddTransactionActivity.class);
				addTransactionIntent.putExtra("TOKEN", token);
				TransactionActivity.this.startActivity(addTransactionIntent);
				
			}
		});
    	
    	/*
    	 * End > Add Transaction button listener for jump to Add Transaction activate 
    	 **/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}

}
