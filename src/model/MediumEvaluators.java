/** OLD EVALUATION CLASS, AI USES NEW EVALUATOR IN SUB PACKAGE evaluators */

/**
 * @author Aleksandr Radevic and Fergus Leahy
 * name Medium Evaluator
 * evaluate method will return final score for evaluated position
 */
package model;

public class MediumEvaluators extends Evaluator {

  private double kingWeight = 1.0 ;
  private double materialWeight = 2.0;
  private double badBishopWeight = 1.0;
  private double badKnightWeight = 2.0;
  private double pieceDevelopment = 2.0;
  
  public MediumEvaluators() {
    super();
  }
  public MediumEvaluators(double m, double k, double b, double n, double a){
    super();
    this.materialWeight = m;
    this.kingWeight = k;
    this.badBishopWeight = b;
    this.badKnightWeight = n;
    this.pieceDevelopment = a;
  }
  


  @Override
  public int evaluate(Board board) {
    return (int) ( (materialBalance(board) * materialWeight)
        + (kingSafety(board) * kingWeight) 
        + (badBishops(board) * badBishopWeight)
        + (badKnights(board) * badKnightWeight)
        + (pieceDevelopmentEval(board) * pieceDevelopment)
        );
  }

}
