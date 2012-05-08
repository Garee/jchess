package model;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * IOParser for the 0x88 board representation
 * @author fergus
 *
 */
public class IOParser {
	private String filename;
	
	/**
	 * Constructor just takes in file name, validates and stores for later use for either 
	 * reading or writing to 
	 * @param String filename
	 */
	public IOParser( String filename ){
		if (filename.endsWith(".j")){
			this.filename = filename;
		}else {
			this.filename = null;
			System.err.println("Error: Not correct file extension; .j");
		}
	}
	
	/**
	 * Parses the filename specified and stores the data into a byte[128] array
	 *
	 * Uses 0x88 to allow the file to contain the valid board and it automatically
     * fills in the invalid board.
	 *
	 @return byte[128] squares 
	 */
	 public byte[] parse() {
		if ( filename == null ) return null;
		Scanner file = readerOpen();
		if ( file == null ) return null;
		// Checks for nulls and returns, expects
		// Caller to do something with this
		byte[] squares = new byte[128];
		String x;
		for ( int i = 0; i < 128; i ++ ) {
			if ( ( i & 0x88 ) != 0 ) {
				squares[i] = 0;
			} else if ( file.hasNext() ) {
				x = file.next();
				try {
					squares[i] = isValid( Byte.parseByte( x ) );
				} catch ( NumberFormatException e ) {
					System.err.printf("Error: %s is invalid, must be an int value 0 - 30.\n", x );
					return null;
					}
			} else {
			System.err.printf( "Error: %s has insufficient values.\n", filename );
			return null;
			}
		}	
    file.close();
    return squares;

	}
	/**
	 * Saves board representation to file which can later be read back in.
	 * 
	 * @param board
	 * @return true if successful, else false
	 */
	public boolean dump(Board board){
		byte[] squares = board.getSquares();
		PrintWriter out = writerOpen();
		if (out == null) return false;
		for ( int i = 0; i < 128; i++ ){
			if ( ( i & 0x88 ) == 0 ){
				if ( ( i % 8 == 0 && i != 0) )out.println();
				out.print(squares[i]);
				out.print(" ");
			}
		}
		out.close();
		return true;
	}
	
	/**
   * Checks byte piece is a valid piece
   * 
   * @param byte piece - to be tested
   * @return byte piece - if successful
   * @throws NumberFormatException
   *           - if piece is invalid
   */
	private byte isValid( byte piece ) throws NumberFormatException{
		if ( !( ( piece >=0 ) && ( piece < 15 ) ) )throw new NumberFormatException();
		else return piece;
	}
	
	
	/**
	* Opens filename for reading from
	* 
	* @return scanner of filename
	*/
	private Scanner readerOpen(){
		Scanner file = null;
		try {
			file = new Scanner( new FileInputStream( this.filename ) );;
		} catch ( FileNotFoundException e ) {
			System.err.printf( "Error: %s not found.\n", this.filename );
		}
		return file;
	}
	
	
	
	 /**
   * Opens filename for writing to. Uses a BufferedWriter to improve efficiency,
   * instead of writing each byte to the file it buffers them and then writes to
   * the file, reducing the amount of writes.
   * 
   * @return PrintWriter of filename
   */
	private PrintWriter writerOpen() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(
				new BufferedWriter( new FileWriter( this.filename ) ) );
			} catch ( IOException e ) {
				System.err.printf( "Error: Couldn't open/create %s", filename );
			}	
		return out;
	}
}
