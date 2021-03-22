package fr.republicraft.common.api.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Simple HTTP GET/POST client for Third party interaction
 */
public class LightHTTPClient {

    /**
     * Get output from response
     *
     * @param conn Http url connection
     * @return output body as string
     * @throws IOException
     */
    private static String getOutput(HttpURLConnection conn) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder output = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            output.append(line);
        }
        conn.disconnect();
        return output.toString();
    }

    /**
     * Create a Http url connection.
     *
     * @param url url to connect
     * @return Http url connection
     * @throws IOException
     */
    HttpURLConnection create(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }


    /**
     * Perform a Http GET request to url with accept "application/json" output.
     *
     * @param url url to connect
     * @return output body as string or null
     */
    public String get(String url) {
        try {
            URL localUrl = new URL(url);
            HttpURLConnection conn = create(localUrl);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() < 200 || conn.getResponseCode() > 299) {
                throw new RuntimeException("Failed : HTTP error code: " + conn.getResponseCode());
            }
            return getOutput(conn);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Perform a Http POST request to url with accept "application/json" output.
     *
     * @param url  url to connect
     * @param body request body
     * @return output body as string or null
     */
    public String post(String url, String body) {

        try {

            URL localUrl = new URL(url);
            HttpURLConnection conn = create(localUrl);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes());
            os.flush();

            if (conn.getResponseCode() < 200 || conn.getResponseCode() > 299) {
                throw new RuntimeException("Failed : HTTP error code: " + conn.getResponseCode());
            }

            return getOutput(conn);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
