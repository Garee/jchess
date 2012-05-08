package view;

public class ViewPlayer {
  private boolean isHuman;
  private int playerIntDifficulty;
  private int playerDepthLevel;
  private String playerName;
  
  /**Creates a new ViewPlayer object give the playerName, the depth, and the int representing evaluator type
   * and a boolean isHuman, true if yes false if no
   * 
   * @param playerName
   * @param playerDepthLevel
   * @param playerIntDifficulty
   * @param isHuman
   */
  public ViewPlayer (String playerName,int playerDepthLevel,int playerIntDifficulty,boolean isHuman){
    this.playerName = playerName;
    this.playerDepthLevel = playerDepthLevel;
    this.playerIntDifficulty = playerIntDifficulty;
    this.isHuman =isHuman;
  }

  /**
   * Method for determines if the player is human or not
   * @return true if player is human, false otherwise
   */
  boolean isHuman() {
    return isHuman;
  }

  /**
   * Gets the playerIntdifficulty, as specified in the constructor
   * @return the value of the objects playerIntDifficulty variable
   */
  int getPlayerIntDifficulty() {
    return playerIntDifficulty;
  }

  /**
   * Gets the depth level for this player
   * @return playerDepthLevel as specified when the object was created
   */
  int getPlayerDepthLevel() {
    return playerDepthLevel;
  }

  /**
   * Gets the player name
   * @return
   */
  String getPlayerName() {
    return playerName;
  }

}
