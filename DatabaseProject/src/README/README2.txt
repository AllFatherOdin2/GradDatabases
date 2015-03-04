John Bosworth
David Modica
CS 542 - Database Management Systems

Assumptions:
-Index file will be read in as a byte array and stored as a TXT file.
-Index entries will be "comma-separated", using "`" between the key and the datavalue, and a "~" between separate entries.
-Entries in index are sorted alphabetically by datavalue, rather than by key
-There are many keys that can be pointed to by a datavalue.
-All keys will be unique
-All records with a given datavalue will be deleted when using remove()
-Hash value may not be unique for all datavalues
-Text files are essentially infinite length (just changes time-to-access), so we are not handling overflow


Decisions to Make
if 1:1 key-dataValue relation, do we overwrite or keep original key?