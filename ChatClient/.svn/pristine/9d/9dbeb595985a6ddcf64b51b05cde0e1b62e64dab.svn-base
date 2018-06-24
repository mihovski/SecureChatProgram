package client;

import common.Utilities;

/**
 * The class is the entry point of the ChatClient program (main method).
 * @author Svetoslav Mihovski.
 * @version 2.0.
 */
public class SecureChat {

  public static final String[] PREF_SERVER_IP_ADDRESSES = {"localhost", "78.90.66.73"};
  private static final int PREF_SERVER_PORT_NUMBER = 32456;

  private static String serverIP = "";
  private static int serverPort = PREF_SERVER_PORT_NUMBER;

  public static final int RSA_KEY_LENGTH = 2048;
  public static final int SESSION_KEY_LENGTH = 128; // session key is key for AES (4 word's AES-128)

  private static ChatClientView clientView;
  private static ClientController clientController;

  /** 
   * Launch the SecureChat Application. 
   */
  public static void main(String[] args) {

    checkArguments(args); // check command line arguments

    clientView = new ChatClientView(); // view
    clientView.initializeChatView(); // start GUI
    clientView.getFrame().setTitle("Secure Chat");

    clientController = new ClientController(clientView, serverIP, serverPort); // controller
    clientController.guiEvents(); // start listen for GUI events
  }

  private static void checkArguments(String[] arguments) {
    if (arguments.length > 0) { // arguments are present
      serverIP = PREF_SERVER_IP_ADDRESSES[0]; // if case to fall
      if ((Utilities.validate(arguments[0]) || (arguments[0].equals("localhost")))) { // validate IP
        serverIP = arguments[0]; // assume that first argument is IP
      }
      if (arguments.length > 1) {
        serverPort = Integer.parseInt(arguments[1]); // assume that second argument is port
      }
    }
  }
}
