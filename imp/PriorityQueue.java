package imp;

import java.util.ArrayList;

import imp.BacktrackingAlgorithm.Backtracking_Node; // This looks silly

public class PriorityQueue { // Min-Heap
    
	static class Node {
        Backtracking_Node Node;
        int priority;

        Node(Backtracking_Node Node, int priority) { // This is the Constructor
            this.Node = Node;
            this.priority = priority;
        }
    }

    ArrayList<Node> heap = new ArrayList<>();

    public void insert(Backtracking_Node Node, int priority) { // Add a new node to the list
        heap.add(new Node(Node, priority));
        int idx = heap.size() - 1; // Starts at the bottom 
        while (idx != 0) { // While not at the Start
            int parentIdx = (idx - 1) / 2; // Get the Parent Node Index
            if (heap.get(parentIdx).priority > heap.get(idx).priority) { // if the Node has a smaller priority, swap node places with the parent
                Node temp = heap.get(parentIdx);
                heap.set(parentIdx, heap.get(idx));
                heap.set(idx, temp);
                idx = parentIdx; // The swapped index becomes the current index
            } else {
                break; // Stops when the priorities are correct
            }
        }
    }

    public Backtracking_Node remove() {
        if (heap.isEmpty()) { // Checks if it is empty
            return null;
        }
        Node root = heap.get(0); // Obtains highest priority Node
        heap.set(0, heap.get(heap.size() - 1)); // Places Last Node on Top
        heap.remove(heap.size() - 1); // Removes Last Node from List
        int idx = 0;
        while (2 * idx + 1 < heap.size()) { // Sorts the Nodes
            int leftChildIdx = 2 * idx + 1;
            int rightChildIdx = 2 * idx + 2;
            int smallestChildIdx = leftChildIdx; // Sets the smallest Child to the left initially
            if (rightChildIdx < heap.size() && heap.get(rightChildIdx).priority < heap.get(leftChildIdx).priority) {
                smallestChildIdx = rightChildIdx; // Sets the smallest Child to right if applicable
            }
            if (heap.get(idx).priority > heap.get(smallestChildIdx).priority) { // if the Last Node placed on top has a lower priority than the smallest Child Swap them
                Node temp = heap.get(idx);
                heap.set(idx, heap.get(smallestChildIdx));
                heap.set(smallestChildIdx, temp);
                idx = smallestChildIdx;
            } else { // if not break out of the loop
                break;
            }
        }
        return root.Node;
    }

    public int size() { // Just gets the size of the Heap
        return heap.size();
    }
    
    public boolean isEmpty() { // Checks if the heap is empty or not
    	return heap.isEmpty();
    }
}