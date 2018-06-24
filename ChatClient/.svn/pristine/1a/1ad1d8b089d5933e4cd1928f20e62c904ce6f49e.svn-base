package common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The class is used to convert message object payload from many messages
 * to one payload message as one byte array.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 */
public class MessageBytes {

  byte[] message = null;

  /**
   * Creates new message payload.
   * @param messageBytes ; messageBytes
   */
  public MessageBytes(byte[] messageBytes) {
    if (messageBytes == null) {
      this.message = new byte[] {(byte) 0x00};
    } else {
      this.message = messageBytes;
    }
  }

  /**
   * Add message to message payload.
   * @param newMessage ; newMessage
   */
  public void addMessage(byte[] newMessage) {

    // first byte contain number of messages
    byte[] newMessageLen = Utilities.convertIntToBytes(newMessage.length);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      outputStream.write(this.message);
      outputStream.write(newMessageLen);
      outputStream.write(newMessage);
      outputStream.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    this.message = outputStream.toByteArray();
    this.message[0] = (byte) (this.message[0] + 1);
  }

  /**
   * Extracts the desired message from message payload.
   * @param messageNumber ; messageNumber
   * @return messageOut
   *  
   */
  public byte[] extractMessage(int messageNumber) {

    int numberOfMessages = this.message[0];
    int posOfMessage = 1;
    int nexMessage = 1;
    int lenOfMessage = 0;
    byte[] messageOut = null;

    if (messageNumber <= numberOfMessages) {
      // find position of message
      for (int i = 1; i <= messageNumber; i++) {
        posOfMessage = nexMessage;
        lenOfMessage =
            ByteBuffer.wrap(Arrays.copyOfRange(this.message, posOfMessage, posOfMessage + 4))
                .getInt();
        nexMessage = posOfMessage + lenOfMessage + 4;
      }
    }

    messageOut =
        Arrays.copyOfRange(
            this.message, posOfMessage + 4, posOfMessage + lenOfMessage + 4); // get message
    return messageOut;
  }

  public byte[] getMessageBytes() {
    return this.message;
  }
}
