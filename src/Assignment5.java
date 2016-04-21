import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/* Array of LinkedList Implementation of Movie-Actor R/ship.
*  Essentially a vertice is an actor that is connected to other neighbors. (next neighbour)
*  Every neighbor has an actor Index, movie name and is a part of a linked list (Not Java List). 
*  Array of Linked Lists
*  Vertex1 -> Neighbor1 -> Neighbor2 -> NULL
*  Vertex2 -> Neighbor4 -> Neighbor1 -> NULL
*  Vertex3 -> Neighbor5 -> Neighbor4 -> NULL
*/

// Building block for Link between Actor and Movie.
class Neighbor {
	int vertexNum; 
	String movie;
	Neighbor next;
	public Neighbor (int num, String _movie, Neighbor ngh ) {
		vertexNum = num;
		movie = _movie;
		next = ngh;
	}
	public String toString() {
		return ":--> " + movie + next;
	}
}

// Represents the Actor
class Vertex {
	String actor;
	Neighbor adjList;   // Each vertex/actor is attached to some neighbors. Linked actors. 
	Boolean visited;    // This flag is used in the BFS traversal of list. 
	int degree;			// Stores the degree of r/ship for Vertex. Used in computing degree.
	int whoBroughtYouIn;// Used to track back from Actor2 to Actor1 for Degree of separation. 
	Vertex (String _actor, Neighbor neighbors) {
		actor = _actor;  // Store Actor name as Vertex in graph.
		adjList = neighbors;
		visited = false;
		degree = -1;
		whoBroughtYouIn = -1;
	}
	
	public String toString () {
		return actor + " : " + adjList;
	}
}


//Undirected Movie Actor Graph
class Graph {
	Vertex [] adjLists;
	private int numActors;  // Keeps track of the number of actors
	// private int [][] graph;
	public Graph(int x) {
		// Create an Adjacency list of x actors (Over 2000 actors for our program)
		adjLists = new Vertex[x];
		numActors = 0; 
		}
	
	// Add connections between actors 1 and 2 to Adjacency list
	public void makeEdge (String actor1, String actor2, String movie) {
		int i, j;
		i = getVertex (actor1);
		j = getVertex (actor2);
		if (i == -1) {
			adjLists[numActors] = new Vertex(actor1, null);
			i = numActors;
			numActors++;
		}
		if (j == -1) {
			adjLists[numActors] = new Vertex(actor2, null);
			j = numActors;
			numActors++;
		}
		adjLists[i].adjList	 = new Neighbor (j, movie, adjLists[i].adjList);
		adjLists[j].adjList	 = new Neighbor (i, movie, adjLists[j].adjList);
	}
	
	// Returns vertex index if it is present, else it returns -1. 
	public int getVertex (String actor) {
		for (int i = 0; i < numActors; i++)
		{
			if (adjLists[i].actor.equals(actor)) return i;
		}
		return -1;
		
	}
	// Returns index of vertex in list when given Vertex
	public int getVertex (Vertex actor) {
		for (int i = 0; i < numActors; i++) {
			if (adjLists[i].equals(actor))
				return i;
		}
		return -1;
	}
	// For two given actors, method returns movie that connected them.  
	public String getMovie(int act1Index, int act2Index) {
		for (Neighbor nbr = adjLists[act1Index].adjList; nbr != null; nbr = nbr.next)
		{
			if (nbr.vertexNum == act2Index)
				return nbr.movie;
		}
		return null;
	}
	
	/* Compute path for given vector. Uses bfs algorithm to compute the path
	 * from actor1 to actor2 following relationships.  
	 */
	public int computePaths(String actor1, String actor2)
	{
		// Vertex path = new Vertex(actor1, null);
		Queue <Vertex> vertexQueue = new LinkedList<Vertex>();
		// Array of visited values. 
		
		// On queue, vertices are added in terms of levels. 1 level has act1 only. 
		// Next level of additions to queue is after all neighbors of act1 are enqueued.
		// Next level is after all neighbors of neighbors of ac1 are enqueued. And so on. 
		// We only go to the next level, degree of separation, after one level of neighbors is dequeued.
		
		int vertIndex = 0;  // Index to index into LinkedList array for actors. 
		vertIndex = getVertex(actor1);
		// Add vertex to Queue if it exists. Else return sentinel, there is no path!
		if (vertIndex != -1) vertexQueue.add(adjLists[vertIndex]);
		else return -1;
		
		// If actor2 is not in our List, return sentinel, there is no path! 
		if (getVertex(actor2) == -1 ) return -1;
		
		while(!vertexQueue.isEmpty()) {
			// Pop vertex from Queue
			Vertex act1 = vertexQueue.poll();  
			// Mark Vertex as visited.
			vertIndex = getVertex(act1);
			adjLists[vertIndex].visited = true;
			
			
			for (Neighbor nbr=act1.adjList; nbr!=null; nbr = nbr.next) 
			{
				// Add all vertex neighbors to queue, if they are not visited yet. 
				if (!adjLists[nbr.vertexNum].visited ) {
					vertexQueue.add(adjLists[nbr.vertexNum]);
					adjLists[nbr.vertexNum].visited = true;
					adjLists[nbr.vertexNum].degree = act1.degree + 1;  // Incrementing degree basing on parent.
					adjLists[nbr.vertexNum].whoBroughtYouIn = vertIndex;
					// Abandon search if degree is greater than 6
					if (adjLists[nbr.vertexNum].degree > 6)
						return -1;  // Sentinel value for high degree
				}
				// If we find needed actor, return degree.
				if (adjLists[nbr.vertexNum].actor.equals(actor2))
					return nbr.vertexNum;
			}
		}
		return -1;
	}
						
						
						
						
	public void printGraph() {
		
		for (int i = 0; i < numActors; i++)
		{
			System.out.print(adjLists[i].actor);
			for (Neighbor nbr=adjLists[i].adjList; nbr != null; nbr = nbr.next)
			{
				System.out.print(" --> " + adjLists[nbr.vertexNum].actor + ": " + nbr.movie);
				System.out.println();
			}
			System.out.println("========================================================================");
		}
		
	}
}

// Main class in Program
public class Assignment5 {

	public static void main(String[] args)
	{	
		// Init Graph class
		Graph graph = new Graph(5000);
		// Open Movie File
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("MovieData.txt"));
			String line, title;
			String [] actors;
			while ((line = br.readLine()) != null)
			{
				String [] words = line.split("[(]");
				int size = words.length;
				title = words[0];
				String actStr = words[size-1].replace(")", "");
				actors = actStr.split(",");
				// Ignore stand alone actors. This data is useless for finding  6 degrees of separation. 
				if (actors.length > 1)
					graph.makeEdge(actors[0].trim(), actors[1].trim(), title);
				// System.out.println("Movie Title: " + title);
				// System.out.println("Actors: " +  Arrays.toString(actors));
			}
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// printGraph();
		if (args.length < 2) {
			System.out.println("Missing Arguments! Exiting Program"); 
			return;
		}
		int vertIndex = graph.computePaths(args[0], args[1]);
		int prevIndex;
		String relationship = "";
		if (vertIndex == -1)
			System.out.println("No links were found!");
		else if (vertIndex == -2)
			System.out.println("The degree of separation is greater than 6. C'est pas possible!");
		else {
			System.out.println(args[0] +" -> " +  args[1] + ": " + graph.adjLists[vertIndex].degree + " degree of separation");
			prevIndex = graph.adjLists[vertIndex].whoBroughtYouIn;
			while (prevIndex!= -1)
			{		
				relationship += graph.getMovie(vertIndex, prevIndex) + ": " + graph.adjLists[prevIndex].actor + "; " + graph.adjLists[vertIndex].actor + "'\n";
				vertIndex = prevIndex;
				prevIndex = graph.adjLists[prevIndex].whoBroughtYouIn;
			}
			// Reversing relationship for output. Relation for Actor1->Actor2 and not Actor2->Actor1
			String [] relations = relationship.split("'\n");
			for (int i = relations.length - 1; i > -1; i-- ) {
				System.out.println(relations[i]);
			}
		}
	}

}
