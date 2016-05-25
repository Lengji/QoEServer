package bupt.lengji.qoe.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RecordWriter {
	private File file = null;
	private String record = null;

	public RecordWriter(String fileName, String record) {
		super();
		file = new File(fileName);
		this.record = record;
	}

	public RecordWriter(File file, String record) {
		super();
		this.file = file;
		this.record = record;
	}

	public boolean execute() {
		if (file == null) {
			return false;
		}
		try {
			if (!file.exists()) {
				File parent = file.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file, true);
			writer.write(record + "\r\n");
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
