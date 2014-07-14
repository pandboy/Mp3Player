package nit.xml;

import java.lang.annotation.Target;
import java.util.List;

import nit.model.Mp3Info;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.text.Html.TagHandler;

public class Mp3ListContentHendler extends DefaultHandler{
	private List<Mp3Info> infos = null;
	private Mp3Info mp3Info = null;
	private String tagName = null;
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		String temp = new String(ch,start,length);
		if(tagName.equals("id")){
			mp3Info.setId(temp);
		}
		else if(tagName.equals("mp3.name")){
			mp3Info.setMp3Name(temp);
		}
		else if(tagName.equals("mp3.size")){
			mp3Info.setMp3Size(temp);
		}
		else if(tagName.equals("lrc.name")){
			mp3Info.setLrcName(temp);
		}
		else if(tagName.equals("lrc.size")){
			mp3Info.setLrcSize(temp);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals("resource")){
			infos.add(mp3Info);
		}
		tagName = "";
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		this.tagName = localName;
		if(tagName.equals("resource")){
			mp3Info = new Mp3Info();
		}
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	public List<Mp3Info> getInfos() {
		return infos;
	}

	public void setInfos(List<Mp3Info> infos) {
		this.infos = infos;
	}

	public Mp3ListContentHendler(List<Mp3Info> infos) {
		super();
		this.infos = infos;
	}

}
