package client;

import common.Client;
import common.Message;
import common.MessageBytes;
import common.Utilities;
import crypto.Aes;
import crypto.PrivateKey;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The class is responsible for the preparation and transfer of the message objects.
 * It is started as a thread from 'ClientController.java' class
 * @author Svetoslav Mihovski.
 * @version 2.0.
 * @see ClientController.java
 * @see MessageReceiver.java
 */
public class MessageSender extends Thread {

  private ClientController clientController;

  private ObjectOutputStream oos;
  private ArrayList<Client> activeClientsList;
  private boolean clientInHandshake;
  private String myName;

  /**
   * The controller creates the message sender thread.
   * @param clientController ; clientController
   */
  public MessageSender(ClientController clientController) {
    this.clientController = clientController;
    this.oos = clientController.getOos();
    this.activeClientsList = clientController.getActiveClientsList();
    this.myName = clientController.getMyName();
    this.clientInHandshake = false;
  }

  /**
   * Thread start.
   */
  @Override
  public synchronized void run() {
    try {
      while (!isInterrupted()) {
        if (this.clientInHandshake) {
          sendSystemMessages(); // in execution of handshake process
        } else {
          wait(); // wait to be notified from MessageReceiver
        }
      }
    } catch (IOException | InterruptedException | NoSuchAlgorithmException ioe) {
      this.clientController.getReadMsg().interrupt(); // interrupt threat MessageReceiver
      clientController.lostConnection(); // reset data
    }
  }

  /**
   * Send user message when secure connection is established.
   * The message can be either text or file.
   * @param textMessage ; textMessage
   * @param partner ; partner
   * @param commandType ; commandType
   */
  public synchronized void sendUserMessage(String textMessage, String partner, byte commandType)
      throws IOException {

    byte[] plainTextMessageByte = null;
    byte[] sessionKey = null;
    byte[] messageForSending = null;

    switch (commandType) {
      case Message.PlainTxtMsg: // send plain text message
        messageForSending = textMessage.getBytes(); // convert text message to byte array
        break;

      case Message.EncryptMsg: // send encypted message
        plainTextMessageByte = textMessage.getBytes(); // convert text message to byte array
        sessionKey =
            clientController.searchClient(partner).getSessionKey(); // get session key of partner
        messageForSending =
            Aes.encryptCbc(plainTextMessageByte, sessionKey); // call AES Block Cipher in CBC mode
        break;

      case Message.EncryptFile: // send file

        // textMessage is name and full path of file
        int index = textMessage.lastIndexOf("\\"); // get beginning of file name
        String fileName = textMessage.substring(index + 1); // get fileName from full path
        byte[] fileNameBytes = fileName.getBytes(); // convert fileName to bytes
        byte[] fileNameLength =
            Utilities.convertIntToBytes(
                fileNameBytes.length); // get fileName length in bytes ( 4 bytes)
        fileNameBytes =
            Utilities.concByteAreas(fileNameLength, fileNameBytes); // add length to file name

        plainTextMessageByte =
            Files.readAllBytes(new File(textMessage).toPath()); // read file in byte array
        plainTextMessageByte =
            Utilities.concByteAreas(fileNameBytes, plainTextMessageByte); // add fileName to file

        sessionKey =
            clientController.searchClient(partner).getSessionKey(); // get session key of partner
        messageForSending =
            Aes.encryptCbc(plainTextMessageByte, sessionKey); // call AES Block Cipher in CBC mode
        textMessage = fileName;
        break;
      default:
        break;
    }

    // construct message
    Message message =
        new Message(Message.UserMessage, commandType, myName, partner, messageForSending);
    sendMessage(message); // send message
    clientController.writeOnTextPane(textMessage, ClientController.MY_MSG);
  }

  /**
   * Implements the main logic of sending system messages based on command in message.
   * The method is responsible for the communication between user/server and user/user during the,
   * session key exchange process.
   */
  private synchronized void sendSystemMessages() throws IOException, NoSuchAlgorithmException {
    // generate and send system messages according to client status
    Message message = null;
    String clientName = "";
    byte clientStatus = 0;
    byte[] challenge = null;

    synchronized (activeClientsList) {
      for (Client client : activeClientsList) {
        clientStatus = client.getStatus();
        clientName = client.getClientName();
        switch (clientStatus) {
          case Client.PubKeyReq: // request to server for public key of chosen partner
            message =
                new Message(
                    Message.SystemMessage,
                    Message.PubKeyReq,
                    myName,
                    "server",
                    clientName.getBytes());
            sendMessage(message);
            client.setStatus(Client.Wait); // wait
            this.clientInHandshake = true;
            break;

          case Client.PubKeyRsv: // received public key

            // --- begin session key exchange with partner ---
            challenge = makeChallenge(client, false); // challenge from Node A to Node B
            // send message to partner with candidate session key and challenge
            message =
                new Message(
                    Message.SystemMessage, Message.SessionKeyReq, myName, clientName, challenge);
            sendMessage(message);
            client.setStatus(Client.Wait); // wait
            this.clientInHandshake = true;
            break;

          case Client.SessionKeyReq: // session key request received
            if (getChallenge(client, false)) { // node B get challenge of node A
              System.out.println("O'K1");
              // --- Node B send response (session key candidate B and challenge ) to Node A ---
              challenge = makeChallenge(client, true); // challenge from Node B to Node A

              // make session key request response
              message =
                  new Message(
                      Message.SystemMessage, Message.SessionKeyRsp, myName, clientName, challenge);
              sendMessage(message);
              client.setStatus(Client.Wait); // wait
              this.clientInHandshake = true;

            } else {
              // need to restart communication
              System.out.println("Not O'K1");
              client.setStatus(Client.SessionKeyBad);
              this.clientInHandshake = true;
            }
            break;

          case Client.SessionKeyReqRsp: // response of session key request
            if (getChallenge(client, true)) { // node A get challenge of node B
              System.out.println("O'K2");
              // last challenge to Node B
              byte[] finalCandidateSessionKey =
                  xor_func(client.getSessionKey(), client.getCandidateSessionKey());
              byte[] encrPartnerCandidateSeseionKey =
                  Aes.encryptCbc(
                      client.getSessionKey(),
                      finalCandidateSessionKey); // call AES Block Cipher in CBC mode
              message =
                  new Message(
                      Message.SystemMessage,
                      Message.SecureChatReady,
                      myName,
                      clientName,
                      encrPartnerCandidateSeseionKey);
              sendMessage(message);

              clientController.getPartner().setText(clientName); // set partner name in GUI
              clientController.setPartnerInChat(client);

              client.setSessionKey(finalCandidateSessionKey); // set final session key
              client.setStatus(Client.Wait); // wait
              this.clientInHandshake = true;

            } else {
              System.out.println("Not O'K2");
              client.setStatus(Client.SessionKeyBad);
              this.clientInHandshake = true;
            }
            break;

          case Client.SessionKeyOKConf: // Start secure chat with client - end of requested
            byte[] encrCandidateSeseionKey = client.getMessage(); // get last challenge
            byte[] finalCandidateSessionKey =
                xor_func(client.getSessionKey(), client.getCandidateSessionKey());
            byte[] decryptedCandidateSessionKey =
                Aes.decryptCbc(encrCandidateSeseionKey, finalCandidateSessionKey);
            if (Arrays.equals(decryptedCandidateSessionKey, client.getCandidateSessionKey())) {
              System.out.println("O'K3");
              client.setSessionKey(finalCandidateSessionKey); // set final session key
              message =
                  new Message(
                      Message.SystemMessage, Message.SessionKeyOK, myName, clientName, null);
              sendMessage(message);
              client.setStatus(Client.SessionKeyOK);
              this.clientInHandshake = true;

            } else {
              System.out.println("Not O'K3");
              client.setStatus(Client.SessionKeyBad);
              this.clientInHandshake = true;
            }
            break;

          case Client.SessionKeyOK: // session key is O'K - end of handshake
            client.setStatus(Client.ReadyForSecureChat);
            if ((clientController.getPartner().getText()).equals("")) {
              clientController.getPartner().setText(clientName); // set partner name in GUI
              clientController.setPartnerInChat(client);
            }
            clientController.writeOnTextPane(
                "Secure chat with : " + clientName + " established", ClientController.SYS_MSG);
            this.clientInHandshake = false;
            break;

          case Client.SessionKeyBad: // session key exchange is BAD
            message =
                new Message(Message.SystemMessage, Message.SessionKeyBad, myName, clientName, null);
            sendMessage(message);
            cleanUpClient(client);
            break;

          case Client.SessionKeyBadRsv: // received "session key BAD" message
            cleanUpClient(client);
            break;
          default:
            break;
        }
      }
    }
  }
  
  /**
   * Creates the challenge from user to another user.
   * The method represents the part of the authentication when session key is exchanged
   * @param client ; client
   * @param encrPcsk ; encrPcsk
   */
  private synchronized byte[] makeChallenge(Client client, boolean encrPcsk)
      throws NoSuchAlgorithmException {
    PrivateKey privateKey = clientController.getRsaKeyPair().getPrivateKey();
    synchronized (activeClientsList) {
      // Step 1. Node A generate candidate session key
      byte[] candidateSessionKey =
          Utilities.getSessionKey(SecureChat.SESSION_KEY_LENGTH); // 128 bit key -> 16 bytes
      client.setCandidateSessionKey(
          candidateSessionKey); // set candidate session key for chosen partner

      //Step 2 - Node A(B)
      // 2.1 Node A(B) encrypt generated session key with public key of Node B(A)
      byte[] encryptedSessionKey = client.getPublicKey().encrypt(candidateSessionKey);

      // 2.2 Node A(B) calculate challenge to Node B(A) : {{H(SKa)}K-a}Kb
      // 2.2.1 calculate hash of session key using SHA-256
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(candidateSessionKey);
      byte[] sessionKeyHash = digest.digest();

      // 2.2.2 encrypt session key hash with private key of Node A(B)
      // 2.2.3 encrypt H(SK)K-a with public key of Node B(A)
      byte[] encrSessionKeyHash = client.getPublicKey().encrypt(privateKey.encrypt(sessionKeyHash));

      // 2.3 Node A sends to Node B following data : {{H(SKa)}Ka-1}Kb, {SKa}Kb
      MessageBytes consrtuctMessage = new MessageBytes(null);
      consrtuctMessage.addMessage(encryptedSessionKey);
      consrtuctMessage.addMessage(encrSessionKeyHash);
      // 2.2.4 encrypt partnes's session key candidate with final session key candidate (Ka XOR Kb)
      if (encrPcsk) {
        byte[] finalCandidateSessionKey =
            xor_func(client.getSessionKey(), client.getCandidateSessionKey());
        byte[] encrPartnerCandidateSeseionKey =
            Aes.encryptCbc(
                client.getSessionKey(),
                finalCandidateSessionKey); // call AES Block Cipher in CBC mode
        consrtuctMessage.addMessage(encrPartnerCandidateSeseionKey);
      }

      return consrtuctMessage.getMessageBytes();
    }
  }
  
  /**
   * Solves the challenge from user.
   * The method represents the part of the authentication when session key is exchanged
   * @param client ; client
   * @param encrPcsk ; encrPcsk
   */
  private synchronized boolean getChallenge(Client client, boolean encrPcsk)
      throws NoSuchAlgorithmException {
    PrivateKey privateKey = clientController.getRsaKeyPair().getPrivateKey();
    synchronized (activeClientsList) {
      //Step 3 - Node B
      byte[] challenge =
          client.getSessionKey(); // get encrypted candidate session key and challenge

      MessageBytes consrtuctMessage = new MessageBytes(challenge);
      byte[] encryptedSessionKey =
          consrtuctMessage.extractMessage(1); // get encrypted candidate session key
      byte[] encrSessionKeyHash = consrtuctMessage.extractMessage(2); // get challenge

      byte[] sessionKey =
          privateKey.decrypt(
              encryptedSessionKey); // decrypt received session key with my private key
      client.setSessionKey(sessionKey); // store decrypted candidate SessionKey

      // Get challenge
      byte[] sessionKeyHash = client.getPublicKey().decrypt(privateKey.decrypt(encrSessionKeyHash));

      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(sessionKey); // calculate hash of session key
      byte[] sessionKeyHashCalc = digest.digest();

      boolean challengePass = false;
      if (Arrays.equals(sessionKeyHash, sessionKeyHashCalc)) {
        challengePass = true; // challenge passed
      }

      boolean challengePcksPass = false;
      if (encrPcsk) {
        byte[] encrPartnerCandidateSeseionKey = consrtuctMessage.extractMessage(3); //
        byte[] finalCandidateSessionKey =
            xor_func(client.getSessionKey(), client.getCandidateSessionKey());
        byte[] decryptedCandidateSessionKey =
            Aes.decryptCbc(encrPartnerCandidateSeseionKey, finalCandidateSessionKey);
        if (Arrays.equals(decryptedCandidateSessionKey, client.getCandidateSessionKey())) {
          challengePcksPass = true;
        }
      }

      if (encrPcsk) {
        if (challengePcksPass && challengePass) {
          return true;
        } else {
          return false;
        }
      } else {
        if (challengePass) {
          return true;
        } else {
          return false;
        }
      }
    }
  }

  private byte[] xor_func(byte[] a, byte[] b) {
    byte[] out = new byte[a.length];
    for (int i = 0; i < a.length; i++) {
      out[i] = (byte) (a[i] ^ b[i]);
    }
    return out;
  }

  /**
   * Send message over object output stream.
   * @param message ; message
   */
  public void sendMessage(Message message) throws IOException {
    oos.writeObject(message);
    oos.flush();
  }

  /**
   * Clean client if session key exchange has failed.
   * @param client ; client
   */
  public synchronized void cleanUpClient(Client client) {
    client.setCandidateSessionKey(null); // clear client
    client.setSessionKey(null);
    client.setPublicKey(null);
    client.setInChat(false);
    client.setStatus(Client.Wait);
    this.clientInHandshake = false;
    clientController.writeOnTextPane(
        "Session key exchange with : " + client.getClientName() + " failed",
        ClientController.SYS_MSG);
  }

  public synchronized boolean isClientInHandshake() {
    return clientInHandshake;
  }

  /**
   * Set if the client is in handshake.
   * @param clientInHandshake ; clientInHandshake
   */
  public synchronized void setClientInHandshake(boolean clientInHandshake) {
    if (clientInHandshake) {
      notify();
    }
    this.clientInHandshake = clientInHandshake;
  }
}
