John Bosworth
David Modica
CS 542 - Database Management Systems

Assumptions
-The first four characters of the data_values are hashed to form index values
-Keys are unique
	-We do not enforce this at the moment. Multiple copies of a key are permitted and can exist in multiple buckets or
		the same bucket. If remove() is called on a key, all copies of a key will be deleted, regardless of location.
-There are 2 types of files: index and data
-data files are named "data"+key+".txt"
-index file is unique and is named "index.txt"
-Buckets are stored as tuples in modified CSV format in the index file
	-The first value is the "bucket" tuple is the index value
	-The remaining values in the bucket (no more than MAX_BUCKET_SIZE, default is 10) will be keys
	-Tuples are separated by \n	
	-The index value will be separated from the keys by an `, and the keys will be separated by a ~
		-We are assuming that no one will use an accent mark or tilde in this program.
		-Additional error checking and database sanitization would solve this issue, but that is outside the scope of this 
			project.
	-The index value of the overflow bucket is OVERFLOW_TITLE (set to "overflow" by default)

	
