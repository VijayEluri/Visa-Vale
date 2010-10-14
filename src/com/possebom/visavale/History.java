/*
 ***********************************************************************
 * This code sample is offered under a modified BSD license.           *
 * Copyright (C) 2010, Motorola, Inc. All rights reserved.             *
 * For more details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf * 
 * in your installation folder.                                        *
 ***********************************************************************
 */

package com.possebom.visavale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.possebom.visavale.R;

public class History extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialoglayout);
		final TextView view = (TextView)findViewById(R.id.view);
		final TextView viewSaldo = (TextView)findViewById(R.id.viewSaldo);
		SharedPreferences settings = getSharedPreferences("VisaValePrefs", 0);
		
		String data = settings.getString("saldo", "");
		String[] items = data.split("\n");
		StringBuffer sb = new StringBuffer();
		StringBuffer sbSaldo = new StringBuffer();
		
		int length = 0;
		for (String item : items)
		{
			if(item.length() > length)
				length = item.length();
		}
		
		for (String item : items)
		{
			if(item.contains(" - "))
			{
				int temp = item.length();
				while(temp <= length)
				{
					item = item.replaceAll("- R\\$", " - R\\$");
					temp = item.length();
				}
				sb.append(item.replaceAll(" - ", " ")).append("\n");
			}
			else
				sbSaldo.append(item).append("\n");
		}
		view.setText(sb.toString());
		viewSaldo.setText(sbSaldo.toString());
	}
}