John Bosworth
David Modica
CS 542 - Database Management Systems

Assumptions
-Data is inserted using standard CSV format. Ex. "value",NULL,"value"\n
-CSV files are stored in the same directory as the program
-Both CSV files fit in memory


How open() Works
	We read in the files as byte arrays, and convert the entire file into a single string. We then explode that string to
	match the standard CSV format, splitting it into a list by separating it on the \n character. Each element of that
	list is then split again on the , character, leaving a list of lists of strings, with each string being an individual
	field in a tuple. However, we still have quotes in each string, so we have to remove those manually in order to get the
	correct values out of the function.
	
How getNext() works
	Using the two buffers generated in the open() function, we can iterate through all of the city tuples. At each tuple, we
	compare the Country Code to the Code of each country, and if the two match, we compare the size of the populations of
	the country and city. If the city population is greater than .4 of the country population, the city name is added to a 
	result list. Once all of the countries are checked, the next city is selected and the process repeats. Once all cities
	have been run through, the list of names is saved in another buffer, and getNext() calls close().
	
How close() works
	This function is quite simple. Its only purpose is to print out the result list, and then null all three buffers, freeing
	the space for use later.
