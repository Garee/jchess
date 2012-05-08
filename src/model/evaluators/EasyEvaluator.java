package model.evaluators;

import model.Board;

/**
 * An evaluator that is suitable for casual players and beginners.
 *
 * @author Gary Blackwood
 */
public class EasyEvaluator implements Evaluator {

  private static final int MATERIAL_WEIGHT = 90;
  private static final int POSITION_WEIGHT = 10;

  public int evaluate( Board board ) {
    return ( ( board.evaluateMaterial() * MATERIAL_WEIGHT )
	     + ( board.evaluatePiecePositions() * POSITION_WEIGHT ) );
  }
}