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

	public int getStuckTimes() {
		int times = 0;
		for (WatchEvent w : eventList) {
			if (w.getEvent() == WatchEvent.STUCK) {
				times++;
			}
		}
		return times;
	}

	public long getStuckDuration() {
		long duration = 0;
		for (WatchEvent w : eventList) {
			if (w.getEvent() == WatchEvent.STUCK) {
				duration += w.getDuration();
			}
		}
		return duration;
	}

	public int getStuckPct() {
		return (int) (100 * getStuckDuration() / totalTime);
	}

	public long[] getLastStuck() {
		long lastStuckPosition = 0;
		long lastStuctDuration = 0;
		for (int i = eventList.size() - 1; i >= 0; i--) {
			WatchEvent event = eventList.get(i);
			if (event.getEvent() == WatchEvent.STUCK) {
				lastStuckPosition = event.getPosition();
				lastStuctDuration = event.getDuration();
				break;
			}
		}
		long result[] = { lastStuckPosition * 100 / totalTime,
				lastStuctDuration };
		return result;
	}

	public int getSeekRightTimes() {
		int times = 0;
		for (WatchEvent w : eventList) {
			if (w.getEvent() == WatchEvent.SEEK_RIGHT) {
				times++;
			}
		}
		return times;
	}

	public long getSeekRightDuration() {
		long duration = 0;
		for (WatchEvent w : eventList) {
			if (w.getEvent() == WatchEvent.SEEK_RIGHT) {
				duration += w.getDuration();
			}
		}
		return duration;
	}

	public int getSeekRightPct() {
		return (int) (getSeekRightDuration() * 100 / duration);
	}

	public int getSeekLeftTimes() {
		int times = 0;
		for (WatchEvent w : eventList) {
			if (w.getEvent() == WatchEvent.SEEK_LEFT) {
				times++;
			}
		}
		return times;
	}

	public long getSeekLeftDuration() {
		long duration = 0;
		for (WatchEvent w : eventList) {
			if (w.getEvent() == WatchEvent.SEEK_LEFT) {
				duration += w.getDuration();
			}
		}
		return duration;
	}

	public int getSeekLeftPct() {
		return (int) (getSeekLeftDuration() * 100 / duration);
	}

	public long getActiveDuration() {
		long duration = 0;
		int i = 0;
		while (i < eventList.size()) {
			WatchEvent event = eventList.get(i++);
			if (event.getEvent() == WatchEvent.PREPARED) {
				duration = totalTime - event.getPosition();
				break;
			}
		}
		if (duration == 0) {
			return 0;
		}
		while (i < eventList.size()) {
			WatchEvent event = eventList.get(i++);
			switch (event.getEvent()) {
			case WatchEvent.PREPARED:
			case WatchEvent.PAUSE_NORMAL:
			case WatchEvent.PAUSE_STUCK:
			case WatchEvent.STUCK:
				duration -= event.getDuration();
				break;
			}
		}
		return duration;
	}

	public int getActivePct() {
		return (int) (getActiveDuration() * 100 / totalTime);
	}

	public int getFullScTimes() {
		int times = 0;
		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).getEvent() == WatchEvent.FULLSCREEN) {
				times++;
			}
		}
		return times;
	}

	public int getFullScPct() {
		long duration = 0;
		long startPosition = 0;
		for (int i = 0; i < eventList.size(); i++) {
			WatchEvent event = eventList.get(i);
			if (event.getEvent() == WatchEvent.FULLSCREEN) {
				startPosition = event.getPosition();
			} else if (event.getEvent() == WatchEvent.FULLSCREEN_EXIT) {
				duration += (event.getPosition() - startPosition);
				startPosition = 0;
			}
		}
		return (int) (duration * 100 / totalTime);
	}

	public ArrayList<Integer> getChangeResolution() {
		ArrayList<Integer> changes = new ArrayList<>();
		int current = WatchEvent.RESOLUTION_HD;
		for (int i = 0; i < eventList.size(); i++) {
			WatchEvent event = eventList.get(i);
			switch (event.getEvent()) {
			case WatchEvent.RESOLUTION_SD:
			case WatchEvent.RESOLUTION_HD:
			case WatchEvent.RESOLUTION_UHD:
				if (event.getEvent() != current) {
					current = event.getEvent();
					changes.add(current - WatchEvent.RESOLUTION_HD);
				}
				break;
			}
		}
		return changes;
	}

	public int getSDTimes() {
		int times = 0;
		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).getEvent() == WatchEvent.RESOLUTION_SD) {
				times++;
			}
		}
		return times;
	}

	public int getSDPct() {
		long duration = 0;
		long startPosition = 0;
		boolean isSD = false;
		for (int i = 0; i < eventList.size(); i++) {
			WatchEvent event = eventList.get(i);
			if (isSD && (event.getEvent() == WatchEvent.RESOLUTION_HD || event.getEvent() == WatchEvent.RESOLUTION_UHD)) {
				duration += (event.getPosition() - startPosition);
				startPosition = 0;
				isSD = false;
			} else if (!isSD && event.getEvent() == WatchEvent.RESOLUTION_SD) {
				startPosition = event.getPosition();
				isSD = true;
			}
		}
		if (isSD) {
			duration += (totalTime - startPosition);
		}
		return (int) (duration * 100 / totalTime);
	}

	public int getHDTimes() {
		int times = 0;
		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).getEvent() == WatchEvent.RESOLUTION_HD) {
				times++;
			}
		}
		return times;
	}
	public int getHDPct() {
		long duration = 0;
		long startPosition = 0;
		boolean isHD = false;
		for (int i = 0; i < eventList.size(); i++) {
			WatchEvent event = eventList.get(i);
			if (isHD && (event.getEvent() == WatchEvent.RESOLUTION_SD || event.getEvent() == WatchEvent.RESOLUTION_UHD)) {
				duration += (event.getPosition() - startPosition);
				startPosition = 0;
				isHD = false;
			} else if (!isHD && event.getEvent() == WatchEvent.RESOLUTION_HD) {
				startPosition = event.getPosition();
				isHD = true;
			}
		}
		if (isHD) {
			duration += (totalTime - startPosition);
		}
		return (int) (duration * 100 / totalTime);
	}

	public int getUHDTimes() {
		int times = 0;
		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).getEvent() == WatchEvent.RESOLUTION_UHD) {
				times++;
			}
		}
		return times;
	}
	public int getUHDPct() {
		long duration = 0;
		long startPosition = 0;
		boolean isUHD = false;
		for (int i = 0; i < eventList.size(); i++) {
			WatchEvent event = eventList.get(i);
			if (isUHD && (event.getEvent() == WatchEvent.RESOLUTION_SD || event.getEvent() == WatchEvent.RESOLUTION_HD)) {
				duration += (event.getPosition() - startPosition);
				startPosition = 0;
				isUHD = false;
			} else if (!isUHD && event.getEvent() == WatchEvent.RESOLUTION_UHD) {
				startPosition = event.getPosition();
				isUHD = true;
			}
		}
		if (isUHD) {
			duration += (totalTime - startPosition);
		}
		return (int) (duration * 100 / totalTime);
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
