/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the "Elastic License
 * 2.0", the "GNU Affero General Public License v3.0 only", and the "Server Side
 * Public License v 1"; you may not use this file except in compliance with, at
 * your election, the "Elastic License 2.0", the "GNU Affero General Public
 * License v3.0 only", or the "Server Side Public License, v 1".
 */

package org.elasticsearch.common;

import org.elasticsearch.common.settings.SecureString;

import java.util.OptionalInt;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Utility class for generating various types of UUIDs.
 */
public class UUIDs {
    private static final AtomicInteger sequenceNumber = new AtomicInteger(SecureRandomHolder.INSTANCE.nextInt());
    public static final Supplier<Long> DEFAULT_TIMESTAMP_SUPPLIER = System::currentTimeMillis;
    public static final Supplier<Integer> DEFAULT_SEQUENCE_ID_SUPPLIER = sequenceNumber::incrementAndGet;
    public static final Supplier<byte[]> DEFAULT_MAC_ADDRESS_SUPPLIER = MacAddressProvider::getSecureMungedAddress;
    private static final RandomBasedUUIDGenerator RANDOM_UUID_GENERATOR = new RandomBasedUUIDGenerator();
    private static final TimeBasedKOrderedUUIDGenerator TIME_BASED_K_ORDERED_GENERATOR = new TimeBasedKOrderedUUIDGenerator(
        DEFAULT_TIMESTAMP_SUPPLIER,
        DEFAULT_SEQUENCE_ID_SUPPLIER,
        DEFAULT_MAC_ADDRESS_SUPPLIER
    );

    private static final TimeBasedUUIDGenerator TIME_UUID_GENERATOR = new TimeBasedUUIDGenerator(
        DEFAULT_TIMESTAMP_SUPPLIER,
        DEFAULT_SEQUENCE_ID_SUPPLIER,
        DEFAULT_MAC_ADDRESS_SUPPLIER
    );

    /**
     * The length of a UUID string generated by {@link #base64UUID}.
     */
    // A 15-byte time-based UUID is base64-encoded as 5 3-byte chunks (each becoming 4 chars after encoding).
    public static final int TIME_BASED_UUID_STRING_LENGTH = 20;

    /**
     * Generates a time-based UUID (similar to Flake IDs), which is preferred when generating an ID to be indexed into a Lucene index as
     * primary key. The id is opaque and the implementation is free to change at any time!
     * The resulting string has length {@link #TIME_BASED_UUID_STRING_LENGTH}.
     */
    public static String base64UUID() {
        return TIME_UUID_GENERATOR.getBase64UUID();
    }

    public static String base64TimeBasedKOrderedUUIDWithHash(OptionalInt hash) {
        return TIME_BASED_K_ORDERED_GENERATOR.getBase64UUID(hash);
    }

    /**
     * The length of a UUID string generated by {@link #randomBase64UUID} and {@link #randomBase64UUIDSecureString}.
     */
    // A 16-byte v4 UUID is base64-encoded as 5 3-byte chunks (each becoming 4 chars after encoding) plus another byte (becomes 2 chars).
    public static final int RANDOM_BASED_UUID_STRING_LENGTH = 22;

    /**
     * Returns a Base64 encoded string representing a <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC4122 version 4 UUID</a>, using the
     * provided {@code Random} instance.
     * The resulting string has length {@link #RANDOM_BASED_UUID_STRING_LENGTH}.
     */
    public static String randomBase64UUID(Random random) {
        return RandomBasedUUIDGenerator.getBase64UUID(random);
    }

    /**
     * Returns a Base64 encoded string representing a <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC4122 version 4 UUID</a>, using a
     * private {@code SecureRandom} instance.
     * The resulting string has length {@link #RANDOM_BASED_UUID_STRING_LENGTH}.
     */
    public static String randomBase64UUID() {
        return RANDOM_UUID_GENERATOR.getBase64UUID();
    }

    /**
     * Returns a Base64 encoded {@link SecureString} representing a <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC4122 version 4
     * UUID</a>, using a private {@code SecureRandom} instance.
     * The resulting string has length {@link #RANDOM_BASED_UUID_STRING_LENGTH}.
     */
    public static SecureString randomBase64UUIDSecureString() {
        return RandomBasedUUIDGenerator.getBase64UUIDSecureString();
    }
}
