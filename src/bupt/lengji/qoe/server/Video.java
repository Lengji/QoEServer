package bupt.lengji.qoe.server;

public class Video {

	private String id = null;
	private int type = 0;
	private String Title = null;
	private String description = null;
	boolean uhd = false;
	boolean hd = false;
	boolean sd = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public static String getTypeString(int type) {
		switch (type) {
		case 0:
			return "Movie";
		case 1:
			return "Episode";
		case 2:
			return "Music";
		case 3:
			return "Cartoon";
		case 4:
			return "Sport";
		case 5:
			return "Entertainment";
		default:
			return "Other";
		}
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isUhd() {
		return uhd;
	}

	public void setUhd(boolean uhd) {
		this.uhd = uhd;
	}

	public boolean isHd() {
		return hd;
	}

	public void setHd(boolean hd) {
		this.hd = hd;
	}

	public boolean isSd() {
		return sd;
	}

	public void setSd(boolean sd) {
		this.sd = sd;
	}

	public Video(String id, int type, String title, String description, boolean uhd, boolean hd, boolean sd) {
		super();
		this.id = id;
		this.type = type;
		Title = title;
		this.description = description;
		this.uhd = uhd;
		this.hd = hd;
		this.sd = sd;
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", type=" + type + ", Title=" + Title + ", description=" + description + ", uhd=" + uhd + ", hd=" + hd + ", sd=" + sd + "]";
	}

}
