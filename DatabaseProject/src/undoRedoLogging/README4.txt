John Bosworth
David Modica
CS 542 - Database Management Systems

Assumptions
-Log statement formats 
	-"read in 'tuple id from file' population is 'old Pop' at 'Time'"
	-"writing to 'file' 'tuple id', population to 'new'"
	-"Commit 'file'"
-Modification of populations only increases by 2%
-Changes in city population do not modify country population
-Relation class will read in from CSV files and be used after that point
	-Contains list of attribute names (which must be entered MANUALLY upon creation), a name, and a list of tuples
	-Data must be read in after creating Relation object
	
Our File System
-LoggingImpl
	Our main class, and home to the open(), getNext(), and close() methods. For more detail on how these functions work, refer
	to the Javadoc comments within the class. Much of the structure is similar to Project 3, with some modifications to
	satisfy requirements that became clear after project 3 was returned.
-LoggingException
	Generic exception class for this project.
-Relation
	Relation is a class that acts as a database table. It contains a name
