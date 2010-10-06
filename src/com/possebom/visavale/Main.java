package com.possebom.visavale;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main extends Activity {
	public static final String PREFS_NAME = "VisaValePrefs";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);    
		final Button button = (Button) findViewById(R.id.button);
		final EditText cardNumber = (EditText)findViewById(R.id.cardNumber); 
		final TextView view = (TextView)findViewById(R.id.view);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		cardNumber.setText(settings.getString("cardNumber", ""));
		view.setText(settings.getString("saldo", ""));

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				updateView();
			}
		});        
	}

	private String getUrl() {
		final EditText cardNumber = (EditText)findViewById(R.id.cardNumber); 
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet post = new HttpGet("http://www.cbss.com.br/inst/convivencia/SaldoExtrato.jsp?numeroCartao=" + cardNumber.getText());
		ByteArrayOutputStream buf = null;
		try {
			HttpResponse response = httpclient.execute(post);            		
			BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
			buf = new ByteArrayOutputStream();
			int result = bis.read();
			while(result != -1) {
				byte b = (byte)result;
				buf.write(b);
				result = bis.read();
			}   
		} catch (Exception e) {
			return e.toString();
		}
		return buf.toString();
	}

	private void updateView() {          
		final TextView view = (TextView)findViewById(R.id.view);
		final EditText cardNumber = (EditText)findViewById(R.id.cardNumber); 
		String data = getUrl();
		StringBuffer strBuf = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new StringReader(data));
			String temp       = br.readLine();

			while(temp != null) {
				if(temp.contains("lido."))
				{
					strBuf.append("Cartão Inválido");
					break;
				}
				if(temp.contains("Saldo d") )
					strBuf.append(temp.replaceAll("\\<.*?>","").replaceAll("dispo.*vel:", " : ").trim());
				temp = br.readLine();
			}
			//Log.v("VISA",strBuf.toString());
			if(strBuf.toString().contains("Saldo"))
			{
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("saldo", strBuf.toString());
				editor.putString("cardNumber", cardNumber.getText().toString());
				editor.commit();
			}
			view.setText(strBuf.toString());
		} catch (Exception e){
			view.setText(e.toString());
		}

	}
}