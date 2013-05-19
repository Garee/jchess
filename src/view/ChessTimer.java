package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static lookup.Pieces.WHITE;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * ChessClock as JPanel in BorderLayout
 * 
 * @author Joshua Schaeuble, Andrew Meikle
 * 
 */
public class ChessTimer extends JPanel {
  // Finals and object instances
  public static final long serialVersionUID = 1L;
  private final Dimension CLOCK_DIMENSION = new Dimension (400, 295);
  private final int AMOUNT_OF_CLOCKS = 2;
  private final int GET_CENTER = 2;
  private final int TIMER_ONE = 0;
  private final int TIMER_TWO = 1;
  private final int BIG_CLOCK_CENTER_MARGIN = 75;
  private final int SMALL_CLOCK_CENTER_MARGIN = 83;
  private final int BIG_CLOCK_LABEL_VALIGN = 160;
  private final int BIG_CLACK_TIME_VALIGN = 130;
  private final int SMALL_CLOCK_LABEL_VALIGN = 140;
  private final int SMALL_CLACK_TIME_VALIGN = 115;
  private final int BIG_CLOCK_LABEL_FONTSIZE = 18;
  private final int BIG_CLOCK_TIME_FONTSIZE = 22;
  private final int SMALL_CLOCK_LABEL_FONTSIZE = 13;
  private final int SMALL_CLOCK_TIME_FONTSIZE = 16;
  private final int CLOCK_IMAGE_MARGIN_LEFT = 30;
  
  // if images are not found: change folder here!
  private final String IMAGE_FOLDER = "img/";
  
  private final int MILI_TO_SECOND = 1000;
  private final int SEC_TO_MIN = 60;
  private final int DAY_TO_HOUR = 24;

  private final String CLOCK_LABEL_UNTIMED = "00:00:00";

  private String player1Name = "White";
  private String player2Name = "Black";
  private String timeWhiteLabel;
  private String timeBlackLabel;
  private TimeThread clock;
  private int turn;
  private int sek[];
  private int min[];
  private int h[];
  boolean rundown;
  boolean timedGame;
  
  private String blackImageDirection = IMAGE_FOLDER + "clocks/blackClock.png";
  private String whiteImageDirection = IMAGE_FOLDER + "clocks/whiteClock.png";
  private BufferedImage blackImage, whiteImage;

  /**
   * constructor to build the timer
   * 
   * @param milliseconds
   *          start amount of time
   * @author Joshua Schaeuble, Andrew Meikle
   */
  public ChessTimer(int milliseconds) {
    int sec, min, hour;
    clock = new TimeThread ();
    clock.start ();
    sec = 0;
    min = 0;
    hour = 0;
    turn = 0;

    try {
      blackImage = ImageIO.read (new File (blackImageDirection));
      whiteImage = ImageIO.read (new File (whiteImageDirection));
    }

    catch (IOException ex) {
      System.out
          .println ("Cannot find image path in ChessTimer.java ca. " +
          		"Edit it manually. Therefore go to ChessTimer.java and change " +
          		"the final string IMAGE_FOLDER.");
    }

    this.setLayout (new FlowLayout ());
    timeWhiteLabel = CLOCK_LABEL_UNTIMED;
    timeBlackLabel = CLOCK_LABEL_UNTIMED;

    this.rundown = false;

    this.sek = new int[AMOUNT_OF_CLOCKS];
    this.sek[TIMER_ONE] = sec;
    this.sek[TIMER_TWO] = sec;

    this.min = new int[AMOUNT_OF_CLOCKS];
    this.min[TIMER_ONE] = min;
    this.min[TIMER_TWO] = min;

    this.h = new int[AMOUNT_OF_CLOCKS];
    this.h[TIMER_TWO] = hour;
    this.h[TIMER_ONE] = hour;

    if ( milliseconds >= 0 ) {
      timedGame = true;
      synchronized (clock) {
        clock.goOn ();
      }
    }
    else {
      synchronized (clock) {
        clock.pause ();
        timedGame = false;
      }
    }
  }

  /**
   * returns the preferred size of the clock
   * 
   * @return
   * @author Joshua Schaeuble, Andrew Meikle
   */
  public Dimension getClockDimension() {
    return CLOCK_DIMENSION;
  }

  /**
   * Paints the clock and pictures of the clock depending on the current turn
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent (g);
    int start = (int) ( this.getSize ().width - CLOCK_DIMENSION.width )
        / GET_CENTER;

    String wName, bName, wTime, bTime;
    Color wColor, bColor;

    if ( turn == 0 ) {// WHITE
      g.drawImage (whiteImage, start, CLOCK_IMAGE_MARGIN_LEFT,
          CLOCK_DIMENSION.width, CLOCK_DIMENSION.height
              - ( GET_CENTER * CLOCK_IMAGE_MARGIN_LEFT ), this);
      wName = player1Name;
      bName = player2Name;
      wTime = timeWhiteLabel;
      bTime = timeBlackLabel;
      bColor = Color.WHITE;
      wColor = Color.BLACK;
    }
    else {
      g.drawImage (blackImage, start, CLOCK_IMAGE_MARGIN_LEFT,
          CLOCK_DIMENSION.width, CLOCK_DIMENSION.height
              - ( GET_CENTER * CLOCK_IMAGE_MARGIN_LEFT ), this);
      wName = player2Name;
      bName = player1Name;
      wTime = timeBlackLabel;
      bTime = timeWhiteLabel;
      bColor = Color.BLACK;
      wColor = Color.WHITE;
    }

    Font f = new Font ("Dialog", Font.PLAIN, BIG_CLOCK_LABEL_FONTSIZE);
    g.setColor (wColor);
    g.setFont (f);
    int a = SwingUtilities.computeStringWidth (g.getFontMetrics (), wName);
    g.drawString (wName, ( ( this.getSize ().width / GET_CENTER )
        - BIG_CLOCK_CENTER_MARGIN - ( a / GET_CENTER ) ),
        BIG_CLOCK_LABEL_VALIGN);

    f = new Font ("Dialog", Font.PLAIN, BIG_CLOCK_TIME_FONTSIZE);
    g.setFont (f);
    a = SwingUtilities.computeStringWidth (g.getFontMetrics (), wTime);
    g.drawString (wTime, ( ( this.getSize ().width / GET_CENTER )
        - BIG_CLOCK_CENTER_MARGIN - ( a / GET_CENTER ) ), BIG_CLACK_TIME_VALIGN);

    f = new Font ("Dialog", Font.PLAIN, SMALL_CLOCK_LABEL_FONTSIZE);
    g.setFont (f);
    g.setColor (bColor);
    a = SwingUtilities.computeStringWidth (g.getFontMetrics (), bName);
    g.drawString (bName, ( ( this.getSize ().width / GET_CENTER )
        + SMALL_CLOCK_CENTER_MARGIN - ( a / GET_CENTER ) ),
        SMALL_CLOCK_LABEL_VALIGN);

    f = new Font ("Dialog", Font.PLAIN, SMALL_CLOCK_TIME_FONTSIZE);
    g.setFont (f);
    a = SwingUtilities.computeStringWidth (g.getFontMetrics (), bTime);
    g.drawString (bTime, ( ( this.getSize ().width / GET_CENTER )
        + SMALL_CLOCK_CENTER_MARGIN - ( a / GET_CENTER ) ),
        SMALL_CLACK_TIME_VALIGN);
  }

  /**
   * switches the timer to the "turn" represented by the given integer
   * 
   * @param a
   *          a=0 represents white, a=1 represents black
   * @author Joshua Schaeuble, Andrew Meikle
   */
  public void switchTimer(int a) {
    synchronized (clock) {
      clock.switchTurn (a);
    }
  }

  /**
   * tells if the timer is rundown
   * 
   * @return true if timer is rundown
   */
  public boolean isRundown() {
    return rundown;
  }

  /**
   * pauses the timer-thread for external access
   */
  public void pauseTimer() {
    synchronized (clock) {
      clock.pause ();
    }
  }

  /**
   * continues the timer-thread for external access
   */
  public void continueTimer() {
    synchronized (clock) {
      clock.goOn ();
    }
  }

  /**
   * resets the timer to the given amount of miliseconds
   * 
   * @param milliseconds
   * @author Andrew Meikle, Joshua Schaeuble
   */
  public void resetTimer(int milliseconds) {
    int sec, min, hour;
    this.rundown = false;
    sec = ( milliseconds / MILI_TO_SECOND ) % SEC_TO_MIN;
    min = ( ( milliseconds / ( MILI_TO_SECOND * SEC_TO_MIN ) ) % SEC_TO_MIN );
    hour = ( ( milliseconds / ( MILI_TO_SECOND * SEC_TO_MIN * SEC_TO_MIN ) ) % DAY_TO_HOUR );

    this.sek = new int[AMOUNT_OF_CLOCKS];
    this.sek[TIMER_ONE] = sec;
    this.sek[TIMER_TWO] = sec;

    this.min = new int[AMOUNT_OF_CLOCKS];
    this.min[TIMER_ONE] = min;
    this.min[TIMER_TWO] = min;

    this.h = new int[AMOUNT_OF_CLOCKS];
    this.h[TIMER_TWO] = hour;
    this.h[TIMER_ONE] = hour;

    turn = 0;

    if ( milliseconds > 0 ) {
      timedGame = true;
      continueTimer ();
    }
    else {
      timedGame = false;
      pauseTimer ();
    }
  }

  /**
   * Thread to controll the timer
   * 
   * @author Joshua Schäuble, Andrew Meikle
   * 
   */
  private class TimeThread extends Thread {
    private boolean running = false;

    public void run() {
      while ( true ) {
        while ( !running ) {
          try {
            wait ();
          }
          catch (Exception e) {}
        }
        try {
          Thread.sleep (1000);
        }
        catch (Exception e) {}
        if ( sek[turn] > 0 ) {
          sek[turn] = sek[turn] - 1;
        }
        else {
          if ( min[turn] > 0 ) {
            sek[turn] = 59;
            min[turn] = min[turn] - 1;
          }
          else {
            if ( h[turn] > 0 ) {
              sek[turn] = 59;
              min[turn] = 59;
              h[turn] = h[turn] - 1;
            }
            else if ( h[turn] == 0 && min[turn] == 0 && sek[turn] == 0 ) {
              running = false;
              if ( timedGame ) {
                rundown = true;
              }
            }
          }

        }

        timeWhiteLabel = ( String.format ("%02d:%02d:%02d", h[TIMER_ONE],
            min[TIMER_ONE], sek[0]) );
        timeBlackLabel = ( String.format ("%02d:%02d:%02d", h[TIMER_TWO],
            min[TIMER_TWO], sek[1]) );
        repaint ();
      }
    }

    /**
     * switches the turn to the color represented by a
     * 
     * @param a
     */
    public void switchTurn(int a) {
      if ( a == WHITE ) {
        turn = TIMER_ONE;
      }
      else {
        turn = TIMER_TWO;
      }
    }

    /**
     * pauses the thread
     */
    public void pause() {
      running = false;
    }

    /**
     * continues the thread
     */
    public void goOn() {
      running = true;
    }

  }

  /**
   * returns the name of player1
   * 
   * @author Andrew Meikle, Joshua Schaeuble
   * @return name of player1
   */
  protected String getPlayer1Name() {
    return player1Name;
  }

  /**
   * sets the name of player2 to the given String
   * 
   * @param player1Name
   * @author Andrew Meikle, Joshua Schaeuble
   */
  protected void setPlayer1Name(String player1Name) {
    this.player1Name = player1Name;
  }

  /**
   * returns the name of Player2
   * 
   * @return name of player2
   * @author Andrew Meikle, Joshua Schaeuble
   */
  protected String getPlayer2Name() {
    return player2Name;
  }

  /**
   * sets the name of player2 to the given string
   * 
   * @param player2Name
   *          string to set name as
   * @author Andrew Meikle,Joshua Schaeuble
   */
  protected void setPlayer2Name(String player2Name) {
    this.player2Name = player2Name;
  }

}
