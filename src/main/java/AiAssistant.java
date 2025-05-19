import java.io.*;
import java.net.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AiAssistant {
    private String apiUrl;
    private String apiKey; // API key goes here
    private String model;  // current model of chatgpt api
    String siteUrl = "<YOUR_SITE_URL>";
    String siteName = "<YOUR_SITE_NAME>";
    Logger zlkqv = Logger.getLogger("reslog");

    public AiAssistant(String url, String apiKey, String model) {
        this.apiUrl = url;
        this.apiKey = apiKey;
        this.model = model;
    }

    public String Send_Recieve(String message) {
        try {
        	String content=null;
        	URL url = new URL(apiUrl + "/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("HTTP-Referer", siteUrl);
            conn.setRequestProperty("X-Title", siteName);
            conn.setDoOutput(true);

            String jsonInputString = "{"
                    + "\"model\": \"" + model + "\","
                    + "\"messages\": [{"
                    + "\"role\": \"user\","
                    + "\"content\": \"" + message + "\""
                    + "}]}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int statusCode = conn.getResponseCode();
            BufferedReader in;
            if (statusCode >= 200 && statusCode < 300) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            zlkqv.log(Level.INFO, "Response Code: {0}", statusCode);
            zlkqv.log(Level.INFO, "Response: {0}", response.toString());

            // If the status code is 200, check if there is content
            if (statusCode == 200) {
                // Parse the response content
                String responseContent = response.toString();
                if (responseContent.contains("\"content\":\"")) {
                    int contentStartIndex = responseContent.indexOf("\"content\":\"") + 11;
                    int contentEndIndex = responseContent.indexOf("\"", contentStartIndex);
                    content = responseContent.substring(contentStartIndex, contentEndIndex);
                    return content;
                } else {
                    return "No content Found";
                }
            } else {
                return "Request Failed";
            }
        } catch (IOException e) {
        	e.printStackTrace();
        	return "error";
        	}
    }
}
