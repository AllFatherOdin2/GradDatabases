John Bosworth
David Modica
CS 542 - Database Management Systems

Assumptions
	-Log statement formats 
		-"READ, <rid>, <time>"
		-"WRITE, <rid>, <index of change>, <Old value>, <New value>, <time>"
		-"COMMIT, <Table Name>, <Block>, <time>"
	-Modification of populations only increases by 2%
	-Changes in city population do not modify country population
	-Relation class will read in from CSV files and be used after that point
		-Contains list of attribute names (which must be entered MANUALLY upon creation), a name, and a list of tuples
		-Data must be read in after creating Relation object
		-Data stored in Relation object is considered "on disk", from Java's perspective.
	-Log statements in log class are considered "on disk", even if it has not been written to file.
	-Blocks have been arbitrarily limited in size to 200 tuples, regardless of what is stored in the tuple.
	-Data will be committed once per block.
	
Our File System
	-LoggingImpl
		Our main class, and home to the open(), getNext(), and close() methods. For more detail on how these functions work, 
		refer to the Javadoc comments within the class. Much of the structure is similar to Project 3, with some modifications
		to satisfy requirements that became clear after project 3 was returned.
	-LoggingException
		Generic exception class for this project.
	-Relation
		Relation is a class that acts as a database table. It contains a name, list of attribute names, and a list of blocks. 
		The blocks are a separate class explained below. This class is responsible for reading data from the CSV files into 
		Java's memory, and parsing it properly into tuples, which are then sorted into the blocks. This sets up a proper 
		Relation object that the rest of our code can then interact with, as if it were interacting with a database on disk.
	-Block
		Block is a class that simply stores a list of tuples. This list is limited in size to 200 tuples arbitrarily, as a
		hardware block is also limited in size. We figured 200 would be a decent size for each block, allowing us to store a
		good amount of data while still being able to divide the tuples into a large enough number of blocks for accurate 
		scale testing.
	-Tuple
		Tuple is a class that stores a single tuple's worth of data as a list of strings. It does little more than act as a
		storage mechanism.
	-Log
		This class stores all log statements in a list, and can write them out to a file at any time. When the statements are
		added to an instantiated object of the class, they are considered "on disk". The log statements can be written to 
		file at any point, and the log used to recover lost data.