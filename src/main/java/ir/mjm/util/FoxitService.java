package ir.mjm.util;

import com.foxit.gsdk.PDFException;
import com.foxit.gsdk.PDFLibrary;
import com.foxit.gsdk.pdf.PDFDocument;
import com.foxit.gsdk.pdf.PDFPage;
import com.foxit.gsdk.pdf.Progress;
import com.foxit.gsdk.pdf.pageobjects.PageObjects;
import com.foxit.gsdk.utils.FileHandler;
import ir.mjm.DBAO.Statistics;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class FoxitService {
  private final static FoxitService INSTANCE = new FoxitService();

  static final Logger log = Logger.getLogger(FoxitService.class);

  public static FoxitService instance() {
    return INSTANCE;
  }

  static {
    try {
      String name = System.getProperty("os.name");
      String arch = System.getProperty("os.arch");
      log.error("os.name = " + name);
      log.error("os.arch = " + arch);
      if (name.toLowerCase().startsWith("win"))//windows
      {
        if (arch.contains("64")) {
          System.load(Statistics.getApplicationPath() + "//WEB-INF//classes//fsdk_java_win64.dll");
        } else {
          System.load(Statistics.getApplicationPath() + "//WEB-INF//classes//fsdk_java_win32.dll");
        }
      } else {//linux
        if (arch.contains("64")) {
          final String libfsdk64Linux = Statistics.getApplicationPath() + "/WEB-INF/classes/libfsdk_java_linux64.so";
          log.error("try to load " + libfsdk64Linux);
          System.load(libfsdk64Linux);
        } else {

          final String libfsdkPath32 = Statistics.getApplicationPath() + "/WEB-INF/classes/libfsdk_java_linux32.so";
          log.error("try to load " + libfsdkPath32);
          System.load(libfsdkPath32);
        }
      }

    } catch (Exception e) {
      System.out.println("Native code library failed to load.\n" + e);
      log.error("Native code library failed to load.\n" + e);
    }
  }

  /**
   * license file
   */
  private static String license_id = "dxertz7Zw21Um6c6ULChTZ5xnEShZIp39tPmaI4KJwOgrxlJ/KCnhQ==";
  private static String unlockCode =
      "8f3o18GNtRkNBDdqtFZS8bag26vXuHklApT0VbrYMFmi5Tn8cP2aKFdzLC30QRvjfyg6bxC69l4OoWcIjB76xzSs7B2+u3q3Mt4Cf4ORp7OPcP3AgWwNfIt+tYeqCILLPsM+/QcLywUd4HOR+iE94sQgNE4iOq74vy/tmbU4LUkxwvN3PrzLYNhuzzza3Xor31wnNbASpXv7gam6YHWEv98Gr1zyofqe9J5A8QyX8NC8I8j43DTLcSme1LhXoM3cAeQu+deRg+ZeKgXjz9oyDD6MvuDJtFSGXUjoLdEbKHM7bldpnj/MruIdVJs5He1AHUreavlCnc43AuJ/x+Ki4e2hLu7dClcDBE0HpURIc25tBaxjjFG7pbsZyKvbHmE3+7xTr9G6L9vSzaEvpC6WU1Mo4Xj0+bov7tYq25KPX7lX9ULghLsXdqtoaLY9w8J86j48xR/fkSvH8ibWtVNE94tLu8U15gcPCZnimEbEcWBi/4QA266WGY2/b5PaQlTgT13GXSRONJRPnmCxhbDjlbB38VJ24XxkiZ+XTTQg2Gw1mUJndVrEJPdU5YXTW4LjTEAHW4fhcTxJaEGYGSaJtZsmmTBBV7hYNUnElQAvnVsq0fQT/fKKY9wKPMDmIIVgMGLQJgTFmrW9oG//uaMBynZHKFOusgXhgP2wSkTEQOnkwzHh1cHK4K6h3KNzLN53Iro6IySzwhTihFVhPXEPx5i60mfYLaJf4ZVvmA3qaJ5oWe4+71EpYnJkNvslOrVnnISvxS1l39ckHwr0ptYYDAeZ6T1lVnY4ghn+hKeBYvY53G/T7joHN11aJ9l0S1u4Yk/xuvHj0wuhcx0rb7ePAycfbi3nmQqJII9o1VuNnmlky30XPylUC4iS104+vpnyI+dxDWj3HRMAPFrmrtIJTJIElV1VeoWHFmEhQawu36g/X5qcxGxuNhdvUmG25D8vITyJW+JRRB7X8AfybwL+dTBnYk6QU837Wq7voveGLQQdys2oW+0iW9Tt91IQJIM6kt9KOL6xTDagXBq9T2EF4qxlMdv1Dsq/870nrwhqXyaSHYLi+Hh41oPN31oM2mQMRju5sMEB/F5OMDscA2JUFB+cxVUaStEfQm+Kwm1ULEPJOu2OZIMaKXU7adi2Qd2w/XSKwzjPABgC2TxLltoncHcL2b7vCsHjquWHzbj/xqVtJcqNUTFKcs0ODGHOh/9T9VuMfVnCkeLvqbztY7SaIkNkEMKKtBPnn8OdlxnfFBqcxZh1f5Jq43Aa2IP+o+xZfUbCt0vjLWBHJ9BgU3MMgzPft74934hl5LCflb4wT1P9aZcO/88aG/33ORF4uUM37flA4bkU+mPctQVfA81aHmfwxjHTxCggqfdvpzK3dvnE+W42HMBYILP4kWs4CK++iY3qkydeSdnEexy1dmfGcFsbWfG0yS5Jlo9wROh9S5Wsm5bWCQsTntWEfA0UCDHYWh+1gZ5umTNSjtGCa4M48E1Hu1ohXAePgNc7SA9441azHGTAVkp1cAEBNezHfj+qxCmK/E8RJLuqo8zai2Wnmep9kh7uOzEfxvYMUGDZaoYsgcjiZLYxpEw4ojjmkkm2c9q3EWnK6INI3Th/1sljQ/gvLmAUuzyeGQnL8wjdzw5RXa2Y/3bUDT2c7OCoYifA7cW95WBy7ZRSzZMrfTUX7p7PrI7+2iRw+1cQRVDPupWQ1nKgdedFaqv2YxVidnAQV8D208blV05hIuLyL1KnhQxmtRUI55SDgQ==";
  private static final float MG_SCALE = 0.5F;
  private static final float SG_SCALE = 0.3F;

  public boolean savePages(String sourceFile, String destinationDirectory) throws IOException {
    boolean oked=false;
    // load Foxit PDF Library
    // TODO Auto-generated method stub
    int nMemorySize = 10 * 1024 * 1024;
    boolean bScaleable = true;
    // initialize
    PDFLibrary pdfLibrary = PDFLibrary.getInstance();
    try {
      pdfLibrary.initialize(nMemorySize, bScaleable);
      pdfLibrary.unlock(license_id, unlockCode);
      log.error("Success: Initlize and unlbScaleableock the library.");
    } catch (PDFException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      log.error("Failed to initlize or unlock the library, the error code is: " + e.getLastError());

    }
    // open an existing pdf file
    PDFDocument originalPdfDocument = null;
    try {
      File srcFile = new File(sourceFile);
      FileHandler handler = FileHandler.create(sourceFile, FileHandler.FILEMODE_READONLY);
      originalPdfDocument = PDFDocument.open(handler, null);

      log.error("Success: Open an existing PDF document.");
      // get page count
      int pageCount = originalPdfDocument.countPages();
      for (int i = 0; i < pageCount; i++) {
        savePageAsPdfFile(destinationDirectory, originalPdfDocument, srcFile, i, "lg_" + i);

        String type = FoxitImageConverterService.INSTANCE.savePageAsImage(
            originalPdfDocument.getPage(i),
            destinationDirectory,
            "sg_" + i,
            null, SG_SCALE);
        FoxitImageConverterService.INSTANCE.savePageAsImage(
            originalPdfDocument.getPage(i),
            destinationDirectory,
            "mg_" + i,
            type, MG_SCALE);
        Thread.sleep(200);
        if ((i / 30) == 0) {
          System.gc();
        }
      }
      log.error("Success: page count = " + pageCount);
      oked=true;
      System.gc();
    } catch (PDFException e) {
      log.error("Foxit pdf process exception: " + e.getMessage(), e);
      e.printStackTrace();
    } catch (InterruptedException e) {
      log.error(e);
    }
    return oked;
  }

  private void savePageAsPdfFile(
      final String destinationDirectory,
      final PDFDocument originalPdfDocument,
      final File srcFile, final int pageNumber, final String fileName) throws PDFException,
                                                                              IOException {//                PDFPage page = orginal.getPage(i);
    PDFDocument newPdfDocument = PDFDocument.create();

    int[] pagesList = {pageNumber, 1};
    Progress progress = PDFDocument.startImportPages(newPdfDocument, 0, originalPdfDocument, pagesList, null);
    waitUntilRelease(progress);
    final PDFPage newPdDocumentPage = newPdfDocument.getPage(0);

    for (int j = 0; j < 5; j++) {

    }
    waitUntilRelease(newPdDocumentPage.startParse(PDFPage.FLATTENFLAG_DISPLAY));
    final PageObjects newPdDocumentPagePageObjects = newPdDocumentPage.getPageObjects();
    final FileHandler handler1 = FileHandler.create(
        destinationDirectory + File.separatorChar +
        fileName + ".pdf",
        FileHandler.FILEMODE_TRUNCATE);
    Progress prog = newPdfDocument.startSaveToFile(
        handler1,
        PDFDocument.SAVEFLAG_OBJECTSTREAM
                                                  );
    waitUntilRelease(prog);

    handler1.release();
    newPdfDocument.close();
    newPdfDocument = null;

  }

  private static void waitUntilRelease(Progress progress) throws PDFException {
    if (progress != null) {
      int ret = Progress.TOBECONTINUED;
      while (Progress.TOBECONTINUED == ret) {
        ret = progress.continueProgress(30);
      }
      progress.release();
    }
  }
}
