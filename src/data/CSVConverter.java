package data;

import java.io.*;
import java.sql.Timestamp;

public class CSVConverter {

	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private FileReader fileReader;
	private FileWriter fileWriter;
	private File inputFile, outputFile;
	private Timestamp prevTimestamp, curTimestamp;
	private int prevUserID, curUserID, sessionID, counter;
	private long timeGap, curEpoc, mins;	
	private static final String INPUT_PATH = "/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/user-ct-test-collection-01.txt";
	private static final String OUTPUT_PATH = "/home/pacman/Documents/UniR/MEI/WS1415/SearchlogAnalyse/AOL-user-ct-collection/user-ct-test-collection-01.csv";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new CSVConverter();

	}
	
	private CSVConverter() throws IOException {
		inputFile = new File(INPUT_PATH);
		outputFile = new File(OUTPUT_PATH);
		fileReader = new FileReader(inputFile);
		fileWriter = new FileWriter(outputFile);
		bufferedReader = new BufferedReader(fileReader);
		bufferedWriter = new BufferedWriter(fileWriter);
		
		readFile(bufferedReader);
		bufferedWriter.close();
	}
	
	private void readFile(BufferedReader reader) throws IOException {
		while (reader.readLine() != null) {
			String input = reader.readLine();
			if (counter > 0) {
//			if (counter > 0 && counter < 100) {
				//System.out.println(str);
				String output = processLine(input);
				writeLine(output);
			}
			counter++;
		}
		reader.close();
	}
	
	private void writeLine(String output) throws IOException {
		bufferedWriter.write(output);
		bufferedWriter.newLine();
		
	}

	private String processLine(String line) {
		prevUserID = curUserID;
		String[] data = line.split("\t");
		curUserID = Integer.parseInt(data[0]);
		String query = data[1];
		String queryTime = data[2];
		if (data.length == 5) {
			String itemRank = data[3];
			String clickURL = data[4];
		}
		convertTimeStamp(queryTime);		
		if (curUserID != prevUserID) {
			timeGap = 0;
			mins = 0;
			sessionID++;
		} else {
			boolean longGap = compareTimeStamps();
			mins = timeGap/1000/60;
			if (longGap){
				timeGap = 0;
				mins = 0;
				sessionID++;			
			}
		}
		String processedLine = sessionID + ","
				+ curUserID + "," 
				+ query + ","
				+ queryTime + ","
				+ curTimestamp + ","
				+ timeGap + ","
				//+ mins + ","
				+ curEpoc;
		/*
		 * System.out.println(processedLine);
		 * */
		return processedLine;
	}
	
	private boolean compareTimeStamps() {
		if (prevTimestamp == null) {
			timeGap = 0;
			//System.out.println("timeGap " + timeGap + " mins " + timeGap);
			return false;
		}
		
		long prevEpoc = prevTimestamp.getTime();
		timeGap = curEpoc - prevEpoc;
		long mins = timeGap/1000/60;
		//System.out.println("timeGap " + timeGap + " mins " + mins);
		if (mins >= 30) {
			return true;
		} else {
			return false;
		}
	}

	private void convertTimeStamp(String time) {
		prevTimestamp = curTimestamp;
//		//System.out.println(time);
//		int year = Integer.parseInt(time.substring(0, 4));
//		int month = Integer.parseInt(time.substring(5, 7));
//		int date = Integer.parseInt(time.substring(8, 10));
//		int hour = Integer.parseInt(time.substring(11, 13));
//		int minute = Integer.parseInt(time.substring(14, 16));
//		int second = Integer.parseInt(time.substring(17, 19));
//		int nano = 0;
//		//System.out.println(year + ";" + month + ";" + date 
//		+ ";" + hour + ";" + minute + ";" + second);
		
		curTimestamp = Timestamp.valueOf(time);
		curEpoc = curTimestamp.getTime();
	}
	

}
