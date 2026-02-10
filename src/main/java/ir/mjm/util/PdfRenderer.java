package ir.mjm.util;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.SimpleRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Created by Serap on 6/26/14.
 */
public final class PdfRenderer {
  static final Logger log = Logger.getLogger(PdfRenderer.class);
  HashMap<Integer, String> pageType;

  int smalRes = 15;
  int MedRes = 30;
  int LarlRes = 200;

  public boolean convert(String sourceFile, String destinationDir) throws IOException {
    //        convert( sourceFile,  destinationDir+"/image_",300);
    long start = System.currentTimeMillis();
    //        pdfboxConverter(sourceFile, destinationDir );
    //    convertGhost4j(sourceFile, destinationDir);
   boolean oked= convertByFoxit(sourceFile, destinationDir);
    //        try {
    //            ImageConverter.setup(sourceFile,destinationDir);
    //        } catch (IOException e) {
    //            log.error("Exception in: ",e);
    //        }
    start = System.currentTimeMillis() - start;
            log.error("Converter Time: "+start+" MS");

   return oked;
  }

  public void pdfboxConverter(String sourceFile, String destinationDir) {
    File pdfFile = new File(sourceFile);
    PDDocument document = null;
    try {

      document = PDDocument.load(pdfFile);
    } catch (IOException e) {
      log.error("Exception in: ", e);
    }
    if (document.isEncrypted()) {

    }
    PDFImageWriter imageWriter = new PDFImageWriter();
    boolean sucs = false;
    boolean sucl = false;
    boolean sucm = false;
    try {

      sucs = imageWriter.writeImage(
          document, "png", "",
          1, Integer.MAX_VALUE, destinationDir + "/s_", BufferedImage.TYPE_INT_RGB, smalRes);

      imageWriter.resetEngine();
      sucm = imageWriter.writeImage(
          document, "png", "",
          1, Integer.MAX_VALUE, destinationDir + "/m_", BufferedImage.TYPE_INT_RGB, MedRes);
      imageWriter.resetEngine();

      sucl = imageWriter.writeImage(
          document, "png", "",
          1, Integer.MAX_VALUE, destinationDir + "/l_");

    } catch (IOException e) {
      log.error("Exception in: ", e);
    }
    if (!sucs) {
      System.err.println(
          "Error: no writer found for Small image format '"
          + BufferedImage.TYPE_INT_RGB + "'");
    }
    if (!sucm) {
      System.err.println(
          "Error: no writer found for Medium image format '"
          + BufferedImage.TYPE_INT_RGB + "'");
    }
    if (!sucl) {
      System.err.println(
          "Error: no writer found for Larg image format '"
          + BufferedImage.TYPE_INT_RGB + "'");
    }

  }


  private synchronized void convertGhost4j(String sourceFile, String destinationDir) {

    pageType = new HashMap<>();

    try {

      // load PDF document

      int dpi[] = {30, 50, 200};
      int pagePSize[] = {1, 1, 1};
      String desDir[] = {"/sg_", "/mg_", "/lg_"};
      // create renderer
      PDFDocument document = new PDFDocument();
      document.load(new File(sourceFile));
      int docpageCount = document.getPageCount();
      SimpleRenderer renderer = new SimpleRenderer();

      for (int j = 0; j < 3; j++) {
        long start = System.currentTimeMillis();

        // set resolution (in DPI)
        //                if(j<dpi.length)
        renderer.setResolution(dpi[j]);
        //                renderer.setAntialiasing();
        int p = 0;
        while (p <= docpageCount) {

          int toP = p + pagePSize[j];
          if (p + pagePSize[j] + 1 > docpageCount) {
            toP = docpageCount;
          }
          // render
          List<Image> images = renderer.render(document, p, toP);
          List<Image> images2 = null;
          if (j == 0) {
            images2 = renderer.render(document, p, toP);
          }


          // write images to files to disk as PNG
          try {
            for (int i = 0; i < images.size(); i++) {
              if (j == 0) {
                //                                log.error("Page: "+i+" and j: "+j);
                File file = new File(destinationDir + desDir[j] + (i + 1 + p) + ".png");
                File file2 = new File(destinationDir + desDir[j] + (i + 1 + p) + "_" + ".jpg");
                //                                log.error("go write jpg");

                ImageIO.write((RenderedImage) images2.get(i), "jpg", file2);
                //                                log.error("wrote jpg");
                //                                log.error("go write png");

                ImageIO.write((RenderedImage) images.get(i), "png", file);
                //                                log.error("wrote png");


                //                                log.error("go compair");

                if (file.length() > file2.length()) {//if png is bigger than jpg
                  pageType.put(i + 1 + p, "jpg");
                  //                                    log.error("go delete: png ");

                  file.delete();
                  //                                    log.error("deleted ");
                  //                                    log.error("go to rename jpg ");


                  file2.renameTo(new File(destinationDir + desDir[j] + (i + 1 + p) + ".jpg"));
                  //                                    log.error("renamed ");
                } else {
                  pageType.put(i + 1 + p, "png");
                  //                                    log.error("go delete: jpg ");

                  file2.delete();
                  //                                    log.error("deleted ");

                }
              } else {
                String ftype = pageType.get(i + 1 + p);
                ftype = ftype == null ? "jpg" : ftype;
                ImageIO.write(
                    (RenderedImage) images.get(i),
                    ftype,
                    new File(destinationDir + desDir[j] + (i + 1 + p) + "." + ftype));

              }
            }
          } catch (IOException e) {
            log.error("ERROR: " + e.getMessage());
          }
          p += pagePSize[j] + 1;
        }
        start = System.currentTimeMillis() - start;
        log.error(desDir[j] + " Converter Time: " + start + " MS");
        System.gc();
        Thread.currentThread().sleep(10);
      }
    } catch (Exception e) {
      log.error("ERROR: " + e.getMessage());
    }

  }

  private boolean convertByFoxit(String sourceFile, String destinationDir) throws IOException {
   return FoxitService.instance().savePages(sourceFile, destinationDir);
  }
}


