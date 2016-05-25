package bupt.lengji.qoe.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Wrapper {
	public static final int UHD = 1;
	public static final int HD = 2;
	public static final int SD = 3;
	public static final int IMG = 4;
	private String SourcePath = null; // 包含fileName
	private String TargetPath = null;
	private String SourceExte = null;
	private String TargetExte = null;
	private int Quality = UHD;

	public Wrapper(String sPath, String tPath, int quality) {
		super();
		Quality = quality;
		SourcePath = sPath;
		if (sPath != null && sPath.length() > 0) {
			int dot = sPath.lastIndexOf('.');
			if (dot > -1 && dot < (sPath.length() - 1)) {
				SourceExte = sPath.substring(dot + 1).toLowerCase();
			}
		}

		TargetPath = tPath;
		if (tPath != null && tPath.length() > 0) {
			File td = new File(tPath).getParentFile();
			if (!td.exists()) {
				td.mkdirs();
			}

			int dot = tPath.lastIndexOf('.');
			if (dot > -1 && dot < (tPath.length() - 1)) {
				TargetExte = tPath.substring(dot + 1).toLowerCase();
			}
		}
	}

	/* 源视频字段 */
	public List<String> getSCode() {
		List<String> commend = new ArrayList<String>();
		commend.add("ffmpeg");
		commend.add("-i");
		commend.add(SourcePath);
		switch (SourceExte) {
		case "mp4":
			if (TargetExte != "mp4") {
				commend.add("-bsf:v");
				commend.add("h264_mp4toannexb");
			}
			break;
		default:
			break;
		}
		return commend;
	}

	/* 分辨率字段 */
	public List<String> getQualityCode() {
		List<String> commend = new ArrayList<String>();
		switch (Quality) {
		case UHD:
			//commend.add("-vf");
			//commend.add("scale=-1:720");
			break;
		case HD:
			commend.add("-vf");
			commend.add("scale=-1:360");
			break;
		case SD:
			commend.add("-vf");
			commend.add("scale=-1:180");
			break;
		default:
			commend.add("-codec");
			commend.add("copy");
			break;
		}
		return commend;
	}

	/* 是否复制源视频，可加快速度，但是有可能转码失败。 */
	public List<String> getCopyCode() {
		List<String> commend = new ArrayList<String>();
		return commend;
	}

	/* 目的视频字段 */
	public List<String> getTCode() {
		List<String> commend = new ArrayList<String>();
		switch (TargetExte) {
		case "m3u8":
			commend.add("-f");
			commend.add("hls");
			// commend.add("-hls_time");
			// commend.add("5"); // 默认为2s
			commend.add("-hls_list_size");
			commend.add("0");
			commend.add("-hls_wrap");
			commend.add("0");
			break;
		default:
			break;
		}
		commend.add(TargetPath);
		return commend;
	}

	public List<String> TransCode() {
		List<String> commend = new ArrayList<String>();
		commend.addAll(getSCode());
		commend.addAll(getQualityCode());
		commend.addAll(getTCode());
		return commend;
	}

	public boolean execute(List<String> commend) {
		try {
			ProcessBuilder builder = new ProcessBuilder();
			Process process = builder.command(commend).redirectErrorStream(true).start();
			new PrintStream(process.getErrorStream()).start();
			new PrintStream(process.getInputStream()).start();
			process.waitFor();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public List<String> ScreenShotCode() {
		List<String> commend = new ArrayList<String>();
		commend.add("ffmpeg");
		commend.add("-i");
		commend.add(SourcePath);
		commend.add("-f");
		commend.add("image2");
		commend.add("-ss");
		commend.add("15");
		commend.add("-t");
		commend.add("0.01");
		commend.add("-vf");
		commend.add("scale=-1:270");
		commend.add(TargetPath);
		return commend;
	}

	public String getSourcePath() {
		return SourcePath;
	}

	public void setSourcePath(String sourcePath) {
		SourcePath = sourcePath;
	}

	public String getTargetPath() {
		return TargetPath;
	}

	public void setTargetPath(String targetPath) {
		TargetPath = targetPath;
	}

	public String getSourceExte() {
		return SourceExte;
	}

	public void setSourceExte(String sourceExte) {
		SourceExte = sourceExte;
	}

	public String getTargetExte() {
		return TargetExte;
	}

	public void setTargetExte(String targetExte) {
		TargetExte = targetExte;
	}

	class PrintStream extends Thread {
		java.io.InputStream __is = null;

		public PrintStream(java.io.InputStream is) {
			__is = is;
		}

		public void run() {
			try {
				while (this != null) {
					int _ch = __is.read();
					if (_ch != -1)
						System.out.print((char) _ch);
					else
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
