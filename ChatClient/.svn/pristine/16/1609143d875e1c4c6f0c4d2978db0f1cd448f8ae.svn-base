package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * The class describes the GUI components and their implementation of application.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 * @see ClientController.java
 */

public class ChatClientView {
  private JFrame frame;
  private JTextPane textPane;
  private JPanel textPanel;
  private JScrollPane textPaneSlider;
  private StyledDocument doc;

  private SimpleAttributeSet left;
  private SimpleAttributeSet right;
  private SimpleAttributeSet sys;
  private SimpleAttributeSet timeStamp;

  private JTextField messageBox;
  private JTextField userName;
  private JTextField partner;
  private JList<String> activeUsersList;
  private DefaultListModel<String> listModel;
  private JButton btnConnect;
  private JButton sendMessage;
  private JButton sendFile;

  /**
   * This class generate GUI of 'Secure Chat'.
   */
  public void initializeChatView() {
    frame = new JFrame();
    frame.setBounds(100, 100, 678, 455);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);

    messageBox = new JTextField();
    messageBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
    messageBox.setColumns(10);
    messageBox.setBounds(211, 359, 323, 36);
    frame.getContentPane().add(messageBox);

    textPane = new JTextPane();
    textPane.setEditable(false);
    textPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
    textPanel = new JPanel(new BorderLayout());
    textPanel.add(textPane, BorderLayout.SOUTH);

    textPaneSlider = new JScrollPane(textPanel);
    textPaneSlider.setBounds(211, 13, 441, 343);
    textPaneSlider.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    textPaneSlider.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    frame.getContentPane().add(textPaneSlider);

    doc = textPane.getStyledDocument();

    left = new SimpleAttributeSet();
    StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
    StyleConstants.setForeground(left, Color.BLACK);

    right = new SimpleAttributeSet();
    StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
    StyleConstants.setForeground(right, Color.BLUE);

    sys = new SimpleAttributeSet();
    StyleConstants.setAlignment(sys, StyleConstants.ALIGN_LEFT);
    StyleConstants.setForeground(sys, Color.RED);

    timeStamp = new SimpleAttributeSet();
    StyleConstants.setAlignment(timeStamp, StyleConstants.ALIGN_RIGHT);
    StyleConstants.setForeground(timeStamp, Color.GRAY);
    StyleConstants.setFontSize(timeStamp, 12);

    btnConnect = new JButton("Connect");
    btnConnect.setToolTipText("Connect to Server");
    btnConnect.setFont(new Font("Calibri", Font.BOLD, 16));
    btnConnect.setBounds(14, 55, 172, 26);
    frame.getContentPane().add(btnConnect);

    sendMessage = new JButton("Send");
    sendMessage.setIcon(null);
    sendMessage.setToolTipText("Send Message");
    sendMessage.setFont(new Font("Calibri", Font.BOLD, 12));
    sendMessage.setBounds(593, 359, 59, 36);
    frame.getContentPane().add(sendMessage);

    sendFile = new JButton("");
    sendFile.setVerticalAlignment(SwingConstants.TOP);
    sendFile.setIcon(
        new ImageIcon(
            ChatClientView.class.getResource("/com/sun/java/swing/plaf/windows/icons/File.gif")));
    sendFile.setToolTipText("Send File");
    sendFile.setBounds(539, 360, 49, 35);
    frame.getContentPane().add(sendFile);

    userName = new JTextField();
    userName.setHorizontalAlignment(SwingConstants.CENTER);
    userName.setFont(new Font("Calibri", Font.PLAIN, 18));
    userName.setText("Enter Username");
    userName.setBounds(14, 16, 172, 26);
    userName.setColumns(10);
    frame.getContentPane().add(userName);

    listModel = new DefaultListModel<String>();
    activeUsersList = new JList<String>(listModel);
    activeUsersList.setForeground(Color.GREEN);
    activeUsersList.setFont(new Font("Tahoma", Font.PLAIN, 18));
    activeUsersList.setBorder(new LineBorder(new Color(0, 0, 0)));
    activeUsersList.setBounds(14, 253, 172, 140);

    frame.getContentPane().add(activeUsersList);

    partner = new JTextField();
    partner.setForeground(Color.RED);
    partner.setFont(new Font("Tahoma", Font.PLAIN, 18));
    partner.setBounds(14, 154, 172, 26);
    partner.setColumns(10);
    partner.setEditable(false);
    frame.getContentPane().add(partner);

    JLabel lblUsersOnline = new JLabel("List of Users Online:");
    lblUsersOnline.setFont(new Font("Calibri", Font.BOLD, 18));
    lblUsersOnline.setBounds(14, 220, 172, 20);
    frame.getContentPane().add(lblUsersOnline);

    JLabel lblChatWith = new JLabel("Currently Chatting with:");
    lblChatWith.setFont(new Font("Calibri", Font.BOLD, 16));
    lblChatWith.setBounds(14, 123, 172, 20);
    frame.getContentPane().add(lblChatWith);

    JSeparator separator1 = new JSeparator();
    separator1.setBounds(14, 94, 172, 2);
    frame.getContentPane().add(separator1);

    JSeparator separator2 = new JSeparator();
    separator2.setBounds(14, 193, 172, 2);
    frame.getContentPane().add(separator2);

    JSeparator separator3 = new JSeparator();
    separator3.setOrientation(SwingConstants.VERTICAL);
    separator3.setBounds(197, 16, 2, 379);
    frame.getContentPane().add(separator3);

    frame.setVisible(true);
  }

  /**
   * Write styled message on TextPane.
   */
  public synchronized void writeOnTextPane(String message, byte type) {

    String timeSt = new SimpleDateFormat("HH:mm").format(new java.util.Date());
    try {
      switch (type) {
        case ClientController.SYS_MSG: // write system message
          doc.setParagraphAttributes(doc.getLength(), 1, sys, false);
          doc.insertString(doc.getLength(), timeSt + " ", timeStamp);
          doc.insertString(doc.getLength(), message + '\n', sys);
          break;

        case ClientController.MY_MSG: // write my message
          doc.setParagraphAttributes(doc.getLength(), 1, right, false);
          doc.insertString(doc.getLength(), message, right);
          doc.insertString(doc.getLength(), " " + timeSt + '\n', timeStamp);
          break;

        case ClientController.PARTNER_MSG: // write partner message
          doc.setParagraphAttributes(doc.getLength(), 1, left, false);
          doc.insertString(doc.getLength(), timeSt + " ", timeStamp);
          doc.insertString(doc.getLength(), message + '\n', left);
          break;

        case ClientController.TIME_STAMP: // write time stamp message
          doc.insertString(doc.getLength(), '\n' + message, timeStamp);
          doc.setParagraphAttributes(doc.getLength(), 1, timeStamp, false);
          break;
        default:
          break;
      }

      textPane.setCaretPosition(doc.getLength());
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
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

  public JScrollPane getTextPaneSlider() {
    return textPaneSlider;
  }

  public void setTextPaneSlider(JScrollPane textPaneSlider) {
    this.textPaneSlider = textPaneSlider;
  }

  public JPanel getTextPanel() {
    return textPanel;
  }

  public void setTextPanel(JPanel textPanel) {
    this.textPanel = textPanel;
  }

  public JTextField getMessageBox() {
    return messageBox;
  }

  public void setMessageBox(JTextField messageBox) {
    this.messageBox = messageBox;
  }

  public JButton getSendMessage() {
    return sendMessage;
  }

  public void setSendMessage(JButton sendMessage) {
    this.sendMessage = sendMessage;
  }

  public JTextField getUserName() {
    return userName;
  }

  public void setUserName(JTextField userName) {
    this.userName = userName;
  }

  public JButton getBtnConnect() {
    return btnConnect;
  }

  public void setBtnConnect(JButton btnConnect) {
    this.btnConnect = btnConnect;
  }

  public JList<String> getActiveUsersList() {
    return activeUsersList;
  }

  public void setActiveUsersList(JList<String> activeUsersList) {
    this.activeUsersList = activeUsersList;
  }

  public DefaultListModel<String> getListModel() {
    return listModel;
  }

  public void setListModel(DefaultListModel<String> listModel) {
    this.listModel = listModel;
  }

  public JTextField getPartner() {
    return partner;
  }

  public void setPartner(JTextField partner) {
    this.partner = partner;
  }

  public JButton getSendFile() {
    return sendFile;
  }

  public void setSendFile(JButton sendFile) {
    this.sendFile = sendFile;
  }
}
