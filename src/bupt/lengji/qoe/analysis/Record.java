package bupt.lengji.qoe.analysis;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Record {
	private String user = null;
	private String videoID = null;
	private long duration = 0;
	private long totalTime = 0;
	private int rating = 0;
	private boolean isCharging = false;
	private int batteryPct = 0;
	private ArrayList<WatchEvent> eventList = null;

	public Record(String jsonString) {
		JSONObject jsonObject = new JSONObject(jsonString);
		user = jsonObject.getString("user");
		videoID = jsonObject.getString("video");
		duration = jsonObject.getLong("duration");
		totalTime = jsonObject.getLong("totalTime");
		rating = jsonObject.getInt("rating");
		isCharging = jsonObject.getBoolean("isCharging");
		batteryPct = jsonObject.getInt("batteryPct");
		JSONArray jsEventArray = jsonObject.getJSONArray("events");
		eventList = new ArrayList<WatchEvent>();
		for (Object jsObj : jsEventArray) {
			eventList.add(new WatchEvent((JSONObject) jsObj));
		}
	}

	public Record(String user, String videoID, long duration, long totalTime,
			int rating, boolean isCharging, int batteryPct,
			ArrayList<WatchEvent> eventList) {
		super();
		this.user = user;
		this.videoID = videoID;
		this.duration = duration;
		this.totalTime = totalTime;
		this.rating = rating;
		this.isCharging = isCharging;
		this.batteryPct = batteryPct;
		this.eventList = eventList;
	}
	
	@Override
	public String toString() {
		return "Record [user=" + user + ", videoID=" + videoID + ", duration="
				+ duration + ", totalTime=" + totalTime + ", rating=" + rating
				+ ", isCharging=" + isCharging + ", batteryPct=" + batteryPct
				+ ", eventList=" + eventList + "]";
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getVideoID() {
		return videoID;
	}

	public void setVideoID(String videoID) {
		this.videoID = videoID;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean isCharging() {
		return isCharging;
	}

	public void setCharging(boolean isCharging) {
		this.isCharging = isCharging;
	}

	public int getBatteryPct() {
		return batteryPct;
	}

	public void setBatteryPct(int batteryPct) {
		this.batteryPct = batteryPct;
	}

	public ArrayList<WatchEvent> getEventList() {
		return eventList;
	}

	public void setEventList(ArrayList<WatchEvent> eventList) {
		this.eventList = eventList;
	}

}
