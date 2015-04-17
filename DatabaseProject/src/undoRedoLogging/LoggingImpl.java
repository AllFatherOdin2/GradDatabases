/**
 * 
 */
package undoRedoLogging;

/**
 * @author DModica
 * @author JBosworth
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoggingImpl {
	
	public static void main(String args[]) throws LoggingException{
		open();
		undoLastChange();
	}
	
	private static final String direct = System.getProperty("user.dir") + "\\";
	private static Relation city;
	private static Relation country;
	private static double queryTime = 0;
	
	/**
	 * Creates city and country Relation objects and populates them
	 * @return
	 * @throws LoggingException
	 */
	public static void open() throws LoggingException{
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
		getNext();
		double endTime = System.nanoTime();
		setQueryTime((endTime-startTime)/1000000000);
	}
	
	
	/**
	 * Modifies both country and city relations and calls close.
	 * @throws LoggingException
	 */
	private static void getNext() throws LoggingException{
		modifyRelation(city);
		modifyRelation(country);
		close();
	}
	
	/**
	 * Writes the log string out to a text file
	 */
	private static void close(){
		Log.writeToFile();
	}
	
	/**
	 * Undo changes from the must recent commit.
	 * @throws LoggingException 
	 */
	public static void undoLastChange() throws LoggingException{
		List<Tuple> oldTuples = null;
		List<Tuple> commitTuples = null;
		List<String> log = Log.getLogFromFile();
		int blockEntry = -1;
		String tableName = "";
		for(int x = log.size() - 1; x >= 0; x--){
			String entry = log.get(x);
			String[] entryArray = entry.split(", ");
			String entryType = entryArray[0];
			
			if(entryType.equals("COMMIT")){
				if(oldTuples != null){
					//so this is the second commit
					break;
				}
				tableName = entryArray[1];
				blockEntry = Integer.parseInt(entryArray[2]);
				List<Block> blocks = null;
				if(tableName.equals(city.getName())){
					blocks = city.getBlocks();
				}else if(tableName.equals(country.getName())){
					blocks = country.getBlocks();
				}else{
					blocks = new ArrayList<Block>();
				}
				oldTuples = blocks.get(blockEntry).getTuples();
				commitTuples = new ArrayList<Tuple>();
			}
			

			if(entryType.equals("WRITE") && oldTuples != null){
				int tupleIndex = Integer.parseInt(entryArray[1]);
				int indexOfChange = Integer.parseInt(entryArray[2]);
				String oldValue = entryArray[3];
				Tuple newTuple = oldTuples.get(tupleIndex);
				
				newTuple.updateData(indexOfChange, oldValue, tupleIndex);
				commitTuples.add(0, newTuple);
			}
		}
		
		Block updatedBlock = new Block(commitTuples);
		
		if(tableName.equals(city.getName())){
			city.updateBlock(blockEntry, updatedBlock);
		}else if(tableName.equals(country.getName())){
			country.updateBlock(blockEntry, updatedBlock);
		}
		
		Log.writeToFile();
	}
	
	/**
	 * Takes a Relation with a population attribute and modifies the population of all tuples to be 102% of original value
	 * @param table Relation being modified
	 * @throws LoggingException
	 */
	private static void modifyRelation(Relation table) throws LoggingException{
		Date date = new Date();
		//Handle city populations
		List<Block> blocks= table.getBlocks();
		//Hardcoded because this is single purpose software at the moment. Easy enough to fix with a parameter, though.
		int attrIndexLoc;
		try {
			attrIndexLoc = table.getAttributeArrayLocation("Population");
		} catch (LoggingException e) {
			throw new LoggingException("The population attribute does not exist for this relation");
		}
		for(int i = 0; i < blocks.size(); i++){
			List<Tuple> blockTuples = blocks.get(i).getTuples();
			for(int k = 0; k < blockTuples.size(); k++){
				Tuple tuple = blockTuples.get(k);
				int origPop = 0;
				try{
					origPop = Integer.parseInt(tuple.getData().get(attrIndexLoc));
				}catch(Exception e){
					continue; //the data was entered incorrectly, so ignore it.
				}
				Log.addLog("READ, " + tuple.getData().get(0) + ", " + date.getTime());
				int newPop = (int) (origPop * 1.02);
				tuple.updateData(attrIndexLoc, newPop+"", k);
			}
			table.updateBlock(i, blocks.get(i));
		}
	}
	
	/**
	 * getter for queryTime
	 * @return queryTime of the most recent query
	 */
	public double getQueryTime() {
		return queryTime;
	}

	/**
	 * setter for query Time
	 * @param queryTime
	 */
	public static void setQueryTime(double queryTime) {
		LoggingImpl.queryTime = queryTime;
	}
}
