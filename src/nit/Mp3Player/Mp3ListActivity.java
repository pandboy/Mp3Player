package nit.Mp3Player;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import nit.Mp3Player.service.DownloadService;
import nit.model.Mp3Info;
import nit.xml.Mp3ListContentHendler;

import nit.Download.HttpDownloader;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Mp3ListActivity extends ListActivity {
	private static final int UPDATE = 1;
	private static final int ABOUT = 2;
	private List<Mp3Info> mp3Infos = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_mp3_list);
        updateListView();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, UPDATE, 1, R.string.mp3list_update);
		menu.add(0, ABOUT, 2, R.string.mp3list_about);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == UPDATE)
		{
			updateListView();
		}
		else if(item.getItemId() == ABOUT)
		{
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	private String downloadXML(String UrlStr)
	{
		HttpDownloader httpDownloader = new HttpDownloader();
		String result = httpDownloader.downStr(UrlStr);
		return result;
	}
	
	private List<Mp3Info> parse(String xmlStr){
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<Mp3Info> infos = new ArrayList<Mp3Info>();
		try{
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			Mp3ListContentHendler mp3ListContentHendler = new Mp3ListContentHendler(infos);
			xmlReader.setContentHandler(mp3ListContentHendler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			for(Iterator iterator = infos.iterator();iterator.hasNext();){
				Mp3Info mp3Info = (Mp3Info)iterator.next();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	return infos;
	}
	
	private void updateListView(){
		String xml = downloadXML("http://10.80.129.80:8080/nitxfl/resources.xml");
	    mp3Infos = parse(xml);
		SimpleAdapter simpleAdapter = buildSimpleAdapter(mp3Infos);
		setListAdapter(simpleAdapter);
	}
	
	private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3Infos){
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			HashMap<String, String> map =new HashMap<String, String>();
			map.put("mp3_name", mp3Info.getMp3Name());
			map.put("mp3_size", mp3Info.getMp3Size());
			list.add(map);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.mp3info_item, new String[]{"mp3_name","mp3_size"}, new int[]{R.id.mp3_name,R.id.mp3_size});
		
		return simpleAdapter;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Mp3Info mp3Info = mp3Infos.get(position);
		Intent intent = new Intent();
		intent.putExtra("mp3Info", mp3Info);
		intent.setClass(this,DownloadService.class);
		startService(intent);
		super.onListItemClick(l, v, position, id);
	}
	
	
}