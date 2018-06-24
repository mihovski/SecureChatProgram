package crypto;

import java.math.BigInteger;
import java.util.Random;

/**
 * The class is used to generate the components of the public and the private keys.
 * @see PrivateKey Class.
 * @see PublicKey Class.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 * 
 *     Code for the class is adopted and modified from:
 *     https://github.com/o083to/rsa/blob/master/src/main/java/algoritm/rsa/RSAKeyPair.java
 *     By o083to (Accessed in March 2018)
 */
public class RsaKeyPair {

  private int modolusBitLength;
  private BigInteger primeP;
  private BigInteger primeQ;
  private BigInteger modulusN;
  private BigInteger functionFi;
  private BigInteger publicE;
  private BigInteger privateD;
  private PublicKey publicKey;
  private PrivateKey privateKey;

  /**
   * Implements the basic principles of RSA key pair generation and
   * generates the public and private keys as an object.
   * @param keyBitLength ; the biLength of the key.
   */
  public RsaKeyPair(int keyBitLength) {
    
    //Traditionally, the "length" of a RSA key is the length, in bits, of the modulus
    modolusBitLength = keyBitLength / 2;

    do {

      //The probability of a BigInteger returned by this method is composite does not exceed 2-100
      primeP = BigInteger.probablePrime(modolusBitLength, new Random());
      primeQ = BigInteger.probablePrime(modolusBitLength, new Random());
      modulusN = primeP.multiply(primeQ);

    } while (modulusN.bitLength() != keyBitLength);
    functionFi = primeP.subtract(BigInteger.ONE).multiply(primeQ.subtract(BigInteger.ONE));
    publicE = publicExponentE();
    privateD = publicE.modInverse(functionFi);
    publicKey = new PublicKey(modulusN, publicE);
    privateKey = new PrivateKey(modulusN, privateD);
  }
  
  /**
   * The method generates the public exponent implementing a random generator and
   * using a while loop until the conditions for the public exponent are met.
   * @param fileName ; any input file.
   * @return exponent.
   */
  private BigInteger publicExponentE() {

    while (true) {
      Random random = new Random();
      int length = modolusBitLength + random.nextInt(functionFi.bitLength() - modolusBitLength);
      BigInteger exponent = new BigInteger(length, new Random());

      if (exponent.compareTo(BigInteger.ONE) != 0
          && exponent.compareTo(functionFi) == -1
          && exponent.gcd(functionFi).compareTo(BigInteger.ONE) == 0) {
        return exponent;
      }
    }
  }

  public PublicKey getPublicKey() {
    return publicKey;
  }

  public PrivateKey getPrivateKey() {
    return privateKey;
  }
}
