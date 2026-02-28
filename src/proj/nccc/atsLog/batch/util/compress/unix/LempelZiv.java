package proj.nccc.atsLog.batch.util.compress.unix;

    // Introduced in Chapter 17  
    import java.io.*;  
import java.util.ArrayList;
import java.util.Scanner;  
      
    /** Lempel-Ziv compression of text. */  
    public class LempelZiv {  
      
      /** Root of the digital search tree. */  
      private DigitalNode<Integer> root;  
      
      /** Direct-addressing table mapping codes to Strings. */  
      private ArrayList<String> strings;  
      
      /** Initialize the codes with ASCII values. */  
      public LempelZiv() {  
        root = new DigitalNode<Integer>(null);  
        strings = new ArrayList<String>();  
        for (char i = 0; i < 128; i++) {  
          root.setChild(i, new DigitalNode<Integer>((int)i));  
          strings.add("" + i);  
        }  
      }  
      
      /** 
       * Add a new code.  It represents the concatenation of the String 
       * for the code at parent and the first character of the String for 
       * code. 
       */  
      protected void addNewCode(DigitalNode<Integer> parent, int code) {  
        if (parent != null) {  
          char firstChar = strings.get(code).charAt(0);  
          parent.setChild(firstChar,  
                          new DigitalNode<Integer>(strings.size()));  
          strings.add(strings.get(parent.getItem()) + firstChar);  
        }  
      }  
      
      /** Read ints from in, write text to out. */  
      public void decode(ObjectInputStream in, PrintWriter out)  
        throws IOException {  
        DigitalNode<Integer> parent = null;  
        while (in.available() > 0) {  
          int code = in.readInt();  
          DigitalNode<Integer> node = root;  
          String s = strings.get(code);  
          for (char c : s.toCharArray()) {  
            out.print(c);  
            node = node.getChild(c);  
          }  
          addNewCode(parent, code);  
          parent = node;  
        }  
        out.close();  
      }  
      
      /** Read text from in, write ints to out. */  
      public void encode(Scanner in, ObjectOutputStream out)  
        throws IOException {  
        DigitalNode<Integer> parent = null;  
        DigitalNode<Integer> node = root;  
        while (in.hasNextLine()) {  
          String line = in.nextLine() + "\n";  
          for (int i = 0; i < line.length(); ) {  
            DigitalNode<Integer> child = node.getChild(line.charAt(i));  
            if (child == null) {  
              int code = node.getItem();  
              out.writeInt(code);  
              addNewCode(parent, code);  
              parent = node;  
              node = root;  
            } else {  
              node = child;  
              i++;  
            }  
          }  
        }  
        out.writeInt(node.getItem());  
        out.close();  
      }  
      
    }  