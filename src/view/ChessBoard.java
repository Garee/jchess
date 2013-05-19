package view;

import static lookup.Pieces.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
//import java.awt.event.MouseMotionAdapter;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.Board;

/**
 * <code>PaintBoard</code> extends JPanel to build a ChessBoard for a Chess GUI
 * implementation
 * 
 */
class ChessBoard extends JPanel {
  private static final long serialVersionUID = -7048813151369302471L;
  private static int rectSize;
  private static int cols;
  private static int rows;

  private final int LEFTLEGEND_SPACE = 25;
  private final int CELL_AMOUNT = 8;
  private final int PREVISOU_LINE_WIDTH = 3;
  private final int DOUBLE_COLUMN_DIVISOR = 2;
  private final double SCALING_TO_HALF = 0.5;

  // Theming:
  private final Color INITIAL = Color.white;
  private Color blackBG = INITIAL;
  private Color whiteBG = INITIAL;
  private Color activeBG = INITIAL;
  private Color possibleBG = INITIAL;
  private Color previousBG = INITIAL;

  private final int PIC_INDEX_BLACK_BISHOP = 0;
  private final int PIC_INDEX_BLACK_KING = 1;
  private final int PIC_INDEX_BLACK_KNIGHT = 2;
  private final int PIC_INDEX_BLACK_PAWN = 3;
  private final int PIC_INDEX_BLACK_QUEEN = 4;
  private final int PIC_INDEX_BLACK_ROOK = 5;
  private final int PIC_INDEX_WHITE_BISHOP = 6;
  private final int PIC_INDEX_WHITE_KING = 7;
  private final int PIC_INDEX_WHITE_KNIGHT = 8;
  private final int PIC_INDEX_WHITE_PAWN = 9;
  private final int PIC_INDEX_WHITE_QUEEN = 10;
  private final int PIC_INDEX_WHITE_ROOK = 11;

  private final int PIC_INDEX_KING_IN_CHECK = 12;
  private final int PIC_INDEX_CAPTURE = 13;

  private final String[] legendAlph = { "a", "b", "c", "d", "e", "f", "g", "h" };
  private final String[] legendNum = { "1", "2", "3", "4", "5", "6", "7", "8" };

  // Highlighting
  private Position activePosition = null;
  private LinkedList<Position> possibleMoves = null;
  private Position[] lastMove = null;

  private Board boardData = new Board ();
  private BufferedImage[] image = new BufferedImage[14];

  // path to images, change here, if error!
  private final String FOLDER_STRUCTURE = "img/imageSet";
  
  // change default image-set here
  int folderNumber = 1;
  //  String folder = FOLDER_STRUCTURE + folderNumber + "/";
  String folder = "img/pieces/";
  String[] PIECES;

  Graphics boardRep;
  byte currentColor = WHITE;
  byte perspective = WHITE;

  /**
   * constructor sets the game, the paintBoard uses to paint and the Cell-size
   * 
   * @param a
   *          cell-size to paint the board
   * @param theme
   * @param gamel
   *          game to get information from
   */
  public ChessBoard(Dimension a, Theme theme) {
    makePiecesArray (folder);
    setBoardSize (a);
    setTheme (theme);
  }

  private void makePiecesArray(String folder) {
    
    String[] newPieces = { folder + "bb.png", folder + "bk.png",
        folder + "bn.png", folder + "bp.png", folder + "bq.png",
        folder + "br.png", folder + "wb.png", folder + "wk.png",
        folder + "wn.png", folder + "wp.png", folder + "wq.png",
        folder + "wr.png", folder + "checkHighlightning.png",
        folder + "dangerHighlightning.png", };
    PIECES = newPieces;
    
    try {
      for (int i = 0; i < PIECES.length; i++) {
	System.out.println(PIECES[i]);
        image[i] = ImageIO.read (new File (PIECES[i]));
      }
    }
    catch (IOException ex) {
      System.out.println ("Cannot find image path. ChessBoard.java, change "
          + "it manually, therefore use the final FOLDER_STRUCTURE in "
          + "ChessBoard.java.");
    }
  }

  public int getBoardDimension() {
    return this.getHeight ();
  }

  // @Override
  public void setBoardSize(Dimension a) {
    rectSize = (int) ( ( a.getWidth () - LEFTLEGEND_SPACE ) / CELL_AMOUNT );
  }

  public int getRectSize() {
    return rectSize;
  }

  public int getLeftLegendSpace() {
    return LEFTLEGEND_SPACE;
  }

  public void setTheme(Theme a) {
    blackBG = a.getBlackColor ();
    whiteBG = a.getWhiteColor ();
    activeBG = a.getActivePosition ();
    possibleBG = a.getPossiblePosition ();
    previousBG = a.getPreviousArrow ();
    folderNumber = a.getFolderNumber ();
    folder = "img/pieces/";
    makePiecesArray (folder);
  }

  public Position getActivePosition() {
    return activePosition;
  }

  public void setActivePosition(Position activePosition) {
    this.activePosition = activePosition;
  }

  public void setPossibleMoves(LinkedList<Position> positions) {
    this.possibleMoves = positions;
  }

  public void setLastMove(Position[] lastMoves) {
    this.lastMove = lastMoves;
  }

  public void setPerspective(byte color) {
    this.perspective = color;
  }

  public byte getPerspective() {
    return this.perspective;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent (g);
    boardRep = g;
    // amount of cols and rows
    cols = CELL_AMOUNT;
    rows = CELL_AMOUNT;
    // paint
    for (int i = 0; i < cols; i++) {

      int cellcol = ( i * rectSize ) + 1 + LEFTLEGEND_SPACE;
      for (int j = 0; j < rows; j++) {

        // calculate position of every field

        int cellrow = ( j * rectSize ) + 1;

        // set its color black, if its a black field
        if ( i % DOUBLE_COLUMN_DIVISOR == 0 ) {
          if ( j % DOUBLE_COLUMN_DIVISOR == 0 ) {
            g.setColor (whiteBG);
          }
          else {
            g.setColor (blackBG);
          }
        }
        else {
          if ( j % DOUBLE_COLUMN_DIVISOR == 0 ) {
            g.setColor (blackBG);
          }
          else {
            g.setColor (whiteBG);
          }
        }
        g.fillRect (cellcol, cellrow, rectSize, rectSize);

      }

      int a;
      if ( perspective == WHITE ) {
        a = i;
      }
      else {
        a = CELL_AMOUNT - 1 - i;
      }
      int fontWidth = g.getFontMetrics ().charWidth (legendAlph[a].indexOf (0));
      int fontSize = (int) ( LEFTLEGEND_SPACE * SCALING_TO_HALF );
      int vertSpaceLeft = (int) ( SCALING_TO_HALF * rectSize + SCALING_TO_HALF
          * fontSize );
      int vertSpaceBottom = (int) ( ( 1 + SCALING_TO_HALF ) * fontSize );
      int horSpaceBottom = (int) ( SCALING_TO_HALF * rectSize - SCALING_TO_HALF
          * fontWidth );
      // paint the legend to the left and the bottom
      Font f = new Font ("Dialog", Font.PLAIN, fontSize);
      g.setColor (Color.BLACK);
      g.setFont (f);
      g.drawString (legendAlph[a], cellcol + horSpaceBottom, rectSize
          * CELL_AMOUNT + vertSpaceBottom);
      g.drawString (legendNum[CELL_AMOUNT - 1 - a], 5, ( i ) * rectSize
          + vertSpaceLeft);

    }

    setHighlightPosition ();

    paintGrid ();
    setHighlightLast ();
    setPiecePics ();
  }

  /**
   * paints a Grid over the Chessboard (rectangles are under the grid) The grid
   * looks like borders for the rectangles
   * 
   * @param g
   *          Chessboard
   */
  private void paintGrid() {

    Graphics2D g2 = (Graphics2D) boardRep;
    g2.setColor (blackBG);
    g2.setStroke (new BasicStroke (1, BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_MITER));
    // paint horizontal lines
    for (int i = 0; i <= rows; i++) {
      boardRep.drawLine (1, ( i * rectSize ) + 1, ( cols * rectSize ) + 1
          + LEFTLEGEND_SPACE, ( i * rectSize ) + 1);
    }

    // paint vertical lines
    for (int i = 0; i <= cols; i++) {
      boardRep.drawLine (( i * rectSize ) + 1 + LEFTLEGEND_SPACE, 1,
          ( i * rectSize ) + 1 + LEFTLEGEND_SPACE, ( rows * rectSize ) + 1
              + LEFTLEGEND_SPACE);
    }
  }

  private void setPiecePics() {
    byte[] usableData = boardData.getSquares ();
    synchronized (usableData) {
      for (int i = 0; i < usableData.length; i++) {
        Byte thisSquare = Board.pieceType (usableData[i]);
        if ( Board.pieceColour (usableData[i]) == BLACK ) {
          switch (thisSquare) {
            case ROOK:
              paintPic (new Position (i), PIC_INDEX_BLACK_ROOK);
              break;
            case KNIGHT:
              paintPic (new Position (i), PIC_INDEX_BLACK_KNIGHT);
              break;
            case BISHOP:
              paintPic (new Position (i), PIC_INDEX_BLACK_BISHOP);
              break;
            case QUEEN:
              paintPic (new Position (i), PIC_INDEX_BLACK_QUEEN);
              break;
            case KING:
              paintPic (new Position (i), PIC_INDEX_BLACK_KING);
              break;
            case PAWN:
              paintPic (new Position (i), PIC_INDEX_BLACK_PAWN);
              break;
            default:
              ;
          }
        }
        else {
          switch (thisSquare) {
            case ROOK:
              paintPic (new Position (i), PIC_INDEX_WHITE_ROOK);
              break;
            case KNIGHT:
              paintPic (new Position (i), PIC_INDEX_WHITE_KNIGHT);
              break;
            case BISHOP:
              paintPic (new Position (i), PIC_INDEX_WHITE_BISHOP);
              break;
            case QUEEN:
              paintPic (new Position (i), PIC_INDEX_WHITE_QUEEN);
              break;
            case KING:
              paintPic (new Position (i), PIC_INDEX_WHITE_KING);
              break;
            case PAWN:
              paintPic (new Position (i), PIC_INDEX_WHITE_PAWN);
              break;
            default:
              ;
          }

        }
      }

    }

  }

  private synchronized void paintPic(Position pos, int pic) {
    int x, y;
    if ( perspective == WHITE ) {
      x = pos.getX () - 1;
      y = pos.getY () - 1;
    }
    else {
      x = CELL_AMOUNT - 1 - ( pos.getX () - 1 );
      y = CELL_AMOUNT - 1 - ( pos.getY () - 1 );
    }

    int cellcol = ( x * rectSize + 1 + LEFTLEGEND_SPACE );
    int cellrow = ( y * rectSize + 1 );
    this.boardRep.drawImage (image[pic], cellcol, cellrow, rectSize, rectSize,
        this);
  }

  public void setBoard(Board board) {
    boardData = board;
    // repaint();
  }

  public Board getBoard() {
    return boardData;
  }

  /**
   * Changes Background-color of selected field
   * 
   * @return
   */
  private void setHighlightPosition() {
    if ( activePosition != null ) {
      int x, y;
      if ( perspective == WHITE ) {
        x = activePosition.getX () - 1;
        y = activePosition.getY () - 1;
      }
      else {
        x = CELL_AMOUNT - activePosition.getX ();
        y = CELL_AMOUNT - activePosition.getY ();
      }

      int cellcol = ( ( x ) * rectSize );
      int cellrow = ( ( y ) * rectSize );
      boardRep.setColor (activeBG);
      boardRep.fillRect (cellcol + 1 + LEFTLEGEND_SPACE, cellrow + 1, rectSize,
          rectSize);
    }

    if ( boardData.kingInCheck () ) {
      Position p = new Position (boardData.getKingPosition ());
      paintPic (p, PIC_INDEX_KING_IN_CHECK);
    }

    if ( possibleMoves != null ) {
      for (int i = 0; i < possibleMoves.size (); i++) {
        Position thisPosition = possibleMoves.get (i);
        int x, y;
        if ( perspective == WHITE ) {
          x = thisPosition.getX () - 1;
          y = thisPosition.getY () - 1;
        }
        else {
          x = CELL_AMOUNT - thisPosition.getX ();
          y = CELL_AMOUNT - thisPosition.getY ();
        }

        int cellcol = ( ( x ) * rectSize );
        int cellrow = ( ( y ) * rectSize );
        boardRep.setColor (possibleBG);
        boardRep.fillRect (cellcol + 1 + LEFTLEGEND_SPACE, cellrow + 1,
            rectSize, rectSize);
        if ( boardData.enemyPieceAt (thisPosition.getSquarePosition ()) ) {
          paintPic (thisPosition, PIC_INDEX_CAPTURE);
        }
      }
    }

  }

  private void setHighlightLast() {
    if ( lastMove != null ) {
      int x, y, xx, yy;
      if ( perspective == WHITE ) {
        x = lastMove[0].getX () - 1;
        y = lastMove[0].getY () - 1;
        xx = lastMove[1].getX () - 1;
        yy = lastMove[1].getY () - 1;
      }
      else {
        x = CELL_AMOUNT - lastMove[0].getX ();
        y = CELL_AMOUNT - lastMove[0].getY ();
        xx = CELL_AMOUNT - lastMove[1].getX ();
        yy = CELL_AMOUNT - lastMove[1].getY ();
      }

      int x1 = ( x ) * rectSize + ( rectSize / DOUBLE_COLUMN_DIVISOR )
          + LEFTLEGEND_SPACE;
      int y1 = ( y ) * rectSize + ( rectSize / DOUBLE_COLUMN_DIVISOR );
      int x2 = ( xx ) * rectSize + ( rectSize / DOUBLE_COLUMN_DIVISOR )
          + LEFTLEGEND_SPACE;
      int y2 = ( yy ) * rectSize + ( rectSize / DOUBLE_COLUMN_DIVISOR );

      Graphics2D g2 = (Graphics2D) boardRep;
      g2.setColor (previousBG);
      g2.setStroke (new BasicStroke (PREVISOU_LINE_WIDTH,
          BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
      g2.drawLine (x1, y1, x2, y2);

    }
  }
}
