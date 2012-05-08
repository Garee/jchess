package view;

import static lookup.Pieces.BLACK;
import static lookup.Pieces.WHITE;
import static lookup.Pieces.EMPTY;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.AI;
import model.Board;
import model.Human;
import model.Move;
import model.Player;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <CODE>myWindow</CODE> distributes methods to build a GUI instance of
 * ChessGame
 */
public final class MainWindow {

  // Instances of all needed graphical objects
  private JFrame frame;
  private JPanel boardPanel, rightPanel, moveHistoryPanel;
  private JMenuBar menuBar;
  private JMenu menu;
  private JMenu highlightMenu;
  private JMenuItem menuItem;
  private JButton continueButton;
  private JButton undoUntilButton;
  private JButton undoButton;
  private JPanel buttonPanel;
  private JCheckBoxMenuItem autoChangeItem;

  // Evaluator Constants
  private final int DEFAULT_EVALUATOR = 2;
  private final int DEFAULT_DEPTH = 3;
  private final int DEFAULT_UNTIMED = -1;
  private final int DEFAULT_THEME = 0;
  private final int HISTORYCOLUMNS = 2;
  private final int SCROLLBAR_RESIZEMENT = 20;
  private final int AMOUNT_OF_CELLS = 8;

  private final String GAME_NAME = "JayChess";
  private final String PLAYER_ONE_DEFAULT_NAME = "Player1";
  private final String PLAYER_TWO_DEFAULT_NAME = "Player2";
  private final String MENU_GAME_LABEL = "Game";
  private final String MENU_NEW_LABEL = "New";
  private final String MENU_VIEW_LABEL = "View";
  private final String MENU_HIGHLIGHTING = "Highlighting";
  private final String MENU_VIEW_PREVIOUS = "View previous move";
  private final String MENU_VIEW_POSSIBLE = "View possible moves";
  private final String MENU_BOARD_PERSPECITVE = "Board Perspective";
  private final String MENU_CHANGE_PERSPECTIVE = "Change Perspective";
  private final String MENU_CHANGE_AUTOPERSPECTIVE = "Auto Change to Human";
  private final String MENU_THEMES = "Themes";

  private final String CURRENT_PLAYERS_TURN_LABEL = "Current players turn is :";
  private final String CONTINUE_BUTTON_TEXT = "Continue";
  private final String UNDO_UNTIL_BUTTON_TEXT = "Undo until...";
  private final String UNDO_BUTTON_TEXT = "Undo";

  // Finals (Numbers and Error-Messages)
  private final Dimension BOARDMINDIM = new Dimension (500, 500);
  private final Dimension PANELMINDIM = new Dimension (400, BOARDMINDIM.height);
  private final Dimension PREFERRED_CURRENT_LABEL_SIZE = new Dimension (60, 30);
  private final Color HISTORY_LIST_BACKGROUND_COLOR = Color.LIGHT_GRAY;

  // Dimensions
  private Dimension BOARDDIM = BOARDMINDIM;
  private Dimension PANELDIM = PANELMINDIM;

  // Graphical Board representation
  private Themes myThemes = new Themes ();
  private ChessBoard myBoard = new ChessBoard (BOARDMINDIM,
      myThemes.getTheme (DEFAULT_THEME));
  // Model Board representation
  private Board board;
  private Player p1 = new Human (PLAYER_ONE_DEFAULT_NAME, WHITE,
      DEFAULT_UNTIMED);
  private Player p2 = new AI (DEFAULT_DEPTH, DEFAULT_EVALUATOR, BLACK,
      PLAYER_TWO_DEFAULT_NAME, DEFAULT_UNTIMED);

  // Instances of all needed game objects
  private List<Move> moveStorage = new LinkedList<Move> ();
  private Move humanMove = null;
  private static MouseListener humanInputListener;
  static MouseListener continueButtonListener, undoUntilButtonListener,
      undoButtonListener;
  private byte currentColor = WHITE;
  private ChessTimer myTimer;

  // Tread that runs, while game is active
  private ActionThread myThread;
  private JLabel currentPlayerColorLabel;

  // Highlight flags
  boolean highlightPrevious = false;
  boolean highlightPossible = false;
  boolean autoChangePerspective = true;

  private JPanel testPanel;
  private JList list = new JList ();
  JScrollPane listScroller = new JScrollPane ();

  /**
   * Main-Method to make class executable
   * 
   * @param args
   * 
   * @author A. Meikle, J. Schaeuble
   */
  public static void main(String[] args) {
    new MainWindow ();
  }

  /**
   * <code>main</code> method builds the JayChess-window
   * 
   * @param args
   *          standard
   * @author A. Meikle, J. Schaeuble
   */
  public MainWindow() {

    // initialize Frame
    frame = new JFrame (GAME_NAME);
    frame.setLayout (new BorderLayout ());

    // implement "closing" function
    frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

    // initialize and start Thread
    myThread = new ActionThread ();
    myThread.start ();

    // initialize the Menu
    menuBar = makeMenu ();

    // initialize the board
    boardPanel = myBoard;

    // initialize the history panel, set its Layout
    rightPanel = makeRightPanel ();

    // initialize all actionListeners
    listenToTheActions ();

    // min-size the board and the panel
    // myBoard.setBoardSize (BOARDMINDIM);
    boardPanel.setMinimumSize (BOARDMINDIM);
    boardPanel.setPreferredSize (BOARDDIM);
    rightPanel.setMinimumSize (PANELMINDIM);
    rightPanel.setPreferredSize (PANELDIM);

    testPanel = new JPanel ();
    BoxLayout niceBox = new BoxLayout (testPanel, BoxLayout.LINE_AXIS);
    testPanel.setLayout (niceBox);
    testPanel.add (boardPanel);
    testPanel.add (rightPanel);

    // add menu, board and panel to frame
    frame.add (BorderLayout.NORTH, menuBar);
    frame.add (BorderLayout.WEST, boardPanel);
    frame.add (BorderLayout.CENTER, rightPanel);

    frame.pack ();
    frame.setMinimumSize (frame.getSize ());

    // set visible
    frame.setVisible (true);

    // initialize the Players
    ViewPlayer player1 = new ViewPlayer (PLAYER_ONE_DEFAULT_NAME,
        DEFAULT_DEPTH, DEFAULT_EVALUATOR, true);
    ViewPlayer player2 = new ViewPlayer (PLAYER_TWO_DEFAULT_NAME,
        DEFAULT_DEPTH, DEFAULT_EVALUATOR, false);
    initGame (player1, player2, DEFAULT_UNTIMED);
  }

  /**
   * generates the Menu as a JMenuBar
   * 
   * @author A. Meikle, J. Schaeuble
   * @return JMenuBar containing the menu
   */
  private JMenuBar makeMenu() {
    JMenuBar thisBar = new JMenuBar ();
    menu = new JMenu (MENU_GAME_LABEL);
    menu.setMnemonic (KeyEvent.VK_G);
    // a group of JMenuItems
    menuItem = new JMenuItem (MENU_NEW_LABEL);
    menuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_N,
        ActionEvent.CTRL_MASK));
    menuItem.addActionListener (new java.awt.event.ActionListener () {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        newButtonActionPerformed (evt);
      }
    });
    menu.add (menuItem);

    thisBar.add (menu);
    menu = new JMenu (MENU_VIEW_LABEL);
    menu.setMnemonic (KeyEvent.VK_V);
    highlightMenu = new JMenu (MENU_HIGHLIGHTING);
    JCheckBoxMenuItem prevMoveItem = new JCheckBoxMenuItem (MENU_VIEW_PREVIOUS);
    prevMoveItem.addItemListener (new ItemListener () {
      @Override
      public void itemStateChanged(ItemEvent arg0) {
        if ( arg0.getStateChange () == 1 ) {
          highlightPrevious = true;
          buildLastMove ();
          frame.repaint ();
        }
        else {
          highlightPrevious = false;
          myBoard.setLastMove (null);
          frame.repaint ();
        }
      }
    });
    highlightMenu.add (prevMoveItem);
    JCheckBoxMenuItem posMoveItem = new JCheckBoxMenuItem (MENU_VIEW_POSSIBLE);
    posMoveItem.addItemListener (new ItemListener () {
      @Override
      public void itemStateChanged(ItemEvent arg0) {
        if ( arg0.getStateChange () == ItemEvent.SELECTED ) {
          highlightPossible = true;
          if ( myBoard.getActivePosition () != null ) {
            buildHighlightPossible (myBoard.getActivePosition ());
          }
          frame.repaint ();
        }
        else {
          highlightPossible = false;
          myBoard.setPossibleMoves (null);
          frame.repaint ();
        }
      }
    });
    highlightMenu.add (posMoveItem);
    menu.add (highlightMenu);

    JMenu perspectiveMenu = new JMenu (MENU_BOARD_PERSPECITVE);
    menuItem = new JMenuItem (MENU_CHANGE_PERSPECTIVE);
    menuItem.addActionListener (new ActionListener () {
      public void actionPerformed(ActionEvent evt) {
        if ( myBoard.getPerspective () == WHITE ) {
          myBoard.setPerspective (BLACK);
        }
        else {
          myBoard.setPerspective (WHITE);
        }
        frame.repaint ();
      }
    });
    perspectiveMenu.add (menuItem);
    autoChangeItem = new JCheckBoxMenuItem (MENU_CHANGE_AUTOPERSPECTIVE);
    autoChangeItem.addItemListener (new ItemListener () {
      @Override
      public void itemStateChanged(ItemEvent arg0) {
        if ( arg0.getStateChange () == ItemEvent.SELECTED ) {
          autoChangePerspective = true;
          if ( currentColor == WHITE && p1.isHuman () ) {
            myBoard.setPerspective (WHITE);
          }
          else if ( currentColor == BLACK && p2.isHuman () ) {
            myBoard.setPerspective (BLACK);
          }
        }
        else {
          autoChangePerspective = false;
        }
        frame.repaint ();
      }
    });
    autoChangeItem.setSelected (autoChangePerspective);
    perspectiveMenu.add (autoChangeItem);
    menu.add (perspectiveMenu);
    JMenu themesMenu = new JMenu (MENU_THEMES);
    String[] themes = myThemes.getThemeList ();
    for (int i = 0; i < myThemes.getNumberOfThemes (); i++) {
      menuItem = new JMenuItem (themes[i]);
      menuItem.addActionListener (new ActionListener () {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          JMenuItem haha = (JMenuItem) arg0.getSource ();
          Theme superTheme = myThemes.getTheme (haha.getText ());
          myBoard.setTheme (superTheme);
          frame.repaint ();
        }
      });
      themesMenu.add (menuItem);
    }
    menu.add (themesMenu);

    thisBar.add (menu);
    return thisBar;
  }

  /**
   * Creates a Panel that is shown right of the Board. It contains the previous
   * move list and the clock
   * 
   * @return Panel on the right side of the board
   */
  private JPanel makeRightPanel() {
    JPanel thisPanel = new JPanel ();
    thisPanel.setLayout (new BorderLayout ());

    // Adds the "current player color" and the changing label to the north
    thisPanel.add (createCurrentPlayerLabel (), BorderLayout.NORTH);

    // Adds the timer to the bottom of andrewssidepanel
    myTimer = new ChessTimer (DEFAULT_UNTIMED);
    myTimer.setPreferredSize (new Dimension (PANELDIM.width, myTimer
        .getClockDimension ().height));
    thisPanel.add (myTimer, BorderLayout.SOUTH);

    // Creates the history-Table
    thisPanel.add (makeHistoryPanel (), BorderLayout.CENTER);

    return thisPanel;
  }

  /**
   * Creates the "current player" label and the "color indicator"
   * 
   * @return JPanel containing the "current player" label
   */
  private JPanel createCurrentPlayerLabel() {
    JPanel currentTurn = new JPanel ();
    JLabel currentPlayer = new JLabel (CURRENT_PLAYERS_TURN_LABEL);
    currentPlayerColorLabel = new JLabel ();
    currentPlayerColorLabel.setPreferredSize (PREFERRED_CURRENT_LABEL_SIZE);
    setCurrentPlayerColorLabel (currentColor);
    currentPlayerColorLabel.setOpaque (true);

    currentTurn.add (currentPlayer);
    currentTurn.add (currentPlayerColorLabel);

    return currentTurn;
  }

  /**
   * Sets the color of the currentPlayerColor
   * 
   * @param color
   *          Color to be set (Black or White)
   */
  private void setCurrentPlayerColorLabel(byte color) {
    if ( color == BLACK ) currentPlayerColorLabel.setBackground (Color.BLACK);
    else
      currentPlayerColorLabel.setBackground (Color.WHITE);
  }

  /**
   * Creates a panel that nests the move-history
   * 
   * @return JPanel containing the move-history
   */
  private JPanel makeHistoryPanel() {
    // mother Panel
    JPanel historyPanel = new JPanel ();
    historyPanel.setLayout (new BorderLayout ());
    // Button Panel
    continueButton = new JButton (CONTINUE_BUTTON_TEXT);
    undoUntilButton = new JButton (UNDO_UNTIL_BUTTON_TEXT);
    undoButton = new JButton (UNDO_BUTTON_TEXT);

    buttonPanel = new JPanel ();
    // Are not put in the code by default -yet
    buttonPanel.add (undoButton);
    buttonPanel.add (continueButton);
    buttonPanel.add (undoUntilButton);

    continueButton.setVisible (false);
    undoUntilButton.setVisible (false);
    undoButton.setVisible (true);
    list = new JList (); // data has type Object[]

    list.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
    list.setLayoutOrientation (JList.HORIZONTAL_WRAP);
    list.setVisibleRowCount (-1);
    listScroller = new JScrollPane (list);
    listScroller
        .setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    listScroller.setOpaque (true);
    list.setOpaque (true);
    ListCellRenderer myHistRenderer = new HistoryCellRenderingDefinition ();
    list.setCellRenderer (myHistRenderer);
    list.setBackground (HISTORY_LIST_BACKGROUND_COLOR);
    list.addListSelectionListener (new ListSelectionListener () {

      @Override
      public void valueChanged(ListSelectionEvent arg0) {
        if ( !myThread.isPaused () ) {
          synchronized (myThread) {
            myThread.pause ();
          }
        }
        boolean adjust = arg0.getValueIsAdjusting ();
        int a = moveStorage.size ();
        if ( adjust ) {
          JList list = (JList) arg0.getSource ();
          int selections[] = list.getSelectedIndices ();
          for (int i = 0, n = selections.length; i < n; i++) {
            a = selections[i];
          }
          myBoard.setActivePosition (null);
          myBoard.setPossibleMoves (null);
          synchronized (myBoard) {
            historyBack (a);
          }
        }
      }
    });
    moveHistoryPanel = new JPanel ();
    historyPanel.add (BorderLayout.CENTER, moveHistoryPanel);
    historyPanel.add (BorderLayout.SOUTH, buttonPanel);
    return historyPanel;
  }

  /**
   * Adds new moves to the history panel
   */
  private void addHistory() {
    String[] data = new String[moveStorage.size ()];
    for (int i = 0; i < moveStorage.size (); i++) {
      if ( i % HISTORYCOLUMNS == 0 ) {
        data[i] = ( i + HISTORYCOLUMNS ) / HISTORYCOLUMNS + ":  \t \t"
            + moveStorage.get (i).getGuiString ();
      }
      else
        data[i] = "  " + moveStorage.get (i).getGuiString ();
    }
    list.setListData (data);
    Dimension dimdim = moveHistoryPanel.getSize ();
    listScroller.setPreferredSize (new Dimension (PANELDIM.width,
        PANELDIM.height / HISTORYCOLUMNS));
    list.setFixedCellWidth (( dimdim.width - SCROLLBAR_RESIZEMENT )
        / HISTORYCOLUMNS);
    moveHistoryPanel.removeAll ();
    moveHistoryPanel.add (listScroller);
    rightPanel.revalidate ();
  }

  private void historyBack(int a) {
    if ( myThread.isPaused () ) {
      if ( a != moveStorage.size () - 1 ) {
        undoUntilButton.setText (UNDO_UNTIL_BUTTON_TEXT);
        undoUntilButton.addMouseListener (undoUntilButtonListener);
        undoUntilButton.setVisible (true);
        undoButton.setVisible (false);
      }
      else {
        undoUntilButton.setVisible (false);
        undoButton.setText (UNDO_BUTTON_TEXT);
      }

      // TODO: no continue button, if game already over!
      continueButton.setText (CONTINUE_BUTTON_TEXT);
      continueButton.addMouseListener (continueButtonListener);
      continueButton.setVisible (true);
    }
    board = new Board ();
    if ( a < 0 ) {
      myBoard.setBoard (board);
    }
    else {
      for (int i = 0; i < a; i++) {
        board.makeMove (moveStorage.get (i));
      }
      myBoard.setBoard (board);
    }
    boardPanel.revalidate ();
    frame.repaint ();
  }

  /**
   * performs a move for a given player, distinguishes between Human and AI
   * players
   * 
   * @param player
   *          Player to make a move.
   */
  private void makeAmove(Player player) {
    if ( highlightPrevious ) {
      buildLastMove ();
    }
    if ( moveStorage.size () < 1 ) {
      undoButton.setEnabled (false);
    }
    else {
      undoButton.setEnabled (true);
    }

    setCurrentPlayerColorLabel (player.getColourByte ());

    if ( player.isHuman () ) {

      if ( autoChangePerspective ) {
        myBoard.setPerspective (player.getColourByte ());
      }
      // activate mouseListener
      followTheMouse (true);

      // wait until humanMove is set or Thread is Paused
      while ( humanMove == null && !myThread.isPaused () ) {
        synchronized (myThread) {
          myThread.waitHere ();
        }
      }
      // if Thread is Paused, deactivate mouseListener
      if ( myThread.isPaused () ) {
        followTheMouse (false);
      }
      // else if humanMove is set...
      else if ( humanMove != null ) {
        // make move on board
        board.makeMove (humanMove);
        if ( isEndGameSituation (humanMove) ) {

        }
        else {
          // update HistoryPanel
          moveStorage.add (humanMove);
          addHistory ();
          // reset humanMove
          humanMove = null;
          followTheMouse (false);
          // change to opponent color
          changeColor ();
        }
      }
    }
    else {
      followTheMouse (false);
      Move aiMove = player.getMove (board);

      if ( !myThread.isPaused () ) {
        board.makeMove (aiMove);
        if ( isEndGameSituation (aiMove) ) {

        }
        else {
          // update HistoryPanel
          moveStorage.add (aiMove);
          addHistory ();
          myBoard.setBoard (board);
          // change to opponent color
          changeColor ();
        }
      }
    }
    frame.repaint ();
  }

  private boolean isEndGameSituation(Move move) {
    String currentString;
    if ( currentColor == BLACK ) {
      currentString = "Black";
    }
    else {
      currentString = "White";
    }
    // check for checkmate
    if ( board.isCheckmate () ) {
      moveStorage.add (move);
      addHistory ();
      frame.repaint ();
      displayMessage ("Checkmate! " + currentString + " wins.");
      synchronized (myThread) {
        myThread.pause ();
      }
      return true;
    }
    // check for stalemate
    else if ( board.isStalemate () ) {
      moveStorage.add (move);
      addHistory ();
      frame.repaint ();
      displayMessage ("Stalemate");
      synchronized (myThread) {
        myThread.pause ();
      }
      return true;
    }
    else if ( myTimer.isRundown () ) {
      changeColorTo(WHITE);
      frame.repaint();
      displayMessage ("Timer run down. " + currentString + " looses.");
      synchronized (myThread) {
        myThread.pause ();
      }
      
      return true;
    }
    else
      return false;
  }

  /**
   * displays messages in a dialog window
   * 
   * @author Gary Blackwood
   * @param message
   *          to be displayed
   */
  private static void displayMessage(String message) {
    JOptionPane.showMessageDialog (new JFrame (), message);
  }

  /**
   * Initializes a new game. Takes two ViewPlayer objects and the time remaining
   * Takes two ViewPlayer objects and the time remaining and constructs the new
   * AI objects
   * 
   * @param player1
   * @param player2
   * @param timeRemaining
   * @return void
   */
  public void initGame(ViewPlayer player1, ViewPlayer player2, int timeRemaining) {
    if(!myThread.isPaused ()){
      synchronized(myThread){
        myThread.pause ();
      }
    }
    
    if ( player1.isHuman () ) {
      p1 = new Human (player1.getPlayerName (), WHITE, timeRemaining);
    }
    else {
      p1 = new AI (player1.getPlayerDepthLevel (),
          player1.getPlayerIntDifficulty (), WHITE, player1.getPlayerName (),
          timeRemaining);
    }

    if ( player2.isHuman () ) {
      p2 = new Human (player2.getPlayerName (), BLACK, timeRemaining);
    }
    else {
      p2 = new AI (player2.getPlayerDepthLevel (),
          player2.getPlayerIntDifficulty (), BLACK, player2.getPlayerName (),
          timeRemaining);
    }
    humanMove = null;
    moveStorage = Collections.synchronizedList (new ArrayList<Move> ());
    myTimer.setPlayer1Name (player1.getPlayerName ());
    myTimer.setPlayer2Name (player2.getPlayerName ());
    myTimer.resetTimer (timeRemaining);
    myTimer.continueTimer ();
    board = new Board ();
    myBoard.setBoard (board);
    myBoard.setLastMove (null);
    myBoard.setPossibleMoves (null);
    myBoard.setActivePosition (null);
    addHistory ();
    changeColorTo(WHITE);
    frame.repaint ();
    synchronized (myThread) {
      myThread.goOn ();
    }
  }

  /**
   * switches the current player to the given color, takes a byte representation
   * of the color (BLACK or WHITE)
   * 
   * @param color
   */
  private void changeColorTo(byte color) {
    currentColor = color;
    myTimer.switchTimer (currentColor);
  }

  /**
   * changes the current player to the oponent player
   */
  private void changeColor() {
    if ( currentColor == WHITE ) {
      currentColor = BLACK;
      myTimer.switchTimer (currentColor);
    }
    else {
      currentColor = WHITE;
      myTimer.switchTimer (currentColor);
    }
  }

  /**
   * Gets a Position object that represents the recently clicked point on the
   * board, returns null if the clicked position is not on the board
   * 
   * @param xPos
   * @param yPos
   */
  private Position getField(int xPos, int yPos) {
    int rectSize = myBoard.getRectSize ();
    int x, y;
    if ( myBoard.getPerspective () == WHITE ) {
      x = ( xPos / rectSize ) + 1;
      y = ( yPos / rectSize ) + 1;
    }
    else {
      x = AMOUNT_OF_CELLS - ( xPos / rectSize );
      y = AMOUNT_OF_CELLS - ( yPos / rectSize );
    }

    if ( x > AMOUNT_OF_CELLS || x < 1 || y > AMOUNT_OF_CELLS || y < 1
        || xPos < 1 ) {
      // position is not on the GameField!!
      return null;
    }
    else {
      return ( new Position (x, y) );
    }
  }

  /**
   * checks if there is a piece in the given color on the given position
   * 
   * @param pos
   *          position to check for
   * @param color
   *          color to check for
   * @return true, if position contains piece of color
   */
  private boolean isPositionSeated(Position pos, byte color) {
    if ( board.pieceTypeAt (pos.getSquarePosition ()) != EMPTY
        && board.pieceColourAt (pos.getSquarePosition ()) == color ) {
      return true;
    }
    return false;
  }

  /**
   * A button listener for the "new game" item in the menu bar.
   * 
   * @param evt
   *          void
   */
  protected void newButtonActionPerformed(ActionEvent evt) {
    synchronized (myThread) {
      myThread.pause ();
    }
    new NewGameOptions (this);
  }

  /**
   * turns the mouseListener for Human input on the board representation on or
   * off depending on the given parameter
   * 
   * @param b
   */
  private void followTheMouse(boolean b) {
    if ( b ) {
      boardPanel.addMouseListener (humanInputListener);
    }
    else {
      boardPanel.removeMouseListener (humanInputListener);
    }
  }

  /**
   * <code>listenToTheAction</code> implements ActionListeners for the JButtons
   * within the MainWindow and for the UserInput on the board
   */
  private void listenToTheActions() {

    // Button listener for the undo Button
    undoButtonListener = new MouseListener () {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        humanMove = null;
        // Stop the thread
        if ( !myThread.isPaused () ) {
          synchronized (myThread) {
            myThread.pause ();
          }
        }

        // only go back if there is something to go back to
        if ( moveStorage.size () > 0 ) {

          // create a new Board and a new Storage
          Board board2 = new Board ();
          List<Move> moveStorage2 = Collections
              .synchronizedList (new ArrayList<Move> ());
          currentColor = WHITE;

          // fill the new board with all previous moves
          for (int i = 0; i < ( moveStorage.size () - 1 ); i++) {
            board2.makeMove (moveStorage.get (i));
            moveStorage2.add (moveStorage.get (i));
          }
          board = new Board ();
          board = board2;
          myBoard.setBoard (board2);

          changeColorTo (board.getTurnColour ());
          moveStorage = Collections.synchronizedList (new ArrayList<Move> ());
          moveStorage = moveStorage2;
          setCurrentPlayerColorLabel (currentColor);

          addHistory ();
          continueButton.setVisible (false);
          undoUntilButton.setVisible (false);

          frame.repaint ();
        }
        if ( moveStorage.size () <= 0 ) {
          changeColorTo (WHITE);
        }
        if ( myThread.isPaused () ) {
          synchronized (myThread) {
            myThread.goOn ();
          }
        }
      }

      @Override
      public void mouseEntered(MouseEvent arg0) {
      }

      @Override
      public void mouseExited(MouseEvent arg0) {
      }

      @Override
      public void mousePressed(MouseEvent arg0) {
      }

      @Override
      public void mouseReleased(MouseEvent arg0) {
      }
    };
    undoButton.addMouseListener (undoButtonListener);

    // Button Listener for the continue Button of the history Panel
    continueButtonListener = new MouseListener () {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        humanMove = null;
        if ( !myThread.isPaused () ) {
          synchronized (myThread) {
            myThread.pause ();
          }
        }
        board = new Board ();
        for (int i = 0; i < moveStorage.size (); i++) {
          board.makeMove (moveStorage.get (i));
        }
        myBoard.setBoard (board);
        addHistory ();
        continueButton.setVisible (false);
        undoUntilButton.setVisible (false);
        undoButton.setVisible (true);
        frame.repaint ();
        synchronized (myThread) {
          myThread.goOn ();
        }
      }

      @Override
      public void mouseEntered(MouseEvent arg0) {
      }

      @Override
      public void mouseExited(MouseEvent arg0) {
      }

      @Override
      public void mousePressed(MouseEvent arg0) {
      }

      @Override
      public void mouseReleased(MouseEvent arg0) {
      }
    };

    // Button listener for the "undo until" button of the history panel
    undoUntilButtonListener = new MouseListener () {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        if ( !myThread.isPaused () ) {
          synchronized (myThread) {
            myThread.pause ();
          }
        }
        humanMove = null;
        Board board2 = new Board ();
        List<Move> moveStorage2 = Collections
            .synchronizedList (new ArrayList<Move> ());
        for (int i = 0; i < board.getAmountOfMoves (); i++) {
          board2.makeMove (moveStorage.get (i));
          moveStorage2.add (moveStorage.get (i));
        }
        board = new Board (board2);
        changeColorTo (board.getTurnColour ());
        myBoard.setBoard (board);
        moveStorage.clear ();
        moveStorage = moveStorage2;
        addHistory ();
        continueButton.setVisible (false);
        undoUntilButton.setVisible (false);
        synchronized (myThread) {
          myThread.goOn ();
        }
      }

      @Override
      public void mouseEntered(MouseEvent arg0) {
      }

      @Override
      public void mouseExited(MouseEvent arg0) {
      }

      @Override
      public void mousePressed(MouseEvent arg0) {
      }

      @Override
      public void mouseReleased(MouseEvent arg0) {
      }
    };

    // Mouse Listener for the board input, records mouseclick positions and
    // calculates board positions of them
    humanInputListener = new MouseListener () {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        // get clicked position
        Point position = arg0.getPoint ();
        int xPos = position.x;
        int yPos = position.y;
        Position clickToPosition = getField (
            xPos - myBoard.getLeftLegendSpace (), yPos);
        if ( clickToPosition == null ) {}
        else {
          if ( myBoard.getActivePosition () == null ) {
            if ( isPositionSeated (clickToPosition, currentColor) ) {
              myBoard.setActivePosition (clickToPosition);
              if ( highlightPossible ) {
                buildHighlightPossible (clickToPosition);
              }
              frame.repaint ();
            }
          }
          else {
            Move myMove = new Move (myBoard.getActivePosition ()
                .getSquarePosition (), clickToPosition.getSquarePosition ());
            if ( board.isValidMove (myMove) ) {
              myBoard.setActivePosition (null);
              myBoard.setPossibleMoves (null);
              humanMove = myMove;
            }
            else if ( isPositionSeated (clickToPosition, currentColor) ) {
              myBoard.setActivePosition (clickToPosition);
              if ( highlightPossible ) {
                buildHighlightPossible (clickToPosition);
              }
              frame.repaint ();
            }
            else {
              myBoard.setActivePosition (null);
              myBoard.setPossibleMoves (null);
              frame.repaint ();
            }
          }
        }
      }

      @Override
      public void mouseEntered(MouseEvent arg0) {
      }

      @Override
      public void mouseExited(MouseEvent arg0) {
      }

      @Override
      public void mousePressed(MouseEvent arg0) {
        this.mouseClicked (arg0);
      }

      @Override
      public void mouseReleased(MouseEvent arg0) {
        this.mouseClicked (arg0);
      }
    };

    // Component Listener that regulates resizing the window and scaling the
    // board
    ComponentListener resizeListener = new ComponentListener () {
      // This method is called after the component's size changes
      @Override
      public void componentResized(ComponentEvent evt) {
        synchronized (frame) {

          Dimension a = frame.getContentPane ().getSize ();
          int x = a.width;
          int y = a.height - menuBar.getHeight ();
          int biggestBoard = Math.min (y, x - PANELMINDIM.width);
          BOARDDIM = new Dimension (biggestBoard, biggestBoard);
          myBoard.setBoardSize (BOARDDIM);
          myBoard.setPreferredSize (BOARDDIM);
          int newPanelWidth = Math.max (x - biggestBoard, PANELMINDIM.width);
          PANELDIM = new Dimension (newPanelWidth, biggestBoard);
          rightPanel.setPreferredSize (PANELDIM);
          listScroller.setPreferredSize (new Dimension (PANELDIM.width,
              PANELDIM.height / HISTORYCOLUMNS));
          list.setFixedCellWidth (( PANELDIM.width - SCROLLBAR_RESIZEMENT )
              / HISTORYCOLUMNS);
          myBoard.revalidate ();
          rightPanel.revalidate ();
          moveHistoryPanel.revalidate ();
          listScroller.revalidate ();
          frame.pack ();
        }
      }

      @Override
      public void componentHidden(ComponentEvent arg0) {
      }

      @Override
      public void componentMoved(ComponentEvent arg0) {
      }

      @Override
      public void componentShown(ComponentEvent arg0) {
      }
    };
    frame.addComponentListener (resizeListener);
  }

  /**
   * generations the LinkedList containing possible/valid moves for the piece
   * set on the given Position
   * 
   * @param positionToCheck
   */
  protected void buildHighlightPossible(Position positionToCheck) {
    LinkedList<Position> possibles = new LinkedList<Position> ();
    int position = positionToCheck.getSquarePosition ();
    byte pieceType = board.pieceTypeAt (position);
    ArrayList<Move> pos = board.generateValidMoves (pieceType, position);
    for (int i = 0; i < pos.size (); i++) {
      possibles.add (new Position (pos.get (i).to ()));
    }
    myBoard.setPossibleMoves (possibles);
  }

  /**
   * generates the last move for the board highlighting
   */
  protected void buildLastMove() {
    if ( board.getAmountOfMoves () > 0 ) {
      int a = moveStorage.get (moveStorage.size () - 1).from ();
      int b = moveStorage.get (moveStorage.size () - 1).to ();
      Position[] lasMov = { new Position (a), new Position (b) };
      myBoard.setLastMove (lasMov);
      frame.repaint ();
    }
    else {
      myBoard.setLastMove (null);
    }
  }

  /**
   * <code>ActionThread</code> extends Thread to build the game-thread that can
   * be stopped and restarted either at the end of a game/beginning of a new
   * game or by using the history panel to get back in the game history
   * 
   */
  private class ActionThread extends Thread {

    // threadPause to distinguish if thread is paused or not
    private boolean threadPause = true;

    /**
     * <code>run</code> decides what happens while thread is running
     */
    public void run() {

      // while thread is alive continue always!
      while ( true ) {

        synchronized (this) {

          // check if thread is paused...
          while ( threadPause ) {
            try {

              // ...and keep it waiting, as long as paused
              wait ();
            }
            catch (InterruptedException e1) {
              e1.printStackTrace ();
            }
          }
          
          if ( currentColor == WHITE ) {
            // ...let player one make a move
            makeAmove (p1);
          }
          else if(currentColor==BLACK){
            // ... let player2 make a move
            makeAmove (p2);
          }
        }

        // while thread is not paused run the game: if currentColor is White...

        
        
      }
    }

    /**
     * <code>pause</code> pauses the thread
     */
    public void pause() {
      followTheMouse (false);
      threadPause = true;
    }

    public void waitHere() {
      try {
        wait (1);
      }
      catch (InterruptedException e1) {
        e1.printStackTrace ();
      }
    }

    /**
     * <code>goOn</code> continues the thread
     */
    public void goOn() {
      threadPause = false;
      this.notify ();
    }

    public boolean isPaused() {
      return threadPause;
    }
  }
}