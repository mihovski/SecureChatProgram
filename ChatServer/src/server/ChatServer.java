package server;

import common.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The class is the entry point of the ChatServer program (main method).
 * It creates and initializes the GUI interface for the server.
 * Creates a server socket and listens for connections from users.
 * Starts 'ServerDispatcher' as thread.
 * When user is connected for each user it starts threads 
 * for sending and listening for messages.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 * @see ServerDispatcher
 */

public class ChatServer {
  private static final int PREF_SERVER_PORT_NUMBER = 32456;

  private static int serverPort = PREF_SERVER_PORT_NUMBER;
  private static ServerSocket serverSocket;
  private static ServerDispatcher serverDispatcher;
  private static ChatServerView serverView; // view

  /**
   * This class is entry point of application 'ChatServer'.
   */
  public static void main(String[] args) {

    checkArguments(args); // check command line arguments

    // Start GUI
    serverView = new ChatServerView(); // view
    serverView.initializeChatView(); // start GUI
    serverView.getFrame().setTitle("Chat Server");

    // Start listening on the server socket
    try {
      serverSocket = new ServerSocket(serverPort);
      serverView.writeMessageOnTextPane("ChatServer started on " + "port " + serverPort);
    } catch (IOException ioe) {
      serverView.writeMessageOnTextPane("Can not start listening on " + "port " + serverPort);
      System.exit(-1);
    }

    // Start the ServerDispatcher thread
    serverDispatcher = new ServerDispatcher(serverView);
    serverDispatcher.start();
    serverView.writeMessageOnTextPane("Server Message Dispatcher started.");

    // Infinitely accept and handle client connections
    serverView.writeMessageOnTextPane("Start listening for clients.");
    handleClientConnections();
  }

  private static synchronized void handleClientConnections() {
    while (true) {
      try {
        Socket socket = serverSocket.accept();
        // New user invite connection
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        Client client = new Client();
        client.socket = socket;
        client.oos = oos;
        client.ois = ois;

        ClientListener clientListener = new ClientListener(client, serverDispatcher);
        client.clientListener = clientListener;
        clientListener.start();

        ClientSender clientSender = new ClientSender(client, serverDispatcher);
        client.clientSender = clientSender;
        clientSender.start();

        //Add new client to list of active clients
        serverDispatcher.addClient(client);

        // wait new user to be accepted from dispatcher
        // Need exit waiting by timeout !!!
        while (!(serverDispatcher.getValidUser())) {}
        ;
        serverView.writeMessageOnTextPane("New user connected");

      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  private static void checkArguments(String[] arguments) {
    if (arguments.length > 0) { // arguments are present
      if (arguments.length == 1) {
        serverPort = Integer.parseInt(arguments[0]); // assume that first argument is port
        if ((serverPort < 0) || (serverPort > 65535)) {
          serverPort = PREF_SERVER_PORT_NUMBER; // if case to fall
        }
      }
    }
  }
}
