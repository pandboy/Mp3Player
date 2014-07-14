package nit.lrc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.integer;
import android.widget.Toast;

public class LrcProcessor {

	public ArrayList<Queue> process(InputStream inputStream){
		Queue<Long> timeMills = new LinkedList<Long>();
		Queue<String> messages = new LinkedList<String>();
		ArrayList<Queue> queues = new ArrayList<Queue>();
		try{
			InputStreamReader inputReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputReader);
			String temp = null;
			int i = 0;
			Pattern p = Pattern.compile("\\[([^\\]]+)\\]");
			System.out.println("p-------->" + p);
			String result = null;
			boolean b = true;
			while((temp = bufferedReader.readLine()) != null){
				i++;
				Matcher m = p.matcher(temp);
				System.out.println("m----->" + m);
				if(m.find()){
					if(result != null){
						messages.add(result);
					}
					String timeStr = m.group();
					System.out.println("m---timeStr----->" + timeStr);
					
					
					
					Long timeMill = time2Long(timeStr.substring(1, timeStr.length() - 1));
					if(b){
						timeMills.offer(timeMill);
					}
					String msg = temp.substring(10);
					result = "" + msg + "\n";
				}
				else{
					result = result + temp + "\n";
				}
			}
			messages.add(result);
			queues.add(timeMills);
			queues.add(messages);
		}catch(Exception e){
			e.printStackTrace();
		}
		return queues;
	}
	public Long time2Long(String timeStr){
		System.out.println("timeStr-------->" + timeStr);
		String s[] = timeStr.split(":");
		System.out.println("s[0]---------->" + s[0]);
		int min = Integer.parseInt(s[0]);
		String ss[] = s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);
		
		return min * 60 *1000 +sec * 1000 + mill * 10L;
	}
}
