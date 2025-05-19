import org.junit.jupiter.api.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class AiAssistantTest {
    private static volatile Supplier<HttpURLConnection> connectionSupplier;

    // Install a URLStreamHandlerFactory once
    static {
        URL.setURLStreamHandlerFactory(protocol -> {
            if ("test".equals(protocol)) {
                return new URLStreamHandler() {
                    @Override
                    protected URLConnection openConnection(URL u) {
                        return connectionSupplier.get();
                    }
                };
            }
            return null;
        });
    }

    // A simple stub HttpURLConnection
    static class StubConnection extends HttpURLConnection {
        int statusCode;
        String responseBody;
        boolean simulateIOException;
        ByteArrayOutputStream requestBody = new ByteArrayOutputStream();
        Map<String, String> requestHeaders = new HashMap<>();

        StubConnection(int code, String body) {
            super(null);
            this.statusCode = code;
            this.responseBody = body;
        }

        @Override public void disconnect() {}
        @Override public boolean usingProxy() { return false; }
        @Override public void connect() throws IOException {}

        @Override
        public OutputStream getOutputStream() throws IOException {
            if (simulateIOException) throw new IOException("Simulated IO Error");
            return requestBody;
        }

        @Override
        public int getResponseCode() throws IOException {
            return statusCode;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(responseBody.getBytes("utf-8"));
        }

        @Override
        public InputStream getErrorStream() {
            try {
                return new ByteArrayInputStream(responseBody.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }

        @Override
        public void setRequestProperty(String key, String value) {
            requestHeaders.put(key, value);
        }
    }

    private AiAssistant assistant;
    private final String apiKey = "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";
    private final String apiUrl = "test://fakehost";
    private final String model  = "qwen/qwq-32b:free";

    @BeforeEach
    void setupAssistant() {
        assistant = new AiAssistant(apiUrl, apiKey, model);
    }

    @Test
    void testSendReceiveSuccessWithContent() throws Exception {
        String jsonResp = "{\"choices\":[{\"message\":{\"content\":\"hello world\"}}]}";
        StubConnection stub = new StubConnection(200, jsonResp);
        connectionSupplier = () -> stub;

        String result = assistant.Send_Recieve("hi there");
        assertEquals("hello world", result);

        // verify headers set
        assertEquals("Bearer " + apiKey, stub.requestHeaders.get("Authorization"));
        assertTrue(stub.requestHeaders.get("Content-Type").contains("application/json"));
    }

    @Test
    void testSendReceiveSuccessNoContent() {
        StubConnection stub = new StubConnection(200, "{}");
        connectionSupplier = () -> stub;

        String result = assistant.Send_Recieve("msg");
        assertEquals("No content Found", result);
    }

    @Test
    void testSendReceiveRequestFailed() {
        StubConnection stub = new StubConnection(500, "error");
        connectionSupplier = () -> stub;

        String result = assistant.Send_Recieve("fail");
        assertEquals("Request Failed", result);
    }

    @Test
    void testSendReceiveIOException() {
        StubConnection stub = new StubConnection(200, "irrelevant");
        stub.simulateIOException = true;
        connectionSupplier = () -> stub;

        String result = assistant.Send_Recieve("ioerror");
        assertEquals("error", result);
    }
}
