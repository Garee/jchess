package model;

import static lookup.Pieces.WHITE;

import java.util.Scanner;
import java.util.Timer;

public abstract class Player {

  private String name;
  protected byte colour;
  private int score;
  private long timeRemaining;
  private boolean human;

  public Player( String name, byte colour ) {
    this.setName( name );
    this.setColour( colour );
    this.score = 0;
    this.setTimeRemaining( -1 );
  }

  public Player( String name, byte colour, long timeRemaining ) {
    this.setName( name );
    this.setColour( colour );
    this.score = 0;
    this.setTimeRemaining( timeRemaining );
  }

  public abstract Move getMove(Board b);

  public abstract boolean isHuman();

  protected String getColour() {
    return ( this.colour == WHITE ? "White" : "Black" );
  }

  public byte getColourByte(){
    return colour;
  }

  protected void setColour( byte colour ) {
    this.colour = colour;
  }

  public String getName() {
    return name;
  }

  protected void setName( String name ) {
    this.name = name;
  }

  protected void setTimeRemaining( long timeRemaining ) {
    this.timeRemaining = timeRemaining;
  }

  protected long getTimeRemaining() {
    return timeRemaining;
  }

}
