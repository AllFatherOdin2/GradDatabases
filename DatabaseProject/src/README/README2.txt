John Bosworth
David Modica
CS 542 - Database Management Systems


For this assignment, we made many assumptions. Firstly, we did not handle any multi-threading issues, which are an issue while
writing an index. However, as with the first project, we are assuming that the system we are using can handle reading and 
writing to files atomically. We also are assuming that all key-datavalue pairs will be unique. However, the keys themselves
do not need to be unique, provided the datavalues they are associated with are different. Also, this was not explictly stated
in the project description, so we feel it appropriate to include here: When using remove(), all records with the given
datavalue are deleted. This can cause an entire bucket to be removed, though if a bucket contains multiple datavalues
(unlikely, but possible), only matching datavalues (and their keys) will be removed.

Additionally, our index is a series of text files, each of which is considered a "bucket" that key-datavalue pairs are hashed
into. The data is stored as a single string in a format similar to CSV. Each record would be of the form "key`datavalue~", and
would be cleaned of accent marks and tildes prior to entry. The text files are essentially infinite length, so there is no 
need to handle overflow. However, we do recognize that there is a reduction in time-to-access when making larger and larger
text files, and while we could add code that prevents files from exceeding a predetermined size, we feel that is out of the
scope of the project. Still, as an explanation for how this would work, once a file reached a particular size, a flag 
character would be added to indicate that it was overflowing, and a second file would be generated to take new data. The 
program would be smart enough to look for these flag characters and would read them in as appropriate.

Another assumption we made is that the entries in each index file will be inserted into a sorted file. This would require a
simple insert() into the string at the right point, which, though we could add code to handle this, again is outside the scope
of this project in our eyes.

We followed the API for the Hashing Index for this project, and found it to be relatively similar to the previous assignment.
As required, put() takes a key-datavalue pair, runs the datavalue through a hash function, and places the pair in the
approrpriate bucket. The appropriate bucket is named "index"+hash(datavalue)+".txt". This makes it very easy to get to the 
desired file very quickly. If the bucket does not exist, it is created first, then the data is inserted. For the get()
function, we did make a single modification to the API, which we explain below. get() takes in a datavalue and immediately 
run it through our hash function to get to the right bucket. When we get the value, we then use regular expressions to split
the string into a 2-dimensional array of strings, which contain the key and the datavalue, with all accent marks and tildes 
removed. Using this array, we are able to pull out a list of all of the keys matching this datavalue. However, while we could
convert this list of strings very easily into a string itself, a string is much less useful than a list, and we use this
functionality in several places to handle duplication checking. For remove(), we made no modifications, and simply search for
the bucket containing the datavalue, get all of the non-matching key-datavalue pairs (using code similar to get()), and delete
the file before writing a new one with the non-matching key-datavalue pairs. This extra work is only applied when 2 datavalues
have the same hash value, which is incredibly rare, but can happen, as we are only using an int for our hash value. Numeric 
overflow can and will likely occur given enough time.

We tested the code using JUnit to obtain the highest code coverage we could obtain. Currently, our coverage for the actual
interface we created is at 88.5%, due to a couple of exceptions that we had difficulty intentionally hitting. These are errors
that will only occur if a built in function from Java fails, which should ideally never occur. We have handled them to the 
best of our ability.

We did not stress-test this code on large files, due to the assumptions we made. Additionally, knowing that the variety in 
datavalues would cause a huge number of files to be generated, we did not expect most of the files to get excessively large.
However, we do acknowledge that millions of small text files can still take up a significant amount of room on disk. The
reason we did not test this is because both of us are running on fairly limited laptops, and the cleanup cost after testing
this functionality would exceed the benefits gained from it. Additionally, improperly cleaning up the index would cause
subsequent tests to fail, preventing us from making them truely autonomous. Our code is proof-of-concept, rather than a
working product.
