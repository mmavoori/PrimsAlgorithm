import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;


public class MST {
	public static void main(String args[]) throws Exception
	{
		if(args.length==2)				//For file as input 
		{
			
			Graph G = new Graph(args[1]);
			if(args[0].equals("-s"))
			{
				Prims p=new Prims(G);		//Calls simple scheme Prims Algorithm
				p.print();
			}
			else if(args[0].equals("-f"))
			{
				PrimFib p=new PrimFib(G);	//Calls FHeap scheme Prims Algorithm
				//p.print();
			}
			else
			{
				System.out.println("argument not matched for the Prims Scheme");
			}
		}
		else if(args.length==3)			//For Random Graph Generator Method accepting no of nodes  
		{			 					//and density of graph as arguments
			if(args[0].equals("-r"))
			{
			int n=Integer.parseInt(args[1]);
		   	int d=Integer.parseInt(args[2]);
		   	int max_edges=(n*(n-1))/2;
			int min_edges=(n-1);
		   	if((min_edges*100/max_edges)>d)
			{
				System.out.println("ENTER SUFFICIENT DENSITY TO BUILD GRAPH");
			}
			else
			{
				
				Graph G = new Graph(n,d);
				Long start_time=System.currentTimeMillis();
				new Prims(G);	//Calls simple scheme Prims Algorithm
				Long end_time=System.currentTimeMillis();
				System.out.println("TIME FOR SIMPLE SCHEME IN SEC:"+(end_time-start_time)/1000);
				Long start_time_sec=System.currentTimeMillis();
				new PrimFib(G);	//Calls FHeap scheme Prims Algorithm
				Long end_time_sec=System.currentTimeMillis();
				System.out.println("TIME FOR F-HEAP SCHEME IN MILLISEC:"+(end_time_sec-start_time_sec));	   	
			}	
			}
			else
				System.out.println("argument not matched for the Prims Scheme");
		}
		else
		{
			System.out.println("insufficient number of arguments");
		}
	}
}

class Edge
{
	int vertex_number;
	int cost;
	Edge next;	
	public Edge(int v_num, int c, Edge ngb)
	{
		this.vertex_number=v_num;
		this.cost=c;
		this.next=ngb;
	}
}

class Vertex
{
	int number;
	Edge adjList;
	boolean visited=false;
	public Vertex() {
		this.number=0;
		this.adjList=null;
	}
	public Vertex(int v,Edge nb,boolean b)
	{
		this.number=v;
		this.adjList=nb;
		this.visited=b;
	}
}

//Graph class for creating Graph depending on input method

class Graph {
	int no_of_vertices;
	int no_of_edges;
    Vertex[] adjLists;
    int max_edges;
    int res=0;
 public Graph(String fname) throws Exception
    {
   	try
   	{
   	Scanner input=new Scanner(new File(fname));
   	this.no_of_vertices=input.nextInt();
   	this.no_of_edges=input.nextInt();
    adjLists=(Vertex[]) new Vertex[no_of_vertices];
   	
   	//Initializing Vertices
   	
   	 for(int v=0; v<adjLists.length;v++)
   	 {
   		adjLists[v]=new Vertex(v,null,false); 
   	 }
   	 
   	 //add Edges to the Graph until input file becomes empty	 
   	 while(input.hasNext())
   	 {
   		 	 int v1 =input.nextInt();
  		  	 int v2 = input.nextInt();
  		     int w = input.nextInt();	//cost of the edge
  		     adjLists[v1].adjList=new Edge(v2,w,adjLists[v1].adjList);
  		     adjLists[v2].adjList=new Edge(v1,w,adjLists[v2].adjList);
   	 }
   	 //System.out.println("--------GRAPH-------");
   	// print(adjLists);
   	}	 
   catch(FileNotFoundException e)	//throws an exception when input file not found
	{
		System.out.println(e);
	}
}
   
   //Parameterized Constructor to generate Graph by random number generator method
public Graph(int n,int d) throws Exception
    {
    	this.no_of_vertices=n;
    	this.max_edges=(n*(n-1))/2;
    	no_of_edges=0;
    	double possible= max_edges*(d/100f);
    	int pos_edges=(int)possible;
    	int edges=0;
    	Random random=new Random();
    	this.adjLists=(Vertex[]) new Vertex[no_of_vertices];
    	boolean b[][]= new boolean[no_of_vertices][no_of_vertices];
    	this.adjLists=(Vertex[]) new Vertex[no_of_vertices];
    	for(int v=0; v<adjLists.length;v++)
       	 	{
        		adjLists[v]=new Vertex(v,null,false); 
       	 	}
    	
    	while(edges<pos_edges ||!dfs())	//Runs until density condition is accomplished 
    	{								//and graph is connected
    	
    		int v1=random.nextInt(no_of_vertices);
        	int v2=random.nextInt(no_of_vertices);
        	while(v2==v1)
        	{
        		v2=random.nextInt(no_of_vertices);
        	}
        	int w=random.nextInt(1000)+1;	
        	if(!b[v1][v2]&& !b[v2][v1])	//add edges to the graph
        	{
        		adjLists[v1].adjList=new Edge(v2,w,adjLists[v1].adjList);
				adjLists[v2].adjList=new Edge(v1,w,adjLists[v2].adjList);
				no_of_edges=no_of_edges+1;
				b[v1][v2]=true;
				b[v2][v1]=true;
				edges++;
        	}	
    	}
    	for(int i=0;i<adjLists.length;i++)
    	{
    		adjLists[i].visited=false;
   	 	}
    	//print();
    }
	//Method to check whether a graph is connected. Returns true if it is connected 
    public boolean dfs()
    {
    	int count=0;
    	Stack s=new Stack();
    	s.push(adjLists[0]);
    	adjLists[0].visited=true;
    	while(!s.isEmpty())
    	{
    		Vertex n=(Vertex)s.peek();
    		int n1=n.number;
    		int child=getUnvisitedVertex(n1);
    		if(child!=0)
    		{
    			adjLists[child].visited=true;
    			s.push(adjLists[child]);
    		}
    		else
    		{
    			s.pop();
    		}
    	}
    	
    	 for(int i=0;i<adjLists.length;i++)	//count variable keep track of visited vertices 
    	 {
    		 if(adjLists[i].visited==true)
    		 {
    			 count++;
    		 }
    	 }
    	 if(count==no_of_vertices)			//checks if all vertices are visited i.e 
    	 {									//whether the graph is connected 
    		 System.out.println("Graph is connected!!");
    		 return true;
    	 }
    	 else
    	 {
    		 return false;
    	 }
    	
    }
    //This Method checks for unvisited child of given vertex 'n'. 
    //Returns the child vertex number.
    public int getUnvisitedVertex(int n)
    {
    	for(Edge nbr=adjLists[n].adjList;nbr!=null;nbr=nbr.next)
    		if(adjLists[nbr.vertex_number].visited==false)
    			return nbr.vertex_number;
    	return 0;	
    }
    
    //Method to print the graph generated
   public void print()
   {
	   for(int i=0;i<adjLists.length;i++)
	   {
		   System.out.println("for vertex"+adjLists[i].number);
		   for(Edge nbr=adjLists[i].adjList;nbr!=null;nbr=nbr.next)
		   {
			   System.out.println("neighbors list="+nbr.vertex_number);
			   System.out.println("costt"+nbr.cost);
		   }
		   System.out.println("\n");
	   }
   }   
}
//Prims Class implementing Prims Algorithm using Simple Scheme with 
//time complexity of O(n2).
class Prims
{    
	   Vertex[] p_adjLists;
	   int p_cost,total_cost;
	   int var=0;
	   Graph g;
	   int counter=1;
	   int flag,i,j;
	   
	   public Prims(Graph p)		//Parameterized constructor which takes graph as input
	   {
		   this.g=p;
		  this.p_adjLists=(Vertex[]) new Vertex[g.no_of_vertices];
		  for(int v=0; v<g.adjLists.length;v++)
	    	 {
	    		p_adjLists[v]=new Vertex(v,null,false); 
	    	 }
		  Check(g.adjLists);
		  //prints the total cost of MST and the edges of MST
		  System.out.println("TOTAL COST SIMPLE SCHEME="+total_cost);
		  
	   } 
	   public void print()
	   {
		   for(int k=0;k<p_adjLists.length-1;k++)
			  {
				   for(Edge nbr=p_adjLists[k].adjList;nbr!=null;nbr=nbr.next)
				   {
					   System.out.print(p_adjLists[k].number);
					   System.out.println("\t"+nbr.vertex_number);
				   }
			  }
	   }  
	   
	   public void Check(Vertex[] adjLists)	//Finds the nearest cost edge among visited vertices
	   {
		   for(i=0;i<g.adjLists.length;i++)
		   {
			   if(g.adjLists[i].visited==true)
			   {
			   if(flag==0)
			   {
			   for(Edge next_nbr=g.adjLists[i].adjList;next_nbr!=null;next_nbr=next_nbr.next)
			   {
				  if(!g.adjLists[next_nbr.vertex_number].visited)
				  {
					  flag=1;
					  j=i;
					  var=next_nbr.vertex_number;	//initially sets a vertex number to compare
					  p_cost=next_nbr.cost;			//initially sets a cost to compare
					  break;
				  }
			   }	
			   }
			   //for loop to find an edge with least cost 
			   for(Edge next_nbr=g.adjLists[i].adjList;next_nbr!=null;next_nbr=next_nbr.next)
			   { 
				   if(!g.adjLists[next_nbr.vertex_number].visited)
				   {
					   if(p_cost<=next_nbr.cost)
					   {
						   continue;	
					   }
					   else
					   {
						   j=i;
						   var=next_nbr.vertex_number;
						   p_cost=next_nbr.cost;
						   continue;
					   }
				   }
		  
			   }
		   }
		   }
		  if(j!=var)
	  	  {
			  total_cost=total_cost+p_cost;		  
		  	  //p_adjLists[j]=new Vertex(j,null,false);	//adding edges to result graph
			  p_adjLists[j].adjList=new Edge(var,p_cost,p_adjLists[j].adjList);		
			  //System.out.println(j);
			  //System.out.println(var);
			  
	  	  }	  
		  g.adjLists[j].visited=true;
		  g.adjLists[var].visited=true;
		  
			  while(counter<g.no_of_vertices)		//runs until all vertices are added to result graph
			  {
				  counter++;
				  flag=0;
				  Check(g.adjLists);
			  }	   
	   }
}

//Prims Algorithm implementation with FHeap Scheme

class PrimFib
{
	Graph g;
	FHeap fh;
	Vertex[] r_adjLists=null;
	int i,m=0;
	int counter=0;
	int total_cost=0;
	public PrimFib(Graph p)					//Parameterized constructor 
	{
		this.g=p;
		fh=new FHeap();						//creating an object of FHeap Class
		this.r_adjLists=(Vertex[]) new Vertex[g.no_of_vertices];
		 for(int v=0; v<g.adjLists.length;v++)
    	 {
    		r_adjLists[v]=new Vertex(v,null,false); 
    	 }
		 int [] vertex_Array= new int[g.no_of_vertices];
		 FHeap.FNode [] refofHeap= new FHeap.FNode[g.no_of_vertices];
		 ArrayList<Integer> visited= new ArrayList<>();
		 vertex_Array[0]=0;
		 refofHeap[0]= fh.insert(0, 0);       //Inserting 1st node in the heap
		 for(int i=1;i<g.no_of_vertices;i++)
		 {
			refofHeap[i]= fh.insert(i, 99999);
		 } 
		 
		 FHeap.FNode x= fh.removeMin();//Removing minimum element from a tree
		 visited.add(x.nodeValue);
		 total_cost += (int)x.priority;
		 for(Edge j=g.adjLists[0].adjList;j!=null;j=j.next)
		 {
			 if(!visited.contains(j.vertex_number))
			 {			 
				{	 
					vertex_Array[j.vertex_number]=x.nodeValue;  
				}
				 fh.decreaseKey(refofHeap[j.vertex_number], j.cost);		
			 }
		 }	 
		 for(int i=1;i<g.no_of_vertices;i++)
		 {
			 
			  x= fh.removeMin();
			 visited.add(x.nodeValue);
			 total_cost += (int)x.priority;		//Adding weight of the minimum edge in the final cost
			 for(Edge j=g.adjLists[x.nodeValue].adjList;j!=null;j=j.next)
			 {
				 if(!visited.contains(j.vertex_number))
				 {		 
					{	 						
						vertex_Array[j.vertex_number]=x.nodeValue;
					}
					
					fh.decreaseKey(refofHeap[j.vertex_number], j.cost);
				 }
			 }
		 }
		/* for(int m=0;m<vertex_Array.length-1;m++)
		 {
			 r_adjLists[m].adjList=new Edge(vertex_Array[m],0,r_adjLists[m].adjList);
		 }*/
		 System.out.println("TOTAL COST WITH FHEAP SCHEME:"+total_cost);  //Printing final cost
	 }
	public void print()
	   {
		   for(int k=0;k<r_adjLists.length-1;k++)
			  {
				   for(Edge nbr=r_adjLists[k].adjList;nbr!=null;nbr=nbr.next)
				   {
					   System.out.print(r_adjLists[k].number);
					   System.out.println("\t"+nbr.vertex_number);
				   }
			  }
	   }  
}
//END
