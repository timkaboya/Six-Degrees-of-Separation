Six Degrees of Separation
------------------------------------------------------------------------------------------------------------------------


What is this?
-------------
This Program investigates the 6 degrees of separation for two actor basing on movies acted
The program accepts as input 2 actors by way of command line arguments.
It then outputs the degree of separation and then a string representation  
of the link between the actors based on the movies acted in together. 

Implementation Details
----------------------

Implementation is an undirected weighted Graph based on an array of linked lists. 
Every actor is a vertice on the graph with neighbors. 
Each neighbor has weights for movie name, an index for other actor involved and pointer to the next neighbor

- The core of the algorithm is the computePath method that is based on the BFS Algorithm. 
  To determine the degrees of separation between two actors, we use a Breadth First Search(BFS) that scours for the
  second link between actors and returns the index of the second actor.

During the course of traversal, the BFS updates the variables to store the relations. 
This method is then called in the main to determine the relationship 

Requirements
------------
None. Note however that the program is implemented in Java


How the Program werks
------------------------
This is the main for the program.
- Initialize the Graph with enough space for all actors. 
- Reach each line of text file, and extract Movie and Actors names.
- Add Actors - Movie link to the Graph till at end of file.
- Call computePath method on Graph to establish link between Actor1 and Actor2
- Output degree of separation computed by computePath method
- Backtrace from actor1 to actor2 outputing the relationship determined by computePath

To compile the program, you need to have javac compiler. Use the following steps
Navigate to src folder in terminal
Terminal$: cd src
Terminal$: javac -d bin/ Assignment5.java

To run the program. 
TerminalS: cd bin
Terminal$: cp ../MovieData.txt .
Run using actor names as arguments
Terminal$: java Assignment5 "Owen Wilson" "Hugh Jackman" 
Output will be the degree of separation and the subsequent movies/actors which link the two. 

Limitations 
-----------
**
- This program has only been tested for instances when a movie has 1 or 2 actors. It may show unexpected results
  for movie data which has multiple actors. 
- OkBye
**


Contact
-------
Current maintainers:
* Timothy Kaboya - tkaboya@andrew.cmu.edu 
