package com.example.awsanalyticspoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amazonaws.android.auth.CognitoCredentialsProvider;
import com.amazonaws.android.mobileanalytics.AmazonMobileAnalytics;
import com.amazonaws.android.mobileanalytics.AnalyticsEvent;
import com.amazonaws.android.mobileanalytics.AnalyticsOptions;
import com.amazonaws.android.mobileanalytics.InitializationException;

public class MainActivity extends ActionBarActivity {
	private Button btnGuestLogin ;
	public static AmazonMobileAnalytics analytics;
	private static final int STATE_LOSE = 0;
	private static final int STATE_WIN = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		CognitoCredentialsProvider cognitoProvider = new CognitoCredentialsProvider(
			    getApplicationContext(), // get the context for the current activity
			    "592812403306",
			    "us-east-1:f13a34b2-464f-478d-bb28-de450a0dd781",
			    "arn:aws:iam::592812403306:role/Cognito_AWSANALYTICSPOCUnauth_DefaultRole",
			    "arn:aws:iam::592812403306:role/Cognito_AWSANALYTICSPOCAuth_DefaultRole"
			); 
		
		try {
	        AnalyticsOptions options = new AnalyticsOptions(); 
	        options.withAllowsWANDelivery(true);
	        analytics = new AmazonMobileAnalytics(
	                cognitoProvider,
	                getApplicationContext(),
	                "com.example.awsanalyticspoc",
	                options
	        );
	    } catch(InitializationException ex) {
	            Log.e(this.getClass().getName(), "Failed to initialize Amazon Mobile Analytics", ex);
	    }
		
		btnGuestLogin = (Button) findViewById(R.id.guestLogin);
		btnGuestLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startNewActivity();
			}
		});
		
		this.onLevelComplete("Lower Dungeon", "Very Difficult", 2734, STATE_WIN);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(analytics != null) {
	        analytics.getSessionClient().pauseSession();
	            //Attempt to send any events that have been recorded to the Mobile Analytics service.
	        analytics.getEventClient().submitEvents();
	    }
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(analytics != null)  {
	        analytics.getSessionClient().resumeSession();
	    }

	}

	private void startNewActivity(){
		Intent intent = new Intent(this, HomeScreen.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	
	/**
	 * This method gets called when the player completes a level.
	 * @param levelName the name of the level
	 * @param difficulty the difficulty setting
	 * @param timeToComplete the time to complete the level in seconds
	 * @param playerState the winning/losing state of the player
	 */
	public void onLevelComplete(String levelName, String difficulty, double timeToComplete, int playerState) {
	        
	    //Create a Level Complete event with some attributes and metrics(measurements)
	    //Attributes and metrics can be added using with statements
	    AnalyticsEvent levelCompleteEvent = analytics.getEventClient().createEvent("LevelComplete")
	            .withAttribute("LevelName", levelName)
	            .withAttribute("Difficulty", difficulty)
	            .withMetric("TimeToComplete", timeToComplete);
	 
	    //attributes and metrics can also be added using add statements
	    if (playerState == STATE_LOSE)
	        levelCompleteEvent.addAttribute("EndState", "Lose");
	    else if (playerState == STATE_WIN)
	        levelCompleteEvent.addAttribute("EndState", "Win");
	 
	    //Record the Level Complete event
	    analytics.getEventClient().recordEvent(levelCompleteEvent);
	} 
	                            
}
