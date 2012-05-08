
/** OLD EVALUATION CLASS, AI USES NEW EVALUATOR IN SUB PACKAGE evaluators */
package model;
/**
 * @author Aleksandr Radevic and Fergus Leahy
 * name easy Evaluator
 * evaluate method will return final score for evaluated position
 */
public class EasyEvaluators extends Evaluator{

  private double kingWeight = 1.0 ;
  private double materialWeight = 2.0;
  private double factor = 2.0;
  public EasyEvaluators() {
    super();
  }

  public EasyEvaluators(double m, double k, double f) {
    super();
    this.materialWeight = m;
    this.kingWeight = k;
    this.factor = f;
  }

  @Override
  public int evaluate(Board board) {
    return (int) ( (materialBalance(board) * materialWeight) +
        (kingSafety(board) * kingWeight) +
        (pieceDevelopment(board) * factor)
        );
  }
  
  

}
