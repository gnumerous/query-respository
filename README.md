# query-respository
A service for freeform text query management

# Support for Repository pattern interfaces. 
The Repository pattern allows users to create interfaces annotated with query text and return type of the collection. 
The manager creates a proxy for this and handles the database interaction. The query text can be a key to an entry in 
the query cache.

Query cache allows large queries with substitution variables to be stored in a resource file from a Repository pattern 
interface.
