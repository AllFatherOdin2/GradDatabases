John Bosworth
David Modica
CS 542 - Database Management Systems

Assumptions
-Data is inserted using standard CSV format. Ex. "value",NULL,"value"\n
-CSV files are stored in the same directory as the program
-Both CSV files fit in memory
-The functions open(), getNext(), and close() feed directly into each other, such that getNext() and close() require open() in
	order to execute properly.
-open() is a public function, but getNext and close() are both private functions.


How open() Works
	We read in the files as byte arrays, and convert the entire file into a single string. We then explode that string to
	match the standard CSV format, splitting it into a list by separating it on the \n character. Each element of that
	list is then split again on the , character, leaving a list of lists of strings, with each string being an individual
	field in a tuple. However, we still have quotes in each string, so we have to remove those manually in order to get the
	correct values out of the function. In our helper function, convertByteArrayTo2DList(), we do need to modify the NULL 
	values that exist so that they are wrapped by quotes. This allows us to remove the quotes from everything cleanly, as well
	as checking for values that would otherwise break our splitting functions.
	
How getNext() works
	Using the two buffers generated in the open() function, we can iterate through all of the city tuples. At each tuple, we
	compare the Country Code to the Code of each country, and if the two match, we compare the size of the populations of
	the country and city. If the city population is greater than .4 of the country population, the city name is added to a 
	result list. Once all of the countries are checked, the next city is selected and the process repeats. Once all cities
	have been run through, the list of names is saved in another buffer, and getNext() calls close().
	
How close() works
	This function is quite simple. Its only purpose is to print out the result list, and then null all three buffers, freeing
	the space for use later.

	
Performance
	We do not have many tests for this program, as it is single purpose code. However, we did run the same query in a MySQL 
	database, and were able to visually confirm that the answers are the same. Despite that, our implementation takes 
	approximately 13 seconds to complete everything, from reading in the two CSV files to deleting the string buffers 
	storing them. The query itself, however, is handled only in the getNext() function, and that takes only 0.0275 seconds, 
	whereas the MySQL query took only 0.005 seconds (give or take 0.0015 seconds). Our query is considerably slower, but we 
	may not be using the optimal physical plan for searching, as Java does not have a good method of filtering Lists.
	Additionally, because our query is hard-coded into getNext(), we only achieved 82% code coverage on our implementation,
	as none of our error-checking code can be triggered by any tests that are thrown at it.
	