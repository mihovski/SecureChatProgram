package crypto;

import common.Utilities;
import java.math.BigInteger;

/**
 * The class represents the 'PrivateKey' object with its components:
 * -the private exponent 'privateD'.
 * -the modulus 'modulusN'.
 * @see KeyPairGenerator Class.
 * @see Utilities Class.
 * @author Svetoslav Mihovski.
 * @version 2..0.
 */
public class PrivateKey {

  private BigInteger modulusN;
  private BigInteger privateD;

  /**
   * Initialize private key components.
   */
  public PrivateKey(BigInteger n, BigInteger d) {
    this.modulusN = n;
    this.privateD = d;
  }

  /**
   * Decrypt message from 'encryptedMessage' using the private key exponent 'd'.
   * @param cipherText ; ciphertext message.
   * @return byte array with decrypted message.
   *    
   *      Code for the method is adopted and modified from:
   *      https://github.com/o083to/rsa/blob/master/src/main/java/algoritm/rsa/PrivateKey.java
   *      By o083to (Accessed in March 2018)
   */
  public byte[] decrypt(byte[] cipherText) {
    return Utilities.modPowByte(cipherText, privateD, modulusN);
  }
  
  /**
   * Encrypt message from 'encryptedMessage' using the private key exponent 'd'.
   * @param plainText ; plaintext message.
   * @return byte array with encrypted message.
   *    
   *      Code for the method is adopted and modified from:
   *      https://github.com/o083to/rsa/blob/master/src/main/java/algoritm/rsa/PrivateKey.java
   *      By o083to (Accessed in March 2018)
   */
  public byte[] encrypt(byte[] plainText) {
    return Utilities.modPowByte(plainText, privateD, modulusN);
  }
}
