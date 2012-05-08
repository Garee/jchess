package model.evaluators;

import model.Board;

/**
 * An evaluator that is suitable for intermediate players.
 *
 * @author Gary Blackwood
 */
public class MediumEvaluator implements Evaluator {

  private static final int MATERIAL_WEIGHT = 50;
  private static final int POSITION_WEIGHT = 25;
  private static final int DEVELOPMENT_WEIGHT = 25;

  public int evaluate( Board board ) {
    return ( ( board.evaluateMaterial() * MATERIAL_WEIGHT )
	     + ( board.evaluatePiecePositions() * POSITION_WEIGHT )
	     + ( board.evaluatePieceDevelopment() * DEVELOPMENT_WEIGHT ) );
  }
}