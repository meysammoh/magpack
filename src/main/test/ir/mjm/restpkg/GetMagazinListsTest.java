package ir.mjm.restpkg;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GetMagazinListsTest {


  private final static String MOBILE_USER_ID="40";
  private final static  String URI = "http://localhost:8080";

  @BeforeMethod
  public void setUp() throws Exception {

  }

  @AfterMethod
  public void tearDown() throws Exception {

  }

  @Test
  public void testGetCats() throws Exception {

  }

  @Test
  public void testGetMags() throws Exception {

  }

  @Test
  public void testGetImage() throws Exception {

  }

  @Test
  public void testGetSug() throws Exception {

  }

  @Test
  public void testGetFirst() throws Exception {

  }

  @Test
  public void testGetLastMags() throws Exception {

  }

  @Test
  public void testFindMagazine() throws Exception {

  }

  @Test
  public void testGetThisMagsImage() throws Exception {

  }

  @Test
  public void testGetLastMagsStream() throws Exception {

   String path = "/webapi/magazine/getCustImg/55/2/m";
    testDownloadService(path);

  }

  public static String testDownloadService(final String path) throws IOException {

    // local variables
    ClientConfig clientConfig = null;
    Client client = null;
    WebTarget webTarget = null;
    Invocation.Builder invocationBuilder = null;
    Response response = null;
    InputStream inputStream = null;
    OutputStream outputStream = null;
    int responseCode;
    String responseMessageFromServer = null;
    String responseString = null;
    String qualifiedDownloadFilePath = null;

    try {
      // invoke service after setting necessary parameters
      clientConfig = new ClientConfig();
      clientConfig.register(MultiPartFeature.class);
      client = ClientBuilder.newClient(clientConfig);
      client.property("accept", "application/*");


      webTarget = client.target(URI).path(path);

      // invoke service
      invocationBuilder = webTarget.request().header("WWW-Authenticate", MOBILE_USER_ID);
      //          invocationBuilder.header("Authorization", "Basic " + authorization);
      response = invocationBuilder.get();

      // get response code
      responseCode = response.getStatus();
      System.out.println("Response code: " + responseCode);

      if (response.getStatus() != 200) {
        throw new RuntimeException("Failed with HTTP error code : " + responseCode);
      }

      // get response message
      responseMessageFromServer = response.getStatusInfo().getReasonPhrase();
      System.out.println("ResponseMessageFromServer: " + responseMessageFromServer);

      // read response string
      inputStream = response.readEntity(InputStream.class);
      String fileName = response.getHeaderString("content-disposition");
      fileName=fileName.substring(fileName.indexOf("=",fileName.indexOf("filename"))+1).trim();
      qualifiedDownloadFilePath = fileName == null ? "MyJerseyZippedFile" : fileName;
      outputStream = new FileOutputStream(qualifiedDownloadFilePath);
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      // set download SUCCES message to return
      responseString = "downloaded successfully at " + qualifiedDownloadFilePath;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      // release resources, if any
      outputStream.close();
      response.close();
      client.close();
    }
    return responseString;
  }

  public void testPassProtectZip_with_params() {
    byte[] inputBytes = null;
    try {
      inputBytes = FileUtils.readFileToByteArray(new File("testGetLastMagsStream.pdf"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("bytes read into array. size = " + inputBytes.length);

    Client client = ClientBuilder.newClient();

    WebTarget target = client.target("http://localhost:8080").path("/webapi/magazine/getCustImg/55/2/l");

    Invocation.Builder builder = target.request(MediaType.APPLICATION_OCTET_STREAM);

    Response resp = builder.put(Entity.entity(inputBytes, MediaType.APPLICATION_OCTET_STREAM));
    System.out.println("response = " + resp.getStatus());
    Assert.assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());

    byte[] zipBytes = resp.readEntity(byte[].class);
    try {
      FileUtils.writeByteArrayToFile(new File("testGetLastMagsStreamTest.pdf"), zipBytes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetLastMagsBaa() throws Exception {

  }

  @Test
  public void testGetThisIssue() throws Exception {

  }

  @Test
  public void testGetLastIssues() throws Exception {

  }

  @Test
  public void testCatImage() throws Exception {

  }

  @Test
  public void testBanner() throws Exception {
    String path = "/webapi/magazine/banner/3";
    testDownloadService(path);
  }
}