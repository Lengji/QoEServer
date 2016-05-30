package bupt.lengji.qoe.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Analysisor {

	public static void main(String[] args) {
		ArrayList<Record> recordList = new ArrayList<Record>();
		File file = new File("E://QoEData/QoEData.log");
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String recordString;
			while ((recordString = br.readLine()) != null) {
				if(recordString.equals(null)||recordString.equals("")){
					continue;
				}
				recordList.add(new Record(recordString));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		WashData(recordList);
		System.out.println(recordList.size());

		stuckPct(recordList);
		//seekRight(recordList);
		//seekLeft(recordList);
		//lastStuck(recordList);
		//activeWatching(recordList);
		//batteryInfo(recordList);
		//fullScInfo(recordList);
		//resolutionInfo(recordList);
		//SDInfo(recordList);
		//HDInfo(recordList);
		//UHDInfo(recordList);
		
		ratingCount(recordList);
		user_videoCount(recordList);
		video_userCount(recordList);
	}




	/*
	 * 清除评分为0（未评分）的记录
	 * */
	public static void WashData(ArrayList<Record> recordList) {
		Iterator <Record> listIterator = recordList.iterator();
		while(listIterator.hasNext()){
			Record record = listIterator.next();

			if(record.getRating() == 0||record.getDuration() == 0 || record.getTotalTime() == 0){
				listIterator.remove();
			}
			
		}
	}
	
	public static void SDInfo(ArrayList<Record> recordList){
		for(Record r:recordList){
			System.out.println(r.getSDTimes()+"---------------"+r.getSDPct()+"--------------"+r.getRating());
		}
	}
	
	public static void HDInfo(ArrayList<Record> recordList){
		for(Record r:recordList){
			System.out.println(r.getHDTimes()+"---------------"+r.getHDPct()+"--------------"+r.getRating());
		}
	}	
	
	public static void UHDInfo(ArrayList<Record> recordList){
		for(Record r:recordList){
			System.out.println(r.getUHDTimes()+"---------------"+r.getUHDPct()+"--------------"+r.getRating());
		}
	}
	
	public static void resolutionInfo(ArrayList<Record> recordList) {
		for(Record r:recordList){
			ArrayList<Integer> changes = r.getChangeResolution();
			System.out.println(changes.size()+"---------------"+changes+"--------------"+r.getRating());
		}
	}
	
	public static void batteryInfo(ArrayList<Record> recordList) {
		for(Record r:recordList){
			System.out.println(r.getBatteryPct()+"---------------"+r.isCharging()+"--------------"+r.getRating());
		}
	}
	
	public static void stuckPct(ArrayList<Record> recordList){
		for(Record r:recordList){
			System.out.println(r.getStuckTimes()+"---------------"+r.getStuckPct()+"--------------"+r.getRating());
		}
	}
	
	public static void lastStuck(ArrayList<Record> recordList){
		for(Record r:recordList){
			System.out.println(r.getLastStuck()[0]+"-------------------"+r.getLastStuck()[1]+"--------------"+r.getRating());
		}
	}
	
	public static void seekRight(ArrayList<Record> recordList){
		for(Record r:recordList){
			System.out.println(r.getSeekRightTimes()+"---------------"+r.getSeekRightPct()+"--------------"+r.getRating());
		}
	}
	
	public static void seekLeft(ArrayList<Record> recordList){
		for(Record r:recordList){
			System.out.println(r.getSeekLeftTimes()+"---------------"+r.getSeekLeftPct()+"--------------"+r.getRating());
		}
	}
	
	public static void activeWatching(ArrayList<Record> recordList){
		for(Record r:recordList){
			System.out.println(r.getActiveDuration()+"---------------"+r.getActivePct()+"--------------"+r.getRating());
		}
	}
	
	public static void fullScInfo(ArrayList<Record> recordList){
		for(Record r:recordList){
			System.out.println(r.getFullScTimes()+"---------------"+r.getFullScPct()+"--------------"+r.getRating());
		}
	}
	
	public static void ratingCount(ArrayList<Record> recordList) {
		HashMap<Integer, Integer> ratingCount = new HashMap<>();
		for (Record r : recordList) {
			int rating = r.getRating();
			if (ratingCount.containsKey(rating)) {
				ratingCount.put(rating, ratingCount.get(rating) + 1);
			} else {
				ratingCount.put(rating, 1);
			}
		}
		System.out.println("-----------------评分分布------------------");
		for (Integer rating : ratingCount.keySet()) {
			System.out.println(rating + "\t:" + ratingCount.get(rating));
		}
	}

	public static void user_videoCount(ArrayList<Record> recordList) {
		HashMap<String, Integer> userCount = new HashMap<>();
		for (Record r : recordList) {
			String user = r.getUser();
			if (userCount.containsKey(user)) {
				userCount.put(user, userCount.get(user) + 1);
			} else {
				userCount.put(user, 1);
			}
		}
		System.out.println("-----------------用户观看视频数------------------");
		System.out.println(userCount.size());
		for (String user : userCount.keySet()) {
			System.out.println(user + "\t:" + userCount.get(user));
		}
	}

	public static void video_userCount(ArrayList<Record> recordList) {
		HashMap<String, Integer> videoCount = new HashMap<>();
		for (Record r : recordList) {
			String video = r.getVideoID();
			if (videoCount.containsKey(video)) {
				videoCount.put(video, videoCount.get(video) + 1);
			} else {
				videoCount.put(video, 1);
			}
		}
		System.out.println("-----------------观看视频的用户数------------------");
		System.out.println(videoCount.size());
		for (String video : videoCount.keySet()) {
			System.out.println(video + "\t:" + videoCount.get(video));
		}
	}

}
