package model;

import static lookup.Pieces.*;
import static lookup.Masks.*;
import static lookup.Coordinates.*;
import static lookup.PieceTables.*;

import java.util.ArrayList;

/**
 * A Chess Board implementation using the 0x88 board representation.
 *
 * @author Gary Blackwood
 */
public class Board {

  private byte[] squares;
  private byte turnColour;
  private Move previousMove;
  private int whiteKingPosition;
  private int blackKingPosition;
  private ArrayList<Move> validMoves;
  private ArrayList<Byte> whitePiecesCaptured;
  private ArrayList<Byte> blackPiecesCaptured;
  private int score;
  private int amountOfMoves;

  /**
   * Initialise and create the board to contain chess pieces arranged in an order such that the resulting positions represent
   * a valid chess starting position.
   */
  public Board() {
    this.squares = new byte[]{ ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                               PAWN,  PAWN,  PAWN,  PAWN,  PAWN,  PAWN,  PAWN,  PAWN,   EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                               EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,  EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                               EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,  EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                               EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,  EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                               EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,  EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                               BPAWN, BPAWN, BPAWN, BPAWN, BPAWN, BPAWN, BPAWN, BPAWN,  EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                               BROOK, BKNIGHT, BBISHOP, BQUEEN, BKING, BBISHOP, BKNIGHT, BROOK, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY };

    this.turnColour = WHITE;
    this.previousMove = null;
    this.whiteKingPosition = E1;
    this.blackKingPosition = E8;
    this.validMoves = generateValidMoves();
    this.whitePiecesCaptured = new ArrayList<Byte>();
    this.blackPiecesCaptured = new ArrayList<Byte>();
    this.score = 0;
    this.amountOfMoves=0;
  }

  /**
   * Construct a new chess board which is a copy of a supplied board.
   *
   * @param board    The chess board to copy.
   */
  public Board( Board board ) {
    this.squares = board.getSquares();
    this.turnColour = board.getTurnColour();
    this.previousMove = board.getPreviousMove();
    this.whiteKingPosition = board.getWhiteKingPosition();
    this.blackKingPosition = board.getBlackKingPosition();
    this.validMoves = board.getValidMoves();
    this.whitePiecesCaptured = board.getWhitePiecesCaptured();
    this.blackPiecesCaptured = board.getBlackPiecesCaptured();
    this.score = board.getScore();
    this.amountOfMoves = board.getAmountOfMoves();
  }

  /**
   * Returns a list of all white pieces captured by the black player.
   *
   * @return A list of all white pieces captured.
   */
  private ArrayList<Byte> getWhitePiecesCaptured() {
    return ( new ArrayList<Byte>( this.whitePiecesCaptured ) );
  }

  /**
   * Returns a list of all black pieces captured by the white player.
   *
   * @return A list of all black pieces captured.
   */
  private ArrayList<Byte> getBlackPiecesCaptured() {
    return ( new ArrayList<Byte>( this.blackPiecesCaptured ) );
  }

  /**
   * Return the zero sum material score of the board.
   *
   * @return The zero sum material score.
   */
  private int getScore() {
    return ( this.score );
  }

  /**
   * Return the byte array of chess board squares.
   *
   * @return The array of chess board squares as bytes.
   */
  public byte[] getSquares() {
    return ( this.squares.clone() );
  }

  /**
   * What is the colour of the player to move?
   *
   * @return A byte representing the colour of the player whos turn it is currently.
   */
  public byte getTurnColour() {
    return ( this.turnColour );
  }

  /**
   * How many moves have been made since board creation?
   *
   * @return The number of moves made since the board was created.
   */
  public int getAmountOfMoves() {
    return this.amountOfMoves;
  }

  /**
   * Get the last move made on the board.
   *
   * @return The last move that was made on the board.
   */
  private Move getPreviousMove() {
    return ( this.previousMove );
  }

  /**
   * Which square is the white king located on?
   *
   * @return The square index of the white king position.
   */
  public int getWhiteKingPosition() {
    return ( this.whiteKingPosition );
  }

  /**
   * Which square is the black king located on?
   *
   * @return The square index of the black king position.
   */
  public int getBlackKingPosition() {
    return ( this.blackKingPosition );
  }

  /**
   * Which square is the current players king on?
   *
   * @return The square index of the current players king.
   */
  public int getKingPosition() {
    return ( isWhiteTurn() ? this.whiteKingPosition : this.blackKingPosition );
  }

  /**
   * Which square is the opponent of the current players king on?
   *
   * @return The square index of opponent of the current players king.
   */
  private int getOpposingKingPosition() {
    return ( isWhiteTurn() ? this.blackKingPosition : this.whiteKingPosition );
  }

  /**
   * Is the square mapped to by the given index empty?
   *
   * @param position    The square index.
   *
   * @return True if there are no pieces on the square, false otherwise.
   */
  private boolean squareEmpty( int position ) {
    return ( pieceAt( position ) == EMPTY );
  }

  /**
   * Get the piece located on the square mapped to by the given index.
   *
   * @param position    The square index.
   *
   * @return A byte representation of the piece located at 'position'.
   */
  private byte pieceAt( int position ) {
    if ( ( position & 0x88 ) == VALID ) {
      return ( this.squares[ position ] );
    }

    return ( EMPTY );
  }

  /**
   * What colour is the piece on the square mapped to by the given index?
   *
   * @param position    The square index.
   *
   * @return The colour, as a byte, of the piece.
   */
  public byte pieceColourAt( int position ) {
    return ( (byte)( pieceAt( position ) & COLOUR_MASK ) );
  }

  /**
   * What is the type of the given piece?
   *
   * @param piece    The piece to check.
   *
   * @return A byte representing the piece type.
   */
  public static byte pieceType( byte piece ) {
    return ( (byte)( piece & PIECE_MASK ) );
  }

  /**
   * What is the colour of the given piece?
   *
   * @param piece    The piece to check.
   *
   * @return A byte representing the piece colour.
   */
  public static byte pieceColour( byte piece ) {
    return ( (byte)( piece & COLOUR_MASK ) );
  }
  
  /**
   * What is the type of the piece located at the given square index?
   *
   * @param position    The square index.
   *
   * @return A byte representing the piece type.
   */
  public byte pieceTypeAt( int position ) {
    return ( (byte)( pieceAt( position ) & PIECE_MASK ) );
  }
  
  /**
   * Is the given square index within the playable game area?
   *
   * @param destination    The square index to check.
   *
   * @return True if the destination square index is on the playable chess board, false otherwise.
   */ 
  private boolean isValidDestination( int destination ) {
    return ( ( ( destination & 0x88 ) == VALID ) &&
             ( squareEmpty( destination ) || ( pieceColourAt( destination ) != this.turnColour ) ) );
  }

  /**
   * Is the piece located at the given square index white?
   *
   * @param position    The square index.
   *
   * @return True if the piece is white, false otherwise.
   */
  private boolean isWhitePiece( int position ) {
    return ( pieceColourAt( position ) == WHITE );
  }

  /**
   * Can the given move be played on the chess board?
   *
   * @param move    The move to check the validity of.
   *
   * @return True if the move is valid, false otherwise.
   */
  public boolean isValidMove( Move move ) {
    return ( this.validMoves.contains( move ) );
  }
  
  /**
   * Set the bit that represents whether or not a piece has moved.
   *
   * @param position    The square index at which to set the bit.
   */
  private void setMovementBit( int position ) {
    this.squares[ position ] |= MOVED;
  }

  /**
   * Has a given piece made at least one move?
   *
   * @param piece    The piece to check.
   *
   * @return True if the piece has moved, false otherwise.
   */
  private static boolean hasPieceMoved( byte piece ) {
    return ( ( piece & MOVED_MASK ) == MOVED );
  }

  /**
   * Does the current board state represent a checkmate situation?
   *
   * @return True if checkmate, false otherwise.
   */
  public boolean isCheckmate() {
    return ( this.validMoves.size() == 0 && kingInCheck() );
  }
  
  /**
   * Does the current board state represent a stalemate situation?
   *
   * @return True if stalemate, false otherwise.
   */
  public boolean isStalemate() {
    return ( this.validMoves.size() == 0 && !kingInCheck() );
  }

  /**
   * Is is the white players turn to move?
   *
   * @return True if it is the white players turn to move, false otherwise.
   */
  private boolean isWhiteTurn() {
    return ( this.turnColour == WHITE );
  }

  /**
   * Set the position of the current players king to a new square.
   *
   * @param position    The new square index.
   */
  private void setKingPosition( int position ) {
    if ( isWhiteTurn() ) {
      this.whiteKingPosition = position;
    } else {
      this.blackKingPosition = position;
    }
  }

  /**
   * Return the colour of the current players opponent.
   *
   * @return A byte representation of the opponents colour.
   */
  private byte opponentColour() {
    return ( isWhiteTurn() ? BLACK : WHITE );
  }

  /**
   * Does the given move perform a king or queenside castle?
   *
   * @param move    The move to check.
   *
   * @return True if the move performs a castling, false otherwise.
   */
  private boolean isCastle( Move move ) {
    return ( isKingMove( move ) && Math.abs( move.to() - move.from() ) == 2 );
  }

  /**
   * Perform the castling move contained in 'move'.
   *
   * @param move    The castling move to perform.
   */
  private void performCastle( Move move ) {
    if ( move.to() > move.from() ) {
      performCastleKingSide( getKingPosition() );
    }
    else {
      performCastleQueenSide( getKingPosition() );
    }

    if ( isWhiteTurn() ) {
      score += 30;
    } else {
      score -= 30;
    }
    setKingPosition( move.to() );
  }

  /**
   * Perform a kingside castling for the king located at 'kingPosition'.
   *
   * @param kingPosition   The position of the king to castle.
   */
  private void performCastleKingSide( int kingPosition ) {
    this.squares[ kingPosition + 1 ] = this.squares[ kingPosition + 3 ];
    this.squares[ kingPosition + 3 ] = EMPTY;
    this.squares[ kingPosition + 2 ] = this.squares[ kingPosition ];
    this.squares[ kingPosition ] = EMPTY;
  }

  /**
   * Perform a queenside castling for the king located at 'kingPosition'.
   *
   * @param kingPosition   The position of the king to castle.
   */
  private void performCastleQueenSide( int kingPosition ) {
    this.squares[ kingPosition - 1 ] = this.squares[ kingPosition - 4 ];
    this.squares[ kingPosition - 4 ] = EMPTY;
    this.squares[ kingPosition - 2 ] = this.squares[ kingPosition ];
    this.squares[ kingPosition ] = EMPTY;
  }

  /**
   * Is the given move an en passent move?
   *
   * @param move    The move to check.
   *
   * @return True if the move performs en passent, false otherwise.
   */
  private boolean isEnPassant( Move move ) {
    return ( ( Math.abs( move.to() - move.from() ) == 17 || Math.abs( move.to() - move.from() ) == 15 ) && squareEmpty( move.to() ) && isPawnMove( move ) );
  }

  /**
   * Is a pawn being moved?
   *
   * @param move    The move to check.
   *
   * @return True if a pawn is being moved.
   */
  private boolean isPawnMove( Move move ) {
    return ( pieceTypeAt( move.from() ) == PAWN );
  }
  
  /**
   * Is a rook being moved?
   *
   * @param move    The move to check.
   *
   * @return True if a rook is being moved.
   */
  private boolean isRookMove( Move move ) {
    return ( pieceTypeAt( move.from() ) == ROOK );
  }

  /**
   * Is a king being moved?
   *
   * @param move     The move to check.
   *
   * @return True if a king is being moved.
   */
  private boolean isKingMove( Move move ) {
    return ( pieceTypeAt( move.from() ) == KING );
  }

  /**
   * Does the move result in a pawn promotion?
   *
   * @param move    The move to check.
   */ 
  private boolean isPawnPromotion( Move move ) {
    return ( isPawnMove( move ) && ( ( move.to() >= A1 && move.to() <= H1 ) || ( move.to() >= A8 && move.to() <= H8 ) ) );
  }

  /**
   * Promote the pawn located on the given square.
   *
   * @param pawnPosition    The square index of the pawn to promote.
   */
  private void promotePawn( int pawnPosition ) {
    this.squares[ pawnPosition ] = isWhiteTurn() ? QUEEN : BQUEEN;
  }

  /**
   * Perform the supplied move on the board.
   *
   * @param move    The move to make.
   */
  public void makeMove( Move move ) {
    setMovementBit( move.from() );
    if ( isPawnMove( move ) ) {
      setMovementBit( move.from() );

      if ( isEnPassant( move ) ) {
        if ( isWhiteTurn() ) {
          updateScore( move.to() - 16 );
          this.squares[ move.to() - 16 ] = EMPTY;
          this.squares[ move.to() ] = this.squares[ move.from() ];
          this.squares[ move.from() ] = EMPTY;
          this.blackPiecesCaptured.add( PAWN );
        }
        else {
          updateScore( move.to() + 16 );
          this.squares[ move.to() + 16 ] = EMPTY;
          this.squares[ move.to() ] = this.squares[ move.from() ];
          this.squares[ move.from() ] = EMPTY;
          this.whitePiecesCaptured.add( PAWN );
        }

        this.turnColour = opponentColour();
        this.previousMove = move;
        this.validMoves = generateValidMoves();
        return;
      } else if ( isPawnPromotion( move ) ) {
        promotePawn( move.from() );
        if ( isWhiteTurn() ) {
          score += 700;
        } else {
          score -= 700;
        }
      }
    } else if ( isKingMove( move ) ) {
      setMovementBit( move.from() );

      if ( isCastle( move ) ) {
        performCastle( move );
        setKingPosition( move.to() );
        this.turnColour = opponentColour();
        this.previousMove = move;
        this.validMoves = generateValidMoves();
        return;
      }

      setKingPosition( move.to() );
    } else if ( isRookMove( move ) ) {
      setMovementBit( move.from() );
    }

    updateScore( move.to() );

    if ( !squareEmpty( move.to() ) ) {
      if ( isWhiteTurn() ) {
        this.blackPiecesCaptured.add( this.squares[ move.to() ] );
      } else {
        this.whitePiecesCaptured.add( this.squares[ move.to() ] );
      }
    }

    this.squares[ move.to() ] = this.squares[ move.from() ];
    this.squares[ move.from() ] = EMPTY;

    this.turnColour = opponentColour();
    this.previousMove = move;
    this.validMoves = generateValidMoves();
    this.amountOfMoves++;
  }

  /**
   * What is the value of the piece located on the given square index?
   *
   * @param position    The index of the square the piece is on.
   *
   * @return A integer value of the piece worth.
   */
  private int pieceValueAt( int position ) {
    switch ( pieceTypeAt( position ) ) {
      case PAWN:   return ( 100 );
      case KNIGHT: return ( 325 );
      case BISHOP: return ( 330 );
      case ROOK:   return ( 500 );
      case QUEEN:  return ( 900 );
      case KING:   return ( 20000 );
    }

    return ( 0 );
  }

  /**
   * Update the zero sum material balance score.
   *
   * @param position    The index of the square at which a piece was captured.
   */
  private void updateScore( int position ) {
    if ( this.turnColour == WHITE ) {
      score += pieceValueAt( position );
    } else {
      score -= pieceValueAt( position );
    }
  }

  /**
   * Can the piece located at 'position' legally move to 'destination'?
   *
   * @param position    The index of the square the piece is currently on.
   * @param destination The index of the square the piece would like to move to.
   *
   * @return True if the piece can move to the destination, false otherwise.
   */ 
  private boolean canMoveTo( int position, int destination ) {
    byte p = this.squares[ position ];
    byte t = this.squares[ destination ];
    if ( pieceType( p ) == KING ) {
      setKingPosition( destination );
    }
    this.squares[ position ] = EMPTY;
    this.squares[ destination ] = p;

    boolean canMove = !kingInCheck();

    if ( pieceType( p ) == KING ) {
      setKingPosition( position );
    }
    this.squares[ position ] = p;
    this.squares[ destination ] = t;

    return ( canMove );
  }
  
  /** 
   * Is the current players king in check?
   *
   * @return True if the king is in check, false otherwise.
   */
  public boolean kingInCheck() {
    return ( squareAttacked( getKingPosition() ) );
  }

  /**
   * Is the square indexed by 'position' under attack from any opponent pieces?
   *
   * @param position    The index of the square to check.
   *
   * @return True if at least one piece is attacking the square, false otherwise.
   */ 
  private boolean squareAttacked( int position ) {
    int[] directions = new int[]{ 15, 17, -15, -17 };
    for ( int direction : directions ) {
      for ( int i = 1; isValidDestination( position + i*direction ); i++ ) {
        if ( enemyPieceAt( position + i*direction ) ) {
          if ( pieceTypeAt( position + i*direction ) == QUEEN || pieceTypeAt( position + i*direction ) == BISHOP ) {
            return ( true );
          } else if ( pieceTypeAt( position + i*direction ) == PAWN && i == 1 ) {
            if ( this.turnColour == WHITE ) {
              return ( direction == 15 || direction == 17 );
            }
            return ( direction == -15 || direction == -17 );
          }
          break;
        }
      }
    }

    for ( int direction : new int[]{ 1, -1, 16, -16 } ) {
      for ( int i = 1; isValidDestination( position + i*direction ); i++ ) {
        if ( enemyPieceAt( position + i*direction ) ) {
          if ( pieceTypeAt( position + i*direction ) == ROOK || pieceTypeAt( position + i*direction ) == QUEEN ) {
            return ( true );
          }
          break;
        }
      }
    }

    directions = new int[]{ 18, 33, 31, 14, -18, -33, -31, -14 };
    for ( int direction : directions ) {
      if ( isValidDestination( position + direction ) && enemyPieceAt( position + direction ) && pieceTypeAt( position + direction ) == KNIGHT ) {
        return ( true );
      }
    }

    return ( false );
  }

  /**
   * Obtain a list of all valid moves that can be played on the current board position.
   *
   * @return An ArrayList of all valid moves that can be played.
   */
  public ArrayList<Move> getValidMoves() {
    return ( new ArrayList<Move>( this.validMoves ) );
  }

  private ArrayList<Move> generateValidMoves() {
    ArrayList<Move> validMoves = new ArrayList<Move>();

    for ( int rank = 0; rank < 8; rank++ ) {
      for ( int file = rank * 16 + 7; file >= rank * 16; file-- ) {
        if ( !squareEmpty( file ) && pieceColourAt( file ) == this.turnColour ) {
          validMoves.addAll( generateValidMoves( pieceTypeAt( file ), file ) );
        }
      }
    }


    return ( validMoves );
  }

  /**
   * Generate all valid moves for the piece of type 'pieceType' located on square 'position'.
   *
   * @param pieceType    The type of the piece to generate moves for.
   * @param position     The index of the current square position of the piece.
   *
   * @return An ArrayList of all valid moves that the piece can make.
   */
  public ArrayList<Move> generateValidMoves( byte pieceType, int position ) {
    ArrayList<Move> validMoves = new ArrayList<Move>();
    ArrayList<Integer> destinations = generateDestinations( pieceType, position );

    for ( int destination : destinations ) {
      if ( isValidDestination( destination ) && canMoveTo( position, destination ) ) {
        validMoves.add( new Move( position, destination ) );
      }
    }

    return ( validMoves );
  }
  

  /**
   * Generate all destinations for the piece of type 'pieceType' located on square 'position'.
   *
   * @param pieceType    The type of the piece to generate moves for.
   * @param position     The index of the current square position of the piece.
   *
   * @return An ArrayList of all destinations that the piece can move to.
   */
  private ArrayList<Integer> generateDestinations( byte pieceType, int position ) {
    switch ( pieceType ) {
      case PAWN:   return ( generatePawnDestinations( position ) );
      case KNIGHT: return ( generateKnightDestinations( position ) );
      case BISHOP: return ( generateBishopDestinations( position ) );
      case ROOK:   return ( generateRookDestinations( position ) );
      case QUEEN:  return ( generateQueenDestinations( position ) );
      case KING:   return ( generateKingDestinations( position ) );
    }

    return ( new ArrayList<Integer>() );
  }

  /**
   * Generate the destinations for a pawn located at 'position'.
   *
   * @param position    The index of the square the pawn is located.
   *
   * @return An ArrayList of all destinations that the pawn can move to.
   */
  private ArrayList<Integer> generatePawnDestinations( int position ) {
    if ( isWhitePiece( position ) ) {
      return ( generateWhitePawnDestinations( position ) );
    }

    return ( generateBlackPawnDestinations( position ) );
  }

  /**
   * Generate the destinations for a white pawn located at 'position'.
   *
   * @param position    The index of the square the white pawn is located.
   *
   * @return An ArrayList of all destinations that the white pawn can move to.
   */
  public ArrayList<Integer> generateWhitePawnDestinations( int position ) {
    ArrayList<Integer> destinations = new ArrayList<Integer>();

    if ( !hasPieceMoved( pieceAt( position ) ) && squareEmpty( position + 16 ) && squareEmpty( position + 32 ) ) {
      destinations.add( position + 32 );
    }
    if ( !squareEmpty( position + 15 ) || whiteCanEnPassantLeft( position ) ) {
      destinations.add( position + 15 );
    }
    if ( !squareEmpty( position + 17 ) || whiteCanEnPassantRight( position ) ) {
      destinations.add( position + 17 );
    }

    if ( squareEmpty( position + 16 ) ) {
      destinations.add( position + 16 );
    }

    return ( destinations );
  }

  /**
   * Can the white pawn located at 'position' perform en passent to the left?
   *
   * @param position    The index of the square the white pawn is located.
   *
   * @return True if the pawn can en passent left, false otherwise.
   */
  private boolean whiteCanEnPassantLeft( int position ) {
    return ( this.previousMove != null && pieceTypeAt( position - 1 ) == PAWN && this.previousMove.from() == ( position + 31 ) ) && this.previousMove.to() == ( position - 1 );
  }

  /**
   * Can the white pawn located at 'position' perform en passent to the right?
   *
   * @param position    The index of the square the white pawn is located.
   *
   * @return True if the pawn can en passent right, false otherwise.
   */
  private boolean whiteCanEnPassantRight( int position ) {
    return ( this.previousMove != null && pieceTypeAt( position + 1 ) == PAWN && this.previousMove.from() == ( position + 33 ) ) && this.previousMove.to() == ( position + 1 );
  }

  /**
   * Generate the destinations for a black pawn located at 'position'.
   *
   * @param position    The index of the square the black pawn is located.
   *
   * @return An ArrayList of all destinations that the black pawn can move to.
   */
  public ArrayList<Integer> generateBlackPawnDestinations( int position ) {
    ArrayList<Integer> destinations = new ArrayList<Integer>();

    if ( !hasPieceMoved( pieceAt( position ) ) && squareEmpty( position - 16 ) && squareEmpty( position - 32 ) ) {
      destinations.add( position - 32 );
    }
    if ( !squareEmpty( position - 15 ) || blackCanEnPassantLeft( position ) ) {
      destinations.add( position - 15 );
    }
    if ( !squareEmpty( position - 17 ) || blackCanEnPassantRight( position ) ) {
      destinations.add( position - 17 );
    }

    if ( squareEmpty( position - 16 ) ) {
      destinations.add( position - 16 );
    }

    return ( destinations );
  }

  /**
   * Can the black pawn located at 'position' perform en passent to the left?
   *
   * @param position    The index of the square the black pawn is located.
   *
   * @return True if the pawn can en passent left, false otherwise.
   */
  private boolean blackCanEnPassantLeft( int position ) {
    return ( this.previousMove != null && pieceTypeAt( position + 1 ) == PAWN && this.previousMove.from() == ( position - 31 ) && this.previousMove.to() == ( position + 1 ) );
  }

  /**
   * Can the black pawn located at 'position' perform en passent to the right?
   *
   * @param position    The index of the square the black pawn is located.
   *
   * @return True if the pawn can en passent right, false otherwise.
   */
  private boolean blackCanEnPassantRight( int position ) {
    return ( this.previousMove != null && pieceTypeAt( position - 1 ) == PAWN && this.previousMove.from() == ( position - 33 ) && this.previousMove.to() == ( position -1 ) );
  }

  /**
   * Generate the destinations for a king located at 'position'.
   *
   * @param position    The index of the square the king is located on.
   *
   * @return An ArrayList of all destinations that the king can move to.
   */
  public ArrayList<Integer> generateKingDestinations( int position ) {
    ArrayList<Integer> destinations = new ArrayList<Integer>();

    if ( canCastleKingSide( position ) ) {
      destinations.add( position + 2 );
    }
    if ( canCastleQueenSide( position ) ) {
      destinations.add( position - 2 );
    }

    for ( int i : new int[]{ 15, 16, 17, 1, -1, -17, -16, -15 } ) {
      if ( !nextToOpponentKing( position + i ) ) {
        destinations.add( position + i );
      }
    }

    return ( destinations );
  }

  /**
   * Can the king located at 'position' perform a king side castling move?
   *
   * @return True if the king can castle, false otherwise.
   */
  private boolean canCastleKingSide( int position ) {
    return ( !hasPieceMoved( pieceAt( position ) ) && !kingInCheck() && pieceTypeAt( position + 3 ) == ROOK && !hasPieceMoved( pieceAt( position + 3 ) )
             && squareEmpty( position + 1 ) && !squareAttacked( position + 1 )
             && squareEmpty( position + 2 ) && !squareAttacked( position + 2 ) );
  }

  /**
   * Can the king located at 'position' perform a queen side castling move?
   *
   * @return True if the king can castle, false otherwise.
   */
  private boolean canCastleQueenSide( int position ) {
    return ( !hasPieceMoved( pieceAt( position ) ) && !kingInCheck() && pieceTypeAt( position - 4 ) == ROOK && !hasPieceMoved( pieceAt( position - 4 ) )
             && squareEmpty( position - 1 ) && !squareAttacked( position - 1 )
             && squareEmpty( position - 2 ) && !squareAttacked( position - 2 )
             && squareEmpty( position - 3 ) && !squareAttacked( position - 3 ) );
  }

  /**
   * Is the king located at 'position' adjacent to an opponents king?
   *
   * @param position    The location of the king to check.
   */
  private boolean nextToOpponentKing( int position ) {
    int[] offsets = new int[]{ 15, 16, 17, -1, 1, -15, -16, -17 };

    for ( int i : offsets ) {
      if ( ( position + i ) == getOpposingKingPosition() ) {
        return ( true );
      }
    }

    return ( false );
  }

  /**
   * Generate the destinations for a knight located at 'position'.
   *
   * @param position    The index of the square the knight is located on.
   *
   * @return An ArrayList of all destinations that the knight can move to.
   */
  public ArrayList<Integer> generateKnightDestinations( int position ) {
    ArrayList<Integer> destinations = new ArrayList<Integer>();

    for ( int d : new int[]{ 18, 33, 31, 14, -18, -33, -31, -14 } ) {
      destinations.add( position + d );
    }

    return ( destinations );
  }

  /**
   * Generate the destinations for a bishop located at 'position'.
   *
   * @param position    The index of the square the bishop is located on.
   *
   * @return An ArrayList of all destinations that the bishop can move to.
   */
  public ArrayList<Integer> generateBishopDestinations( int position ) {
    return ( generateUpDownDestinations( position, new int[]{ 15, 17, -15, -17 } ) );
  }

  /**
   * Generate the destinations for a rook located at 'position'.
   *
   * @param position    The index of the square the rook is located on.
   *
   * @return An ArrayList of all destinations that the rook can move to.
   */
  public ArrayList<Integer> generateRookDestinations( int position ) {
    return ( generateUpDownDestinations( position, new int[]{ 1, -1, 16, -16 } ) );
  }

  /**
   * Generate the destinations for a queen located at 'position'.
   *
   * @param position    The index of the square the queen is located on.
   *
   * @return An ArrayList of all destinations that the queen can move to.
   */
  public ArrayList<Integer> generateQueenDestinations( int position ) {
    return ( generateUpDownDestinations( position, new int[]{ 1, -1, 16, -16, 15, 17, -15, -17 } ) );
  }

  /**
   * Is there an enemy piece located at the given square index?
   *
   * @param position    The square index to check.
   *
   * @return True if an opponents piece is located at that position, false otherwise.
   */
  public boolean enemyPieceAt( int position ) {
    return ( !squareEmpty( position ) && pieceColourAt( position ) != this.turnColour );
  }

  /**
   * Multipurpose method that can generate destinations for sliding pieces.
   *
   * @param position    The square index of the locations of the piece.
   * @param directions  The directions in which the piece should be moving.
   *
   * @return An ArrayList of all destinations that the piece can move to.
   */
  private ArrayList<Integer> generateUpDownDestinations( int position, int[] directions ) {
    ArrayList<Integer> destinations = new ArrayList<Integer>();

    for ( int direction : directions ) {
      for ( int i = 1; isValidDestination( position + i*direction ); i++ ) {
        if ( enemyPieceAt( position + i*direction ) ) {
          destinations.add( position + i*direction );
          break;
        }

        destinations.add( position + i*direction );
      }
    }

    return ( destinations );
  }

  /*-----------------------------------------------*/
  /*--------------Evaluation Methods---------------*/
  /*-----------------------------------------------*/

  /**
   * Evaluate the material balance on the board.
   *
   * @return The zero sum material score.
   */
  public int evaluateMaterial() {
    return ( isWhiteTurn() ? this.score : -this.score );
  }

  /**
   * Evaluate the positional score of each piece on the board.
   *
   * @return The material score for the current player.
   */
  public int evaluatePiecePositions() {
    int score = 0;

    for ( int rank = 0; rank < 8; rank++ ) {
      for ( int file = rank * 16 + 7; file >= rank * 16; file-- ) {
        if ( !squareEmpty( file ) && pieceColourAt( file ) == this.turnColour ) {
          if ( isWhiteTurn() ) {
            score += piecePositionScore( pieceTypeAt( file ), file );
          } else {
            score -= piecePositionScore( pieceTypeAt( file ), file );
          }
        }
      }
    }

    return isWhiteTurn() ? score : -score;
  }

  /**
   * Give a score to a piece based on its position on the board.
   *
   * @return A score representing how good/bad the position of the piece is.
   */
  public int piecePositionScore( byte pieceType, int pos ) {
    switch ( pieceType ) {
      case PAWN:
        return isWhiteTurn() ? WPAWN_POSITION_TABLE[ pos ] : BPAWN_POSITION_TABLE[ pos ];
      case KNIGHT:
        return KNIGHT_POSITION_TABLE[ pos ];
      case BISHOP:
        return isWhiteTurn() ? WBISHOP_POSITION_TABLE[ pos ] : BBISHOP_POSITION_TABLE[ pos ];
      case ROOK:
        return isWhiteTurn() ? WROOK_POSITION_TABLE[ pos ] : BROOK_POSITION_TABLE[ pos ];
      case QUEEN:
        return this.amountOfMoves < 15 ? OPENING_QUEEN_POSITION_TABLE[ pos ] : QUEEN_POSITION_TABLE[ pos ];
      case KING:
        return isWhiteTurn() ? WKING_POSITION_TABLE[ pos ] : BKING_POSITION_TABLE[ pos ];
    }

    return 0;
  }

  /**
   * Evaluate the development of knights and bishops.
   *
   * @return A score representing how good the piece development of the current player is.
   */
  public int evaluatePieceDevelopment() {
    int score = 0;

    int[] minorPiecePositions = new int[]{ 1, 2, 5, 6 };
    int turn = isWhiteTurn() ? 0 : 112;

    for ( int pos : minorPiecePositions ) {
      if ( !squareEmpty( pos + turn ) && !hasPieceMoved( pieceAt( pos + turn ) ) ) {
        if ( isWhiteTurn() ) {
          score -= 50;
        } else {
          score += 50;
        }
      }
    }

    return isWhiteTurn() ? score : -score;
  }
}
