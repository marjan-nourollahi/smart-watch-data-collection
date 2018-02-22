package eecs.wsu.datacollection;

import java.util.GregorianCalendar;



import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmSchedule extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_schedule);
		
		
	}
	
	public void setAlarm(View view) {
		DatePicker Date = (DatePicker) findViewById(R.id.datePicker1);
		TimePicker Time = (TimePicker) findViewById(R.id.timePicker1);
		
		int year = Date.getYear();
        int month = Date.getMonth();
        int day = Date.getDayOfMonth();
        int hour = Time.getCurrentHour();
        int minute = Time.getCurrentMinute();
        
        GregorianCalendar calendar = new GregorianCalendar(year,month,day, hour, minute);
		// time at which alarm will be scheduled here alarm is scheduled at 1 day from current time, 
		// we fetch  the current time in milliseconds and added 1 day time
		// i.e. 24*60*60*1000= 86,400,000   milliseconds in a day       
		//Long time = System.currentTimeMillis()+1*60*1000;
        
        long time = calendar.getTimeInMillis();

		// create an Intent and set the class which will execute when Alarm triggers, here we have
		// given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
		// alarm triggers and 
		//we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
		Intent intentAlarm = new Intent(this, PushQuestions.class);
		intentAlarm.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// create the object
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		//set the alarm for particular time
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,time,24*60*60*1000 , PendingIntent.getActivity(this,111,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
		Toast.makeText(this, "Alarm Scheduled!", Toast.LENGTH_LONG).show();
	}
	
	public void cancelAlarm(View view){
		Intent intentstop = new Intent(this, PushQuestions.class);
		PendingIntent senderstop = PendingIntent.getActivity(this, 111, intentstop, 0);
		AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManagerstop.cancel(senderstop);
		Toast.makeText(this, "Alarm Canceled!", Toast.LENGTH_LONG).show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
