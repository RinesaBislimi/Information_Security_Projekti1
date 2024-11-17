package frodokem;

import org.bouncycastle.crypto.digests.SHAKEDigest;
import java.security.SecureRandom;

public class CSPRNG {
    private static final SecureRandom secureRandom;

    static {
        // Convert the integer seed to a byte array
        byte[] seedBytes = intToBytes(Constants.SEED);

        // Create a SHAKE-256 instance
        SHAKEDigest shake256 = new SHAKEDigest(256);
        shake256.update(seedBytes, 0, seedBytes.length);

        // Generate random seed material
        byte[] randomSeed = new byte[32]; // 256-bit seed
        shake256.doFinal(randomSeed, 0, randomSeed.length);

        // Use the random seed to initialize SecureRandom
        secureRandom = new SecureRandom(randomSeed); // Deterministic RNG
    }

    public static SecureRandom getInstance() {
        return secureRandom;
    }

    // Helper method to convert integer to byte array
    private static byte[] intToBytes(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }
}