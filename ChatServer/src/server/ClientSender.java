package server;

import common.Client;
import common.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Sends messages to the client. Messages waiting to be sent are stored in a message queue. When the
 * queue is empty, ClientSender falls in sleep until a new message is arrived in the queue. When the
 * queue is not empty, ClientSender sends the messages from the queue to the client socket.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 * @see ServerDispatcher
 * 
 *     Code for the class is adopted and modified from:
 *     http://www.nakov.com/inetjava/lectures/part-1-sockets/InetJava-1.10-Chat-client-server.html (Accessed in March 2018)
 */
public class ClientSender extends Thread {
  private ArrayList<Message> messageQueue = new ArrayList<Message>();

  private ServerDispatcher serverDispatcher;
  private Client client;
  private ObjectOutputStream out;

  /**
   * Constructor of ClientSender class.
   */
  public ClientSender(Client client, ServerDispatcher serverDispatcher) throws IOException {
    this.client = client;
    this.serverDispatcher = serverDispatcher;
    this.out = client.oos;
  }

  /**
   * Adds given message to the message queue and notifies this thread (actually
   * getNextMessageFromQueue method) that a message is arrived. sendMessage is always called by
   * other threads (ServerDispatcher).
   */
  public synchronized void sendMessage(Message message) {
    messageQueue.add(message);
    notify();
  }

  /**
   * Return and deletes the next message from the message queue. If the queue is empty, falls in
   * sleep until notified for message arrival by sendMessage method.
   */
  private synchronized Message getNextMessageFromQueue() throws InterruptedException {
    while (messageQueue.size() == 0) {
      wait();
    }
    Message message = messageQueue.get(0);
    messageQueue.remove(0);
    return message;
  }

  /**
   * Sends given message to the client's socket.
   */
  private void sendMessageToClient(Message message) {
    try {
      out.writeObject(message);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   *Until interrupted, reads messages from the message queue and sends them to the client's socket.
   */
  @Override
  public void run() {
    try {
      while (!isInterrupted()) {
        Message message = getNextMessageFromQueue();
        sendMessageToClient(message);
      }
    } catch (Exception e) {
      // Communication problem
    }

    // Communication is broken. Interrupt both listener
    // and sender threads
    client.clientListener.interrupt();
    serverDispatcher.deleteClient(client);
  }
}
