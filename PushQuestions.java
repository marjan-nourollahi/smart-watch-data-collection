package eecs.wsu.datacollection;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

public class PushQuestions extends Activity {
	private static TextView textView1 = null;
	private static TextView textView2 = null;
	private static Button b1 = null;
	private static Button b2 = null;
	private static Spinner spin1 = null;
	private static RatingBar RB1 = null;
	
	private int questionAM= -1;
	private int questionPM= -1;
	private int FollowUpQuestion= -1;
	private int NextQuestion= -1;
	private float NextQuestion2= -1;
	
	private float AM1a_reg= -1;
	private float AM1b_reg= -1;
	private float AM1c_reg= -1;
	private float AM1d_reg= -1;
	private float PM1a_reg= -1;
	private float PM1b_reg= -1;
	private float PM1c_reg= -1;
	private float PM1d_reg= -1;	
	
	private float AM2a_reg= -1;
	private float AM2b_reg= -1;
	private float AM2c_reg= -1;
	private float AM2d_reg= -1;
	private float PM2a_reg= -1;
	private float PM2b_reg= -1;
	private float PM2c_reg= -1;
	private float PM2d_reg= -1;
	
	private float AM3a_reg= -1;
	private float AM3b_reg= -1;
	private float AM3c_reg= -1;
	private float AM3d_reg= -1;
	private float PM3a_reg= -1;
	private float PM3b_reg= -1;
	private float PM3c_reg= -1;
	private float PM3d_reg= -1;	
	
	private float AM4a_reg= -1;
	private float AM4b_reg= -1;
	private float AM4c_reg= -1;
	private float AM4d_reg= -1;
	private float PM4a_reg= -1;
	private float PM4b_reg= -1;
	private float PM4c_reg= -1;
	private float PM4d_reg= -1;	
	
	private float AM5a_reg= -1;
	private float AM5b_reg= -1;
	private float AM5c_reg= -1;
	private float AM5d_reg= -1;
	private float PM5a_reg= -1;
	private float PM5b_reg= -1;
	private float PM5c_reg= -1;
	private float PM5d_reg= -1;	
	
	private float AM6a_reg= -1;
	private float AM6b_reg= -1;
	private float AM6c_reg= -1;
	private float AM6d_reg= -1;
	private float PM6a_reg= -1;
	private float PM6b_reg= -1;
	private float PM6c_reg= -1;
	private float PM6d_reg= -1;	
	
	private float AM7a_reg= -1;
	private float AM7b_reg= -1;
	private float AM7c_reg= -1;
	private float AM7d_reg= -1;
	private float PM7a_reg= -1;
	private float PM7b_reg= -1;
	private float PM7c_reg= -1;
	private float PM7d_reg= -1;

	Boolean end_of_the_week = false;
	
	int day = -1;
	
	private static final File Directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DataCollectionService");
    //private static final SimpleDateFormat FilenameFormat = new SimpleDateFormat("'questions2-'yyyy-MM-dd-HH-mm'.csv'", Locale.US);
    private static final SimpleDateFormat TimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	private BufferedOutputStream mFileOutputStream;
	private WakeLock mWakeLock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_push_questions);
		
		//wakelock so the cpu doesn't idle
		PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DataCollectionServiceWakeLock");
		mWakeLock.acquire();
		
		final Window win = getWindow();
		//dismiss keyguard
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		              | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD 
		              | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		              | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); 
//		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//		              | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		Directory.mkdirs();
		
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
		spin1 = (Spinner) findViewById(R.id.spinner1);
		RB1 = (RatingBar) findViewById(R.id.ratingBar1);
		
			
		//Repeating Alarm
        
		long currenttime = System.currentTimeMillis();
		Date date = new Date(currenttime);
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		int hours = time.get(Calendar.HOUR_OF_DAY);
		//System.out.println(hours);
//		if(hours == 9 || hours == 15)
//		{
			long alarmtime = System.currentTimeMillis()+1*60*1000;
			Intent intentAlarm = new Intent(this, PushQuestions.class);
			intentAlarm.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// create the object
			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			//set the alarm for particular time
			alarmManager.set(AlarmManager.RTC_WAKEUP,alarmtime, PendingIntent.getActivity(this,123,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

			//have to get permission in manifest to use vibrator
			Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(500);
//		}
		
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK); 
		
		b2.setEnabled(false);
		//PM5a();
		
		if(hours >= 0 && hours<12)
		{
			switch(day){
			case 1:
				AM1a();
				break;
			case 2:
				AM2a();
				break;
			case 3:
				AM3a();
				break;
			case 4:
				AM4a();
				break;
			case 5:
				AM5a();
				break;
			case 6:
				AM6a();
				break;
			case 7:
				AM7a();
				break;
			}
		}
		else
		{
			switch(day){
			case 1:
				PM1a();
				break;
			case 2:
				b2.setEnabled(true);
				PM2a();
				break;
			case 3:
				PM3a();
				break;
			case 4:
				PM4a();
				break;
			case 5:
				PM5a();
				break;
			case 6:
				b2.setEnabled(true);
				PM6a();
				break;
			case 7:
				PM7a();
				break;
			}
		}
		
	}
	
////////////////////Initial Test////////////////////////////

		
////////////////////Day1 AM////////////////////////////
	
	public void AM1a() {
		
		textView1.setTextSize(32);
		textView1.setText("My appertite in the last 24 hour is:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("very poor ---> very good");
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
				AM1a_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM1b();
			}
			
		});
		
	}
	
	public void AM1b() {
		
		textView1.setTextSize(32);
		textView1.setText("what percent of your last meal did you eat?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		
		
		textView2.setTextSize(24);
		textView2.setText("0%---50%---100%");
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
				AM1b_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM1c();
			}
			
		});
		
	}
	
	public void AM1c() {
		
		textView1.setTextSize(32);
		textView1.setText("How many pounds you lost in past 3 month?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		
		textView2.setTextSize(24);
		textView2.setText("0--5--10--15--20+");
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
				AM1c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM1d();
			}
			
		});
		
	}
	
public void AM1d() {
		
	textView1.setTextSize(32);
	textView1.setText("I eat less because:");
	textView1.setVisibility(View.VISIBLE);
	textView2.setVisibility(View.GONE);
	b1.setVisibility(View.GONE);
	b2.setVisibility(View.GONE);
	
	//	((RB1) findViewById(R.id.ratingBar1))
	
	spin1.setVisibility(View.VISIBLE);
	RB1.setVisibility(View.GONE);
	
	ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.spinnerArrayAM1d, R.layout.activity_spinner_layout2); //change the last argument here to your xml above.
	spin1.setAdapter(typeAdapter);
	
		spin1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long flag){
				switch(position){
				case 0:
					AM1d_reg = 1;
					b1.setVisibility(View.VISIBLE);
					b2.setVisibility(View.GONE);
					break;
				case 1: 
					AM1d_reg = 2;
					b1.setVisibility(View.VISIBLE);
					b2.setVisibility(View.GONE);
					break;
				case 2: 
					AM1d_reg = 3;
					b1.setVisibility(View.VISIBLE);
					b2.setVisibility(View.GONE);
					break;
				case 3: 
					AM1d_reg = 4;
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
	
	////////////////////Day1 PM////////////////////////////
	
	public void PM1a() {
		
		
		textView1.setTextSize(32);
		textView1.setText("What is your pain level now?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		RB1.setStepSize(0.5f);
		
		textView2.setTextSize(22);
		textView2.setText("none ---> very severe");
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
				PM1a_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM1b();
			}
			
		});
		
	}
	
	public void PM1b() {
		
		
		textView1.setTextSize(30);
		textView1.setText("Pain interfere with daily activities?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setStepSize(1);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> Very Much");
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
				PM1b_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM1c();
			}
			
		});
		
	}
	
	public void PM1c() {
		
		
		textView1.setTextSize(30);
		textView1.setText("How much energy do you have?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> very much");
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
				PM1c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM1d();
			}
			
		});
		
	}
	
	public void PM1d() {
		
		textView1.setTextSize(32);
		textView1.setText("My mind is as sharp as usual:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> very much");
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
				PM1d_reg = RB1.getRating();
				b1.setVisibility(View.VISIBLE);
				b2.setVisibility(View.GONE);
			}
			
		});
		
	}
	
	////////////////////Day2 AM////////////////////////////
	
	public void AM2a() {
	
		textView1.setTextSize(32);
		textView1.setText("My sleep quality was:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("very poor ---> very good");
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
				AM2a_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM2b();
			}
			
		});
	
	}
	
	public void AM2b() {
		
		textView1.setTextSize(32);
		textView1.setText("My sleep was refreshing:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> Very Much");
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
				AM2b_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM2c();
			}
			
		});
	
	}
	
	public void AM2c() {
	
	
		textView1.setTextSize(32);
		textView1.setText("What is your pain level?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		RB1.setStepSize(0.5f);
		
		textView2.setTextSize(22);
		textView2.setText("No pain ---> very severe");
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
				AM2c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM2d();
			}
		});
	
	}
	
	public void AM2d() {
	
	textView1.setTextSize(30);
	textView1.setText("Pain interfere with participation in social activities?");
	textView1.setVisibility(View.VISIBLE);
	b1.setVisibility(View.GONE);
	b2.setVisibility(View.VISIBLE);
	
	//	((RB1) findViewById(R.id.ratingBar1))
	
	spin1.setVisibility(View.GONE);
	RB1.setVisibility(View.VISIBLE);
	RB1.setRating(0);
	RB1.setStepSize(1);
	
	textView2.setTextSize(22);
	textView2.setText("Not at all ---> very much");
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
			AM2d_reg = RB1.getRating();
			b1.setVisibility(View.VISIBLE);
			b2.setVisibility(View.GONE);
		}
	
	});
	
	}
	
	////////////////////Day2 PM////////////////////////////
	
	public void PM2a() {
	
	
		textView1.setTextSize(32);
		textView1.setText("Please remember these 3 words:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.GONE);
		RB1.setRating(0);
		
		textView2.setTextSize(32);
		textView2.setText("Apple, Table, Penny");
		textView2.setVisibility(View.VISIBLE);
		
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RB1.setRating(0);
				b2.setEnabled(false);
				PM2b();	
			}
		
		});
	
	}
	
	public void PM2b() {
	
	
		textView1.setTextSize(32);
		textView1.setText("Do you have someone to take you to the doctor if needed?");
		textView1.setVisibility(View.VISIBLE);
		textView2.setVisibility(View.GONE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.GONE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.VISIBLE);
		RB1.setVisibility(View.GONE);
		RB1.setRating(0);
		
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.spinnerArray2, R.layout.activity_spinner_layout); //change the last argument here to your xml above.
		spin1.setAdapter(typeAdapter);
		
			spin1.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int position, long flag){
					
					switch(position){
					case 0:
						break;
					case 1: 
						PM2b_reg = 1;
						RB1.setRating(0);
						b2.setEnabled(false);
						PM2c();	
						break;
					case 2: 
						PM2b_reg = 2;
						RB1.setRating(0);
						b2.setEnabled(false);
						PM2c();	
						break;
					}
				}
					
				@Override
		        public void onNothingSelected(AdapterView<?> adapterView) {
		            // TODO Auto-generated method stub
		        }
			
		});
	}
	
	public void PM2c() {
	
	
		textView1.setTextSize(32);
		textView1.setText("Do you feel helpless?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(24);
		textView2.setText("Never ---> Always");
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
			PM2c_reg = RB1.getRating();
			RB1.setRating(0);
			b2.setEnabled(false);
			PM2d();
			}
		
		});
	
	}

	public void PM2d() {
	
		textView1.setTextSize(32);
		textView1.setText("What were the 3 words?");
		textView1.setVisibility(View.VISIBLE);
		textView2.setVisibility(View.GONE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.GONE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.VISIBLE);
		RB1.setVisibility(View.GONE);
		
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.spinnerArrayPM2d, R.layout.activity_spinner_layout); //change the last argument here to your xml above.
		spin1.setAdapter(typeAdapter);
		
			spin1.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int position, long flag){
					switch(position){
					case 0:
						PM2d_reg = 1;
						b1.setVisibility(View.VISIBLE);
						b2.setVisibility(View.GONE);
						break;
					case 1: 
						PM2d_reg = 2;
						b1.setVisibility(View.VISIBLE);
						b2.setVisibility(View.GONE);
						break;
					case 2: 
						PM2d_reg = 3;
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
	
	///////////////////////// Day3 AM  ///////////////////////

	public void AM3a() {
		
		
		textView1.setTextSize(32);
		textView1.setText("My appetite in last 24 hours is:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(23);
		textView2.setText("very poor ---> very good");
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
				AM3a_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM3b();
			}
			
		});
		
	}
	
	public void AM3b() {
		
		
		textView1.setTextSize(30);
		textView1.setText("What percent of your last breakfast did you eat?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		
		textView2.setTextSize(24);
		textView2.setText("0%---50%---100%");
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
				AM3b_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM3c();
			}
			
		});
		
	}
	
	public void AM3c() {
		
		textView1.setTextSize(32);
		textView1.setText("How many pounds you lost in past 2 month?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(24);
		textView2.setText("0--5--10--15--20+");
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
				AM3c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM3d();
			}
			
		});
		
	}
	
public void AM3d() {
		
		textView1.setTextSize(32);
		textView1.setText("What percent of your last lunch did you eat?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(24);
		textView2.setText("0%---50%---100%");
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
				AM3d_reg = RB1.getRating();
				b1.setVisibility(View.VISIBLE);
				b2.setVisibility(View.GONE);
			}
			
		});
		
	}
	
	////////////////////Day3 PM////////////////////////////
	
	public void PM3a() {
		
		
		textView1.setTextSize(32);
		textView1.setText("What is your pain level now?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		RB1.setStepSize(0.5f);
		
		textView2.setTextSize(22);
		textView2.setText("none ---> very severe");
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
				PM3a_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM3b();
			}
			
		});
		
	}
	
	public void PM3b() {
		
		
		textView1.setTextSize(30);
		textView1.setText("Pain interfere with work at home?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		RB1.setStepSize(1);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> Very Much");
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
				PM3b_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM3c();
			}
			
		});
		
	}
	
	public void PM3c() {
		
		
		textView1.setTextSize(30);
		textView1.setText("How do you rate your overal health");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(24);
		textView2.setText("very poor ---> Excellent");
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
				PM3c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM3d();
			}
			
		});
		
	}
	
	public void PM3d() {
		
		textView1.setTextSize(32);
		textView1.setText("My memory is as good as usual:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> Very much");
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
				PM3d_reg = RB1.getRating();
				b1.setVisibility(View.VISIBLE);
				b2.setVisibility(View.GONE);
			}
			
		});
		
	}

///////////////////////// Day4 AM ////////////////////////////////
	
	public void AM4a() {
		
		
		textView1.setTextSize(32);
		textView1.setText("What is your pain level?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		RB1.setStepSize(0.5f);
		
		textView2.setTextSize(22);
		textView2.setText("No pain ---> very severe");
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
				AM4a_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM4b();
			}
			
		});
		
	}
	
	public void AM4b() {
		
		
		textView1.setTextSize(30);
		textView1.setText("Pain interfere with your household chores?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		RB1.setStepSize(1);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> Very Much");
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
				AM4b_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM4c();
			}
			
		});
		
	}
	
	public void AM4c() {
		
		textView1.setTextSize(32);
		textView1.setText("I had a problem sleeping:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> very much");
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
				AM4c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM4d();
			}
			
		});
		
	}
	
	public void AM4d() {
		
		textView1.setTextSize(32);
		textView1.setText("I had difficulty falling asleep:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> Very much");
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
				AM4d_reg = RB1.getRating();
				b1.setVisibility(View.VISIBLE);
				b2.setVisibility(View.GONE);
			}
			
		});
		
	}
	
	////////////////////Day4 PM////////////////////////////
	
	public void PM4a() {
		
		textView1.setTextSize(32);
		textView1.setText("I feel fearful:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(24);
		textView2.setText("never ---> always");
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
				PM4a_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM4b();
			}
			
		});
		
	}
	
	public void PM4b() {
		
		
		textView1.setTextSize(32);
		textView1.setText("Do you have help if you are confined to bed");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.GONE);
		textView2.setVisibility(View.GONE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.VISIBLE);
		RB1.setVisibility(View.GONE);
		RB1.setRating(0);
		
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.spinnerArray2, R.layout.activity_spinner_layout); //change the last argument here to your xml above.
		spin1.setAdapter(typeAdapter);
		
			spin1.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int position, long flag){
					
					switch(position){
					case 0:
						break;
					case 1: 
						PM4b_reg = 1;
						b2.setEnabled(false);
						PM4c();	
						break;
					case 2: 
						PM4b_reg = 2;
						b2.setEnabled(false);
						PM4c();	
						break;
					}
				}
					
				@Override
		        public void onNothingSelected(AdapterView<?> adapterView) {
		            // TODO Auto-generated method stub
		        }
			
		});
	}
	
	public void PM4c() {
		
		textView1.setTextSize(32);
		textView1.setText("Is your thinking as fast as usual?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> very much");
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
				PM4c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM4d();
			}
			
		});
		
	}
	
	public void PM4d() {
		
		textView1.setTextSize(32);
		textView1.setText("What is your pain level now?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		RB1.setStepSize(0.5f);
		
		textView2.setTextSize(22);
		textView2.setText("None ---> Very Severe");
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
				PM4d_reg = RB1.getRating();
				b1.setVisibility(View.VISIBLE);
				b2.setVisibility(View.GONE);
			}
			
		});
		
	}

//////////////////////////////// Day5 AM /////////////////////////
	
	public void AM5a() {
			
		textView1.setTextSize(32);
		textView1.setText("My appetite in last 24 hours is:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		RB1.setStepSize(1);
		
		textView2.setTextSize(23);
		textView2.setText("very poor ---> very good");
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
				AM5a_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM5b();
			}
			
		});
		
	}
	
	public void AM5b() {
		
		
		textView1.setTextSize(31);
		textView1.setText("What percent of your last dinner did you eat?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(24);
		textView2.setText("0%---50%---100%");
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
				AM5b_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM5c();
			}
			
		});
		
	}
	
	public void AM5c() {
		
		
		textView1.setTextSize(31);
		textView1.setText("How many pound you lost in past 1 month?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(23);
		textView2.setText("0--5--10--15--20+");
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
				AM5c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM5d();
			}
			
		});
		
	}
	
public void AM5d() {
		
	textView1.setTextSize(34);
	textView1.setText("How is your mobility?");
	textView1.setVisibility(View.VISIBLE);
	textView2.setVisibility(View.GONE);
	b1.setVisibility(View.GONE);
	b2.setVisibility(View.GONE);
	
	//	((RB1) findViewById(R.id.ratingBar1))
	
	spin1.setVisibility(View.VISIBLE);
	RB1.setVisibility(View.GONE);
	
	ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.spinnerArrayAM5d, R.layout.activity_spinner_layout2); //change the last argument here to your xml above.
	spin1.setAdapter(typeAdapter);
	
		spin1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long flag){
				switch(position){
				case 0:
					AM5d_reg = 1;
					b1.setVisibility(View.VISIBLE);
					b2.setVisibility(View.GONE);
					break;
				case 1: 
					AM5d_reg = 2;
					b1.setVisibility(View.VISIBLE);
					b2.setVisibility(View.GONE);
					break;
				case 2: 
					AM5d_reg = 3;
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
	
	//////////////////// Day5 PM ////////////////////////////
	
	public void PM5a() {
		
		
		textView1.setTextSize(32);
		textView1.setText("What is your pain level now?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		RB1.setStepSize(0.5f);
		
		textView2.setTextSize(22);
		textView2.setText("none ---> very severe");
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
				PM5a_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM5b();
			}
			
		});
		
	}
	
	public void PM5b() {
		
		
		textView1.setTextSize(30);
		textView1.setText("Pain interfere with daily activities?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		RB1.setStepSize(1);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> Very Much");
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
				PM5b_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM5c();
			}
			
		});
		
	}
	
	public void PM5c() {
		
		
		textView1.setTextSize(30);
		textView1.setText("How much energy do you have?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> very much");
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
				PM5c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM5d();
			}
			
		});
		
	}
	
	public void PM5d() {
		
		textView1.setTextSize(30);
		textView1.setText("It's hard to focus on anything but my anxiety");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(24);
		textView2.setText("Never ---> Always");
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
				PM5d_reg = RB1.getRating();
				b1.setVisibility(View.VISIBLE);
				b2.setVisibility(View.GONE);
			}
			
		});
		
	}

///////////////////////////// Day6 AM ////////////////////////////////

	public void AM6a() {
		
		
		textView1.setTextSize(32);
		textView1.setText("My Sleep Quality Was:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("very poor ---> very good");
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
				AM6a_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM6b();
			}
			
		});
		
	}
	
	public void AM6b() {
		
		
		textView1.setTextSize(32);
		textView1.setText("My Sleep Was Refreshing:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> Very Much");
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
				AM6b_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM6c();
			}
			
		});
		
	}
	
	public void AM6c() {
		
		
		textView1.setTextSize(32);
		textView1.setText("I had a problem sleeping:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> very much");
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
				AM6c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM6d();
			}
			
		});
		
	}
	
	public void AM6d() {
		
		textView1.setTextSize(32);
		textView1.setText("I had difficulty falling asleep:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> very much");
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
				AM6d_reg = RB1.getRating();
				b1.setVisibility(View.VISIBLE);
				b2.setVisibility(View.GONE);
			}
			
		});
		
	}
	
	////////////////////Day6 PM////////////////////////////
	
	public void PM6a() {
		
		
		textView1.setTextSize(32);
		textView1.setText("Please recall these 3 objects:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.GONE);
		RB1.setRating(0);
		
		textView2.setTextSize(32);
		textView2.setText("Orange, Flower, Pencil");
		textView2.setVisibility(View.VISIBLE);
		
		b2.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
				b2.setEnabled(false);	
				PM6b();
			}
		
		});
	
	}
	
	public void PM6b() {
		
		
		textView1.setTextSize(30);
		textView1.setText("Do you have someone to help with chores if you are sick?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.GONE);
		textView2.setVisibility(View.GONE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.VISIBLE);
		RB1.setVisibility(View.GONE);
		RB1.setRating(0);
		
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.spinnerArray2, R.layout.activity_spinner_layout); //change the last argument here to your xml above.
		spin1.setAdapter(typeAdapter);
		
			spin1.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int position, long flag){
					
					switch(position){
					case 0:
						break;
					case 1: 
						PM6b_reg = 1;
						b2.setEnabled(false);
						PM6c();	
						break;
					case 2: 
						PM6b_reg = 2;
						b2.setEnabled(false);
						PM6c();	
						break;
					}
				}
					
				@Override
		        public void onNothingSelected(AdapterView<?> adapterView) {
		            // TODO Auto-generated method stub
		        }		
		});
	
	}
	
	public void PM6c() {
	
	
		textView1.setTextSize(30);
		textView1.setText("Are your worries overwhelming you?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(24);
		textView2.setText("Never ---> Always");
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
			PM6c_reg = RB1.getRating();
			RB1.setRating(0);
			b2.setEnabled(false);
			PM6d();
			}
		
		});
	
	}

	public void PM6d() {
	
		textView1.setTextSize(32);
		textView1.setText("What were the 3 objects?");
		textView1.setVisibility(View.VISIBLE);
		textView2.setVisibility(View.GONE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.GONE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.VISIBLE);
		RB1.setVisibility(View.GONE);
		RB1.setRating(0);
		
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.spinnerArrayPM6d, R.layout.activity_spinner_layout); //change the last argument here to your xml above.
		spin1.setAdapter(typeAdapter);
		
			spin1.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int position, long flag){
					switch(position){
					case 0:
						PM6d_reg = 1;
						b1.setVisibility(View.VISIBLE);
						b2.setVisibility(View.GONE);
						break;
					case 1: 
						PM6d_reg = 2;
						b1.setVisibility(View.VISIBLE);
						b2.setVisibility(View.GONE);
						break;
					case 2: 
						PM6d_reg = 3;
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

////////////////////////// Day7 AM /////////////////////////
	
	public void AM7a() {
		
		textView1.setTextSize(32);
		textView1.setText("My Sleep Quality Was:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("very poor ---> very good");
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
				AM7a_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM7b();
			}
			
		});
		
	}
	
	public void AM7b() {
		
		
		textView1.setTextSize(32);
		textView1.setText("My Sleep Was Refreshing:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> Very Much");
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
				AM7b_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM7c();
			}
			
		});
		
	}
	
	public void AM7c() {
		
		
		textView1.setTextSize(32);
		textView1.setText("I had a problem sleeping:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> very much");
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
				AM7c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				AM7d();
			}
			
		});
		
	}
	
	public void AM7d() {
		
		textView1.setTextSize(32);
		textView1.setText("I had difficulty falling asleep:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> very much");
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
				AM7d_reg = RB1.getRating();
				b1.setVisibility(View.VISIBLE);
				b2.setVisibility(View.GONE);
			}
			
		});
		
	}
	
	////////////////////Day7 PM////////////////////////////
	
	public void PM7a() {
		
		
		textView1.setTextSize(32);
		textView1.setText("Do you feel uneasy?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(24);
		textView2.setText("Never ---> Always");
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
				PM7a_reg = RB1.getRating();
				RB1.setRating(0);
				PM7b();
				b2.setEnabled(false);
			}
			
		});
		
	}
	
	public void PM7b() {
		
		
		textView1.setTextSize(30);
		textView1.setText("Do you have someone to run errands if you need it?");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.GONE);
		textView2.setVisibility(View.GONE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.VISIBLE);
		RB1.setVisibility(View.GONE);
		RB1.setRating(0);
		
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,R.array.spinnerArray2, R.layout.activity_spinner_layout); //change the last argument here to your xml above.
		spin1.setAdapter(typeAdapter);
		
			spin1.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int position, long flag){
					
					switch(position){
					case 0:
						break;
					case 1: 
						PM7b_reg = 1;
						b2.setEnabled(false);
						PM7c();	
						break;
					case 2: 
						PM7b_reg = 2;
						b2.setEnabled(false);
						PM7c();	
						break;
					}
				}
					
				@Override
		        public void onNothingSelected(AdapterView<?> adapterView) {
		            // TODO Auto-generated method stub
		        }
			
		});
		
	}
	
	public void PM7c() {
		
		
		textView1.setTextSize(30);
		textView1.setText("I can keep track of what I'm doing, even if interrupted");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(22);
		textView2.setText("Not at all ---> Very much");
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
				PM7c_reg = RB1.getRating();
				RB1.setRating(0);
				b2.setEnabled(false);
				PM7d();
			}
			
		});
		
	}
	
	public void PM7d() {
		
		textView1.setTextSize(32);
		textView1.setText("I feel depressed:");
		textView1.setVisibility(View.VISIBLE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.VISIBLE);
		
		//	((RB1) findViewById(R.id.ratingBar1))
		
		spin1.setVisibility(View.GONE);
		RB1.setVisibility(View.VISIBLE);
		RB1.setRating(0);
		
		textView2.setTextSize(24);
		textView2.setText("Never ---> Always");
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
				PM7d_reg = RB1.getRating();
				b1.setVisibility(View.VISIBLE);
				b2.setVisibility(View.GONE);
			}
			
		});
		end_of_the_week = true;		
	}

////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////
	
	public void sendMessage(View view){
		sendMessage();
	}

    public void sendMessage() {
        // Do something in response to button
    	try{
    			File Logfile = new File(Directory, "questions2.csv");
    			mFileOutputStream = new BufferedOutputStream(new FileOutputStream(Logfile, true));
    			
    		String timestamp = TimeFormat.format(System.currentTimeMillis());
    		    		
    		String questions = 
    				timestamp+","+
    				AM1a_reg+","+
    				AM1b_reg+","+
    				AM1c_reg+","+
    				AM1d_reg+","+
    				PM1a_reg+","+
    				PM1b_reg+","+
    				PM1c_reg+","+
    				PM1d_reg+","+
    				AM2a_reg+","+
    				AM2b_reg+","+
    				AM2c_reg+","+
    				AM2d_reg+","+
    				PM2a_reg+","+
    				PM2b_reg+","+
    				PM2c_reg+","+
    				PM2d_reg+","+
    				AM3a_reg+","+
    				AM3b_reg+","+
    				AM3c_reg+","+
    				AM3d_reg+","+
    				PM3a_reg+","+
    				PM3b_reg+","+
    				PM3c_reg+","+
    				PM3d_reg+","+
    				AM4a_reg+","+
    				AM4b_reg+","+
    				AM4c_reg+","+
    				AM4d_reg+","+
    				PM4a_reg+","+
    				PM4b_reg+","+
    				PM4c_reg+","+
    				PM4d_reg+","+
    				AM5a_reg+","+
    				AM5b_reg+","+
    				AM5c_reg+","+
    				AM5d_reg+","+
    				PM5a_reg+","+
    				PM5b_reg+","+
    				PM5c_reg+","+
    				PM5d_reg+","+
    				AM6a_reg+","+
    				AM6b_reg+","+
    				AM6c_reg+","+
    				AM6d_reg+","+
    				PM6a_reg+","+
    				PM6b_reg+","+
    				PM6c_reg+","+
    				PM6d_reg+","+
    				AM7a_reg+","+
    				AM7b_reg+","+
    				AM7c_reg+","+
    				AM7d_reg+","+
    				PM7a_reg+","+
    				PM7b_reg+","+
    				PM7c_reg+","+
    				PM7d_reg+","+"\n";
    				
    		
    		mFileOutputStream.write(questions.getBytes());
    		
			mFileOutputStream.flush();
			mFileOutputStream.close();
			mFileOutputStream = null;
    	} 
    	
    	catch (IOException e) {
    		e.printStackTrace();
    	}
		
    	questionAM = -1;
		questionPM = -1;
		FollowUpQuestion = -1;
		NextQuestion = -1;
		
		if (end_of_the_week)
		{
			AM1a_reg = -1;AM1b_reg = -1;AM1c_reg = -1;AM1d_reg = -1;
			PM1a_reg = -1;PM1b_reg = -1;PM1c_reg = -1;PM1d_reg = -1;
			AM2a_reg = -1;AM2b_reg = -1;AM2c_reg = -1;AM2d_reg = -1;
			PM2a_reg = -1;PM2b_reg = -1;PM2c_reg = -1;PM2d_reg = -1;
			AM3a_reg = -1;AM3b_reg = -1;AM3c_reg = -1;AM3d_reg = -1;
			PM3a_reg = -1;PM3b_reg = -1;PM3c_reg = -1;PM3d_reg = -1;
			AM4a_reg = -1;AM4b_reg = -1;AM4c_reg = -1;AM4d_reg = -1;
			PM4a_reg = -1;PM4b_reg = -1;PM4c_reg = -1;PM4d_reg = -1;
			AM5a_reg = -1;AM5b_reg = -1;AM5c_reg = -1;AM5d_reg = -1;
			PM5a_reg = -1;PM5b_reg = -1;PM5c_reg = -1;PM5d_reg = -1;
			AM6a_reg = -1;AM6b_reg = -1;AM6c_reg = -1;AM6d_reg = -1;
			PM6a_reg = -1;PM6b_reg = -1;PM6c_reg = -1;PM6d_reg = -1;
			
			end_of_the_week = false;
		}
		Intent intentstop = new Intent(this, PushQuestions.class);
		PendingIntent senderstop = PendingIntent.getActivity(this, 123, intentstop, 0);
		AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManagerstop.cancel(senderstop);
    	
    	Intent intent = new Intent(this, MainActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);

    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pushquestions, menu);
		return true;
	}
	
    //Cancelling Alarm w/o finishing question
    private void cancelAlarm(){
		Intent intentstop = new Intent(this, PushQuestions.class);
		PendingIntent senderstop = PendingIntent.getActivity(this, 123, intentstop, 0);
		AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManagerstop.cancel(senderstop);
		Toast.makeText(this, "Alarm Canceled!", Toast.LENGTH_LONG).show();
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//		case R.id.question1:
//			question1();
//			return true;
//		case R.id.question2:
//			question2();
//			return true;
//		case R.id.question3:
//			question3();
//			return true;
//		case R.id.question4:
//			question4();
//			return true;
		
		case R.id.cancel_alarm:
			cancelAlarm();
			return true;
			
		}
		return false;
	}

}
