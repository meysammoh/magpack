package ir.mjm.util;

import com.foxit.gsdk.PDFException;
import com.foxit.gsdk.image.Bitmap;
import com.foxit.gsdk.pdf.PDFPage;
import com.foxit.gsdk.pdf.Progress;
import com.foxit.gsdk.pdf.RenderContext;
import com.foxit.gsdk.pdf.Renderer;
import com.foxit.gsdk.utils.Size;
import com.foxit.gsdk.utils.SizeF;
import org.apache.log4j.Logger;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * @author Jafar Mirzaei (jm.csh2009@gmail.com)
 * @version 1.0 2015.1001
 * @since 1.0
 */
public enum FoxitImageConverterService {
  INSTANCE;

  static final Logger log = org.apache.log4j.Logger.getLogger(FoxitImageConverterService.class);

  private static final String EXTENTION_JPG = ".jpg";
  private static final String EXTENTION_PNG = ".png";

  public String savePageAsImage(
      final PDFPage page,
      String directory,
      String name, String imageType, float scaleDown) throws
                                                      PDFException, IOException {
    Progress progress = page.startParse(PDFPage.RENDERFLAG_NORMAL);
    if (progress != null) {
      int ret = Progress.TOBECONTINUED;
      while (ret == Progress.TOBECONTINUED) {
        ret = progress.continueProgress(30);
      }
    }
    progress.release();
    SizeF pageSize = page.getSize();
    com.foxit.gsdk.utils.Matrix matrix = new com.foxit.gsdk.utils.Matrix();
    int width = (int) (pageSize.getWidth() * scaleDown);
    int height = (int) (pageSize.getHeight() * scaleDown);
    matrix = page.getDisplayMatrix(0, 0, width, height, 0);
    Size size = new Size();
    size.setWidth(width);
    size.setHeight(height);
    Bitmap bmp = Bitmap.create(size, Bitmap.FORMAT_24BPP_BGR, null, 0);

    Renderer render = Renderer.create(bmp);
    RenderContext renderContext = RenderContext.create();
    renderContext.setMatrix(matrix);
    renderContext.setFlags(RenderContext.FLAG_ANNOT);
    Progress renderProgress = page.startRender(renderContext, render, 0);
    if (renderContext != null) {
      int ret = Progress.TOBECONTINUED;
      while (ret == Progress.TOBECONTINUED) {
        ret = renderProgress.continueProgress(30);
      }
    }
    renderProgress.release();
    return this.saveImageToFileWithAutoDetectPngOrJpgAndReturnTypeString(
        directory,
        name,
        bmp.convertToBufferedImage(),
        imageType);
  }

  public String saveImageToFileWithAutoDetectPngOrJpgAndReturnTypeString(
      String destinationDir,
      String fileName,
      RenderedImage renderedImage, String imageType) throws
                                                     IOException {
    fileName = File.separator + fileName;
    if (imageType == null) {
      String lowSizeType = "png";
      File file = new File(destinationDir + fileName + EXTENTION_PNG);
      File file2 = new File(destinationDir + fileName + "_" + EXTENTION_JPG);

      ImageIO.write(renderedImage, "jpg", file2);

      ImageIO.write(renderedImage, "png", file);


      if (file.length() > file2.length()) {//if png is bigger than jpg
        lowSizeType = "jpg";
        //                                    log.error("go delete: png ");

        file.delete();

        file2.renameTo(new File(destinationDir + fileName + EXTENTION_JPG));
        //                                    log.error("renamed ");
      } else {
        lowSizeType = "png";
        //                                    log.error("go delete: jpg ");

        file2.delete();
        //                                    log.error("deleted ");

      }
      return lowSizeType;

    } else {
      ImageIO.write(
          renderedImage,
          imageType,
          new File(destinationDir + fileName + "." + imageType));
      return imageType;
    }

  }
}

