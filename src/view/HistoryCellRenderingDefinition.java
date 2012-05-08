package view;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Cell Rendering definition for the move-history list
 * 
 * @author Joshua Schaeuble (all containing methods)
 * 
 */
public class HistoryCellRenderingDefinition extends JLabel implements
    ListCellRenderer {

  private static final long serialVersionUID = 1L;

  private final Color HIGHLIGHTING = Color.LIGHT_GRAY;

  public HistoryCellRenderingDefinition() {
    super ();
    setOpaque (true);
  }

  @Override
  public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {

    // Assumes the stuff in the list has a pretty toString
    setText (value.toString ());

    // here the highlighting of every second row is defined
    if ( ( index + 2 ) % 4 == 0 ) setBackground (HIGHLIGHTING);
    else if ( ( index + 1 ) % 4 == 0 ) setBackground (HIGHLIGHTING);
    else
      setBackground (Color.WHITE);

    if ( cellHasFocus ) {
      setBackground (Color.BLUE);
    }
    return this;
  }
}
