SIX DEGREES OF SEPARATION

An investigation of the Degrees of Separation. 

Compute the degree of separation between two actors.. 
Ex: Two actors or actresses are directly linked if they appeared in a movie together
This is 0 degree of separation. 


 How it works. 
 - Parse data from textfile. 
 - Create Undirected Graph with Actors as Vertices and Neigbours the movies linking them
 - To compute the degree of separation, do a breadth first search for Second Actor
 - count the number of edges to endpoint, this is the Degree of separation. 