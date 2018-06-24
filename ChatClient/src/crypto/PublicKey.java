package crypto;

import common.Utilities;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The class represents the 'PublicKey' object with its components:
 * -the public exponent 'publicE'.
 * -the modulus 'modulusN'.
 * @see KeyPairGenerator Class.
 * @see Utilities Class.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 */
public class PublicKey {

  private BigInteger modulusN;
  private BigInteger publicE;

  /**
   * Initialize public key components.
   */
  public PublicKey(BigInteger n, BigInteger e) {
    this.modulusN = n;
    this.publicE = e;
  }

  public BigInteger getN() {
    return modulusN;
  }

  public BigInteger getE() {
    return publicE;
  }

  /**
   * Encrypt input plaintext message in byte array format.
   * @param plainText ; plainText message.
   * @return encryptedMessage.
   * 
   *      Code for the method is adopted and modified from:
   *      https://github.com/o083to/rsa/blob/master/src/main/java/algoritm/rsa/PublicKey.java
   *      By o083to (Accessed in March 2018)s
   */
  public byte[] encrypt(byte[] plainText) {
    return Utilities.modPowByte(plainText, publicE, modulusN);
  }

  /**
   * Decrypt input ciphertext message in byte array format.
   * @param cipherText ; ciphertext message.
   * @return plainText.
   * 
   *      Code for the method is adopted and modified from:
   *      https://github.com/o083to/rsa/blob/master/src/main/java/algoritm/rsa/PublicKey.java
   *      By o083to (Accessed in March 2018)
   */
  public byte[] decrypt(byte[] cipherText) {
    return Utilities.modPowByte(cipherText, publicE, modulusN);
  }

  /**
   * Convert components of public key to one byte array.
   * @return publicKeyBytes
   */
  public byte[] convertToBytes() {

    byte[] publicN = getN().toByteArray();
    byte[] publicE = getE().toByteArray();
    byte[] lenOfN = Utilities.convertIntToBytes(publicN.length);
    byte[] lenOfE = Utilities.convertIntToBytes(publicE.length);
    byte[] publicKeyBytes = null;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try {
      outputStream.write(lenOfN);
      outputStream.write(publicN);
      outputStream.write(lenOfE);
      outputStream.write(publicE);
      publicKeyBytes = outputStream.toByteArray();
      outputStream.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    return publicKeyBytes;
  }

  /**
   * Generates PublicKey object from byte array.
   * @param publicKeyBytes ; publicKeyBytes
   * @return PublicKey (as object)
   */
  public static PublicKey convertFromBytes(byte[] publicKeyBytes) {

    int lenOfN = ByteBuffer.wrap(Arrays.copyOfRange(publicKeyBytes, 0, 7)).getInt();
    int lenOfE =
        ByteBuffer.wrap(Arrays.copyOfRange(publicKeyBytes, lenOfN + 4, lenOfN + 8)).getInt();
    BigInteger n = new BigInteger(Arrays.copyOfRange(publicKeyBytes, 4, lenOfN + 4));
    BigInteger e =
        new BigInteger(Arrays.copyOfRange(publicKeyBytes, lenOfN + 8, lenOfN + 8 + lenOfE));

    return new PublicKey(n, e);
  }
}
