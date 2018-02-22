package eecs.wsu.datacollection;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class DataCollectionService extends Service implements SensorEventListener {
	
	private static final SimpleDateFormat sFilenameFormat = new SimpleDateFormat("yyyy-MM-dd-HH'.csv'", Locale.US);
	private static final File sDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DataCollectionService");
	private static final int SAMPLE_RATE = 10;
	private static final int sSensorDelay = 1000000 / SAMPLE_RATE;
	
	private BufferedOutputStream mFileOutputStream;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mGyroscope;
	private WakeLock mWakeLock;
	private long mHour = -1;
	private long mCurrentHour = -1;
	
	private long[] mBufferedTimestamps;
	private float[][] mBufferedReadings;
	private float[] mLastGyrReadings;
	private int ptrBuffer = 0;
	
	// WHI UNDERGRADUATE PROGRAM //
	private LinkedList<Double[]> mReadings = new LinkedList<Double[]>();
	private final int windowSize = 5;
	// END OF WHI UNDERGRADUATE PROGRAM EDITS //
	
	@Override
	public void onCreate() {
		super.onCreate();		
		mBufferedReadings = new float[SAMPLE_RATE * 4000][6];
		mBufferedTimestamps = new long[SAMPLE_RATE * 4000];
		mLastGyrReadings = new float[3];
		
		// Prevent the CPU from sleeping
		PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DataCollectionServiceWakeLock");
		mWakeLock.acquire();
		
		// Create the log directory
		sDirectory.mkdirs();
		
		//initializing the sensors
		// Register this class as a listener for the sensors we want
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mSensorManager.registerListener(this, mAccelerometer, sSensorDelay);
		mSensorManager.registerListener(this, mGyroscope, sSensorDelay);
		
		// Start foreground
		@SuppressWarnings("deprecation")
		Notification note = new Notification( 0, null, System.currentTimeMillis() );
	    note.flags |= Notification.FLAG_NO_CLEAR;
	    startForeground( 42, note );
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop gathering sensor data
		mSensorManager.unregisterListener(this);
		
		// Write any last events to file before we're killed off
		writeEventsToFile();
		
		// Relase the wake lock
		if (mWakeLock != null) {
			mWakeLock.release();
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// Write out data every hour and start over
		mCurrentHour = System.currentTimeMillis() / 3600000; 
		if (mHour != mCurrentHour) {
			writeEventsToFile();
			mHour = mCurrentHour;
		}
		
		// Buffer this sensor event
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mBufferedTimestamps[ptrBuffer] = System.currentTimeMillis();
			mBufferedReadings[ptrBuffer][0] = event.values[0];
			mBufferedReadings[ptrBuffer][1] = event.values[1];
			mBufferedReadings[ptrBuffer][2] = event.values[2];
			mBufferedReadings[ptrBuffer][3] = mLastGyrReadings[0];
			mBufferedReadings[ptrBuffer][4] = mLastGyrReadings[1];
			mBufferedReadings[ptrBuffer][5] = mLastGyrReadings[2];
			++ptrBuffer;
			
			// WHI UNDERGRADUATE PROGRAM //
			
			Double[] sensorReadings = new Double[6];
			sensorReadings[0] = (double) event.values[0];
			sensorReadings[1] = (double) event.values[1];
			sensorReadings[2] = (double) event.values[2];
			sensorReadings[3] = (double) mLastGyrReadings[0];
			sensorReadings[4] = (double) mLastGyrReadings[1];
			sensorReadings[5] = (double) mLastGyrReadings[2];
			
			if (mReadings.size() < windowSize) {
				mReadings.add(sensorReadings);
			} else if (mReadings.size() == windowSize) {
				// TODO - send to feature extractor
				mReadings.removeFirst(); // pops the first xyzxyz values
				mReadings.add(sensorReadings); // adds xyzxyz values
			}
			
			// TESTING FUNCTIONALITY
			System.out.println("///////////// SIZE OF mReadings: " + mReadings.size());
			for (Double[] i : mReadings) {
				System.out.print(i[0] + " " + i[1] + " " + i[2] + " " + i[3] + " " + i[4] + " " + i[5] + " ");
				System.out.println("///////////////////////////");	
			}
			
			// END OF WHI UNDERGRADUATE PROGRAM EDITS //
			
		} else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			mLastGyrReadings[0] = event.values[0];
			mLastGyrReadings[1] = event.values[1];
			mLastGyrReadings[2] = event.values[2];
		}
	}
	
	private void writeEventsToFile() {
		try {
			// Open our data file, append if it already exists
			File file = new File(sDirectory, sFilenameFormat.format(new Date()));
			mFileOutputStream = new BufferedOutputStream(new FileOutputStream(file, true));
			
			// Write out each line of sensor data
			for (int i=0; i<ptrBuffer; ++i) {
				String mDataLine = 
						mBufferedTimestamps[i]+","+
						mBufferedReadings[i][0]+","+
						mBufferedReadings[i][1]+","+
						mBufferedReadings[i][2]+","+
						mBufferedReadings[i][3]+","+
						mBufferedReadings[i][4]+","+
						mBufferedReadings[i][5]+"\n";
				mFileOutputStream.write(mDataLine.getBytes());
			}
			
			// Flush and close the stream to ensure data is written
			mFileOutputStream.flush();
			mFileOutputStream.close();
			mFileOutputStream = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ptrBuffer = 0;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// No implementation necessary
	}

	@Override
	public IBinder onBind(Intent intent) {
		// No implementation necessary
		return null;
	}
}