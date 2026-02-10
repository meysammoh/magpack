package ir.mjm.gcmservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Serap on 11/25/2014.
 */
public class GCMSender {
  static final Logger log = Logger.getLogger(GCMSender.class);

  final static String Project_ID = "magpackv-1-0";
  final static String GCM_sender_ID = "150098184016"; //Project Number
  //    final static String API_key="AIzaSyBGuHnEnUFTE59ZtQTYT0ga1m9wKhw9fC4";
  final static String API_key = "AIzaSyCya_sLvWUxwCaNVgK5mSsx4OhsHhLDjWk";
  final static String collpaseKey = "gcm_message";
  final static Sender sender = new Sender(API_key);
  private static GCMSender ourInstance = new GCMSender();

  public static GCMSender getInstance() {
    return ourInstance;
  }

  private GCMSender() {
  }

  public static void post(String apiKey, GCMContent content) {

    try {

      // 1. URL
      URL url = new URL("https://android.googleapis.com/gcm/send");

      // 2. Open connection
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      // 3. Specify POST method
      conn.setRequestMethod("POST");

      // 4. Set the headers
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Authorization", "key=" + apiKey);

      conn.setDoOutput(true);

      // 5. Add JSON data into POST request body

      //`5.1 Use Jackson object mapper to convert Contnet object into JSON
      ObjectMapper mapper = new ObjectMapper();

      // 5.2 Get connection output stream
      DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

      // 5.3 Copy Content "JSON" into
      mapper.writeValue(wr, content);

      // 5.4 Send the request
      wr.flush();

      // 5.5 close
      wr.close();

      // 6. Get the response
      int responseCode = conn.getResponseCode();
      log.error("\nSending 'POST' request to URL : " + url);
      log.error("Response Code : " + responseCode);

      BufferedReader in = new BufferedReader(
          new InputStreamReader(conn.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      // 7. Print result
      log.error("Response : " + response.toString());

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public synchronized void sendMessage(String messageStr, List<String> androidTargets) throws IOException {
    if (androidTargets != null && androidTargets.size() > 0) {
      sendMessage(API_key, collpaseKey, messageStr, androidTargets);
    }
  }

  public synchronized void sendMessage(
      String API_KEY,
      String collpaseKey,
      String messageStr,
      List<String> androidTargets) throws IOException {
    //        String API_KEY = ""; //sender id got from google api console project
    //        String collpaseKey = "gcm_message"; //if messages are sent and not delivered yet to android device (as device might not be online), then only deliver latest message when device is online
    //        String messageStr = "message content here"; //actual message content
    //        String messageId = "APA91bGgGzVQWb88wkRkACGmHJROeJSyQbzLvh3GgP2CASK_NBsuIXH15HcnMta3e9ZXMhdPN6Z3FSD2Pezf6bhgUuM2CF74SgZbG4Zr57LA76VVaNvSi7XM7QEuAVLIiTsXnVq3QAUFDo-ynD316bF10JGT3ZOaSQ"; //gcm registration id of android device

    Sender sender = new Sender(API_KEY);
    Message.Builder builder = new Message.Builder();

    builder.collapseKey(collpaseKey);
    builder.timeToLive(30);
    builder.delayWhileIdle(true);
    builder.addData("message", messageStr);

    Message message = builder.build();

    //        List<String> androidTargets = new ArrayList<String>();
    //        if multiple messages needs to be deliver then add more message ids to this list
    //        androidTargets.add(messageId);

    MulticastResult result = sender.send(message, androidTargets, 5);
    log.error("result = " + result);

    if (result.getResults() != null) {
      int canonicalRegId = result.getCanonicalIds();
      log.error("canonicalRegId = " + canonicalRegId);

      if (canonicalRegId != 0) {
      }
    } else {
      int error = result.getFailure();
      log.error("Broadcast failure: " + error);
    }
  }
}
