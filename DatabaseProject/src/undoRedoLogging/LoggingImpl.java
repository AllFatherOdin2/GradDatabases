/**
 * 
 */
package undoRedoLogging;

/**
 * @author DModica
 * @author JBosworth
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import util.ByteStringManipulator;
import ProjectCode.QueryExecException;
import ProjectCode.QueryExecImpl;


public class LoggingImpl {
	private static final String direct = System.getProperty("user.dir") + "\\";
	private static List<List<String>> cities = new ArrayList<List<String>>();
	private static List<List<String>> countries = new ArrayList<List<String>>();
	private static List<String> results = new ArrayList<String>();
	private static List<String> log = new ArrayList<String>();
	
	private static double queryTime = 0;
	
	/**
	 * 
	 * @return
	 * @throws LoggingException
	 */
	public List<String> open() throws LoggingException{
		final String INPUT_FILE_CITY = direct + "city.csv";
		final String INPUT_FILE_COUNTRY = direct + "country.csv";
		final String LOG_FILE = direct + "log.txt";
		
		byte[] cityBytes = null;
		try (InputStream inputStream = new FileInputStream(INPUT_FILE_CITY)){
			cityBytes = new byte[inputStream.available()];
			inputStream.read(cityBytes);
			cities = ByteStringManipulator.convertByteArrayto2DList(cityBytes);

			if(cities.size() == 0){
				throw new LoggingException("That Data table is empty");
			}
			
		} catch(IOException e){
			throw new LoggingException("No city CSV could be found");
		}
		
		byte[] countryBytes = null;
		try (InputStream inputStream = new FileInputStream(INPUT_FILE_COUNTRY)){
			countryBytes = new byte[inputStream.available()];
			inputStream.read(countryBytes);
			countries = ByteStringManipulator.convertByteArrayto2DList(countryBytes);

			if (countries.size() == 0){
				throw new LoggingException("That Data table is empty");
			}
			
		} catch(IOException e){
			throw new LoggingException("No city CSV could be found");
		}


		double startTime = System.nanoTime();
		List<String> result = getNext();
		double endTime = System.nanoTime();
		setQueryTime((endTime-startTime)/1000000000);
		
		return result;
	}
	
	
	//TODO: read one in, write to log file. "read in 'tuple id from file' population is 'old Pop' at 'Time'","writing to 'file' 'tuple id', population to 'new'", "Commit 'file'"
	private List<String> getNext() throws LoggingException{
		Date date = new Date();
		for(List<String> city : cities){
			//String countryCode = city.get(2);
			//Read in tuple
			int cityPop = Integer.parseInt(city.get(4));
			log.add("READ " + city.get(1) + " FROM city, population: " + city.get(4) + ",TIME: " + date.getTime());
			
			//Modify tuple
			cityPop += .2*cityPop;
			
			//write out tuple
			city.set(4, ""+cityPop);
			log.add("WRITE " + city.get(1) + "FROM city, population: " + city.get(4) + ",TIME: " + date.getTime());
		}
		//write back to CSV file
		//commit and push to log file
		for (List<String> country : countries){
			/*String code = country.get(0);
				if (countryCode.compareTo(code) == 0){
					
					int countryPop = Integer.parseInt(country.get(6));
					int cityPop = Integer.parseInt(city.get(4));
					if(cityPop > .4 * countryPop){
						results.add(city.get(1));
				}
			}*/
			int countryPop = Integer.parseInt(country.get(6));
			log.add("READ " + country.get(1) + "FROM country, population: " + country.get(6) + ",TIME: " + date.getTime());
			
			countryPop += .2*countryPop;
			
			country.set(6, ""+countryPop);
			log.add("WRITE " + country.get(1) + "FROM country, population: " + country.get(6) + ",TIME: " + date.getTime());
		}
		//write to log file
		
		if(results.size() == 0){
			throw new LoggingException("No matches were found.");
		}
		return close();
	}
	
	private List<String> close(){
		/*
		System.out.println(results.size());
		for(String city : results){
			System.out.println(city);
		}
		*/
		List<String> tempResults = results;
		cities = null;
		countries = null;
		results = null;
		return tempResults;
		
	}
	
	public double getQueryTime() {
		return queryTime;
	}

	public static void setQueryTime(double queryTime) {
		LoggingImpl.queryTime = queryTime;
	}
}
