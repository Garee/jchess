package model;

import model.evaluators.EasyEvaluator;
import model.evaluators.MediumEvaluator;
import model.evaluators.Evaluator;
import model.evaluators.MediumEvaluator;

/**
 * The artificial intelligence player.
 *
 * @author Fergus Leahy
 * @author Gary Blackwood
 * @author Aleksandr Radevic
 */
public class AI extends Player {

  private int depth;
  private Evaluator evaluator;


  /**
   * Creates an AI with an extra parameter to select the evaluator and changed difficulty level to be the depth
   * @param depth
   * @param evaluatorToSelect
   * @param colour
   * @param name
   * @param timeRemaining
   */
  public AI(int depth,int evaluatorToSelect, byte colour, String name, long timeRemaining) {
    super( name, colour, timeRemaining );
    this.depth = depth;
    if (evaluatorToSelect == 1) this.evaluator = new EasyEvaluator();
    else this.evaluator = new MediumEvaluator();
  }
  
  /**
   *
   * Given a board, getMove will return the best scoring move for the AI.
   *
   * <p>It uses the alphaBetaNegamax algorithm to calculate the best move.</p>
   *
   * @param board
   * @return Best move for AI
   */
  public Move getMove( Board board ) {
    Move bestMove = null;

    int bestScore = Integer.MIN_VALUE;
    for ( Move move : board.getValidMoves() ) {
      Board copy = new Board (board);
      copy.makeMove ( move );
      int score = -alphaBetaNegamax( copy, this.depth, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1 );
      if ( score > bestScore ) {
	bestScore = score;
	bestMove = move;
      }
    }

    return ( bestMove );
  }

  /**
   * <b>Alpha Beta Negamax </b><p>
   *
   * Utilises the Negamax algorithm with Alpha Beta pruning to calculate the best move</p><p>
   *
   * This is normally called with  alphaBetaNegamax(board, depth, +ve infinity, -ve infinity)
   * The calling method handles the first move made by the AI.</p>
   *
   * @param board - board to evaluate and make next move on
   * @param depth - how much further to traverse the tree before returning
   * @param alpha
   * @param beta
   * @return Best move for a given board
   */
  public int alphaBetaNegamax( Board board, int depth, int alpha, int beta ) {
    if ( board.isCheckmate() ) {
      return ( Integer.MIN_VALUE + 1 + this.depth - depth );
    } else if ( board.isStalemate() ) {
      return 0;
    } else if ( depth <= 0 ) {
      return ( evaluator.evaluate( board ) );
    }

    int score = Integer.MIN_VALUE + 1;
    for ( Move move : board.getValidMoves() ) {
      Board child = new Board( board );
      child.makeMove( move );
      score = -alphaBetaNegamax( child, depth - 1, -beta, -alpha );
      if ( score >= beta ) return ( score );
      if ( score > alpha ) alpha = score;
    }

    return ( alpha );
  }

  public boolean isHuman() {
    return false;
  }
}
