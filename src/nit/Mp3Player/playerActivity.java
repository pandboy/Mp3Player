package nit.Mp3Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Queue;

import nit.Mp3Player.service.PlayerService;
import nit.lrc.LrcProcessor;
import nit.model.Mp3Info;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.method.QwertyKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class playerActivity extends Activity{

	ImageButton beginButton = null;
	ImageButton pauseButton = null;
	ImageButton stopButton = null;
	MediaPlayer mediaPlayer = null;
	
	private ArrayList<Queue> queues = null;
	private TextView lrcTextView = null;
	private Mp3Info mp3Info = null;
	private Handler handler = new Handler();
	private UpdateTimeCallback updateTimeCallback = null;
	private long begin = 0;
	private long nextTimeMill = 0;
	private long currentTimeMill = 0;
	private String message = null;
	private long pauseTimeMills = 0;
	
	private boolean isPlaying = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		
		Intent intent = getIntent();
		mp3Info = (Mp3Info)intent.getSerializableExtra("mp3Info");
		beginButton = (ImageButton)findViewById(R.id.begin);
		pauseButton = (ImageButton)findViewById(R.id.pause);
		stopButton = (ImageButton)findViewById(R.id.stop);
		beginButton.setOnClickListener(new BeginButtonListener());
		pauseButton.setOnClickListener(new PauseButtonListener());
		stopButton.setOnClickListener(new StopButtonListener());
		lrcTextView = (TextView)findViewById(R.id.lrcText);
	}
	
	private void prepareLrc(String lrcName){
		try{
			System.out.println("--------->"+lrcName);
			InputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "mp3/" + mp3Info.getLrcName());
			System.out.println(inputStream);
			LrcProcessor lrcProcessor = new LrcProcessor();
			queues = lrcProcessor.process(inputStream);
			updateTimeCallback = new UpdateTimeCallback(queues);
			begin = 0;
			currentTimeMill = 0;
			nextTimeMill = 0;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}

	class BeginButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("mp3Info", mp3Info);
			intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
			intent.setClass(playerActivity.this, PlayerService.class);
			System.out.println("--------->"+mp3Info.getLrcName());
			prepareLrc(mp3Info.getLrcName());
			startService(intent);
			begin = System.currentTimeMillis();
			handler.postDelayed(updateTimeCallback, 5);
			isPlaying = true;
			/*if(!isPlaying){
				String path = getMp3Path(mp3Info);
				mediaPlayer = MediaPlayer.create(playerActivity.this, Uri.parse("file://" + path));
				mediaPlayer.setLooping(false);
				mediaPlayer.start();
				isPlaying = true;
				isReleased = false;
			}*/
		}
		
		
	}
	
	class PauseButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(playerActivity.this, PlayerService.class);
			intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
			startService(intent);
			if(isPlaying){
				handler.removeCallbacks(updateTimeCallback);
				pauseTimeMills = System.currentTimeMillis();
			/*if(mediaPlayer != null){
				if(!isReleased){
					if(!isPasue){
						mediaPlayer.pause();
						isPasue = true;
						isPlaying = true;
					}
					else{
						mediaPlayer.start();
						isPasue = false;
					}
				}*/
			}
			else{
				handler.postDelayed(updateTimeCallback, 5);
				begin = System.currentTimeMillis() - pauseTimeMills + begin; 
			}
			isPlaying = isPlaying ? false : true;
		}
		
		
	}
	
	class StopButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(playerActivity.this,PlayerService.class);
			intent.putExtra("MSG", AppConstant.PlayerMsg.STOP_MSG);
			startService(intent);
			handler.removeCallbacks(updateTimeCallback);
			/*if(mediaPlayer != null)
				if(isPlaying){
					if(!isReleased){
						mediaPlayer.stop();
						mediaPlayer.release();
						isReleased = true;
					}
					isPlaying = false;
				}*/
		}
		
		
	}
	
	private String getMp3Path(Mp3Info mp3Info){
		String sDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
		String path = sDCardRoot + File.separator + "mp3" + File.separator + mp3Info.getMp3Name();
		return path;
	}
	
	class UpdateTimeCallback implements Runnable{
		
		Queue times = null;
		Queue messages = null;

		public UpdateTimeCallback(ArrayList<Queue> queues){
			times = queues.get(0);
			messages = queues.get(1);
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			long offset = System.currentTimeMillis() - begin;
			System.out.println(offset);
			if(currentTimeMill == 0){
				nextTimeMill =(Long)times.poll();
				message = (String)messages.poll();
			}
			if(offset >= nextTimeMill){
				lrcTextView.setText(message);
				message = (String)messages.poll();
				nextTimeMill = (Long)times.poll();
			}
			currentTimeMill = currentTimeMill + 10;
			handler.postDelayed(updateTimeCallback, 10);
		}
		
	}
}
