package server;

import common.Client;
import common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

/** ClientListener class listens for client messages and forwards them to ServerDispatcher.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 * @see ServerDispatcher
 * 
 *     Code for the class is adopted and modified from:
 *     http://www.nakov.com/inetjava/lectures/part-1-sockets/InetJava-1.10-Chat-client-server.html (Accessed in March 2018)
 */

public class ClientListener extends Thread {
  private ServerDispatcher serverDispatcher;
  private Client client;
  private ObjectInputStream socketReader;

  /** Constructor of ClientListener class. */
  public ClientListener(Client client, ServerDispatcher dispatcher) throws IOException {
    serverDispatcher = dispatcher;
    this.client = client;
    socketReader = client.ois;
  }

  /**
   * Until interrupted, reads messages from the client socket, forwards them to the server
   * dispatcher's queue and notifies the server dispatcher.
   */
  @Override
  public void run() {
    try {
      while (!isInterrupted()) {
        Message message = (Message) socketReader.readObject();
        serverDispatcher.addToSendMessageQueue(message);
      }

    } catch (IOException | ClassNotFoundException ioex) {
      System.out.println("Problem reading from socket (broken connection)");
    }

    // Communication is broken. Interrupt both listener and
    // sender threads
    client.clientSender.interrupt();
    serverDispatcher.deleteClient(client);
  }
}
