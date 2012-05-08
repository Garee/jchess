package view;

import java.awt.Color;

/**
 * Themes gives a collection of all themes and methods to access them
 * 
 * @author Andrew Meikle, Joshua Schaeuble (all containing methods)
 * 
 */
public class Themes {

  private Theme[] themesList;

  /**
   * constructor to add themes to the themelist, new themes are simply added
   * here, additionally a folder in the same form (with ascending foldername) of the existing folders,
   * containing images with the same names has to be added to the resources
   */
  public Themes() {
    Theme[] themesListe = {
        // add new themes here
        // ("Themename", colorBlack, colorWhite,
        // colorActive,colorPossible,colorPrevious,FolderNumberforIcons)
        new Theme ("Default", Color.GRAY, Color.WHITE, Color.CYAN, Color.GREEN,
            Color.RED, 1),
        new Theme ("Team Jay", Color.GRAY, Color.WHITE, Color.BLUE,
            Color.GREEN, Color.RED, 2) };
    themesList = themesListe;
  }

  /**
   * returns List of Theme Names
   * @return
   */
  public String[] getThemeList() {
    String[] themeNames = new String[themesList.length];
    for (int i = 0; i < themesList.length; i++) {
      themeNames[i] = themesList[i].getName ();
    }
    return themeNames;
  }

  /**
   * returns the Theme that is identified by the given integer
   * @param i
   * @return
   */
  public Theme getTheme(int i) {
    return themesList[i];
  }

  /**
   * returns the amount of themes implemented right now
   * @return
   */
  public int getNumberOfThemes() {
    return themesList.length;
  }

  /**
   * returns null or the first Theme that is identified by the given string
   * @param text
   * @return
   */
  public Theme getTheme(String text) {
    for (int i = 0; i < themesList.length; i++) {
      if ( themesList[i].getName ().equals (text) ) {
        return themesList[i];
      }
    }
    return null;
  }

}
