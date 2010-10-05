package com.possebom.visavale;

import android.app.Activity;
import android.os.Bundle;
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
            	view.setText(mTextSearch.getText());
            }
        });        
    }
}