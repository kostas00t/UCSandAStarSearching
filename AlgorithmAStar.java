/*/**
 * Artificial Intelligence - MYY602
 * Semester Project - Exercise 1b
 * KONSTANTINOS KIKIDIS - 4387
 * CHRISTOS KROKIDAS - 4399 
 * KONSTANTINOS TSAMPIRAS - 4508
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.lang.Math;

public class AlgorithmAStar {

	public static void main (String[] args) {

		// Get the list of numbers from the user
		System.out.println("Give starting list (eg: 4,2,3,1):");
		Scanner input = new Scanner(System.in);
		String startingList = input.nextLine();
		input.close();

		// Initialization
		String[] startString;
		List<Integer> target = new ArrayList<Integer>();
		List<Integer> start = new ArrayList<Integer>();
		HashSet<List<Integer>> visitedNodes = new HashSet<List<Integer>>();
		ArrayList<NodeExtended> searchingNodes = new ArrayList<NodeExtended>();
		boolean targetReachedFlag = false;

		// Convert the string to an ArrayList of numbers
		// and find my target list
		startString = startingList.split(",");
		for (String i : startString) {
			int x = Integer.parseInt(i);
			start.add(x);
			target.add(x);
		}
		Collections.sort(target);
		
		// Print the starting list and my target list
		System.out.println("\nTarget List: " + target.toString());
		System.out.println("Starting List: " + start.toString()+"\n");

		// Create the first node (root)
		NodeExtended startingNode = new NodeExtended(start);
		searchingNodes.add(startingNode);
		visitedNodes.add(startingNode.getNumberList());

		// Check if starting and target list are the same
		if (start.equals(target)) {
			targetReachedFlag = true;
			System.out.println("Starting List == Target List \n[]");
		}

		// While we haven't reached our target
		while (!targetReachedFlag) {

			// Set the first node of searching nodes list
			// as the node with the smallest cost 
			NodeExtended minCostNode = searchingNodes.get(0);
			
			// Find the node with the smallest cost
			for (NodeExtended i : searchingNodes) {
				if (i.getCost() < minCostNode.getCost()) {
					minCostNode = i;
				}
			}

			// Check if target reached, by if the minCostNode is our target,
			// if found, make flag true, and stop execution 

			//System.out.println("Node " +minCostNode+ " Selected, with k= "+ minCostNode.getKValue() + ", level= " + minCostNode.getLevel() + ", cost= " +minCostNode.getCost());
			if (minCostNode.checkIfTargetReached(target)) {
				//System.out.println(minCostNode.getCost());
				targetReachedFlag = true;
				break;
			}

			// Else, remove the minCostNode from the searchingNodes list, create the child 
			// nodes of minCostNode and add them to searchingNodes list (if not found already)
			searchingNodes.remove(minCostNode);
			for (int k = 2; k <= minCostNode.getNumberList().size(); k++) {
				NodeExtended next = new NodeExtended(minCostNode.getNumberList(), k, minCostNode, minCostNode.getLevel()+1, 0);
				//System.out.println("Node " +next+ " Created, with k= "+ next.getKValue() + ", level= " + next.getLevel() + ", cost= " +next.getCost());
				if (!visitedNodes.contains(next.getNumberList())){
		 			visitedNodes.add(next.getNumberList());
					searchingNodes.add(next);
				}
			}
		}

		System.out.println("Nodes Created: "+visitedNodes.size());
		System.out.println("searchingNodes Size: "+searchingNodes.size());
	}
}

class NodeExtended {

	// Basic Attributes
	private List<Integer> numberList ;
	private int k;
	private NodeExtended parent;
	private int level;
	private int cost;


	// Constructors
	// For the root node
	public NodeExtended(List<Integer> start) {
		this.numberList = start;
		this.k = 0;
		this.level = 0;
		this.cost = 0;
		calculateCost();
	}

	// For every other node
	public NodeExtended(List<Integer> start, int k, NodeExtended parent, int level, int cost) {
		List<Integer> numberList = new ArrayList<Integer>(start.size());
		for (Integer i : start) {
			numberList.add(i);
		}
		this.numberList = numberList;
		this.k = k;
		this.parent = parent;
		this.level = level;
		this.cost = cost;
		rearrange();
		calculateCost();
	}

	// Rearrange the number list of a node, based on it's k value
	public void rearrange() {
		List<Integer> aL = numberList.subList(0, k);
		List<Integer> aR = numberList.subList(k, numberList.size());
		Collections.reverse(aL);
		aL.addAll(aR);
		numberList = aL;
	}

	// Calculate the cost of a node, from it's numberList,
	// report.pdf explains how it works in detail
	public void calculateCost() {
		int gn = this.level;
		int hn = 0;
		int i = 0;
		for (i = this.numberList.size()-1; i >= 0; i--) {
			if (i != numberList.get(i)-1) {
				hn++;
				break;
			}	
		}
		for (int j = i; j >= 1; j--) {
			if ((this.numberList.get(j) > this.numberList.get(j-1)) && (this.numberList.get(j) - this.numberList.get(j-1) != 1)) {
				hn++;
			}
			else if (Math.abs(this.numberList.get(j-1)-this.numberList.get(j)) >= 2) {
				hn++;
			}
		}
		this.cost = hn+gn;
		//System.out.println(this.numberList.toString() + "\t\thn = "+ hn + ", gn=level= " + gn + ", cost=" +this.cost);
	}

	// Check if the numberlist of a node, is the target,
	// if it is, found the path to root
	public boolean checkIfTargetReached (List<Integer> target) {
		if (numberList.equals(target)) {
			findPath(target);
		}
		return numberList.equals(target);
	}

	// Find the path to root node from target node
	// and print the path
	public void findPath(List<Integer> target) {
		List<Integer> kList = new ArrayList<Integer>();
		List<NodeExtended> parentList = new ArrayList<NodeExtended>();
		kList.add(k);
		while (parent != null) {
			parentList.add(parent);
			parent = parent.parent;
		}
		
		for (NodeExtended i: parentList) {
			kList.add(i.getKValue());
		}

		Collections.reverse(kList);
		kList.remove(0);
		Collections.reverse(parentList);
		System.out.println("Cost: "+kList.size());
		System.out.println("k Steps: "+kList.toString());
		prettyPrint(parentList, kList, target);
	} 
	
	// A way to get the numberList as a string
	public String toString() {
		return String.format(getNumberList().toString());
	}

	// A nice way to print the path
	public void prettyPrint(List<NodeExtended> parentList, List<Integer> kList, List<Integer> target) {
		int i = 0;
		System.out.print("Path: ");
		for (NodeExtended j: parentList) {
			System.out.print(j + " --(k="+ kList.get(i) +")--> ");
			i++;
		}
		System.out.println(target);
	}

	// Getters
	public List<Integer> getNumberList() {
		return numberList;
	}
	
	public int getKValue() {
		return k;
	}
	
	public NodeExtended getParent() {
		return parent;
	}

	public int getCost() {
		return cost;
	}

	public int getLevel() {
		return level;
	}

	// Setters
	public void setK(int k) {
		this.k = k;
	}
	
	public void setParent(NodeExtended parent) {
		this.parent = parent;
	}

	public void setNumberList(List<Integer> numberList) {
		this.numberList = numberList;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
