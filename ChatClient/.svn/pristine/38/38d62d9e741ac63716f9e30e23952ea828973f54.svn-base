package client;

import common.Client;
import common.Message;
import crypto.RsaKeyPair;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The class implements the logic of the SecureChat application.
 * It creates the GUI interface and listens for events from GUI and 
 * processes the events observed.
 * Also the class starts the communication with the server and 
 * the threads 'MessageSender' and 'MessageReceiver'
 * @author Svetoslav Mihovski.
 * @version 2.0.
 * @see MessageSender.java
 * @see MessageReceiver.java
 */
public class ClientController {

  // define types of message style
  public static final byte SYS_MSG = 0;
  public static final byte MY_MSG = 1;
  public static final byte PARTNER_MSG = 2;
  public static final byte TIME_STAMP = 10;

  private ChatClientView clientView; // view
  private MessageSender sendMsg;
  private MessageReceiver readMsg;

  private Socket conn = null;
  private String serverIp = "";
  private int serverPort = 0;
  private ObjectOutputStream oos = null;
  private ObjectInputStream ois = null;
  private boolean connected = false;

  private ArrayList<Client> activeClientsList;
  private String myName = "";
  private RsaKeyPair rsaKeyPair;

  private String fullFileName = "";

  /** 
   * The constructor of the ClientController.
   * Creates an client controller object.
   * @param serverIp ; IP address of server
   * @param serverPort ; Port of server
   */
  public ClientController(ChatClientView clientView, String serverIp, int serverPort) {
    this.clientView = clientView;
    this.serverIp = serverIp;
    this.serverPort = serverPort;

    this.activeClientsList = new ArrayList<>(); // create empty client list array
  }

  private void connectToServer() {

    // try to make socket connection to the chat server
    if (serverIp.equals("")) { // no command line argument -> try preferred IP's
      for (String prefServerIp : SecureChat.PREF_SERVER_IP_ADDRESSES) { // loop over preferred IP's
        trySocketConnection(prefServerIp, serverPort);
        if (connected) {
          break;
        }
      }
    } else {
      trySocketConnection(serverIp, serverPort); // try with command line parameters
    }

    if (connected) {
      clientView.getBtnConnect().setEnabled(false); // disable Connect button
      loginToServer(); // connection established, continue logging
    }
  }

  /** 
   * Makes an attempt for a connection with a server using a specific IP adress and port.
   * @param serverIp ; IP address of server
   * @param serverPort ; Port of server
   */
  private void trySocketConnection(String serverIp, int serverPort) {

    clientView.writeOnTextPane(
        "Try to connect to " + serverIp + " on port : " + serverPort, SYS_MSG);
    try {
      conn = new Socket(serverIp, this.serverPort);
      oos = new ObjectOutputStream(conn.getOutputStream()); // create output stream over connection
      ois = new ObjectInputStream(conn.getInputStream()); // create input stream over connection
      writeOnTextPane("Connected to server " + serverIp + " on port : " + serverPort, SYS_MSG);
      connected = true;
    } catch (IOException ioe) {
      writeOnTextPane("Can not connect to " + serverIp + " on port : " + serverPort, SYS_MSG);
      writeOnTextPane("Try again later", SYS_MSG);
      connected = false;
    }
  }

  // login user to server
  private void loginToServer() {

    this.rsaKeyPair = new RsaKeyPair(SecureChat.RSA_KEY_LENGTH); // generate RSA key pair for client

    // start thread for sending messages
    sendMsg = new MessageSender(this);
    sendMsg.start();

    // start thread for handling received messages
    readMsg = new MessageReceiver(this, sendMsg);
    readMsg.start();

    // Send initial message with user name and public key
    byte[] pubKey = rsaKeyPair.getPublicKey().convertToBytes(); // convert public key to byte area
    Message message =
        new Message(Message.SystemMessage, Message.InitMessage, myName, "server", pubKey);
    try {
      sendMsg.sendMessage(message);
    } catch (IOException e) {
      writeOnTextPane("Communication error", SYS_MSG);
    }
    clientView.getBtnConnect().setEnabled(true); // enable Connect button
    clientView.getBtnConnect().setText("Disconnect");

    // Client wait to be accepted and to receive active clients list
    writeOnTextPane("Wait for partners", SYS_MSG);
  }

  private void disconnectFromServer() {

    try {

      myName = "";
      clientView.getUserName().setText("");
      clientView.getPartner().setText("");
      clientView.getListModel().clear();
      activeClientsList.clear();
      connected = false;
      conn.close();
      clientView.getBtnConnect().setText("Connect");

    } catch (IOException e) {
      writeOnTextPane("Communication error", SYS_MSG);
    }
  }

  /** 
   * Clean up some parameters when connection is lost.
   */
  public synchronized void lostConnection() {

    myName = "";
    clientView.getUserName().setText("");
    clientView.getPartner().setText("");
    clientView.getListModel().clear();
    activeClientsList.clear();
    connected = false;
    clientView.getUserName().setEditable(true);

    try {
      writeOnTextPane("Disconnected from server", SYS_MSG);
      conn.close();
    } catch (IOException e) {
      writeOnTextPane("Communication error", SYS_MSG);
    }
    clientView.getBtnConnect().setText("Connect");
  }

  private void getConnect() {

    if (!connected) {
      myName = clientView.getUserName().getText();
      if (!myName.equals("")) {
        connectToServer();
      } else {
        writeOnTextPane("Please enter login name", SYS_MSG);
      }

    } else { // logout
      disconnectFromServer();
    }

    if (connected) {
      clientView.getUserName().setEditable(false);
    } else {
      clientView.getUserName().setEditable(true);
    }
  }

  private void sendMessage() {
    Client inChat = searchClientInChat();
    if (!(inChat == null)) {
      String chosenPartner = inChat.getClientName();
      String newMessage = clientView.getMessageBox().getText();
      try {
        if (!newMessage.equals("")) {
          sendMsg.sendUserMessage(newMessage, chosenPartner, Message.EncryptMsg);
        }
        clientView.getMessageBox().setText("");
      } catch (IOException e1) {
        writeOnTextPane("Communication error", SYS_MSG);
      }
    } else {
      writeOnTextPane("No chosen partner !", SYS_MSG);
    }
  }

  /** 
   * This method handles all the GUI events.
   */
  public void guiEvents() {

    // Clear text field userName when clicked
    clientView
        .getUserName()
        .addMouseListener(
            new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                if (!connected) {
                  clientView.getUserName().setText("");
                }
              }
            });

    // Connect / Disconnect Button
    clientView
        .getBtnConnect()
        .addActionListener(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                getConnect();
              }
            });

    // Connect on Enter
    clientView
        .getUserName()
        .addKeyListener(
            new KeyAdapter() {
              @Override
              public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                  getConnect();
                }
              }
            });

    // wait for choose partner
    // when partner is chosen, client request server for his public key
    clientView
        .getActiveUsersList()
        .addListSelectionListener(
            new ListSelectionListener() {
              @Override
              public void valueChanged(ListSelectionEvent e) {
                if (clientView.getActiveUsersList().getValueIsAdjusting()) {
                  String chosenPartner =
                      clientView.getActiveUsersList().getSelectedValue().toString();
                  Client client = searchClient(chosenPartner);
                  if (client.getStatus() == Client.ReadyForSecureChat) {
                    chosenPartner = client.getClientName();
                    clientView.getPartner().setText(chosenPartner); // set partner name in GUI
                    writeOnTextPane("Secure chat with : " + chosenPartner + " started", SYS_MSG);
                    setPartnerInChat(client);
                  } else {
                    client.setStatus(Client.PubKeyReq); // prepare for public key request
                    sendMsg.setClientInHandshake(true); // wake up tread
                  }
                }
              }
            });

    // Send message Button
    clientView
        .getSendMessage()
        .addActionListener(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                sendMessage();
              }
            });

    // Send Message on Enter
    clientView
        .getMessageBox()
        .addKeyListener(
            new KeyAdapter() {
              @Override
              public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                  sendMessage();
                }
              }
            });

    // Send file Button
    clientView
        .getSendFile()
        .addActionListener(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                Client inChat = searchClientInChat();
                if (!(inChat == null)) {
                  String chosenPartner = inChat.getClientName();

                  FileDialog dialog = new FileDialog((Frame) null, "Choose File to Send");
                  dialog.setMode(FileDialog.LOAD);
                  dialog.setVisible(true);

                  String filePatch = dialog.getDirectory();
                  String fileName = dialog.getFile();

                  //clientView.getMessageBox().setText(fileName);
                  fullFileName = filePatch + fileName;

                  try {
                    if (!fullFileName.equals("nullnull")) {
                      writeOnTextPane("Sending file ..." + fileName, SYS_MSG);
                      sendMsg.sendUserMessage(fullFileName, chosenPartner, Message.EncryptFile);
                    }
                  } catch (IOException e1) {
                    writeOnTextPane("Communication error", SYS_MSG);
                  }
                } else {
                  writeOnTextPane("No chosen partner !", SYS_MSG);
                }
              }
            });
  }

  public synchronized void writeOnTextPane(String message, byte messageType) {
    clientView.writeOnTextPane(message, messageType);
  }

  public DefaultListModel<String> getListModel() {
    return clientView.getListModel();
  }

  public JList<String> getActiveUsersList() {
    return clientView.getActiveUsersList();
  }

  public JTextField getPartner() {
    return clientView.getPartner();
  }

  public ArrayList<Client> getActiveClientsList() {
    return activeClientsList;
  }

  /**
   * Search client by name in client's list.
   */
  public Client searchClient(String name) {
    synchronized (activeClientsList) {
      for (Client client : activeClientsList) {
        if (client.getClientName().equals(name)) {
          return client;
        }
      }
      return null;
    }
  }

  /** 
   * Search client that is 'inChat'.
   */
  public Client searchClientInChat() {
    synchronized (activeClientsList) {
      for (Client client : activeClientsList) {
        if (client.isInChat()) {
          return client;
        }
      }
      return null;
    }
  }

  /**
   * Set partner 'inChat' and clear all other.
   * */
  public void setPartnerInChat(Client partnerInChat) {
    synchronized (activeClientsList) {
      for (Client client : activeClientsList) {
        if (client.equals(partnerInChat)) {
          client.setInChat(true);
        } else {
          client.setInChat(false);
        }
      }
    }
  }

  public RsaKeyPair getRsaKeyPair() {
    return rsaKeyPair;
  }

  public String getMyName() {
    return myName;
  }

  public MessageSender getSendMsg() {
    return sendMsg;
  }

  public MessageReceiver getReadMsg() {
    return readMsg;
  }

  public ObjectOutputStream getOos() {
    return oos;
  }

  public ObjectInputStream getOis() {
    return ois;
  }
}
