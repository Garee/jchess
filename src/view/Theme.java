package view;

import java.awt.Color;
/**
 * Theme provides Theme objects with color-information, a name and the folder number of a theme
 * @author Andrew Meikle, Joshua Schaeuble (all containing methods)
 *
 */
public class Theme {
  private String name;
  private Color blackColor;
  private Color whiteColor;
  private Color activePosition;
  private Color possiblePosition;
  private Color previousArrow;
  private int folderNumber;

  public Color getBlackColor() {
    return blackColor;
  }

  public Color getWhiteColor() {
    return whiteColor;
  }

  public Color getActivePosition() {
    return activePosition;
  }

  public Color getPossiblePosition() {
    return possiblePosition;
  }

  public Color getPreviousArrow() {
    return previousArrow;
  }

  /**
   * constructor to instantiate a new theme
   * @param themename
   * @param black
   * @param white
   * @param active
   * @param possible
   * @param previous
   * @param folderNumber
   */
  public Theme(String themename, Color black, Color white, Color active, Color possible, Color previous, int folderNumber) {
    this.name=themename;
    this.blackColor=black;
    this.whiteColor=white;
    this.activePosition=active;
    this.possiblePosition=possible;
    this.previousArrow=previous;
    this.folderNumber=folderNumber;    
  }
  
  public int getFolderNumber(){
    return this.folderNumber;
  }
  
  public String getName () {
    return name;
  }
}
