/*Riya Affrin
5/31/24
CSE 123 BG
TA: Eric Bae
P3: Huffman

In Huffman we are creating a program that takes a text file and creates a new file where the text
is now in code. Essentially this code is compressing a text into a binary format.
This code also creates a binary tree based on the frequencies of a character. 
*/
import java.util.*;
import java.io.*;


//Here is the class header along with the field HuffmanNode overallRoot
public class HuffmanCode{
    private HuffmanNode overallRoot;


    //In this constructor we are passing in an int array, frequencies. and not returning anything
    //here the code is creating a huffman tree which has ordered the characters by frequencies
    public HuffmanCode(int[] frequencies){
        /* Psuedo code notes remember each huffman node has a frequency and a letter
        header of class will be private static class huffmanNode implements comparable<huffmanNode>
        this iscause we need to create a node and compare each node value we need four
        fields frequency,letter

        THIS IS THE ENTIRE COMPARETO METHOD - write this in your huffmanNode class
        compareTo method -> public int compareTo(huffmanNode other){
        returning the difference of frequencies -> return this.frequency - other.frequency;

            public huffman(int[] frequencies){
                1. create priority queue
                2. add all frequencies ot the priority queue as a huffmanNode
                3. combine 2 nodes until 1 node
                4. set root = the one node
                so get frequency, create new huffmanNode, and add huffmanNode to priority queue

                this can be done iterativly
                all of this happens in this one constructior 
            }
        }
        */

        Queue<HuffmanNode> priority = new PriorityQueue<>();
       
        for(int i = 0; i < frequencies.length; i++){
           
            if (frequencies[i] > 0) {
                priority.add(new HuffmanNode(frequencies[i], (char) i));
            }
        }
       
        while(priority.size() > 1){ 
            HuffmanNode left = priority.remove();
            HuffmanNode right = priority.remove();
            HuffmanNode newRoot = new HuffmanNode(left.data + right.data, left, right);
            priority.add(newRoot);

        } 

        overallRoot = priority.peek();
    }


    //In this constructor we take in a scanner and return nothing
    //Here we are using the scanner to create a huffman tree based on the character
    public HuffmanCode(Scanner input){
        overallRoot = new HuffmanNode(0, null, null);
        while(input.hasNextLine()){

            int ascii = Integer.parseInt(input.nextLine());
            String code = input.nextLine(); 
            insert(overallRoot, (char) ascii, 0, code);

        }
    }

    //This is a helper method used for our constructor. This method returns nothing
    //and passes in a HuffmanNode node, char c, String code, and an int num
    //The purpose of this method is to put a character in the proper area in the tree. 
    private void insert(HuffmanNode node, char c , int num, String code){
        if (num == code.length()){
            node.character = c;
        }else{
            if (code.charAt(num) == '0'){
                if (node.left == null) {
                    node.left = new HuffmanNode(0, null, null); 
                } 

                insert(node.left, c, num + 1, code); 

            }else{
                if(node.right == null){
                    node.right = new HuffmanNode(0, null, null);
                }

                insert(node.right, c, num + 1, code);
            }
        }
    }
   

    //This is the save method. We return nothing and pass in a PrintStream named output
    //This method is used to save the current code to an output stream
    public void save(PrintStream output){
        save(output, overallRoot, "");
    }

    //This is the helper to the save method above. Here we return nothing and pass in 
    //a PrintStream output, HuffmanNode root, and a String place.
    //This method checks which areas we can put a new character frequency node
    private void save(PrintStream output, HuffmanNode root, String place){
        if (root != null){
            if (root.isLeaf()){
                output.println((int) root.character);
                output.println(place);

            }else{
                save(output, root.left, place + "0"); 
                save(output, root.right, place + "1"); 
            }
        } 

    }


    //This is the translate method, it returns nothing and passes in a BitInputStream
    //and a PrintStream. This method is used to read bits from the PrintStream and write 
    //the correct characters. If the BitInputStream is empty it will stop reading it
    public void translate(BitInputStream input, PrintStream output){
        HuffmanNode current = overallRoot;

        while (input.hasNextBit()){
            int bit = input.nextBit();

            if (bit == 0){
                current = current.left;
            }else{
                current = current.right;
            }

            if(current.isLeaf()){
                output.write(current.character);
                current = overallRoot; 
            }
        }
    }



    //Here we have created a HuffmanNode class which implements a comparable
    private static class HuffmanNode implements Comparable<HuffmanNode>{
        //These are the fields used we have two nodes a right and a left as well as
        // an int and character
        public HuffmanNode left;
        public HuffmanNode right;
        public final int data;
        public char character;
       

        //This method is used to create the leaf nodes of the tree
        public HuffmanNode(int data, char character){
            this(data, null, null);

            this.character = character;

        }

        //This method is used to initialize the fields
        public HuffmanNode(int data, HuffmanNode left, HuffmanNode right) {
            this.data = data; 
            this.left = left;
            this.right = right;
            this.character = ' ';

        }

        //This method returns a boolean
        // if there is a right and left leaf then it will be true otherwise false
        private boolean isLeaf() {
            return (left == null && right == null); 
        }

        //This is the comapare to method, it returns an int and passes in a huffmanNode
        // It compares the data in the current node and the other node
        //It returns a negative integer zero or a positive integer if the data in the current
        //node is less than equal to or greater than the data in the other node, respectively
        public int compareTo(HuffmanNode other) {
            return Integer.compare(this.data, other.data);
        }

    }


}