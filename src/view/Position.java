package view;

public class Position {
	private int x;
	private int y;
	private String xString;
	private int yString;
	private int squarePosition;

	/**
	 * calculates the graphical position out of the byte-square-board
	 * @param squarePos
	 */
	public Position(int squarePos){
	  this.x=(squarePos%16)+1;
	  this.y=intMe(((squarePos-(squarePos%16))/16)+1);
	  this.yString=intMe(this.y);
	  this.xString=stringMe(this.x);
    this.squarePosition=squarePos;
	}
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
		this.xString=stringMe(x);
		this.yString = intMe(y);
		this.squarePosition = ((this.x-1))+(intMe(this.y)-1)*16;
	}

	public Position(String x, int y) {
		this.xString = x;
		this.yString = y;
		this.x=intMe(x.charAt(0));
		this.y = intMe(y);
		this.squarePosition = ((this.x-1))+(intMe(this.y)-1)*16;
	}

	public Position(String string) {
		char[] c = string.toCharArray();
		if(c.length==2){
			this.yString=Character.digit(c[1], 10);
			this.xString = String.valueOf(c[0]);
			this.x=intMe(c[0]);
			this.y = intMe(Character.digit(c[1], 10));
			this.squarePosition = ((this.x-1))+(intMe(this.y)-1)*16;
		}
		
	}


	private int intMe(char valueOf) {
		switch (valueOf){
		case 'a': return 1;
		case 'b': return 2;
		case 'c': return 3;
		case 'd': return 4;
		case 'e': return 5;
		case 'f': return 6;
		case 'g': return 7;
		case 'h': return 8;
		default: return 0;
				
		}
	}

	private int intMe(int value) {
		int zahl = value;
		switch (zahl) {
		case 1:
			return 8;
		case 2:
			return 7;
		case 3:
			return 6;
		case 4:
			return 5;
		case 5:
			return 4;
		case 6:
			return 3;
		case 7:
			return 2;
		case 8:
			return 1;
		default:
			return 0;

		}

	}

	private String stringMe(int xValue) {

		int zahl = xValue;
		switch (zahl) {
		case 1:
			return "a";
		case 2:
			return  "b";
		case 3:
			return  "c";
		case 4:
			return  "d";
		case 5:
			return  "e";
		case 6:
			return  "f";
		case 7:
			return  "g";
		case 8:
			return  "h";
		default:
			return null;

		}

	}

	

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getXName() {
		return xString;
	}

	public int getYName() {
		return yString;
	}
	
	public int getSquarePosition(){
	  return squarePosition;
	}

	public String getName() {
		if (xString != "null" && yString != 0) {
			return xString + yString;
		} else
			return ("null");
	}

}
