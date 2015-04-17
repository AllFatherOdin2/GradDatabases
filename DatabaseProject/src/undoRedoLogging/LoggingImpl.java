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
	private static Relation city;
	private static Relation country;
	/*
	private static List<List<String>> cities = new ArrayList<List<String>>();
	private static List<List<String>> countries = new ArrayList<List<String>>();
	private static List<String> results = new ArrayList<String>();
	private static List<String> log = new ArrayList<String>();
	*/
	private static double queryTime = 0;
	
	/**
	 * Creates city and country Relation objects and populates them
	 * @return
	 * @throws LoggingException
	 */
	public List<String> open() throws LoggingException{
		final String INPUT_FILE_CITY = direct + "city.csv";
		final String INPUT_FILE_COUNTRY = direct + "country.csv";
		ArrayList<String> cityAttrs = new ArrayList<String>();
		cityAttrs.add("ID");
		cityAttrs.add("Name");
		cityAttrs.add("CountryCode");
		cityAttrs.add("District");
		cityAttrs.add("Population");
		//Not pretty, but effective.
		ArrayList<String> countryAttrs = new ArrayList<String>();
		countryAttrs.add("Code");
		countryAttrs.add("Name");
		countryAttrs.add("Continent");
		countryAttrs.add("Region");
		countryAttrs.add("SurfaceArea");
		countryAttrs.add("IndepYear");
		countryAttrs.add("Population");
		countryAttrs.add("LifeExpectancy");
		countryAttrs.add("GNP");
		countryAttrs.add("GNPOld");
		countryAttrs.add("GovernmentForm");
		countryAttrs.add("HeadOfState");
		countryAttrs.add("Captial");
		countryAttrs.add("Code2");
		countryAttrs.add("LocalName");
		
		city = new Relation("City", cityAttrs);
		country = new Relation("Country", countryAttrs);
		
		city.loadTuples(INPUT_FILE_CITY);
		country.loadTuples(INPUT_FILE_COUNTRY);

		double startTime = System.nanoTime();
		List<String> result = getNext();
		double endTime = System.nanoTime();
		setQueryTime((endTime-startTime)/1000000000);
		
		return result;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws LoggingException
	 */
	private List<String> getNext() throws LoggingException{
		Date date = new Date();
		//Handle city populations
		List<Block> cityBlocks= city.getBlocks();
		//Hardcoded because this is single purpose software at the moment. Easy enough to fix with a parameter, though.
		int cityIndexLoc = city.getAttributeArrayLocation("Population");
		for(Block block : cityBlocks){
			List<Tuple> blockTuples = block.getTuples();
			for(Tuple tuple : blockTuples){
				int origPop = Integer.parseInt(tuple.getData().get(cityIndexLoc));
				Log.addLog("READ, " + tuple.getData().get(0) + ", " + date.getTime());
				int newPop = (int) (origPop * 1.02);
				tuple.updateData(cityIndexLoc, newPop+"");
			}
			
		}
		
		//Handle country populations
		List<Block> countryBlocks= country.getBlocks();
		int countryIndexLoc = country.getAttributeArrayLocation("Population");
		for(Block block : countryBlocks){
			List<Tuple> blockTuples = block.getTuples();
			for(Tuple tuple : blockTuples){
				
			}
		}
		
		
		/*
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
			String code = country.get(0);
				if (countryCode.compareTo(code) == 0){
					
					int countryPop = Integer.parseInt(country.get(6));
					int cityPop = Integer.parseInt(city.get(4));
					if(cityPop > .4 * countryPop){
						results.add(city.get(1));
				}
			}
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
	 	*/	
		return close();
	 
	}
	
	/**
	 * 
	 * @return
	 */
	private List<String> close(){
		/*
		System.out.println(results.size());
		for(String city : results){
			System.out.println(city);
		}
		*/
		List<String> tempResults = null;
		
		return tempResults;
		
	}
	
	public double getQueryTime() {
		return queryTime;
	}

	public static void setQueryTime(double queryTime) {
		LoggingImpl.queryTime = queryTime;
	}
}
