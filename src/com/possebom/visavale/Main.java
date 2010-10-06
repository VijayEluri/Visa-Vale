package com.possebom.visavale;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);        
		final EditText mTextSearch = (EditText)findViewById(R.id.cardNumber);                    
		final TextView view = (TextView)findViewById(R.id.view);
		final Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpGet post = new HttpGet("http://www.cbss.com.br/inst/convivencia/SaldoExtrato.jsp?numeroCartao=" + mTextSearch.getText());

				try {
					HttpResponse response = httpclient.execute(post);            		
					BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
					ByteArrayOutputStream buf = new ByteArrayOutputStream();
					int result = bis.read();
					while(result != -1) {
						byte b = (byte)result;
						buf.write(b);
						result = bis.read();
					}        

					StringBuffer strBuf = new StringBuffer();

					try {
						BufferedReader br = new BufferedReader(new StringReader(buf.toString()));
						String temp       = br.readLine();

						while(temp != null) {
							if(temp.contains("Saldo d") )
							{
								strBuf.append(temp.replaceAll("\\<.*?>","").replaceAll("dispo.*vel:", " : ").trim());
							}
							temp = br.readLine();
						}
						Log.v("VISA",strBuf.toString());
					} catch (Exception e){
						//
					}
					//	view.setText(Html.fromHtml(buf.toString().replaceAll(".*clear", "")));
					view.setText(strBuf.toString());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});        
	}
}