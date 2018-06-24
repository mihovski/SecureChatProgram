package test;

import static org.junit.Assert.assertArrayEquals;

import common.Utilities;
import crypto.Aes;
import crypto.PrivateKey;
import crypto.PublicKey;
import crypto.RsaKeyPair;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

/**
 * Tests used to record data on the speed and efficiency of the encrypting algorithms.
 * @author Svetoslav Mihovski.
 * @version 2.0.
 */

class PerformanceTests {

  byte[] testData = null;
  byte[] encryptedData = null;
  byte[] decryptedData = null;

  Instant start;
  Instant end;
  long between;

  @Test
  void testRsa() {

    testData = Utilities.getSessionKey(256); // 256 bit ( 32 bytes array )
    int[] rsaKey = {512, 1024, 2048, 4096};

    for (int rsaKeyLength : rsaKey) {
      start = Instant.now();
      RsaKeyPair rsaKeyPair = new RsaKeyPair(rsaKeyLength);
      end = Instant.now();
      between = Duration.between(start, end).toMillis();
      System.out.println(
          "Time for generation RSA keypair with "
              + rsaKeyLength
              + "bit key length : "
              + between
              + " Milliseconds");

      PublicKey publicKey = rsaKeyPair.getPublicKey();
      start = Instant.now();
      encryptedData = publicKey.encrypt(testData);
      end = Instant.now();
      between = Duration.between(start, end).toMillis();
      System.out.println(
          "Time for RSA encryption with "
              + rsaKeyLength
              + "bit key length : "
              + between
              + " Milliseconds");

      PrivateKey privateKey = rsaKeyPair.getPrivateKey();
      start = Instant.now();
      decryptedData = privateKey.decrypt(encryptedData);
      end = Instant.now();
      between = Duration.between(start, end).toMillis();
      System.out.println(
          "Time for RSA decryption with "
              + rsaKeyLength
              + "bit key length : "
              + between
              + " Milliseconds");

      assertArrayEquals(testData, decryptedData);
    }
  }

  @Test
  void testAes() {

    testData = Utilities.getSessionKey(4096); // get random data
    int[] aesKey = {128, 192, 256};
    byte[] testKey = null;

    for (int aesKeyLength : aesKey) {

      testKey = Utilities.getSessionKey(aesKeyLength);

      start = Instant.now();
      encryptedData = Aes.encryptCbc(testData, testKey);
      end = Instant.now();
      between = Duration.between(start, end).toMillis();
      System.out.println(
          "Time for AES encryption with "
              + aesKeyLength
              + "bit key length : "
              + between
              + " Milliseconds");

      start = Instant.now();
      decryptedData = Aes.decryptCbc(encryptedData, testKey);
      end = Instant.now();
      between = Duration.between(start, end).toMillis();
      System.out.println(
          "Time for AES decryption with "
              + aesKeyLength
              + "bit key length : "
              + between
              + " Milliseconds");

      assertArrayEquals(testData, decryptedData);
    }
  }
}
