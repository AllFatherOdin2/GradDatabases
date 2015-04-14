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
import java.util.List;

import ProjectCode.QueryExecException;
import ProjectCode.QueryExecImpl;


public class LoggingImpl {
	private static final String direct = System.getProperty("user.dir") + "\\";
	private static List<List<String>> cities = new ArrayList<List<String>>();
	private static List<List<String>> countries = new ArrayList<List<String>>();
	private static List<String> results = new ArrayList<String>();
	
	private static double queryTime = 0;
	
	/**
	 * 
	 * @return
	 * @throws LoggingException
	 */
	public List<String> open() throws LoggingException{
		final String INPUT_FILE_CITY = direct + "city.csv";
		final String INPUT_FILE_COUNTRY = direct + "country.csv";
		
		byte[] cityBytes = null;
		try (InputStream inputStream = new FileInputStream(INPUT_FILE_CITY)){
			cityBytes = new byte[inputStream.available()];
			inputStream.read(cityBytes);
			cities = convertByteArrayto2DList(cityBytes);

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
			countries = convertByteArrayto2DList(countryBytes);

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
		for(List<String> city : cities){
			String countryCode = city.get(2);
			for (List<String> country : countries){
				String code = country.get(0);
				if (countryCode.compareTo(code) == 0){
					
					int countryPop = Integer.parseInt(country.get(6));
					int cityPop = Integer.parseInt(city.get(4));
					if(cityPop > .4 * countryPop){
						results.add(city.get(1));
					}
				}
			}
		}
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
	
	/**
	 * Converts a byte array into a list of lists of strings in CSV format.
	 * 
	 * @param data Input byte array
	 * @return List of tuples, which are lists of strings.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<List<String>> convertByteArrayto2DList(byte[] data) {
		String result = Arrays.toString(data);
		//Will be in array form, so this removes "[" and "]"
		result = result.substring(1, result.length()-1); 
		
		String[] resultArray = result.split(", ");
		
		result = "";
		for(String s : resultArray){
			result += (char)Integer.parseInt(s);
		}
		result = result.replaceAll("\\bNULL\\b","\"NULL\"");

		resultArray = result.split("\n");

		List<List<String>> resultList = new ArrayList<List<String>>();
		for(String s: resultArray){
			s = s.substring(1, s.length()-2); //remove new line character
			String[] columnArray = s.split("\",\"");
			for(int i = 0; i < columnArray.length; i++){
				
				if(columnArray[i].length() > 0){
					columnArray[i] = columnArray[i];
				}
			}
			
			resultList.add(new ArrayList(Arrays.asList(columnArray)));
		}
		
		return resultList;
	}
	
	public double getQueryTime() {
		return queryTime;
	}

	public static void setQueryTime(double queryTime) {
		LoggingImpl.queryTime = queryTime;
	}
}
