package ir.mjm.DBAO;

import ir.mjm.gcmservice.GCMSenderThread;
import ir.mjm.util.PdfRenderer;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by Serap on 6/20/14.
 */
public class ConverterThread extends Thread {
  static final Logger log = Logger.getLogger(ConverterThread.class);

  String dir_path, fileName;
  int mag_id;

  public ConverterThread(String dir_path, String fileName, int mag_id) {
    this.dir_path = dir_path;
    this.fileName = fileName;
    this.mag_id = mag_id;
  }

  @Override
  public void run() {
    try {
      boolean oked = convertPDF(dir_path, fileName);
      //            DBFacade.getInstance().updateMagazinConvertedto(mag_id,true);
       HiberDBFacad.getInstance().updateMagazinConvertedto(mag_id, oked);
      GCMSenderThread gcmSenderThread = new GCMSenderThread(
          mag_id,
          "A new issue of " + HiberDBFacad.getInstance().getMagazineById(mag_id).getTitle() + "  published.");
      gcmSenderThread.run();
    } catch (Exception e) {
      log.error("Exception in: ", e);
      //            magazine_tblTable.setConverted(false);

    }
  }

  private boolean convertPDF(String dir_path, String fileName) throws Exception {
    boolean oked=false;
    File destinationFile = (new File(dir_path + fileName));
    File imageDestinationDir = new File(dir_path + "/image/");
    if (!imageDestinationDir.exists() && imageDestinationDir.mkdir()) {
      final File[] listFiles = imageDestinationDir.listFiles();
      if (listFiles!=null&&listFiles.length == 0) {
        //            log.error("send to converter: "+destinationFile.getPath()+imageDestinationDir.getPath());
        PdfRenderer pdfRenderer = new PdfRenderer();
       oked= pdfRenderer.convert(destinationFile.getPath(), imageDestinationDir.getPath());
        //            magazine_tblTable.setConverted(true);
        //            log.error("Converting done.");
      }
    }
    return oked;
  }


}
