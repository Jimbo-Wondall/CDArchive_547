/**
 * **************************************************************************
 */
/*                                                                           */
/*                    Doubly-Linked List Manipulation                        */
/*                                                                           */
/*                     January 1998, Toshimi Minoura                         */
/*                                                                           */
/**
 * **************************************************************************
 */
// Filename: Doubly-LinkedList_ToshimiMinoura
// Source:   TBA

package Utils;

import Utils.DList.*;

// A Node is a node in a doubly-linked list.
class Node
{                               // class for nodes in a doubly-linked list
    // previous Node in a doubly-linked list
    Node prev;
    // next Node in a doubly-linked list
    Node next;
    // data stored in this Node
    Words myWords;

    // constructor for head Node of an empty doubly-linked list
    Node()
    {
        prev = this;
        next = this;
        myWords = new Words();
    }

    // constructor for a Node with data
    Node(String...strings)
    {
        prev = null;
        next = null;
        myWords = new Words(strings);
    }

    // attach newNode after this Node
    public void append(Node newNode)
    {
        newNode.prev = this;
        newNode.next = next;
        if (next != null)
        {
            next.prev = newNode;
        }
        next = newNode;
        System.out.println("Node with data " + newNode.myWords.toString()
                + " appended after Node with data " + myWords.toString());
    }

    // attach newNode before this Node
    public void insert(Node newNode)
    {
        newNode.prev = prev;
        newNode.next = this;
        prev.next = newNode;;
        prev = newNode;
        System.out.println("Node with data " + newNode.myWords.toString()
                + " inserted before Node with data " + myWords.toString());
    }

    // remove this Node
    public void remove()
    {
        // bypass this Node
        next.prev = prev;
        prev.next = next;
        System.out.println("Node with data " + myWords.wordList[0] + " removed");
    }
    public String toString(){
        return String.join(" - ", this.myWords.wordList);
    }
}

public class DList
{
    Node head;

    public DList()
    {
        head = new Node();
    }

    public DList(String...strings)
    {
        head = new Node(strings);
    }

    public void add(String...strings)
    {
        head.insert(new Node(strings));
    }

    // find Node containing x
    public Node find(String wrd1)
    {
        for (Node current = head.next; current != head; current = current.next)
        {
            // is x contained in current Node?
            if (current.myWords.wordList[0].compareToIgnoreCase(wrd1) == 0)
            {
                System.out.println("Data " + wrd1 + " found");
                // return Node containing x
                return current;
            }
        }
        System.out.println("Data " + wrd1 + " not found");
        return null;
    }

    //This Get method Added by Matt C
    public Node get(int i)
    {
        Node current = this.head;
        if (i < 0 || current == null)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        while (i > 0)
        {
            i--;
            current = current.next;
            if (current == null)
            {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        return current;
    }

    public String toString()
    {
        // list is empty, only header Node
        if (head.next == head)
        {
            return "List Empty";
        }
        StringBuilder str = new StringBuilder();
        for (Node current = head.next;
             current != head && current != null;
             current = current.next)
        {
            str.append(current.myWords.toString()).append("\n");
        }
        return str.toString();
    }
    public static class Words {
        String[] wordList;
        public Words() {
            wordList = new String[0];
        }

        public Words(String...strings) {
            wordList = strings;
        }
        @Override
        public String toString() {
            return String.join(" - ", wordList);
        }
  }
}
