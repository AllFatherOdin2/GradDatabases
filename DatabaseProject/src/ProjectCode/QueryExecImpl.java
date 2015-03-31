package ProjectCode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryExecImpl {
	private static final String direct = System.getProperty("user.dir") + "\\";
	private static List<List<String>> cities;
	private static List<List<String>> countries;
	private static List<String> results;

	/**
	 * Default constructor
	 */
	public QueryExecImpl() {
		cities = new ArrayList<List<String>>();
		countries = new ArrayList<List<String>>();
		results = new ArrayList<String>();
	}
	
	/**
	 * Opens the two data-table CSV files and reads them into memory
	 * @throws QueryExecException
	 */
	public static void open() throws QueryExecException{
		final String INPUT_FILE_CITY = direct + "city.csv";
		final String INPUT_FILE_COUNTRY = direct + "country.csv";
		
		byte[] cityBytes = null;
		try (InputStream inputStream = new FileInputStream(INPUT_FILE_CITY)){
			cityBytes = new byte[inputStream.available()];
			inputStream.read(cityBytes);
			cities = convertByteArrayto2DList(cityBytes);

			if(cities.size() == 0){
				throw new QueryExecException("That Data table is empty");
			}
			
		} catch(IOException e){
			throw new QueryExecException("No city CSV could be found");
		}
		
		byte[] countryBytes = null;
		try (InputStream inputStream = new FileInputStream(INPUT_FILE_COUNTRY)){
			countryBytes = new byte[inputStream.available()];
			inputStream.read(countryBytes);
			countries = convertByteArrayto2DList(countryBytes);

			if (countries.size() == 0){
				throw new QueryExecException("That Data table is empty");
			}
			
		} catch(IOException e){
			throw new QueryExecException("No city CSV could be found");
		}
		getNext();
	}
	
	/**
	 * Converts a byte array into a list of lists of strings in CSV format.
	 * 
	 * @param data Input byte array
	 * @return List of tuples, which are lists of strings.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<List<String>> convertByteArrayto2DList(byte[] data) {
		String result = Arrays.toString(data);
		//Will be in array form, so this removes "[" and "]"
		result = result.substring(1, result.length()-1); 
		
		String[] resultArray = result.split(", ");
		
		result = "";
		for(String s : resultArray){
			result += (char)Integer.parseInt(s);
		}

		resultArray = result.split("\n");

		List<List<String>> resultList = new ArrayList<List<String>>();
		for(String s: resultArray){
			String[] columnArray = s.split(",");
			for(int i = 0; i < columnArray.length; i++){
				columnArray[i] = columnArray[i].substring(1, columnArray[i].length() - 1);
			}
			
			resultList.add(new ArrayList(Arrays.asList(columnArray)));
		}
		
		return resultList;
	}

	/**
	 * Gets a list of all cities whose population is > .4 * their countries population.
	 * @throws QueryExecException 
	 */
	public static void getNext() throws QueryExecException{
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
			throw new QueryExecException("No matches were found.");
		}
		close();
	}
	
	public static void close(){
		for(String city : results){
			System.out.println(city);
		}
		cities = null;
		countries = null;
		results = null;
	}
	
	
	
}
