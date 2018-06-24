package common;

import java.io.Serializable;

/**
 * Message class define structure of message. Define constants for field of message,
 * to one payload message as one byte array.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 */
public class Message implements Serializable {

  private static final long serialVersionUID = 6061715552155830190L;

  // constants related with 'messageType'
  public static final byte SystemMessage = 0; // message is system message
  public static final byte UserMessage = 1; // message is user message

  //constants related with 'commandType' of system messages
  public static final byte InitMessage = 1; // initial message, when client login
  public static final byte ListOfClients = 2; // message with online client's
  public static final byte NewClient = 3; // notify clients for new client login
  public static final byte DscClient = 4; // notify clients for disconnected client
  public static final byte LogOut = 5; // system logout message

  public static final byte PubKeyReq = 10; // user send public key request to server
  public static final byte PubKeyRsp = 11; // user receive public key from server
  public static final byte SessionKeyReq = 12; // user send session key request to other user
  public static final byte SessionKeyRsp = 13; // user response to session key request
  public static final byte SessionKeyOK = 14; // received session key check is O'K
  public static final byte SessionKeyBad = 15; // received session key check is fail
  public static final byte SecureChatReady = 20; // successful session key exchange

  //constants related with 'commandType' of user messages
  public static final byte PlainTxtMsg = 50; // payload of message is plain text
  public static final byte EncryptMsg = 51; // payload of message is encrypted text
  public static final byte File = 52; // payload is file
  public static final byte EncryptFile = 53; // payload is encrypted file

  private byte messageType;
  private byte commandType;
  private String sender;
  private String recipient;
  private byte[] payload;

  /**
   * Create the message object.
   * @param messageType ; messageType
   * @param commandType ; commandType
   * @param sender ; sender
   * @param recipient ; recipient
   * @param payload ; payload
   */
  public Message(
      byte messageType, byte commandType, String sender, String recipient, byte[] payload) {
    this.messageType = messageType;
    this.commandType = commandType;
    this.sender = sender;
    this.recipient = recipient;
    this.payload = payload;
  }

  public int getMessageType() {
    return messageType;
  }

  public void setMessageType(byte messageType) {
    this.messageType = messageType;
  }

  public int getCommandType() {
    return commandType;
  }

  public void setCommandType(byte commandType) {
    this.commandType = commandType;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public byte[] getPayload() {
    return payload;
  }

  public void setPayload(byte[] keys) {
    this.payload = keys;
  }
}
