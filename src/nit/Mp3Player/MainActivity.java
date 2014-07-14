package nit.Mp3Player;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabHost = getTabHost();
		Intent remoteIntent = new Intent();
		remoteIntent.setClass(this,Mp3ListActivity.class);
		TabHost.TabSpec remoteSpec = tabHost.newTabSpec("Remote");
		Resources res = getResources();
		remoteSpec.setIndicator("Remote", res.getDrawable(R.drawable.remote));
		remoteSpec.setContent(remoteIntent);
		tabHost.addTab(remoteSpec);
		
		Intent localIntent = new Intent();
		localIntent.setClass(this,localMp3ListActivity.class);
		TabHost.TabSpec localSpec = tabHost.newTabSpec("Local");
		localSpec.setIndicator("Local", res.getDrawable(R.drawable.local));
		localSpec.setContent(localIntent);
		tabHost.addTab(localSpec);
	}

}
