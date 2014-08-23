import java.util.ArrayList;
import java.util.List;


public class FHeap {
	
	public class FNode{
		
		int degree=0;
		Boolean isMarked=false;
		int nodeValue;
		FNode pNext,pPrev;
		int priority;
		FNode parentNode=null,childNode=null;
		public FNode(int val,int priority)
		{
			this.nodeValue=val;
			this.priority=priority;
			this.isMarked=false;
			pNext=pPrev=this;
		}	
	
	}
	Long heapSize=0L;
	
    FNode minValeNode=null;
    
	// inserts and return the node with min value
	public FNode insert(int node_val,int priority)
	{
		
		FNode newNode=new FNode(node_val,priority);
		//System.out.println("inserted value"+newNode.nodeValue);
		minValeNode=mergeNodes(minValeNode,newNode);//newNode);
		++heapSize;
		return newNode;
		
	}
	
	public FNode mergeNodes(FNode one,FNode two)
	{
		
		if(one==null && two==null)
			return null;
		else if(one==null && two!=null)
			return two;
		else if(one!=null && two==null)
			return one;
		else
		{
			FNode next=one.pNext;
			one.pNext=two.pNext;
			one.pNext.pPrev=one;
			two.pNext=next;
			two.pNext.pPrev=two;
			return one.priority<two.priority? one : two;
		}	
	}
	//code to merge two heaps
	
	public FHeap merge(FHeap first_heap, FHeap sec_heap)
	{
		FHeap result=new FHeap();
		result.minValeNode=mergeNodes(first_heap.minValeNode, sec_heap.minValeNode);
		result.heapSize=first_heap.heapSize+sec_heap.heapSize;
		first_heap.heapSize=sec_heap.heapSize=0L;
		first_heap=sec_heap=null;
		return result;
	}
	
	
	
	//code for remove min
	
	public FNode removeMin()
	{
		if(minValeNode==null)
		{
			System.out.println("heap is empty");
			return null;
		}
		else
		{
			heapSize--;
			FNode removeNode=minValeNode;
			if(minValeNode.pNext==minValeNode)
			{
				minValeNode=null;
			}
			else
			{
				minValeNode.pPrev.pNext = minValeNode.pNext;
	            minValeNode.pNext.pPrev = minValeNode.pPrev;
	            minValeNode = minValeNode.pNext;
			}
			//Making the parents's field of the remove node children to null
			
			if (removeNode.childNode != null) {
	            /* Keep track of the first visited node. */
	            FNode child = removeNode.childNode;
	            do {
	                child.parentNode = null;
	                child = child.pNext;
	            } while (child != removeNode.childNode);
	        }
			
			minValeNode=mergeNodes(minValeNode,removeNode.childNode);
			
			if(minValeNode==null)
				return removeNode;
			
			List<FNode> track=new ArrayList<FNode>();
			
			/* tracking the visited nodes*/
			List<FNode> vnodes=new ArrayList<FNode>();
			for (FNode curr = minValeNode; vnodes.isEmpty() || vnodes.get(0) != curr; curr = curr.pNext)
	            vnodes.add(curr);
			/* performing union steps*/
			
			 for (FNode curr: vnodes) 
			 {
				 while(true)
				 {
					 while(curr.degree>=track.size())
						 track.add(null);
					 
						 if(track.get(curr.degree)==null)
						 {
							 track.set(curr.degree,curr);
							 break;
						 }
						 FNode addNode=track.get(curr.degree);
						 track.set(curr.degree,null);
						 FNode min = (addNode.priority < curr.priority)? addNode : curr;
			             FNode max = (addNode.priority < curr.priority)? curr  : addNode;			             
			             max.pNext.pPrev = max.pPrev;
			             max.pPrev.pNext = max.pNext;			             
			             	max.pNext = max.pPrev = max;
			                min.childNode = mergeNodes(min.childNode, max);
			                max.parentNode = min;
			                max.isMarked = false;
			                ++min.degree;
			                curr = min;
					 }
					 if (curr.priority <= minValeNode.priority) minValeNode = curr;
				 }
				 return removeNode;
		}
	 }	
			
	public void decreaseKey(FNode node, int newValue)
	{
		if(node.priority>newValue)
		{
			node.priority=newValue;
			if(node.parentNode!=null && node.priority<=node.parentNode.priority)
				cascadeCut(node);
			if(node.priority<=minValeNode.priority)
				minValeNode=node;
			
		}
	}
	public void cascadeCut(FNode cutNode)
	{
		cutNode.isMarked=false;
		if(cutNode.parentNode==null)
			return;
		
		/*making free from siblings list*/
		if(cutNode.pNext!=cutNode)
		{
			cutNode.pNext.pPrev = cutNode.pPrev;
            cutNode.pPrev.pNext = cutNode.pNext;
		}
		if (cutNode.parentNode.childNode == cutNode) 
		{

            if (cutNode.pNext != cutNode) {
                cutNode.parentNode.childNode = cutNode.pNext;
            }
           else {
                cutNode.parentNode.childNode = null;
            }
        }
		--cutNode.parentNode.degree;
		
		/* join this cut tree to root list*/
		cutNode.pPrev = cutNode.pNext = cutNode;
        minValeNode = mergeNodes(minValeNode, cutNode);
        
        /*mark the parent and recursively cut the parent if it is already marked*/
        if(cutNode.parentNode.isMarked)
        	cascadeCut(cutNode.parentNode);
        else 
        	cutNode.parentNode.isMarked=true;
        
        /*now its a root*/
        cutNode.parentNode=null;
	}
/*
	public static void main(String args[])
	{
		FHeap heap=new FHeap();
		heap.insert(1,100);
		heap.insert(2,200);
		heap.insert(3,50);
		heap.insert(4,25);
		int node_val=heap.removeMin().nodeValue;
		System.out.println("min value="+node_val);
		int node_val2=heap.removeMin().nodeValue;
		System.out.println("min value="+node_val2);
		int node_val3=heap.removeMin().nodeValue;
		System.out.println("min value="+node_val3);
		int node_val4=heap.removeMin().nodeValue;
		System.out.println("min value="+node_val4);
		//heap.print();
	}
	*/
}