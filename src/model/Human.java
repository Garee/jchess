package model;

import static lookup.Pieces.*;

import java.util.Scanner;

public class Human extends Player {

  public Human( String name, byte colour ,long timeRemaining ) {
    super( name, colour, timeRemaining );
  }

  public Human( String name, byte colour ) {
    super( name, colour );
  }

  public boolean isHuman() {
    return true;
  }

  public Move getMove( Board b ) {
    return null;
  }
}





