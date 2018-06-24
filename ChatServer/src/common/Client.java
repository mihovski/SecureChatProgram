package common;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.ClientListener;
import server.ClientSender;

/**
 * Client class contains information about a users, connected to the server.
 *
 * @author Svetoslav Mihovski.
 * @version 2.0.
 */
public class Client {
  public String clientName = "";
  public byte[] publicKey = null;
  public Socket socket = null;
  public ObjectOutputStream oos = null;
  public ObjectInputStream ois = null;
  public ClientListener clientListener = null;
  public ClientSender clientSender = null;

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }
}
