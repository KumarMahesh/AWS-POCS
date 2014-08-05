package com.example.awsanalyticspoc;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amazonaws.android.mobileanalytics.AnalyticsEvent;

public class HomeScreen extends ActionBarActivity {

	private boolean BUTTON__CLICKED_STATUS = false;
	private Button btnClickOnAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		btnClickOnAdd = (Button) findViewById(R.id.btnadd);
		btnClickOnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
               BUTTON__CLICKED_STATUS = true;
			}
		});
	}

	public void customOnClickEvent(String addName , boolean buttonClickStatus) {
           AnalyticsEvent onAddClickEvent = MainActivity.analytics.getEventClient().createEvent("AddClickEvent")
        		   .withAttribute("Add Name", addName);
           if(buttonClickStatus)
        	   onAddClickEvent.addAttribute("addStatus", "clicked");
           else
        	   onAddClickEvent.addAttribute("addStatus", "not clicked");
           MainActivity.analytics.getEventClient().recordEvent(onAddClickEvent);
           BUTTON__CLICKED_STATUS = false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		customOnClickEvent("Boost" , BUTTON__CLICKED_STATUS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
