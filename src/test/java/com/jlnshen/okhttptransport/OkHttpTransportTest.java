/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jlnshen.okhttptransport;

import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.testing.http.javanet.MockHttpURLConnection;
import com.google.api.client.util.ByteArrayStreamingContent;
import com.google.api.client.util.StringUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.net.URL;

/**
 * Unit test for simple App.
 */
public class OkHttpTransportTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public OkHttpTransportTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(OkHttpTransportTest.class);
    }

    private static final String[] METHODS =
            {"GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE"};

    public void testExecute_mock() throws Exception {
        for (String method : METHODS) {
            boolean isPutOrPost = method.equals("PUT") || method.equals("POST");
            MockHttpURLConnection connection = new MockHttpURLConnection(new URL(HttpTesting.SIMPLE_URL));
            connection.setRequestMethod(method);
            OkHttpRequest request = new OkHttpRequest(connection);
            setContent(request, null, "");
            request.execute();
            assertEquals(isPutOrPost, connection.doOutputCalled());
            setContent(request, null, " ");
            if (isPutOrPost) {
                request.execute();
            } else {
                try {
                    request.execute();
                    fail("expected " + IllegalArgumentException.class);
                } catch (IllegalArgumentException e) {
                    // expected
                }
            }
            assertEquals(isPutOrPost, connection.doOutputCalled());
        }
    }

    private void setContent(OkHttpRequest request, String type, String value) throws Exception {
        byte[] bytes = StringUtils.getBytesUtf8(value);
        request.setStreamingContent(new ByteArrayStreamingContent(bytes));
        request.setContentType(type);
        request.setContentLength(bytes.length);
    }
}
