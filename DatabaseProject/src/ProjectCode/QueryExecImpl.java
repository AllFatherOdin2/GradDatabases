package ProjectCode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryExecImpl {
	private static String direct = System.getProperty("user.dir") + "\\";
	private static List<List<String>> cities;
	private static List<List<String>> countries;

	public QueryExecImpl() {
		cities = new ArrayList<List<String>>();
		countries = new ArrayList<List<String>>();
	}
	
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
	}
	
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

	public static void getNext(){
		
	}
	
	public static void close(){
		
	}
	
}
