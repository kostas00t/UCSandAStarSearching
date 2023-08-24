/*/**
 * Artificial Intelligence - MYY602
 * Semester Project - Exercise 1a
 * KONSTANTINOS KIKIDIS - 4387
 * CHRISTOS KROKIDAS - 4399
 * KONSTANTINOS TSAMPIRAS - 4508
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class AlgorithmUCS {

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
		ArrayList<Node> currentLevelNodes = new ArrayList<Node>();
		ArrayList<Node> nextLevelNodes = new ArrayList<Node>();
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
		Node startingNode = new Node(start);
		currentLevelNodes.add(startingNode);
		visitedNodes.add(startingNode.getNumberList());

		int level = 0;

		// Check if starting and target list are the same
		if (start.equals(target)) {
			targetReachedFlag = true;
			System.out.println("Starting List == Target List \n[]");
			level++;
		}

		// While we haven't reached our target
		while (!targetReachedFlag) {

			// Check if target reached, by looking the nodes of current level,
			// if found, make flag true, and stop execution
			for (Node i : currentLevelNodes) {
				if (i.checkIfTargetReached(target)) {
					targetReachedFlag = true;
					break;
				}

				// Else, create new nodes
				for (int k = 2; k <= i.getNumberList().size(); k++) {
					Node next = new Node(i.getNumberList(), k, i);

					// If we haven't seen that number list yet, add the
					// node to visited nodes set and next level nodes list
					if (!visitedNodes.contains(next.getNumberList())){
						visitedNodes.add(next.getNumberList());
						nextLevelNodes.add(next);
					}
				}
			}

			// Clear the current level node list, and add the nodes from the next level,
			// then clear the next level node list and increment the level counter
			currentLevelNodes.clear();
			for (Node i: nextLevelNodes) {
				currentLevelNodes.add(i);
			}
			nextLevelNodes.clear();
			level++;
		}

		// Print how many nodes we have created till we reach our target, and how many levels deep we went
		System.out.println("Nodes Created: "+visitedNodes.size()+", Depth Reached: "+ (level-1)+"\n");
	}
}

class Node {

	// Basic Attributes
	private List<Integer> numberList ;
	private int k;
	private Node parent;

	// Constructors
	// For the root node
	public Node(List<Integer> start) {
		this.numberList = start;
		this.k = 0;
	}

	// For every other node
	public Node(List<Integer> start, int k, Node parent) {
		List<Integer> numberList = new ArrayList<Integer>(start.size());
		for (Integer i : start) {
			numberList.add(i);
		}
		this.numberList = numberList;
		this.k = k;
		this.parent = parent;
		rearrange();
	}

	// Rearrange the number list of a node, based on it's k value
	public void rearrange() {
		List<Integer> aL = numberList.subList(0, k);
		List<Integer> aR = numberList.subList(k, numberList.size());
		Collections.reverse(aL);
		aL.addAll(aR);
		numberList = aL;
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
		List<Node> parentList = new ArrayList<Node>();
		kList.add(k);
		while (parent != null) {
			parentList.add(parent);
			parent = parent.parent;
		}

		for (Node i: parentList) {
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
	public void prettyPrint(List<Node> parentList, List<Integer> kList, List<Integer> target) {
		int i = 0;
		System.out.print("Path: ");
		for (Node j: parentList) {
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

	public Node getParent() {
		return parent;
	}

	// Setters
	public void setK(int k) {
		this.k = k;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public void setNumberList(List<Integer> numberList) {
		this.numberList = numberList;
	}
}
