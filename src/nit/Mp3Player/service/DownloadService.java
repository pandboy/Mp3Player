package nit.Mp3Player.service;

import nit.model.Mp3Info;
import nit.Download.HttpDownloader;
import nit.Mp3Player.MainActivity;
import nit.Mp3Player.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Mp3Info mp3Info = (Mp3Info)intent.getSerializableExtra("mp3Info");
		DownloadThread downloadThread = new DownloadThread(mp3Info);
		Thread thread = new Thread(downloadThread);
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}
	class DownloadThread implements Runnable{
		private NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		private Mp3Info mp3Info = null;
		public DownloadThread(Mp3Info mp3Info){
			this.mp3Info = mp3Info;
		}
		public void run() {
			String mp3Name = mp3Info.getMp3Name();
			String lrcName = mp3Info.getLrcName();
			String mp3Url = "http://10.80.129.80:8080/nitxfl/"+mp3Info.getMp3Name();
			String mp3Lrc ="http://10.80.129.80:8080/nitxfl/"+mp3Info.getLrcName();
			HttpDownloader httpDownloader = new HttpDownloader();
			int lrcResult = httpDownloader.downFile(mp3Lrc, "mp3/", mp3Info.getLrcName());
			int mp3Result = httpDownloader.downFile(mp3Url, "mp3/", mp3Info.getMp3Name());
			String resultMessage = null;
			if(mp3Result == -1){
				setNotification_mp3("mp3","Mp3_Title",mp3Name+"下载失败",R.drawable.icon);
				System.out.println("-1");
			}
			else if(mp3Result == 0){
				setNotification_mp3("mp3","Mp3_Title",mp3Name+"下载成功",R.drawable.icon);
				System.out.println("0");
			}
			else if(mp3Result == 1){
				setNotification_mp3("mp3","Mp3_Title",mp3Name+"已存在",R.drawable.icon);
				System.out.println("1");
			}
			if(lrcResult == -1){
				setNotification_lrc("lrc","Lrc_Title",lrcName+"下载失败",R.drawable.icon);
				System.out.println("-1");
			}
			else if(lrcResult == 0){
				setNotification_lrc("lrc","Lrc_Title",lrcName+"下载成功",R.drawable.icon);
				System.out.println("0");
			}
			else if(lrcResult == 1){
				setNotification_lrc("lrc","Lrc_Title",lrcName+"已存在",R.drawable.icon);
				System.out.println("1");
			}
		}
		
		private void setNotification_lrc(String tickerText, String title, String content,
				int drawable) {
			//图片 图片后面的字  时间
			Notification notification = new Notification(drawable, 
					tickerText,System.currentTimeMillis());
			//定义选中后的跳转
			PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
					new Intent(getApplicationContext(), MainActivity.class), 0);

			notification.setLatestEventInfo(getApplicationContext(), title, content, contentIntent);
			//加载到notification管理器中  
			mNotificationManager.notify(R.layout.main, notification);
		}
		
		private void setNotification_mp3(String tickerText, String title, String content,
				int drawable) {
			//图片 图片后面的字  时间
			Notification notification = new Notification(drawable, 
					tickerText,System.currentTimeMillis());
			//定义选中后的跳转
			PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
					new Intent(getApplicationContext(), MainActivity.class), 0);

			notification.setLatestEventInfo(getApplicationContext(), title, content, contentIntent);
			//加载到notification管理器中  
			mNotificationManager.notify(R.layout.local_sys_list, notification);
		}
	}
	
}
