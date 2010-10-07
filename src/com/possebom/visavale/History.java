/*
 ***********************************************************************
 * This code sample is offered under a modified BSD license.           *
 * Copyright (C) 2010, Motorola, Inc. All rights reserved.             *
 * For more details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf * 
 * in your installation folder.                                        *
 ***********************************************************************
 */

package com.possebom.visavale;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
		view.setText(getUrl());
	}

	private String getUrl() {
		SharedPreferences settings = getSharedPreferences("VisaValePrefs", 0);
		URI myURL = null;
		try {
			myURL = new URI("http://www.cbss.com.br/inst/convivencia/SaldoExtrato.jsp?numeroCartao=" + settings.getString("cardNumber", ""));
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(myURL);
		HttpResponse httpResponse;

		String result = "Erro pegando dados.";

		try {
			httpResponse = httpClient.execute(getMethod);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader( new InputStreamReader(instream));
				StringBuilder sb = new StringBuilder();
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						if(line.contains("lido."))
						{
							sb.append("Cartão Inválido");
							break;
						}
						if(line.contains("topTable"))
								continue;
						if(line.contains("400px") )
						{
							sb.append(line.replaceAll("\\<.*?>","").replaceAll("\\&nbsp\\;", " ").trim()).append(" - ");
						}
						if(line.contains("50px") )
						{
							if(line.contains("R"))
								sb.append(line.replaceAll("\\<.*?>","").replaceAll("\\&nbsp\\;", " ").trim()).append("\n");
							else if (line.contains("/"))
								sb.append(line.replaceAll("\\<.*?>","").replaceAll("\\&nbsp\\;", " ").trim()).append(" - ");
						}

					}
				} catch (Exception e) {
					result = "Erro carregando dados.";
				} finally {
					try {
						instream.close();
					} catch (Exception e) {
						result = "Erro carregando dados.";
					}
				}
				result = sb.toString();
			}
		} catch (Exception e) {
			result = "Erro carregando dados.";
		}
		return result;
	}
}