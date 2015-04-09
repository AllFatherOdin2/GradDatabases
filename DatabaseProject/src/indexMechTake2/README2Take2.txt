John Bosworth
David Modica
CS 542 - Database Management Systems

Assumptions
	-The first four characters of the data_values are hashed to form index values
	-Keys are unique
		-We do not enforce this at the moment, so do not add multiple copies of a key. Multiple copies of a key are permitted 
		and can exist in multiple buckets or the same bucket. 
	-There are 2 types of files: index and data
	-Data files are named indexValue+".txt"
		-Split files include a number of "s"s on the end.
	-Index file is unique and is named "index.txt"
	-Buckets are stored as tuples in modified CSV format in the index file
		-The first value is the "bucket" tuple is the index value
		-The remaining values in the bucket (no more than MAX_BUCKET_SIZE, default is 10) will be keys
		-Tuples are separated by \n	
		-The index value will be separated from the keys by an `, and the keys will be separated by a ~
			-We are assuming that no one will use an accent mark or tilde in this program.
			-Additional error checking and database sanitization would solve this issue, but that is outside the scope of 
			 this project.
		-The index value of the overflow bucket is OVERFLOW_TITLE (set to "overflow" by default)
	-Before modifying any files, the user must call getBuckets(), and after all modifications are complete, the user must call
	 printBuckets() in order to "commit" the changes. These functions are integral to the proper function of the API
	 functions, and forgetting them has catastrophic effects.

Class Structure
	-Bucket:
		The Bucket class is a basic class that we use to represent a tuple in the index file. It can add and remove keys from
		the list of keys that is stored as a class variable, and each has a unique index value (based on hashed data values),
		which act as identifiers. However, we do not enforce the index value be unique, as there may need to be multiple
		buckets with the same index after one overflows.
	-ByteStringManipulator
		The ByteStringManipulator contains all of our code for reading in a text file as a byte stream and converting it to
		a String array, which we then parse into a Bucket.
	-IndexBucketImpl
		This class includes the API functions that were required for this project, as well as many helper functions. For more
		information on the helper functions, please read the Javadoc comments provided in the class. The API functions operate
		as one would expect for an implementation of Extensible Hashing.
	-BucketFilledException
		This exception gets thrown when a bucket is full and a split bucket exists elsewhere.
	-BucketOverflowException
		This exception gets thrown when a bucket overflows.
		
Depreciated Files
	-IndexMech2
		This class was our initial attempt at properly implementing this project. While we had several good insights as we
		worked on it, we realized that we were going down the wrong path, and our code was getting very hard to read. Because
		of this, we chose to start fresh and add the abstraction of Bucket and the other files. This code does not function,
		but still provides insight, so we have not deleted it. It can, however, be safely ignored.
		

	
