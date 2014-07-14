package nit.Mp3Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import nit.Download.FileUtils;
import nit.model.Mp3Info;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class localMp3ListActivity extends ListActivity{

	List<Mp3Info> mp3Infos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_sys_list);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getList();
		
		super.onResume();
	}
	
	public void getList(){
		FileUtils fileUtils = new FileUtils();
		mp3Infos = fileUtils.getMp3Files("mp3/");
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			HashMap<String, String> map =new HashMap<String, String>();
			if(mp3Info.getMp3Name() != null){
				map.put("mp3_name", mp3Info.getMp3Name());
				map.put("mp3_size", mp3Info.getMp3Size());
				list.add(map);
			}
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.mp3info_item, new String[]{"mp3_name","mp3_size"}, new int[]{R.id.mp3_name,R.id.mp3_size});
		setListAdapter(simpleAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		if(mp3Infos != null){
			Mp3Info mp3Info = mp3Infos.get(position);
			Intent intent = new Intent();
			intent.putExtra("mp3Info", mp3Info);
			intent.setClass(this, playerActivity.class);
			startActivity(intent);
		}
	}

	
}
