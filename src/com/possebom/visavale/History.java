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
import android.text.Html;
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


		int i=0;
		for (String item : items)
		{
			if(item.contains(" - "))
			{
				i++;
				if(i%2==0)
					sb.append("<font color=white>");
				else
					sb.append("<font color=yellow>");
					
				String[] dados = item.split(" - ");
				String dia = dados[0];
				String nome = dados[1];
				String valor = dados[2];

				sb.append(dia);
				sb.append(" ");

				if(nome.length() > 20)
					sb.append(nome.substring(0, 20));
				else
					sb.append(String.format("%1$-20s", nome).replaceAll(" ", "&nbsp;"));
				sb.append(" ");
				sb.append(valor);
				sb.append("<br>\n");	
				sb.append("</font>");
			}
			else
				sbSaldo.append(item).append("\n");			
		}

		view.setText(Html.fromHtml(sb.toString()));
		viewSaldo.setText(sbSaldo.toString());
	}
}