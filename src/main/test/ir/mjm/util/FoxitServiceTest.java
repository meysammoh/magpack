package ir.mjm.util;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;


public class FoxitServiceTest {
  private String testDir = System.getProperty("user.dir") + "/src/main/test/";
  private String destinationDirectory = testDir + "test";
  private String pdfFilePath = testDir + "test.pdf";

  @BeforeMethod
  public void setUp() throws Exception {
    Assert.assertTrue(new File(pdfFilePath).exists(), "Can not find pdf file in path: " + pdfFilePath);
    final File destDir = new File(destinationDirectory);
    if (destDir.exists()) {
      Assert.assertTrue(destDir.delete(), "Can not delete destination dir: " + destinationDirectory);
    }
    Assert.assertTrue(destDir.mkdir(), "Can not create destination directory: " + destinationDirectory);
  }

  @AfterMethod
  public void tearDown() throws Exception {

  }

  @Test
  public void testSavePages() throws Exception {
    FoxitService.instance().savePages(pdfFilePath, destinationDirectory);
  }
}