package com.rookeryradio.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.rookeryradio.android.fragments.FrontPageFragment;
import com.rookeryradio.android.receivers.ReceiverHandler;

public class RadioService extends Service {

	private NotificationManager mNM;
	private final IBinder mBinder = new LocalBinder();
	private int NOTIFICATION = 123312;
	private int NOTIFICATION_DJ = NOTIFICATION+1;
	private int NOTIFICATION_SONG = NOTIFICATION+2;
	
	private static MediaPlayer mediaPlayer;
	private PhpRequestHandler rhandler;
	private Handler handler;
	
	protected String lastVotedSong = "NullSongHere";
	protected String lastVotedDj = "NullDjHere";
	public String currentDj="";
	public String currentSong="";
	public int lastShoutout=0;
	public int lastRequest=0;
	private ArrayList<String> songs;
	
	public static final int AC_CHOON = 1;
	public static final int AC_POON = 2;
	public static final int AC_STOP_RADIO = 3;
	
	long playUntil = 0;
	
	private boolean loading = false;
	protected boolean requestPlaying = false;
	
	public final static String infoIntent = "com.rookeryradio.android.RadioService.INFO";

	public static String convertStandardJSONString(String data_json) {
		data_json = data_json.replaceAll("\\\\r\\\\n", "");
		data_json = data_json.replace("\"{", "{");
		data_json = data_json.replace("}\",", "},");
		data_json = data_json.replace("}\"", "}");
		return data_json;
	}
	
	protected ScheduledFuture<?> task;
	
	private ReceiverHandler receiverHandler;
	
	public void playUntil(int hour, int min){
		
		if(requestPlaying==false){
			new Thread(new Runnable(){
				public void run(){
					startRadio();
				}
			}).start();
		}
		long now = 0;
		now += (min*60);
		now += (hour*60*60);
		Log.i("info", "playing for "+now);
		now +=(System.currentTimeMillis()/1000);
		
		playUntil = now;
	}
	
	private void showMessage(final String message){
		handler.post(new Runnable(){
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	/*public void shoutout(final String shoutout){
		new Thread(new Runnable(){
			@Override
			public void run(){
				rhandler.shoutout(shoutout);
			}
		}).start();
		
		showMessage("Shoutout sent");
		lastShoutout = FrontPageFragment.timeSec();
	}
	
	public void request(final String request){
		new Thread(new Runnable(){
			@Override
			public void run(){
				rhandler.request(request);
			}
		}).start();
		
		showMessage("Request sent");
		lastRequest = FrontPageFragment.timeSec();
	}
	
	public void choon(){
		if (!currentSong.equals(lastVotedSong))
		{
			new Thread(new Runnable(){
				@Override
				public void run(){
					rhandler.choon();
				}
			}).start();
			
			lastVotedSong = currentSong;
			showMessage("Choon sent");
		}
		else
		{
			showMessage("You already rated this song!");
		}
	}
	
	public void poon(){
		if (!currentSong.equals(lastVotedSong))
		{
			new Thread(new Runnable(){
				@Override
				public void run(){
					rhandler.poon();
				}
			}).start();
			lastVotedSong = currentSong;
			showMessage("Poon sent");
		}
		else
		{
			showMessage("You already rated this song!");
		}
	}
	
	public void ftw(){
		if (!lastVotedDj.equals(currentDj))
		{
			new Thread(new Runnable(){
				@Override
				public void run(){
					rhandler.djftw(currentDj);
				}
			}).start();
			lastVotedDj = currentDj;
			showMessage("Ftw sent");
		}
		else
		{
			showMessage("You already rated this dj");
		}
	}
	*/
	public SharedPreferences getPrefs(){
		return PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	public Editor editPrefs(){
		return getPrefs().edit();
	}
	
    @Override
    public void onCreate() {
    	super.onCreate();
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        
        rhandler = new PhpRequestHandler(this);
        
        boolean begin_playing = getPrefs().getBoolean("begin_playing", false);
		
		songs = new ArrayList<String>();
		handler = new Handler();
		setTask();
    	
		receiverHandler = new ReceiverHandler(this);
		receiverHandler.register();
		
    	if(begin_playing){
			new Thread(new Runnable(){
				public void run(){
					startRadio();
				}
			}).start();
		}
    }
    
	public static MediaPlayer getMediaPlayer(){
		return mediaPlayer;
	}
	
	public void stopRadio(){
		playUntil=0;
		requestPlaying = false;
        loading = false;
		
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.reset();
		}

        mNM.cancel(NOTIFICATION);
		loading = false;
	}
	
	public void startRadio(){
		requestPlaying = true;
		while(requestPlaying == true)
		{
			if (!mediaPlayer.isPlaying()) 
	    	{
				loading = true;
	    		String url = "http://stream.rookeryradio.com:8088/live";
	    		
	    		showNotification();
	    		
	    		mediaPlayer.reset();
	    		try 
	    		{
	    			mediaPlayer.setDataSource(url);
	    		} 
	    		catch (Exception e) 
	    		{
	    		}
	    		try
				{
					mediaPlayer.prepare();
				} catch (IllegalStateException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
	    		mediaPlayer.start();
	    		loading=false;
	    		showNotification();
	    	}
		}
	}
    
    private void showNotification() {
    	RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.radio_notification);
    	
    	notificationView.setOnClickPendingIntent(R.id.stop_radio, makeActionIntent(AC_STOP_RADIO));
    	
    	notificationView.setTextViewText(R.id.c_song, "Song: "+ currentSong);
    	notificationView.setTextViewText(R.id.c_dj, "DJ: " + currentDj);
    	
    	if(loading){
    		notificationView.setImageViewResource(R.id.stop_radio, android.R.drawable.progress_indeterminate_horizontal);
    	} else {
    		notificationView.setImageViewResource(R.id.stop_radio, android.R.drawable.ic_media_pause);
    	}
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.logo)
    	        .setContent(notificationView)
    	        .setOngoing(true);
    	
    	if(loading){
    		mBuilder.setContentTitle("Hive365 radio loading");
    	} else {
    		mBuilder.setContentTitle("Hive365 radio playing");
    	}
    
    	mBuilder.setContentIntent(homeIntent());
    	
    	// mId allows you to update the notification later on.
    	mNM.notify(NOTIFICATION, mBuilder.build());
    }
    
    /**private void newSongNotification() {
    	RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.new_song);
    	
    	notificationView.setOnClickPendingIntent(R.id.new_song_choon, makeActionIntent(AC_CHOON));
    	notificationView.setOnClickPendingIntent(R.id.new_song_poon, makeActionIntent(AC_POON));
    	
    	notificationView.setTextViewText(R.id.new_song, "New song: "+ currentSong);
    	
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.logo)
    	        .setContentTitle("New song")
    	        .setContent(notificationView)
    	        .setAutoCancel(true);
    	
    	mBuilder.setContentText(currentSong);
    	mBuilder.setContentIntent(homeIntent());
    	
    	// mId allows you to update the notification later on.
    	mNM.notify(NOTIFICATION_SONG, mBuilder.build());
    }
    **/
    private PendingIntent makeActionIntent(int action){
    	Intent intent = new Intent(this, RadioService.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	intent.putExtra("action", action);
    	PendingIntent pIntent = PendingIntent.getService(this, 0, intent, 0);
    	return pIntent;
    }

    private PendingIntent homeIntent(){
    	Intent homeIntent = new Intent(this, MainActivity.class);
    	homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    	homeIntent.setAction(Intent.ACTION_MAIN);
    	homeIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    	PendingIntent appIntent = PendingIntent.getActivity(this, 0,homeIntent , 0);
    	return appIntent;
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	


    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        //Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	if(intent!=null){
	    	if(intent.hasExtra("action")){
	    		int action = intent.getIntExtra("action", 0);
	    		
	    		switch(action){
	    	//		case AC_CHOON:
	    	//			choon();
	    	//		case AC_POON:
	    	//			poon();
	    			case AC_STOP_RADIO:
	    				stopRadio();
	    		}
	    	}
    	}
    	
      return Service.START_STICKY;
    }
    
    private void setTask(){
    	updateInfo();
		ScheduledExecutorService scheduler = Executors
				.newSingleThreadScheduledExecutor();
		task = scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				updateInfo();
			}
		}, 5, 5, TimeUnit.SECONDS);
    }
	
    public class LocalBinder extends Binder {
    	RadioService getService() {
            return RadioService.this;
        }
    }
    
    private void newDj(){
    	Log.i("info", "New dj: "+currentDj);
    	
    	Intent intent = new Intent();
    	intent.setAction(infoIntent);
    	intent.putExtra("type", "newdj");
    	LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    	
    	if(mediaPlayer.isPlaying()){
    		showNotification();
    	}
    }
    
    private void newSong(){
    	Log.i("info", "New song: "+currentSong);
    	//if(songs.get(0)!=currentSong){
    		songs.add(0, currentSong);
    	//}
    	while(songs.size()>20){
    		songs.remove(songs.size()-1);
    	}
    	
    	
    	int sett = Integer.parseInt(getPrefs().getString("newsong", "2"));
    	//Log.i("info", "sett: "+sett);
    	
    	//if(sett==1 || (sett==2 && requestPlaying)){
    	//	newSongNotification();
    	//}
    	
    	if(mediaPlayer.isPlaying()){
    		showNotification();
    	}
    	
    	Intent intent = new Intent();
    	intent.setAction(infoIntent);
    	intent.putExtra("type", "newsong");
    	LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    
    public ArrayList<String> getSongs(){
    	return songs;
    }
    
    private void noInternet(){
    	Intent intent = new Intent();
    	intent.setAction(infoIntent);
    	intent.putExtra("type", "nointernet");
    	LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    
	public void updateInfo() 
	{
		long now = (System.currentTimeMillis()/1000);
		if(playUntil!=0 && now>playUntil){
			stopRadio();
			playUntil = 0;
		}
		String temp_dj = "";
		String temp_song = "";
		String temp_info = "";

		try 
		{
			//Log.d("RadioService", rhandler.getList().getString("icestats"));
			temp_info = rhandler.getList().getString("icestats").toString().replace(",}","}}");
			//Log.d("RadioService", "Temp Info is:" + temp_info);
			JSONObject info = new JSONObject(temp_info);
			//Log.d("RadioService", "JSON object is: " + info);
			temp_dj = info.getJSONObject("source").getString("server_name").replace("&amp;", "&").trim();
			//Log.d("RadioService", "Info is: " + info.toString());
			temp_song = info.getJSONObject("source").getString("title").replace("&amp;", "&").trim();
			
			if(!temp_dj.equals(currentDj) && !temp_dj.equals("")){
				currentDj = temp_dj;
				newDj();
			}
			
			if(!temp_song.equals(currentSong) && !temp_song.equals("")){
				currentSong = temp_song;
				newSong();
			}

		} 
		catch (Exception e) 
		{
			//noInternet();
			e.printStackTrace();
		}
		
		

	}

	//public String getUsername() {
	//	return getPrefs().getString("username", "Unknown");
	//}
	
	public boolean getStatus()
    {
    	return requestPlaying;
    }
	
    public boolean getPlaying()
    {
    	return mediaPlayer.isPlaying();
    }
    public boolean getLoading()
    {
    	if(requestPlaying == true && mediaPlayer.isPlaying() == false)
    		return true;
    	else
    		return false;
    }

}
