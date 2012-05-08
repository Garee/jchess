package lookup;

public final class Pieces {

  // White Pieces
  public static final byte EMPTY = 0;
  public static final byte PAWN = 1;
  public static final byte KNIGHT = 2;
  public static final byte BISHOP = 3;
  public static final byte ROOK = 4;
  public static final byte QUEEN = 5;
  public static final byte KING = 6;

  /* The fourth bit represents the colour of the piece therefore
     add eight to each constant to represent black. */
  public static final byte BPAWN = 9;
  public static final byte BKNIGHT = 10;
  public static final byte BBISHOP = 11;
  public static final byte BROOK = 12;
  public static final byte BQUEEN = 13;
  public static final byte BKING = 14;

  // Bit 3 is 0 for white therefore 00000000 when masked.
  public static final byte WHITE = 0;
  public static final byte BLACK = 8;

  // Bit 4 is 1 when a piece has moved therefore 00010000 when masked.
  public static final byte MOVED = 0x10;

  /* When you bitwiseAND a board position with 10001000 using the 0x88 board representation
   * the destination is valid if the result is 0. */
  public static final byte VALID = 0;

  // The total number of pieces at the start of a chess game.
  public static final int N_STARTING_PIECES = 32;
}