package nit.Mp3Player.service;

import java.io.File;

import nit.Mp3Player.AppConstant;
import nit.Mp3Player.playerActivity;
import nit.model.Mp3Info;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

public class PlayerService extends Service{

	private boolean isPlaying = false;
	private boolean isPasue = false;
	private boolean isReleased = false;
	private Mp3Info mp3Info = null;
	MediaPlayer mediaPlayer = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Mp3Info mp3Info = (Mp3Info)intent.getSerializableExtra("mp3Info");
		int MSG = intent.getIntExtra("MSG", 0);
		if(mp3Info != null){
			if(MSG == AppConstant.PlayerMsg.PLAY_MSG){
				play(mp3Info);
			}
		}
		else{
			if(MSG == AppConstant.PlayerMsg.PAUSE_MSG){
				pause();
			}
			else if(MSG == AppConstant.PlayerMsg.STOP_MSG){
				stop();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void play(Mp3Info mp3Info){
		
			String path = getMp3Path(mp3Info);
			mediaPlayer = new MediaPlayer().create(this, Uri.parse("file://" + path));
			mediaPlayer.setLooping(false);
			mediaPlayer.start();
			isPlaying = true;
			isReleased = false;
	}
	
	private void pause(){
		
		if(mediaPlayer != null){
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
			}
		}
	}
	
	private void stop(){
		
		if(mediaPlayer != null)
			if(isPlaying){
				if(!isReleased){
					mediaPlayer.stop();
					mediaPlayer.release();
					isReleased = true;
				}
				isPlaying = false;
			}
	}
	
	private String getMp3Path(Mp3Info mp3Info){
		String sDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
		String path = sDCardRoot + File.separator + "mp3" + File.separator + mp3Info.getMp3Name();
		return path;
	}
}
