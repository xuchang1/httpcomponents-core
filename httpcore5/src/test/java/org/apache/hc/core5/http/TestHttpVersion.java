/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.hc.core5.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test cases for HTTP version class
 */
public class TestHttpVersion {

    @Test
    public void testEqualsMajorMinor() {
        Assertions.assertTrue(HttpVersion.HTTP_0_9.equals(0, 9));
        Assertions.assertTrue(HttpVersion.HTTP_1_0.equals(1, 0));
        Assertions.assertTrue(HttpVersion.HTTP_1_1.equals(1, 1));
        Assertions.assertTrue(HttpVersion.HTTP_2.equals(2, 0));
        Assertions.assertTrue(HttpVersion.HTTP_2_0.equals(2, 0));
        //
        Assertions.assertFalse(HttpVersion.HTTP_0_9.equals(2, 0));
    }

    @Test
    public void testGet() {
        Assertions.assertEquals(HttpVersion.HTTP_0_9, HttpVersion.get(0, 9));
        Assertions.assertEquals(HttpVersion.HTTP_1_0, HttpVersion.get(1, 0));
        Assertions.assertEquals(HttpVersion.HTTP_1_1, HttpVersion.get(1, 1));
        Assertions.assertEquals(HttpVersion.HTTP_2_0, HttpVersion.get(2, 0));
        Assertions.assertEquals(HttpVersion.HTTP_2, HttpVersion.get(2, 0));
        Assertions.assertNotEquals(HttpVersion.HTTP_2_0, HttpVersion.get(2, 1));
        //
        Assertions.assertSame(HttpVersion.HTTP_0_9, HttpVersion.get(0, 9));
        Assertions.assertSame(HttpVersion.HTTP_1_0, HttpVersion.get(1, 0));
        Assertions.assertSame(HttpVersion.HTTP_1_1, HttpVersion.get(1, 1));
        Assertions.assertSame(HttpVersion.HTTP_2_0, HttpVersion.get(2, 0));
        Assertions.assertSame(HttpVersion.HTTP_2, HttpVersion.get(2, 0));
        Assertions.assertNotSame(HttpVersion.HTTP_2_0, HttpVersion.get(2, 1));
    }

    @SuppressWarnings("unused")
    @Test
    public void testHttpVersionInvalidConstructorInput() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new HttpVersion(-1, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new HttpVersion(0, -1));
    }

    @Test
    public void testHttpVersionEquality() throws Exception {
        final HttpVersion ver1 = new HttpVersion(1, 1);
        final HttpVersion ver2 = new HttpVersion(1, 1);

        Assertions.assertEquals(ver1.hashCode(), ver2.hashCode());
        Assertions.assertEquals(ver1, ver1);
        Assertions.assertEquals(ver1, ver2);
        Assertions.assertEquals(ver1, ver1);
        Assertions.assertEquals(ver1, ver2);

        Assertions.assertFalse(ver1.equals(Float.valueOf(1.1f)));

        Assertions.assertEquals((new HttpVersion(0, 9)), HttpVersion.HTTP_0_9);
        Assertions.assertEquals((new HttpVersion(1, 0)), HttpVersion.HTTP_1_0);
        Assertions.assertEquals((new HttpVersion(1, 1)), HttpVersion.HTTP_1_1);
        Assertions.assertNotEquals((new HttpVersion(1, 1)), HttpVersion.HTTP_1_0);

        Assertions.assertEquals((new ProtocolVersion("HTTP", 0, 9)), HttpVersion.HTTP_0_9);
        Assertions.assertEquals((new ProtocolVersion("HTTP", 1, 0)), HttpVersion.HTTP_1_0);
        Assertions.assertEquals((new ProtocolVersion("HTTP", 1, 1)), HttpVersion.HTTP_1_1);
        Assertions.assertNotEquals((new ProtocolVersion("http", 1, 1)), HttpVersion.HTTP_1_1);

        Assertions.assertEquals(HttpVersion.HTTP_0_9, new ProtocolVersion("HTTP", 0, 9));
        Assertions.assertEquals(HttpVersion.HTTP_1_0, new ProtocolVersion("HTTP", 1, 0));
        Assertions.assertEquals(HttpVersion.HTTP_1_1, new ProtocolVersion("HTTP", 1, 1));
        Assertions.assertNotEquals(HttpVersion.HTTP_1_1, new ProtocolVersion("http", 1, 1));
    }

    @Test
    public void testHttpVersionComparison() {
        Assertions.assertTrue(HttpVersion.HTTP_0_9.lessEquals(HttpVersion.HTTP_1_1));
        Assertions.assertTrue(HttpVersion.HTTP_0_9.greaterEquals(HttpVersion.HTTP_0_9));
        Assertions.assertFalse(HttpVersion.HTTP_0_9.greaterEquals(HttpVersion.HTTP_1_0));

        Assertions.assertTrue(HttpVersion.HTTP_1_0.compareToVersion(HttpVersion.HTTP_1_1) < 0);
        Assertions.assertTrue(HttpVersion.HTTP_1_0.compareToVersion(HttpVersion.HTTP_0_9) > 0);
        Assertions.assertEquals(0, HttpVersion.HTTP_1_0.compareToVersion(HttpVersion.HTTP_1_0));
   }

    @Test
    public void testSerialization() throws Exception {
        final HttpVersion orig = HttpVersion.HTTP_1_1;
        final ByteArrayOutputStream outbuffer = new ByteArrayOutputStream();
        try (final ObjectOutputStream outStream = new ObjectOutputStream(outbuffer)) {
            outStream.writeObject(orig);
            outStream.close();
            final byte[] raw = outbuffer.toByteArray();
            final ByteArrayInputStream inBuffer = new ByteArrayInputStream(raw);
            final ObjectInputStream inStream = new ObjectInputStream(inBuffer);
            final HttpVersion clone = (HttpVersion) inStream.readObject();
            Assertions.assertEquals(orig, clone);
        }
    }

}

