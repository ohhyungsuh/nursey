package com.example.be.global.util;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

public class CustomUUIDGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static UUID generate() {
        long timestamp = Instant.now().toEpochMilli();
        int randomBits = random.nextInt();

        long mostSigBits = (timestamp << 32) | (randomBits & 0xFFFFFFFFL);
        long leastSigBits = random.nextLong();

        return new UUID(mostSigBits, leastSigBits);
    }

    public static byte[] toBytes(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    public static UUID fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long mostSigBits = buffer.getLong();
        long leastSigBits = buffer.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }
}
