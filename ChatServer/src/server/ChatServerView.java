package server;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

/** 
 * The class describes the GUI components and their implementation of application.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 * @see ServerDispatcher
 */

public class ChatServerView {
  private JFrame frame;
  private JTextPane textPane;
  private JScrollPane textPaneSlider;

  /**
   * This class generate GUI of chat server.
   */
  public void initializeChatView() {
    frame = new JFrame();
    frame.setBounds(100, 100, 452, 652);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);

    textPane = new JTextPane();
    textPane.setEditable(false);
    textPaneSlider = new JScrollPane(textPane);
    textPaneSlider.setBounds(12, 16, 408, 564);
    textPaneSlider.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    frame.getContentPane().add(textPaneSlider);

    frame.setVisible(true);
  }

  public synchronized void writeMessageOnTextPane(String message) {
    textPane.setText(getTextPane().getText() + message + '\n');
    textPane.setCaretPosition(textPane.getText().length());
  }

  public JFrame getFrame() {
    return frame;
  }

  public void setFrame(JFrame frame) {
    this.frame = frame;
  }

  public JTextPane getTextPane() {
    return textPane;
  }

  public void setTextPane(JTextPane textPane) {
    this.textPane = textPane;
  }
}
