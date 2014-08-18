package com.cagline.buxfer.helpers;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.cagline.buxfer.R;

public abstract class APIRequestTask extends AsyncTask<String, String, String> {

	private Context context;
	private Dialog pdia;
	private TextView textResult;
	private String requestURL;
	private List<NameValuePair> requestParams;

	
	public APIRequestTask(Context context, TextView textResult, String requestURL, List<NameValuePair> requestParams) {
		this.context = context;
		this.textResult = textResult;
		this.requestURL = requestURL;
		this.requestParams = requestParams;
		
	}
	
	@Override
	protected void onPreExecute() {
		//textResult.setText("Loadding...");
		pdia = new Dialog(context);
		pdia.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pdia.setContentView(R.layout.waiting_dialog);
        TextView text = (TextView) pdia.findViewById(R.id.watingMessage);
		text.setText("Loading...");
        pdia.setTitle("Custom Title");
        pdia.show(); 
	}
	
	@Override
	protected String doInBackground(String... params) {
 
		ServiceHandler sh = new ServiceHandler();
		String res = sh.makeServiceCall(requestURL, ServiceHandler.GET, requestParams, null);
		return res;
	}

	@Override
	protected void onPostExecute(String result) {
		pdia.dismiss();
		//Log.i(TAG, result);
		JSONObject json;

		try {

			json = new JSONObject(result);
			getResponse(json);
			//if (dialog.isShowing()) {
			 
	       // }

		} catch (Exception e) {
 
			Toast.makeText(context.getApplicationContext(), "Problem with API request.", Toast.LENGTH_SHORT).show();
			
		}

	}

	public abstract void getResponse(JSONObject getJSONResponse);
 

}
