package common;

import crypto.PublicKey;

/**
 * The class defines the structure of a client object.
 * This object contains data of user.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 */
public class Client {

  // these constants define client status
  public static final byte Wait = 0;
  public static final byte PubKeyReq = 1;
  public static final byte PubKeyRsv = 2;
  public static final byte SessionKeyReq = 3;
  public static final byte SessionKeyRsv = 4;
  public static final byte SessionKeyReqRsp = 5;
  public static final byte SessionKeyOK = 10;
  public static final byte SessionKeyOKConf = 11;
  public static final byte SessionKeyBad = 12;
  public static final byte SessionKeyBadRsv = 13;
  public static final byte ReadyForSecureChat = 100;

  private String clientName;
  private PublicKey publicKey;
  private byte[] message;
  private byte[] candidateSessionKey;
  private byte[] sessionKey;
  private byte status;
  private boolean inChat;

  /**
   * Creates the client object.
   * @param clientName ;clientName
   * @param status ; status
   * @param inChat ; inChat
   */
  public Client(String clientName, byte status, boolean inChat) {
    this.clientName = clientName;
    this.publicKey = null;
    this.message = null;
    this.candidateSessionKey = null;
    this.sessionKey = null;
    this.status = status;
    this.inChat = inChat;
  }

  public synchronized String getClientName() {
    return clientName;
  }

  public synchronized void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public synchronized PublicKey getPublicKey() {
    return publicKey;
  }

  public synchronized void setPublicKey(PublicKey publicKey) {
    this.publicKey = publicKey;
  }

  public synchronized byte[] getMessage() {
    return message;
  }

  public synchronized void setMessage(byte[] message) {
    this.message = message;
  }

  public synchronized byte[] getCandidateSessionKey() {
    return candidateSessionKey;
  }

  /**
   * Sets the candidate session key by clone method.
   * @param candidateSessionKey ; candidateSessionKey
   */
  public synchronized void setCandidateSessionKey(byte[] candidateSessionKey) {
    if (candidateSessionKey == null) {
      this.candidateSessionKey = null;
    } else {
      this.candidateSessionKey = candidateSessionKey.clone();
    }
  }

  public synchronized byte[] getSessionKey() {
    return sessionKey;
  }

  /**
   * Sets the session key by clone method.
   * @param sessionKey ; sessionKey
   */
  public synchronized void setSessionKey(byte[] sessionKey) {
    if (sessionKey == null) {
      this.sessionKey = null;
    } else {
      this.sessionKey = sessionKey.clone();
    }
  }

  public synchronized byte getStatus() {
    return status;
  }

  public synchronized void setStatus(byte status) {
    this.status = status;
  }

  public synchronized boolean isInChat() {
    return inChat;
  }

  public synchronized void setInChat(boolean inChat) {
    this.inChat = inChat;
  }
}
