package test;

import client.ChatClientView;
import client.ClientController;

import org.junit.jupiter.api.Test;

/**
 * Tests used to test the whole functionality of the program.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 */

class TestChat {

  TestChatClient clientA;
  TestChatClient clientB;

  TestChat() throws InterruptedException {

    this.clientA = new TestChatClient();
    this.clientB = new TestChatClient();

    Thread clientA;
    clientA = new Thread(this.clientA);
    clientA.start();

    Thread.sleep(1000);

    Thread clientB;
    clientB = new Thread(this.clientB);
    clientB.start();
  }

  class TestChatClient extends Thread {
    private static final String SERVER_IP_ADDRESS = "localhost";
    private static final int SERVER_PORT_NUMBER = 32456;

    public static final int RSA_KEY_LENGTH = 512;
    public static final int SESSION_KEY_LENGTH =  128;

    private ChatClientView clientView;
    private ClientController clientController;

    @Override
    public void run() {
      clientView = new ChatClientView(); // view
      clientView.initializeChatView(); // start GUI
      clientView.getFrame().setTitle("Secure Chat");

      clientController =
          new ClientController(clientView, SERVER_IP_ADDRESS, SERVER_PORT_NUMBER); // controller
      clientController.guiEvents(); // start listen for GUI events
    }
  }

  @Test
  void test1() throws InterruptedException {

    clientA.clientView.getUserName().setText("Alice");
    clientA.clientView.getBtnConnect().doClick();
    Thread.sleep(1000);
    clientB.clientView.getUserName().setText("Bob");
    clientB.clientView.getBtnConnect().doClick();
    Thread.sleep(2000);
    clientA.clientView.getActiveUsersList().setValueIsAdjusting(true);
    clientA.clientView.getActiveUsersList().setSelectedIndex(0);
    Thread.sleep(2000);
    clientA.clientView.getMessageBox().setText("Hello Bob");
    clientA.clientView.getSendMessage().doClick();
    Thread.sleep(1000);
    clientB.clientView.getMessageBox().setText("Hello Alice");
    clientB.clientView.getSendMessage().doClick();
    Thread.sleep(1000);
    clientA.clientView.getActiveUsersList().setValueIsAdjusting(false);
    clientA.clientView.getBtnConnect().doClick();
    Thread.sleep(5000);
    clientB.clientView.getBtnConnect().doClick();
    Thread.sleep(5000);
    
  }
}
