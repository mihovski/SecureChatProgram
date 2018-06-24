package common;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

/**
 * The class contents common utilities for blockcipher and rsa packages.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 * 
 *     Code for the class is adopted and modified from:
 *     https://github.com/o083to/rsa/blob/master/src/main/java/algoritm/rsa/RSAUtils.java 
 *     https://github.com/o083to/rsa/blob/master/src/main/java/algoritm/utils/CipherUtils.java
 *     By o083to (Accessed in March 2018)
 */
public class Utilities {

  private static Field bigIntegerDataField;

  /**
   * Calculates the modular exponent.
   * @param arg ; message
   * @param e ; publicE
   * @param n ; modulusN
   */
  public static byte[] modPowByte(byte[] arg, BigInteger e, BigInteger n) {
    BigInteger source = new BigInteger(1, arg);
    BigInteger result = source.modPow(e, n);
    hideBigInteger(source);
    return getBytesWithoutSign(result);
  }

  /**
   * Removes sign from BigInteger and return as byte array.
   * @param input ; input.
   * @return withoutSign.
   */
  private static byte[] getBytesWithoutSign(BigInteger arg) {
    byte[] sourceArray = arg.toByteArray();
    if (sourceArray[0] != 0) {
      return sourceArray;
    } else {
      byte[] withoutSign = new byte[sourceArray.length - 1];
      System.arraycopy(sourceArray, 1, withoutSign, 0, withoutSign.length);
      return withoutSign;
    }
  }

  private static void hideBigInteger(BigInteger source) {
    try {
      if (bigIntegerDataField == null) {
        bigIntegerDataField = BigInteger.class.getDeclaredField("mag");
        bigIntegerDataField.setAccessible(true);
      }
      int[] mag = (int[]) bigIntegerDataField.get(source);
      for (int i = 0; i < mag.length; i++) {
        mag[i] = 0;
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      System.err.println("[Warning] Parts of the plaintext may remain in memory");
    }
  }

  /**
   * Converts an array of ints into an array of bites.
   * @param value ; array of ints
   * @return array of bytes.
   */
  public static byte[] convertIntToBytes(int value) {
    return ByteBuffer.allocate(4).putInt(value).array();
  }

  /**
   * Converts input byte array to int array.
   * @param bytes ; input byte array
   * @return ints ; integers
   */
  public static int convertBytesToInt(byte[] bytes) {
    return ByteBuffer.wrap(bytes).getInt();
  }

  /**
   * Generates byte array with given size of random bytes.
   * @param size ; size.
   * @return bytes.
   */
  private static byte[] generateRandomBytes(int count) {
    byte[] bytes = new byte[count];
    SecureRandom random = new SecureRandom();
    random.nextBytes(bytes);
    return bytes;
  }

  /**
   * Generates the session key with input bit length.
   * @param bitLength ; key bit length.
   * @return byte array of session key.
   */
  public static byte[] getSessionKey(int bitLength) {
    return generateRandomBytes(bitLength / 8); // convert bit's to bytes
  }

  /** Concatenate two byte arrays in one.
   * @param a ; byte array a
   * @param b ; byte array b
   * @return byte array c
   */
  public static byte[] concByteAreas(byte[] a, byte[] b) {
    byte[] c = new byte[a.length + b.length];
    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);
    return c;
  }

  /** 
   * Validate the IP address using regular expression.
   */
  public static boolean validate(final String ip) {
    String pattern =
        "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}"
            + "(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

    return ip.matches(pattern);
  }
}
