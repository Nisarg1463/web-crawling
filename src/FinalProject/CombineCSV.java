package FinalProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CombineCSV {
	
	public static List<String[]> readCSV(String filePath, String dlim) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split by commas
                String[] values = line.split(dlim);
                data.add(values);
            }
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
        }
        return data;
    }
	
	public static void writeCSV(String filePath, List<String[]> data) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : data) {
            	writer.append("\"");
                writer.append(String.join("\",\"", row));
                writer.append("\"");
                writer.append("\n");
            }
            
        } catch (IOException e) {
            System.out.println("Error writing the CSV file: " + e.getMessage());
        }
    }
	
	public static void combine() {
		List<String[]> lines = new ArrayList<String[]>();
		String[] titles = {"Name", "Price", "Features"};
		lines.add(titles);
		List<String[]> data = readCSV("Garmin.csv", "\",\"");
		for(String[] line: data.subList(1, data.size())) {
			String features = "";
			for(int i = 2;i<line.length;i++) {
				features = features+line[i]+" ";
			}
			String[] temp = {line[0], line[1], features};			
			lines.add(temp);
		}
		
		data = readCSV("GoNoise.csv", ",");
		for(String[] line: data.subList(1, data.size())) {
			String features = "";
			for(int i = 2;i<line.length;i++) {
				features = features+line[i]+" ";
			}
			String[] temp = {line[0], line[1], features};
			lines.add(temp);
		}

		data = readCSV("Samsung_Smart_watches.csv", ",");
		for(String[] line: data.subList(1, data.size())) {
			String features = "";
			for(int i = 2;i<line.length;i++) {
				features = features+line[i]+" ";
			}
			String[] temp = {line[0], line[1], features};
			lines.add(temp);
		}

		data = readCSV("Suunto_Watches_Data.csv", ",");
		for(String[] line: data.subList(1, data.size())) {
			String features = "";
			for(int i = 2;i<line.length;i++) {
				features = features+line[i]+" ";
			}
			String[] temp = {line[0], line[1], features};
			lines.add(temp);
		}

		data = readCSV("smartwatchDetails.csv", ",");
		for(String[] line: data.subList(1, data.size())) {
			String features = "";
			for(int i = 2;i<line.length;i++) {
				features = features+line[i]+" ";
			}
			String[] temp = {line[0], line[1], features};
			lines.add(temp);
		}
		
		writeCSV("Output.csv", lines);
	}
}
