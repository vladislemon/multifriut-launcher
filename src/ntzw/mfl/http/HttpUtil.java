package ntzw.mfl.http;

import ntzw.mfl.StringPair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    public static HttpResponse post(String host, String payload, Map<String, String> properties) throws IOException {
        URL url = new URL(host);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        Charset charset = null;
        for(Map.Entry<String, String> entry : properties.entrySet()) {
            if(entry.getKey().equalsIgnoreCase("Accept-Charset")) {
                charset = Charset.forName(entry.getValue());
            }
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        if(charset == null) {
            charset = StandardCharsets.UTF_8;
            connection.setRequestProperty("Accept-Charset", charset.name());
        }

        connection.setDoOutput(true);

        OutputStreamWriter outputWriter = new OutputStreamWriter(connection.getOutputStream(), charset);
        //DEBUG
        //System.out.println(payload);
        outputWriter.write(payload);
        outputWriter.flush();

        int responseCode = connection.getResponseCode();
        Map<String, List<String>> responseProperties = connection.getHeaderFields();

        if(responseCode < 400) {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = inputReader.readLine()) != null) {
                response.append(inputLine);
                //DEBUG
                //System.out.println(inputLine);
            }
            inputReader.close();
            return new HttpResponse(responseCode, responseProperties, response.toString());
        }

        return new HttpResponse(responseCode, responseProperties, "");
    }

    public static HttpResponse post(String host, String payload, StringPair... properties) throws IOException {
        if(properties != null && properties.length > 0) {
            Map<String, String> map = new HashMap<>();
            for(StringPair pair : properties) {
                map.put(pair.getFirst(), pair.getSecond());
            }
            return post(host, payload, map);
        }
        return post(host, payload, new HashMap<>());
    }
}
