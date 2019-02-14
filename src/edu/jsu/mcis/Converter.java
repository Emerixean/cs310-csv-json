package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {

    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            ArrayList<String> jsonColHeaders = new ArrayList<>();
            ArrayList<String> jsonRowHeaders = new ArrayList<>();
            ArrayList<ArrayList<Integer>> data = new ArrayList<>();
            JSONObject json = new JSONObject();

            String[] colHeaders = full.get(0);

            for (String s: colHeaders){
                jsonColHeaders.add(s);
            }

            for(int i = 1; i < full.size(); ++i){
                String[] row = full.get(i);
                jsonRowHeaders.add(row[0]);
                ArrayList<Integer> dataRow = new ArrayList<>();
                for(int j = 1; j < row.length; ++j){
                    dataRow.add(Integer.parseInt(row[j]));
                }
                data.add(new ArrayList(dataRow));
            }

            json.put("colHeaders", jsonColHeaders);
            json.put("data", data);
            json.put("rowHeaders", jsonRowHeaders);

            results = json.toJSONString();

        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(jsonString);

            JSONArray colHeaders = (JSONArray)json.get("colHeaders");
            JSONArray rowHeaders = (JSONArray)json.get("rowHeaders");
            JSONArray data = (JSONArray)json.get("data");

            String[] colArray = new String[colHeaders.size()];
            String[] rowArray = new String[rowHeaders.size()];
            String[] dataArray = new String[data.size()];

            for(int i = 0; i < colHeaders.size(); i++){
                colArray[i] = colHeaders.get(i).toString();
            }

            csvWriter.writeNext(colArray);

            for(int i = 0; i < rowHeaders.size(); i++){
                rowArray[i] = rowHeaders.get(i).toString();
                dataArray[i] = data.get(i).toString();
            }

            for(int i = 0; i < dataArray.length; i++){
                JSONArray dataValues = (JSONArray)parser.parse(dataArray[i]);
                String[] row = new String[dataValues.size() + 1];
                row[0] = rowArray[i];
                for(int j = 0; j < dataValues.size(); j++){
                    row[j + 1] = dataValues.get(j).toString();
                }
                csvWriter.writeNext(row);
            }

            results = writer.toString();
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}