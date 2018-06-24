package client;

import common.Client;
import common.Message;
import common.Utilities;
import crypto.Aes;
import crypto.PublicKey;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The class is responsible for the processing of received messages.
 * It is started as a thread from 'ClientController.java' class
 * @author Svetoslav Mihovski.
 * @version 2.0.
 * @see ClientController.java
 * @see MessageSender.java
 */
public class MessageReceiver extends Thread {

  private ClientController clientController;
  private MessageSender sendMsg;

  private ObjectInputStream ois;
  private ArrayList<Client> activeClientsList;

  /**
   * The controller creates the message receiver thread.
   * @param clientController ; clientController
   */
  public MessageReceiver(ClientController clientController, MessageSender sendMsg) {
    this.clientController = clientController;
    this.sendMsg = sendMsg;

    this.ois = clientController.getOis();
    this.activeClientsList = clientController.getActiveClientsList();
  }

  /**
   * Starts the thread.
   */
  @Override
  public synchronized void run() {
    Client client = null;
    String partnerName = "";
    Message message = null;
    byte[] byteSessionKey = null;
    while (!isInterrupted()) {
      // Read message from object input stream, take action or print on TextPane
      try {
        message = (Message) ois.readObject();
        client = clientController.searchClient(message.getSender()); // find partner
        synchronized (activeClientsList) {
          switch (message.getMessageType()) {
            case Message.SystemMessage: // receive system message
              switch (message.getCommandType()) {
                case Message.ListOfClients: // initial message with client list from server
                  String clientList = new String(message.getPayload());
                  String[] clientListArray = clientList.split(";"); // write received list to array
                  for (String partner : clientListArray) {
                    if (!partner.equals("")) {
                      client = new Client(partner, Client.Wait, false);
                      activeClientsList.add(client); // add client to client's list
                      clientController.getListModel().addElement(partner); // update list box
                    }
                  }
                  break;

                case Message.NewClient: // new client notification
                  partnerName = new String(message.getPayload()); // get partner's name from message
                  client = new Client(partnerName, Client.Wait, false);
                  activeClientsList.add(client); // add client to client's list
                  clientController.getListModel().addElement(partnerName);
                  break;

                case Message.PubKeyRsp: // receive public key of partner from server
                  PublicKey partnerPublicKey =
                      PublicKey.convertFromBytes(message.getPayload()); // get partner's public key
                  client.setPublicKey(partnerPublicKey); // set public key of partner

                  if (client.getSessionKey()
                      == null) { // 1st case - for this partner, I don't have session key
                    client.setStatus(Client.PubKeyRsv); // set status:PublicKeyReceived
                  } else { // 2nd case - for this partner, I already have session key
                    client.setStatus(
                        Client.SessionKeyReq); // set status:Session Key request received
                  }
                  break;

                case Message.SessionKeyReq: // receive encrypted session Key
                  client.setSessionKey(
                      message.getPayload()); // store encrypted session key candidate and challenge

                  if (!(client.getPublicKey() == null)) {
                    // public key already received - do something
                  } else {
                    client.setStatus(Client.PubKeyReq); // need public key
                    sendMsg.setClientInHandshake(true); // wake up sender
                  }
                  break;

                case Message.SessionKeyRsp: // receive response of session key request
                  client.setSessionKey(message.getPayload()); // store session key and challenge
                  client.setStatus(Client.SessionKeyReqRsp);
                  break;

                case Message.SecureChatReady: // Ready for secure chat
                  client.setMessage(message.getPayload()); // store session key
                  client.setStatus(Client.SessionKeyOKConf);
                  break;

                case Message.SessionKeyOK: // Session key check O'K
                  client.setStatus(Client.SessionKeyOK);
                  break;

                case Message.SessionKeyBad: // Session key check fail
                  client.setStatus(Client.SessionKeyBadRsv);
                  break;

                case Message.DscClient: // Disconnected client notification
                  activeClientsList.remove(client); // remove client from client's list

                  if ((clientController.getPartner().getText()).equals(message.getSender())) {
                    clientController.getPartner().setText("");
                  }

                  // Get number of items in the list
                  int size = clientController.getListModel().getSize();
                  // Get all item objects
                  for (int i = 0; i < size; i++) {
                    String item = clientController.getListModel().getElementAt(i);
                    partnerName = new String(message.getPayload());
                    if (partnerName.equals(item)) {
                      clientController.getListModel().remove(i); // remove from GUI list
                      break;
                    }
                  }

                  break;
                default:
                  break;
              }
              break;

            case Message.UserMessage:
              switch (message.getCommandType()) {
                case Message.PlainTxtMsg: // receive plain text message
                  String newMessage = new String(message.getPayload());
                  clientController.writeOnTextPane(newMessage, ClientController.PARTNER_MSG);
                  break;

                case Message.EncryptMsg: // receive encypted message
                  byteSessionKey =
                      clientController.searchClient(message.getSender()).getSessionKey();

                  // AES Block Cipher
                  String decryptedMessage =
                      new String(Aes.decryptCbc(message.getPayload(), byteSessionKey));

                  clientController.writeOnTextPane(
                      message.getSender() + " : " + decryptedMessage, ClientController.PARTNER_MSG);
                  break;

                case Message.EncryptFile: // receive file
                  byte[] fileBytes = message.getPayload(); // get message
                  byteSessionKey =
                      clientController
                          .searchClient(message.getSender())
                          .getSessionKey(); // get partner session key
                  byte[] decryptedFile = Aes.decryptCbc(fileBytes, byteSessionKey); // decrypt file

                  int fileNameLength =
                      Utilities.convertBytesToInt(
                          Arrays.copyOfRange(decryptedFile, 0, 4)); // get file name length
                  String fileName =
                      new String(
                          Arrays.copyOfRange(
                              decryptedFile, 4, fileNameLength + 4)); // get file name from message

                  clientController.writeOnTextPane(
                      "Receive File : " + fileName, ClientController.PARTNER_MSG);
                  FileDialog dialog = new FileDialog((Frame) null, "Choose location to save file");

                  dialog.setMode(FileDialog.SAVE);
                  dialog.setFile(fileName);
                  dialog.setVisible(true);

                  String filePatch = dialog.getDirectory();
                  Path file = Paths.get(filePatch + fileName);

                  decryptedFile =
                      Arrays.copyOfRange(
                          decryptedFile,
                          fileNameLength + 4,
                          decryptedFile.length); // remove file name from message
                  Files.write(file, decryptedFile); // write file to file system

                  break;
                default:
                  break;
              }
              break;
            default:
              break;
          }
        }

      } catch (ClassNotFoundException | IOException e) {
        //clientController.writeOnTextPane("Lost connection to server", ClientController.SYS_MSG);
        this.clientController.getSendMsg().interrupt();
        clientController.lostConnection();
      }
    }
  }
}
