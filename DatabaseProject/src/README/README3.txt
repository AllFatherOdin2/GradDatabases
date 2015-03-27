John Bosworth
David Modica
CS 542 - Database Management Systems

Assumptions
-Data is inserted using standard CSV format. Ex. "value","value","value"\n
-CSV files are stored in the same directory as the program


How open() Works
	We read in the files as byte arrays, and convert the entire file into a single string. We then explode that string to
	match the standard CSV format, splitting it into a list by separating it on the \n character. Each element of that
	list is then split again on the , character, leaving a list of lists of strings, with each string being an individual
	field in a tuple. However, we still have quotes in each string, so we have to remove those manually in order to get the
	correct values out of the function.
