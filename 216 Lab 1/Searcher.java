/*
 * Word search program.  Requests the file name of a word search puzzle,
 * which is just a text file containing an array of letters, each line of
 * input being one line of the puzzle.  It will then accept search words
 * from the keyboard and announce if they are present in the puzzle.
 */
import java.util.*;
import java.io.*;

class Letter
{
    public static final int E = 0;
    public static final int SE = 1;
    public static final int S = 2;
    public static final int SW = 3;
    public static final int W = 4;
    public static final int NW = 5;
    public static final int N = 6;
    public static final int NE = 7;
    public static final int NODIR = -1;

    char value;
    Letter[] neighbor = new Letter[8];

    Letter(char Let)
    {
        value = Let;
    }

    void setNeighbor(int dir, Letter let)
    {
        neighbor[dir] = let;
        if (let != null)
        {
            let.neighbor[rev(dir)] = this;
        }
    };

    Letter getNeighbor(int dir)
    {
        return neighbor[dir];
    };

    boolean matches(String tofind, int dir)
    {
        Letter scan = this;
        for(int i = 0; i < tofind.length(); ++i)
        {
            if(scan == null || scan.value != tofind.charAt(i))
            {
                return false;
            }
            scan = scan.getNeighbor(dir);
        }
        return true;
    };

    int matches(String tofind)
    {
        for(int dir = 0; dir <= 7; ++dir) 
        {
            if(matches(tofind, dir))
            {
                return dir;
            }
        }
        return NODIR;
    };

    int rev(int dir)
    {
        if(dir == E) 
            return W;
        else if(dir == SE) 
            return NW;
        else if(dir == S) 
            return N;
        else if(dir == SW) 
            return NE;
        else if(dir == W) 
            return E;
        else if(dir == NW) 
            return SE;
        else if(dir == N) 
            return S;
        else if(dir == NE) 
            return SW;
        else return NODIR;
    };

}
class Searcher {
    // Read a line from the argument BufferedReader, and return it.
    // Return null if there is an exception.
    public static String doRead(BufferedReader rdr)
    {
        try{
            return rdr.readLine();
        } catch(Exception e) {
            return null;
        }
    }
    
    // Search the puzzle starting at first, and announce the result.
    public static void search(Letter first, String tofind) 
    {
        // Direction names for printing.
        String[] dirs = { "E", "SE", "S", "SW", "W", "NW", "N", "NE" };

        // Try each of the puzzle positions.  We do this with two
        // fingers.  The rowscan moves across each row, and rowstart
        // moves down the left side of the puzzle, to provide the starting
        // position for each row.
        int row = 0;
        int col = 0;
        Letter rowstart = first;
        Letter rowscan = rowstart;
        int direction = Letter.NODIR;
        while(direction == Letter.NODIR && rowstart != null) {
            // See if the word is at the current location.
            direction = rowscan.matches(tofind);

            if(direction == Letter.NODIR) {
                // If we didnt' find it, move to the next square
                // in the scan.  Reading order: right one step, until
                // the end of the row, then to the start of the next.

                // Move the row scanner to the right.
                rowscan = rowscan.getNeighbor(Letter.E);
                ++col;
                if(rowscan == null) {
                    // If that produced null, we're at the row end.  Start on
                    // the next row.
                    rowstart = rowstart.getNeighbor(Letter.S);
                    rowscan = rowstart;

                    ++row;
                    col = 0;
                }
            }
        }

        // Announce result.
        if(direction == Letter.NODIR) {
            System.out.println("Word " + tofind + " not found.");
        } else {
            System.out.println("Word " + tofind + " found at row " +
                               row + ", column " + col + ", heading " +
                               dirs[direction]);
        }
    }


    public static void main(String[] args)
    {
        // Prompt for the name of the puzzle file, and open the file.
        Scanner in = new Scanner(System.in);
        System.out.print("What file contains the puzzle? ");
        String fn;
        fn = in.nextLine();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fn));
        } catch(Exception e) {
            System.out.println("Cannot open file: " + e);
            return;
        }
        System.out.println();

        // This holds the first letter in the puzzle, upper left corner.
        Letter first = null;

        // Read the puzzle file and fill in the puzzle list.
        Vector<Letter> lastrow = null;  // Previous row (null at first)
        Vector<Letter> thisrow =        // Row under construction.
            new Vector<Letter>();
        String line;                    // One line of file input.
        int lineno = 0;                 // Line number, for printing.
        while ((line = doRead(reader)) != null) {
            // Force the line to upper case.
            line = line.toUpperCase();

            // Print the line number.
            System.out.printf("%3d ", lineno);

            for(int i = 0; i < line.length(); ++i) {
                System.out.print(" " + line.charAt(i));

                // Where will does this object go in the line?
                int position = thisrow.size();

                // Create the object to hold this letter.
                Letter let = new Letter(line.charAt(i));

                // Connect the letter to the last one we read in.
                if(position > 0) {
                    thisrow.lastElement().setNeighbor(Letter.E, let);
                }

                // Connect to the letters in the previous row (NW, N, NE).
                if(lastrow != null) {
                    // This loop goes through the positions of items in the
                    // previous row relative to position.  That is, let is
                    // in position position in its row, and connects to
                    // position-1, position and position+1 in the row above.
                    for(int j = -1; j <= 1; ++j) {
                        int last_row_pos = position + j;
                        if(last_row_pos >= 0 && last_row_pos < lastrow.size())
                            let.setNeighbor(Letter.N + j,
                                             lastrow.get(last_row_pos));
                    }
                }

                // Put the letter in the puzzle, and also the last row record.
                if(first == null) first = let;
                thisrow.addElement(let);
            }
            System.out.println();
            
            // Move the current row to the last row for the next trip
            // around.  But first, check that the sizes are the same.  We 
            // only handle rectangular puzzles.
            if(lastrow != null && lastrow.size() != thisrow.size()) {
                System.out.println("All puzzle rows must be the same length.");
                return;
            }
            lastrow = thisrow;
            thisrow = new Vector<Letter>();

            ++lineno;
        }
        System.out.println();

        // This prints the column numbers at the bottom.  Assumes that
        // there are no more than 99 columns.  
        int rowsize = lastrow.size();
        if(rowsize >= 10) {
            // If there are more than 10 columns, we print the digits
            // one or larger.  First space over so they line up correctly.
            System.out.print("                        ");
            for(int i = 10; i < rowsize; ++i)
                System.out.printf(" %d", i / 10);
            System.out.println();
        }

        // Now print the second (or only) line of column number digits.
        System.out.print("    ");
        for(int i = 0; i < rowsize; ++i)
            System.out.printf(" %d", i % 10);
        System.out.println();
        System.out.println();

        // Okay.  Start asking for strings to search.  Stop on empty string.
        String trythis;
        do {
            // Read the word to try
            System.out.print("Enter word to try (Blank to quit): ");
            trythis = in.nextLine();
            trythis = trythis.toUpperCase();

            if(!trythis.equals("")) search(first, trythis);
        } while(!trythis.equals(""));
    }
}
