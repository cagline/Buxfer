package com.cagline.buxfer.helpers;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cagline.buxfer.R;

public class ListItem extends ArrayAdapter<String> {
	private final Activity context;
	private final ArrayList<String> transactionTitle;
	private final ArrayList<String> transactionAmount;
	private final ArrayList<String> transactionTags;
	private final ArrayList<String> transactionDate;
	private final ArrayList<String> transactionType;

	public ListItem(Activity context, ArrayList<String> transactionTitle, ArrayList<String> transactionAmount, ArrayList<String> transactionTags, ArrayList<String> transactionDate, ArrayList<String> transactionType) {
		super(context, R.layout.list_item, transactionTitle);
		this.context = context;
		this.transactionTitle = transactionTitle;
		this.transactionAmount = transactionAmount;
		this.transactionTags = transactionTags;
		this.transactionDate = transactionDate;
		this.transactionType = transactionType;
	} 

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_item, null, true);
		TextView txtVTransactionTitle = (TextView) rowView.findViewById(R.id.txtVTransactionTitle);
		TextView txtVTransactionAmount = (TextView) rowView.findViewById(R.id.txtVTransactionAmount);
		TextView txtVTransactionTags = (TextView) rowView.findViewById(R.id.txtVTransactionTags);
		TextView txtVTransactionDate = (TextView) rowView.findViewById(R.id.txtVTransactionDate);
		TextView txtVTransactionType = (TextView) rowView.findViewById(R.id.txtVTransactionType);
		TextView imgVTransactionType = (TextView) rowView.findViewById(R.id.imgVTransactionType);
		
		//ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtVTransactionTitle.setText(transactionTitle.get(position).toString());
		txtVTransactionAmount.setText(transactionAmount.get(position).toString());
		txtVTransactionTags.setText(transactionTags.get(position).toString());
		txtVTransactionDate.setText(transactionDate.get(position).toString());
		
		String strTransactionType = (String)transactionType.get(position).toString();
		
		txtVTransactionType.setText(strTransactionType);
		
		if(strTransactionType.equalsIgnoreCase("expense")){
			
			imgVTransactionType.setBackgroundResource(R.drawable.expense);

	    }else if(strTransactionType.equalsIgnoreCase("income")){
	    	
	    	imgVTransactionType.setBackgroundResource(R.drawable.income);

	    }else if(strTransactionType.equalsIgnoreCase("transfer")){
	    	
	    	imgVTransactionType.setBackgroundResource(R.drawable.transfer);
	    	
	    }

		return rowView;
	}
}
