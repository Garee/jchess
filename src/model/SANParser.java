package model;
import static lookup.Pieces.*;

import java.util.ArrayList;

/**
 * Parser from SAN notation into jMove notation (jMove is our move type)
 * 
 * @author fergus, andrew
 * 
 */
public class SANParser {

  private Board board;
  private int to;

  public SANParser() {
  }

  /**
   * 
   * @param board
   * @param san
   */
  protected Move sanToJ( Board board, byte colour, String san ) {
    /**
     * e3 Ne3 Nfe3 N3e3 Nf3e3
     * 
     * 
     */
    this.board = board;
    san = san.trim().replace( "x", "" ).replace( "+", "" ).replace( "#", "" );
    System.out.println( san );
    to = Move.notationToIndex( san.substring( san.length() - 2 ) );

    if ( san.length() == 2 )
      return new Move( getPawnOrigin( san, colour ), to );
    else return new Move( decide( san, colour ), to );

  }

  private int decide( String san, byte colour ) {
    switch ( san.charAt( 0 ) ) {
      case 'K':
        return getKingOrigin( san.substring( 1 ), colour );
      case 'Q':
        return getQueenOrigin( san.substring( 1 ), colour );
      case 'B':
        return getBishopOrigin( san.substring( 1 ), colour );
      case 'N':
        return getKnightOrigin( san.substring( 1 ), colour );
      case 'R':
        return getRookOrigin( san.substring( 1 ), colour );
      default:
        return getPawnOrigin( san, colour );
    }
  }

  /**
   * generateRookDestinations(to) for each destination if (
   * board.pieceTypeAt(destination) == ROOK && board.pieceColourAt(colour){ if
   * (from.length ==2) return destination; if (from.length ==3){ else if (
   * from.charAt(0).isAlpha && from.charAt(0) == destination.file ) return
   * destination; else if ( from charAt(0).isNum && from.charAt(0) ==
   * destination.rank ) return destination; else if (from.charAt(0) ==
   * destination.file && from.charAt(1) == destination.rank return destination;
   * }
   */

  private int getRookOrigin( String san, byte colour ) {

    ArrayList<Integer> destinations = board.generateRookDestinations( to );
    for ( int origin : destinations ) {
      if ( board.pieceTypeAt( origin ) == ROOK
          && board.pieceColourAt( origin ) == colour ) {
        if ( san.length() == 2 )
          return origin;
        else if ( san.length() == 3 ) {
          if ( san.substring( 0, 1 ).matches( "[a-h]" )
              && san.charAt( 0 ) == getFile( origin ) )
            return origin;
          else if ( san.substring( 0, 1 ).matches( "[0-7]" )
              && san.charAt( 0 ) == getRank( origin ) ) return origin;
        } else if ( san.charAt( 0 ) == getFile( origin )
            && san.charAt( 0 ) == getRank( origin ) ) return origin;
      }
    }

    return - 1;
  }

  /**
   * 
   * @param san
   * @param colour
   * @return origin of knight
   */

  private int getKnightOrigin( String san, byte colour ) {

    ArrayList<Integer> destinations = board.generateKnightDestinations( to );
    for ( int origin : destinations ) {
      if ( board.pieceTypeAt( origin ) == KNIGHT
          && board.pieceColourAt( origin ) == colour ) {
        if ( san.length() == 2 )
          return origin;
        else if ( san.length() == 3 ) {
          if ( san.substring( 0, 1 ).matches( "[a-h]" )
              && san.charAt( 0 ) == getFile( origin ) )
            return origin;
          else if ( san.substring( 0, 1 ).matches( "[0-7]" )
              && san.charAt( 0 ) == getRank( origin ) ) return origin;
        } else if ( san.charAt( 0 ) == getFile( origin )
            && san.charAt( 0 ) == getRank( origin ) ) return origin;
      }
    }

    return - 1;
  }

  /**
   * 
   * @param san
   * @param colour
   * @return origin of bishop
   */
  private int getBishopOrigin( String san, byte colour ) {
    ArrayList<Integer> destinations = board.generateBishopDestinations( to );
    for ( int origin : destinations ) {
      if ( board.pieceTypeAt( origin ) == BISHOP
          && board.pieceColourAt( origin ) == colour ) {
        if ( san.length() == 2 )
          return origin;
        else if ( san.length() == 3 ) {
          if ( san.substring( 0, 1 ).matches( "[a-h]" )
              && san.charAt( 0 ) == getFile( origin ) )
            return origin;
          else if ( san.substring( 0, 1 ).matches( "[0-7]" )
              && san.charAt( 0 ) == getRank( origin ) ) return origin;
        } else if ( san.charAt( 0 ) == getFile( origin )
            && san.charAt( 0 ) == getRank( origin ) ) return origin;
      }
    }

    return - 1;
  }

  /**
   * 
   * @param san
   * @param colour
   * @return origin of queen
   */

  private int getQueenOrigin( String san, byte colour ) {
    ArrayList<Integer> destinations = board.generateQueenDestinations( to );
    for ( int origin : destinations ) {
      if ( board.pieceTypeAt( origin ) == QUEEN
          && board.pieceColourAt( origin ) == colour ) {
        if ( san.length() == 2 )
          return origin;
        else if ( san.length() == 3 ) {
          if ( san.substring( 0, 1 ).matches( "[a-h]" )
              && san.charAt( 0 ) == getFile( origin ) )
            return origin;
          else if ( san.substring( 0, 1 ).matches( "[0-7]" )
              && san.charAt( 0 ) == getRank( origin ) ) return origin;
        } else if ( san.charAt( 0 ) == getFile( origin )
            && san.charAt( 0 ) == getRank( origin ) ) return origin;
      }
    }

    return - 1;
  }

  /**
   * 
   * @param san
   * @param colour
   * @return origin of king
   */

  private int getKingOrigin( String san, byte colour ) {
    ArrayList<Integer> destinations = board.generateKingDestinations( to );
    for ( int origin : destinations ) {
      if ( board.pieceTypeAt( origin ) == KING
          && board.pieceColourAt( origin ) == colour ) {
        if ( san.length() == 2 )
          return origin;
        else if ( san.length() == 3 ) {
          if ( san.substring( 0, 1 ).matches( "[a-h]" )
              && san.charAt( 0 ) == getFile( origin ) )
            return origin;
          else if ( san.substring( 0, 1 ).matches( "[0-7]" )
              && san.charAt( 0 ) == getRank( origin ) ) return origin;
        } else if ( san.charAt( 0 ) == getFile( origin )
            && san.charAt( 0 ) == getRank( origin ) ) return origin;
      }
    }

    return - 1;
  }

  /**
   * 
   * @param san
   * @param colour
   * @return origin of pawn
   */
  private int getPawnOrigin( String san, byte colour ) {

    ArrayList<Integer> destinations;
    if ( colour == WHITE )
      destinations = board.generateBlackPawnDestinations( to );
    // Uses opposites colour generator as pawns can't move backwards
    else destinations = board.generateWhitePawnDestinations( to );

    for ( int origin : destinations ) {
      if ( board.pieceTypeAt( origin ) == PAWN
          && board.pieceColourAt( origin ) == colour ) {
        if ( san.length() == 2 )
          return origin;
        else if ( san.length() == 3 ) {
          if ( san.substring( 0, 1 ).matches( "[a-h]" )
              && san.charAt( 0 ) == getFile( origin ) )
            return origin;
          else if ( san.substring( 0, 1 ).matches( "[0-7]" )
              && san.charAt( 0 ) == getRank( origin ) ) return origin;
        } else if ( san.charAt( 0 ) == getFile( origin )
            && san.charAt( 0 ) == getRank( origin ) ) return origin;
      }
    }

    return - 1;
  }

  private char getFile( int position ) {
    return ( intToLetter( position % 16 ) );
  }

  private int getRank( int position ) {
    return position / 16;
  }

  private int letterToInt( char c ) {
    return ( c - 'a' );
  }

  private char intToLetter( int i ) {
    return ( ( char ) ( i + 'a' ) );
  }
}
