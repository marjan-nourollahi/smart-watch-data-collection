package eecs.wsu.datacollection;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class InitialTest extends Activity {

	private static TextView textView1 = null;
	private static TextView textView2 = null;
	private static Button b1 = null;
	private static Button b2 = null;
	private static Spinner spin1 = null;
	private static RatingBar RB1 = null;

	private WakeLock mWakeLock;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_initial_test);
			
		//have to get permission in manifest to use vibrator
		Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);

		// wakelock so the cpu doesn't idle
		PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"DataCollectionServiceWakeLock");
		mWakeLock.acquire();

		final Window win = getWindow();
		// dismiss keyguard
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
		spin1 = (Spinner) findViewById(R.id.spinner1);
		RB1 = (RatingBar) findViewById(R.id.ratingBar1);
		
		AM1a();
	}

	// //////////////////Initial Test////////////////////////////

	public void AM1a() {

		textView1.setTextSize(32);
		textView1.setText("Please select 3 stars.");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);

		// ((RB1) findViewById(R.id.ratingBar1))

		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);

		textView2.setTextSize(22);
		textView2.setText("1 star ------> 5 stars");
		textView2.setVisibility(View.VISIBLE);

		RB1.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub

				b2.setEnabled(true);
			}
		});

		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RB1.setRating(0);
				b2.setEnabled(false);
				AM1d();
			}

		});

	}
	
	public void AM1d() {
		
		textView1.setTextSize(32);
		textView1.setText("Please select C.");
		textView1.setVisibility(View.VISIBLE);
		textView2.setVisibility(View.GONE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.GONE);
		
		spin1.setVisibility(View.VISIBLE);
		RB1.setVisibility(View.GONE);
		
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.spinnerArrayTest, R.layout.activity_spinner_layout); //change the last argument here to your xml above.
		spin1.setAdapter(typeAdapter);
		
			spin1.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int position, long flag){
					switch(position){
					case 0:
						b1.setVisibility(View.VISIBLE);
						b2.setVisibility(View.GONE);
						break;
					case 1: 
						b1.setVisibility(View.VISIBLE);
						b2.setVisibility(View.GONE);
						break;
					case 2: 
						b1.setVisibility(View.VISIBLE);
						b2.setVisibility(View.GONE);
						break;
					case 3: 
						b1.setVisibility(View.VISIBLE);
						b2.setVisibility(View.GONE);
						break;
					}
				}
					
				@Override
		        public void onNothingSelected(AdapterView<?> adapterView) {
		            // TODO Auto-generated method stub
		        }
			
			});
		}

	// //////////////////////////////////////////////////////////////

	public void sendMessage(View view) {
		sendMessage();
	}

	public void sendMessage() {
		// Do something in response to button

		Intent intentstop = new Intent(this, InitialTest.class);
		PendingIntent senderstop = PendingIntent.getActivity(this, 123,
				intentstop, 0);

		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.initial_test, menu);
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
