package server;

import common.Client;
import common.Message;

import java.util.ArrayList;

/**
 * ServerDispatcher class is purposed to listen for messages received from the clients and to
 * dispatch them to all the clients connected to the chat server.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 * @see ServerDispatcher
 * 
 *     Code for the class is adopted and modified from:
 *     http://www.nakov.com/inetjava/lectures/part-1-sockets/InetJava-1.10-Chat-client-server.html (Accessed in March 2018)
 */

public class ServerDispatcher extends Thread {

  private ArrayList<Message> messagesForSend = new ArrayList<Message>();
  private ArrayList<Client> activeClientsList = new ArrayList<Client>();
  private ChatServerView serverView;
  private boolean validUser;

  public ServerDispatcher(ChatServerView serverView) {
    this.serverView = serverView;
    this.validUser = false;
  }

  /**
   * Adds given client to the server's client list.
   * */
  public synchronized void addClient(Client client) {
    activeClientsList.add(client);
  }

  /**
   * Deletes given client from the server's client list if the client is in the list.
   */
  public synchronized void deleteClient(Client client) {
    int clientIndex = activeClientsList.indexOf(client);
    if (clientIndex != -1) {
      String dscClient = client.clientName;
      serverView.writeMessageOnTextPane("Disconnected client : " + dscClient);
      // Notify connected users for disconnected client
      Message message =
          new Message(
              Message.SystemMessage, Message.DscClient, dscClient, "", dscClient.getBytes());
      addToSendMessageQueue(message);

      activeClientsList.remove(clientIndex); // remove from list
    }
  }

  /**
   * Adds given message to the dispatcher's message queue and notifies this thread to wake up the
   * message queue reader (getNextMessageFromQueue method). dispatchMessage method is called by
   * other threads (ClientListener) when a message is arrived.
   */
  public synchronized void addToSendMessageQueue(Message message) {

    if ((message.getRecipient().equals("server"))) {
      handlingSystemMessages(message);
    } else {
      messagesForSend.add(message);
      notify();
    }
  }

  /**
   * Return and deletes the next message from the message queue. If there is no messages in the
   * queue, falls in sleep until notified by dispatchMessage method.
   */
  private synchronized Message getMessageForSend() throws InterruptedException {

    while (messagesForSend.size() == 0) {
      wait();
    }
    Message message = messagesForSend.get(0);
    messagesForSend.remove(0);
    return message;
  }

  private synchronized void sendMessageToClient(Message message) {

    String sender = message.getSender();
    String recipient = message.getRecipient();
    if ((recipient.equals(""))) { // if recipient is missing ("") -> send to all
      for (Client client : activeClientsList) {
        if (!(client.clientName.equals(sender))) {
          client.clientSender.sendMessage(message); // call send method from client thread
        }
      }
    } else {
      for (Client client : activeClientsList) {
        if (client.clientName.equals(recipient)) {
          client.clientSender.sendMessage(message); // call send method from client thread
        }
      }
    }
  }

  /**
   * Generate list of connected users.
   */
  public String listOfAllClients(String newClient) {
    String connectedUsers = "";

    for (Client client : activeClientsList) {
      if (!client.clientName.equals(newClient)) {
        if (connectedUsers.equals("")) {
          connectedUsers = client.clientName;
        } else {
          connectedUsers = connectedUsers + ";" + client.clientName;
        }
      }
    }
    return connectedUsers;
  }

  /** 
   * Notify for valid connected user.   
   */
  public synchronized boolean getValidUser() {
    if (this.validUser) {
      this.validUser = false;
      return true;
    } else {
      return false;
    }
  }

  private void handlingSystemMessages(Message message) {

    String clientName = message.getSender();
    switch (message.getMessageType()) {
      case Message.SystemMessage: // receive system message
        switch (message.getCommandType()) {
          case Message.InitMessage: // initial message from client
            activeClientsList.get(activeClientsList.size() - 1).clientName = message.getSender();
            activeClientsList.get(activeClientsList.size() - 1).publicKey =
                message.getPayload(); // store public key
            this.validUser = true;
            serverView.writeMessageOnTextPane("New user login : " + clientName);

            //Server send list of connected clients to new client
            String clientList = listOfAllClients(clientName);
            message =
                new Message(
                    Message.SystemMessage,
                    Message.ListOfClients,
                    "server",
                    clientName,
                    clientList.getBytes());
            addToSendMessageQueue(message);

            //Server send new client to other active clients
            message =
                new Message(
                    Message.SystemMessage,
                    Message.NewClient,
                    clientName,
                    "",
                    clientName.getBytes());
            addToSendMessageQueue(message);

            break;

          case Message.LogOut: // client logout
            deleteClient(searchClient(clientName));
            System.out.println("Disconnect client :" + clientName);
            break;

          case Message.PubKeyReq: // server receive PublicKey request
            String publicKeyOf = new String(message.getPayload()); // request for public key OF
            byte[] pubKey = searchClient(publicKeyOf).publicKey; // get public key
            message =
                new Message(
                    Message.SystemMessage, Message.PubKeyRsp, publicKeyOf, clientName, pubKey);
            addToSendMessageQueue(message);
            break;

          default:
            break;
        }
        break;

      default:
        break;
    }
  }

  /** 
   * Search and return client by name in client's list. 
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
   * Infinitely reads messages from the queue and dispatches them to all clients connected to the
   * server.
   */
  @Override
  public synchronized void run() {
    Message message;
    try {
      while (true) {
        message = getMessageForSend();
        sendMessageToClient(message);
      }
    } catch (InterruptedException ie) {
      // Thread interrupted. Stop its execution
    }
  }
}
