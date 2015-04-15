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
