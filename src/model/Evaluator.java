/** OLD EVALUATION CLASS, AI USES NEW EVALUATOR IN SUB PACKAGE evaluators */

package model;

import static lookup.Pieces.*;

public abstract class Evaluator {
  protected String evaluatorName;
  private static int BADBISHOPSCORE = 10;
  private static int BADKNIGHTSCORE = 10;
  private static int SAFETYSCORE = 10;

  /**
   * A simple constructor for the Evaluator
   */
  public Evaluator() {
  };

  /**
   * Main evaluation method When called evaluates a given board using any
   * defined methods.
   *
   * <p>
   * i.e. Material evaluator
   * </p>
   *
   * @param board
   *          to evaluate.
   * @return resultant score from evaluation.
   */
  public abstract int evaluate(Board board);

  /**
   * Gets name of evaluator.
   * <p>
   * Might be useful for comparing/debugging.
   * </p>
   *
   * @return evaluator name
   */
  public String getName() {
    return this.evaluatorName;
  }

  protected int materialBalance(Board board) {
    return board.evaluateMaterial ();
  }

  /**
   * @author Aleksandr Radevic
   * @param board
   * @return score for current board position relative to Bishop location
   */
  protected int badBishops(Board board) {
    return badBishopsEval (board);
  }

  /**
   * @author Aleksandr Radevic
   * @param board
   * @return score for current board position relative to Knight location
   */
  protected int badKnights(Board board) {
    return badKnightsEval (board);
  }

  /**
   * @author Aleksandr Radevic
   * @param board
   * @return score for current board
   */
  protected int pieceDevelopment(Board board) {
    return pieceDevelopmentEval (board);
  }

  /**
   * @author Aleksandr Radevic
   * @param board
   * @return score for current board position relative to King location
   */
  protected int kingSafety(Board node) {
    int total = 0;
    int position;
    byte squares[] = node.getSquares ();
    // analyze white
    if ( node.getTurnColour () == WHITE ) {
      position = node.getWhiteKingPosition ();
      // check if King is surrounded by white pawns
      for (int i = 15; i < 18; i++) {
        if ( node.pieceTypeAt (position + i) == PAWN ) {
          total += SAFETYSCORE;
        }
      }
    }
    else {
      position = node.getBlackKingPosition ();
      for (int i = 15; i < 18; i++) {
        if ( node.pieceTypeAt (position - i) == BPAWN ) {
          total += SAFETYSCORE;
        }
      }
    }
    return total;
  }

  private int badBishopsEval(Board node) {
    int score = 0;

    if ( node.getTurnColour () == WHITE ) {
      // find bishop
      for (int rank = 0; rank < 8; rank++)
        for (int file = rank * 16 + 7; file >= ( rank * 16 ); file--)
          // if we found bishop
          if ( node.pieceColourAt (file) == WHITE
              && node.pieceTypeAt (file) == BISHOP ) {
            // if bishop is surrounded by the same color pawns penalize
            if ( node.pieceTypeAt (file + 17) == PAWN
                || node.pieceTypeAt (file + 15) == PAWN )
              score -= BADBISHOPSCORE;
          }

    }
    else {
      if ( node.getTurnColour () == BLACK ) {
        // find bishop
        for (int rank = 0; rank < 8; rank++)
          for (int file = rank * 16 + 7; file >= ( rank * 16 ); file--)

            if ( node.pieceColourAt (file) == BLACK
                && node.pieceTypeAt (file) == BBISHOP ) {
              if ( ( file - 17 ) > 0 ) {
                byte squares[] = node.getSquares ();
                if ( node.pieceTypeAt (file - 17) == BPAWN
                    || node.pieceTypeAt (file - 15) == BPAWN )
                  score -= BADBISHOPSCORE;
              }
            }
      }
    }
    return score;
  }

  private int badKnightsEval(Board node) {
    int score = 0;

    if ( node.getTurnColour () == WHITE ) {

      // if we find knight on the right side, penalize
      for (int j = 0; j < 112; j += 16)
        if ( node.pieceColourAt (j) == WHITE && node.pieceTypeAt (j) == KNIGHT )
          score -= BADKNIGHTSCORE;
      // if we find knight on the left side, penalize
      for (int j = 7; j < 120; j += 16)
        if ( node.pieceColourAt (j) == WHITE && node.pieceTypeAt (j) == KNIGHT )
          score -= BADKNIGHTSCORE;
    }
    else {

      for (int j = 0; j < 112; j += 16)
        if ( node.pieceColourAt (j) == BLACK && node.pieceTypeAt (j) == BKNIGHT )
          score -= BADKNIGHTSCORE;
      for (int j = 7; j < 120; j += 16)
        if ( node.pieceColourAt (j) == BLACK && node.pieceTypeAt (j) == BKNIGHT )
          score -= BADKNIGHTSCORE;
    }

    return score;
  }

  protected int pieceDevelopmentEval(Board node) {
    int score = 0;
    // analyze white AI
    if ( node.getTurnColour () == WHITE ) {
      // if AI has not advanced center pawn
    if ( node.getSquares ()[19] == PAWN || node.getSquares ()[20] == PAWN )
        score -= 20;
    else score += pieceDevelopmentEvalII(node);
    }
    else {
      if ( node.getSquares ()[99] == BPAWN || node.getSquares ()[100] == BPAWN )
        score -= 20;
      else score += pieceDevelopmentEvalII(node);
    }
    return score;
  }

  /**
   * @author Aleksandr Radevic
   * @param node
   * @return score for Knight possible destinations
   */
  protected int pieceDevelopmentEvalII(Board node) {
    int score = 0;

    if ( node.getTurnColour () == WHITE ) {

      for (int rank = 0; rank < 8; rank++)
        for (int file = rank * 16 + 7; file >= ( rank * 16 ); file--) {
          if ( node.pieceColourAt (file) == WHITE
              && node.pieceTypeAt (file) == KNIGHT ) {
            // pawn e2-e4
            // generate Knight destinations
            if ( node.generateKnightDestinations (file).contains ((Object) 68) ) {
              score += 40;
            } else
              score -= 20;
          }
        }
      // analyze black AI
    }
    else {

      for (int rank = 0; rank < 8; rank++)
        for (int file = rank * 16 + 7; file >= ( rank * 16 ); file--) {
          if ( node.pieceColourAt (file) == BLACK
              && node.pieceTypeAt (file) == PAWN ) {
            // generate Knight destinations
            // pawn e7-e5
            if ( node.generateKnightDestinations (file).contains ((Object) 52)
                || node.generateKnightDestinations (file)
                    .contains ((Object) 51) ) {
              score += 40;
            } else {
              score -= 20;
            }
          }
        }
    }
    return score;
  }
}
