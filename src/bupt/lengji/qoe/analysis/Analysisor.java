package bupt.lengji.qoe.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import qoe.ChartAttribute;

public class Analysisor {

	public static void main(String[] args) {
		File file = new File("E://QoEData/QoEData.log");
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			ArrayList<Record> recordList = new ArrayList<Record>();
			String aRecord;
			while((aRecord = br.readLine())!=null){
				recordList.add(new Record(aRecord));
			}
			
			System.out.println(recordList);
			//ChartAttribute
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
