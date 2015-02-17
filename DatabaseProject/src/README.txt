John Bosworth
David Modica
CS 542 - Database Management Systems

For this assignment, we made a couple of assumptions. Foremost was that the file system we are using can handle creation,
modification, retrieval, and deletion of data in an atomic manner. Because of this, we do not have any multi-threading code
implemented in this project, and all interactions with the datastore are handled by a single thread, imitating a 
transaction model. Another assumption that we made was that when data is "put" into a store using an existing key, the data 
previously stored at that key location would be overwritten completely. This causes the previous data to be lost 
permanently, but the key location itself is not changed. The third assumption we made was that the user was actually 
storing their data in a text file. This may not be true, and there are multiple different types of files that can be used 
to store data. However, for byte arrays, a text file is sufficient, so we used that.

The API given to us was followed to the letter. Please check our Javadoc commentary for precise details on how we
implemented each function. As a basic overview, put() creates or locates an existing text file based on the key, and shoves
the data into the file. This deletes any previously existing data in the file (where applicable). get() returns the byte
array exactly as entered into the file. We chose to keep it as ASCII values, rather than converting back to chars. This is
a simple process, and because of that, did not seem to be necessary for this project. Furthermore, conversion to a char
array could not occur until after the get() method returned, as get() is required by the API to return a byte array. 
Finally, remove() deletes the entire file named with the given key. The data is lost completely after deletion, and cannot
be retrieved at this point, as that was outside of the scope of this project.

We tested the code using JUnit to obtain the highest code coverage we could obtain. Currently, our coverage for the actual
interface we created is at 88.7%, due to a couple of exceptions that we had difficulty intentionally hitting. These are errors
that will only occur if a built in function from Java fails, which should ideally never occur. We have handled them to the 
best of our ability.

In terms of performance, our code works extremely efficiently. It takes only 0.003 seconds to write one million bytes worth 
of data to a file, a nearly imperceptible amount of time from human standards. While there are likely faster methods that 
are used by modern databases, Java does have limits, and cannot compete with the speed of lower-level write calls,
especially not while generating the data at the same time. It takes approximately twice as long to delete the same amount of
data (1,000,000 bytes), but takes over 30 times as long to generate those bytes, write them, and then read them back. This 
means that it takes approximately 0.13 seconds to read 1,000,000 bytes worth of data, which is quite slow. Unfortunately, we
used the most efficient Java method we could find, and were unable to increase that speed in any way.




