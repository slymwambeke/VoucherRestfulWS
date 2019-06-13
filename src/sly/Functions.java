package sly;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Functions {
	public static void writeToJSONFile(String fileName, String message) throws IOException, NullPointerException  {
		
		System.out.println(message);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
 		//get current date time with Date()
 		Date date = new Date();
 		
 		DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddhhmmss");
 		//get current date time with Date()
 		Date date1 = new Date();
 		
 		String directoryName = System.getProperty("user.dir")+"\\logs\\";
		
 		File directory = new File(String.valueOf(directoryName));

 		if(!directory.exists()){

 			directory.mkdir();
 		}
		
 		File log = new File(directoryName+dateFormat1.format(date1)+"_"+fileName);
 		 		
 	    BufferedWriter bw = null;
 		
		if(!log.exists()){		        
			log.createNewFile();
	    }
		bw = new BufferedWriter(new FileWriter(log, true)); 	
		
		bw.append(message);
		
		bw.close();
 			
	}
	
	public static void writeToFile(String fileName, String message) throws IOException, NullPointerException  {
				
		System.out.println(message);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
 		//get current date time with Date()
 		Date date = new Date();
 		
 		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
 		//get current date time with Date()
 		Date date1 = new Date();
 		
 		String directoryName = System.getProperty("user.dir")+"\\logs\\";
		
 		File directory = new File(String.valueOf(directoryName));

 		if(!directory.exists()){

 			directory.mkdir();
 		}
		
 		File log = new File(directoryName+fileName);
 		 		
 	    BufferedWriter bw = null;
 		
		if(!log.exists()){		        
			log.createNewFile();
	    }
		bw = new BufferedWriter(new FileWriter(log, true)); 	
		
		bw.append(dateFormat1.format(date1)+" - "+message+"\n");
		
		bw.close();
 			
	}
}
