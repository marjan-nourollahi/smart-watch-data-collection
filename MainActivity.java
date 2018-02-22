package eecs.wsu.datacollection;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;






import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView mStatusText;
    private static boolean buttonFlag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mStatusText = (TextView) findViewById(R.id.txtStatus);
		//System.out.println(mStatusText);
		
		Button button = (Button) findViewById(R.id.initialTest);
		if (buttonFlag) {
			button.setVisibility(View.VISIBLE);
		} else {
			button.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_start_data_collection:
			sendBroadcast(new Intent("edu.cs.ucla.galaxygear.datacollection.startservice"));
			return true;
		case R.id.action_stop_data_collection:
			sendBroadcast(new Intent("edu.cs.ucla.galaxygear.datacollection.stopservice"));
			return true;
		case R.id.action_check_data_collection:
			setStatusText();
			return true;
		case R.id.action_set_alarm:
			setAlarm();
			return true;
		case R.id.action_cancel_alarm:
			cancelAlarm();
			return true;
		}
		return false;
	}

	private void setStatusText() {
		if (dataCollectionServiceIsRunning()) {
			mStatusText.setText("Service is running");
		} else {
			mStatusText.setText("Service is not running");
		}
		
		// prepare intent which is triggered if the
		// notification is selected

		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// build notification
		// the addAction re-use the same intent to keep the example short
		Notification n  = new Notification.Builder(this)
		        .setContentTitle("New mail from " + "test@gmail.com")
		        .setContentText("Subject")
		        .setSmallIcon(R.drawable.ic_jog_dial_vibrate_on)
		        .setContentIntent(pIntent)
		        .setAutoCancel(true)
		        .build();
		    
		  
		NotificationManager notificationManager = 
		  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		notificationManager.notify(0, n); 
	}
	
	private void setAlarm(){
		long currentime = System.currentTimeMillis();
		Date date = new Date(currentime);
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		int year = time.get(Calendar.YEAR);
		int month = time.get(Calendar.MONTH);
		int day = time.get(Calendar.DAY_OF_MONTH);
		int hour = 9;
		int minute = 0;
		int hour2 = 15;
		int minute2 = 0;
		 	
		// time at which alarm will be scheduled here alarm is scheduled at 1 day from current time, 
		// we fetch  the current time in milliseconds and added 1 day time
		// i.e. 24*60*60*1000= 86,400,000   milliseconds in a day       
		//Long time = System.currentTimeMillis()+1*60*1000;
		
		//ALARM1
        GregorianCalendar calendar1 = new GregorianCalendar(year,month,day, hour, minute);
        long settime1 = calendar1.getTimeInMillis();
        
        if(settime1 <= currentime)
        {    settime1 = settime1 + (AlarmManager.INTERVAL_DAY+1);}
        else
        {    settime1 = settime1;}
        
		Intent intentAlarm1 = new Intent(this, PushQuestions.class);
		intentAlarm1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,settime1,24*60*60*1000 , PendingIntent.getActivity(this,111,  intentAlarm1, PendingIntent.FLAG_UPDATE_CURRENT));
		
		//ALARM2
		GregorianCalendar calendar2 = new GregorianCalendar(year,month,day, hour2, minute2);
		long settime2 = calendar2.getTimeInMillis();
		
        if(settime2 <= currentime)
        {    settime2 = settime2 + (AlarmManager.INTERVAL_DAY+1);}
        else
        {    settime2 = settime2;}
		
		Intent intentAlarm2 = new Intent(this, PushQuestions.class);
		intentAlarm2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		AlarmManager alarmManager2  = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, settime2, 24*60*60*1000 , PendingIntent.getActivity(this,112,  intentAlarm1, PendingIntent.FLAG_UPDATE_CURRENT));
				
		//TOAST
		Toast.makeText(this, "Alarm Scheduled!", Toast.LENGTH_LONG).show();
		
	}
	
	
	private void cancelAlarm(){
		//CANCEL ALARM1
		Intent intentstop = new Intent(this, PushQuestions.class);
		PendingIntent senderstop = PendingIntent.getActivity(this, 111, intentstop, 0);
		AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManagerstop.cancel(senderstop);
		//CANCEL ALARM2
		Intent intentstop2 = new Intent(this, PushQuestions.class);
		PendingIntent senderstop2 = PendingIntent.getActivity(this, 112, intentstop2, 0);
		AlarmManager alarmManagerstop2 = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManagerstop2.cancel(senderstop2);
		
		
		//TOAST
		Toast.makeText(this, "Alarm Canceled!", Toast.LENGTH_LONG).show();
	}
	private boolean dataCollectionServiceIsRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			System.out.println(service.service.getClassName());
			if (DataCollectionService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
    public void scheduleAlarm(View view)
    {
    	Intent intent = new Intent(this, AlarmSchedule.class);
    	
    	startActivity(intent);
    			
    }
    public void Annotation(View view)
    {
    	sendBroadcast(new Intent("edu.cs.ucla.galaxygear.datacollection.startservice"));
		
    			
    }
    
    public void initialTest(View view) {
    		Intent intentAlarm = new Intent(this, InitialTest.class);
        	startActivity(intentAlarm); 
        	buttonFlag = false;
    }
}