package ir.mjm.DBAO;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Serap on 6/20/14.
 */

public class ConverterFacad {
  static final Logger log = Logger.getLogger(ConverterFacad.class);

  private static ExecutorService threadPool;
  private static ConverterJob converterJob;
  private static boolean stop = false;
  private static Object syncob = new Object();
  private static Object synupList = new Object();
  ArrayList<ConverterMag> uploadedMagazine = new ArrayList<ConverterMag>();
  ArrayList<Integer> updateConverted = new ArrayList<Integer>();

  public synchronized void convertAll(ArrayList<Magazine_tblTable> uploadedMagazine) {
    for (Magazine_tblTable magazine_tblTable : uploadedMagazine) {
      ConverterMag converterMag = new ConverterMag();
      converterMag.setMag_id(magazine_tblTable.getMagazine_id());
      converterMag.setDir_path(magazine_tblTable.getDir_path());
      converterMag.setFileName(magazine_tblTable.getFileName());
      this.uploadedMagazine.add(converterMag);
    }
    if (converterJob == null) {

      converterJob = new ConverterJob();
      converterJob.start();
    }

  }

  public synchronized void stopConverter() {
    synchronized (syncob) {
      stop = true;
    }
  }

  class ConverterJob extends Thread {
    @Override
    public void run() {

      while (true) {
        boolean hasjob = false;
        synchronized (uploadedMagazine) {
          hasjob = !uploadedMagazine.isEmpty();
        }
        if (hasjob) {
          convert();
        } else {
          try {
            Thread.currentThread().sleep(2000);
          } catch (InterruptedException e) {
            log.error("Exception in: ", e);
          }
        }

        synchronized (syncob) {
          if (stop) {
            break;
          }
        }
      }
    }

    private synchronized void convert() {
      if (threadPool == null) {
        threadPool = Executors.newFixedThreadPool(1);
      }
      //            log.error("in Converter.");
      while (!uploadedMagazine.isEmpty()) {
        ConverterMag magazine;
        synchronized (uploadedMagazine) {
          magazine = uploadedMagazine.remove(0);
        }
        ConverterThread convrt = new ConverterThread(
            Statistics.appPath.replaceAll("\\\\", "/") + magazine.getDir_path(),
            magazine.getFileName(),
            magazine.getMag_id());
        threadPool.execute(convrt);

        while (convrt.isAlive()) {
          try {
            Thread.currentThread().sleep(200);
          } catch (InterruptedException e) {
            log.error("Exception in: ", e);
          }
        }


      }
      //            log.error("Convert Finished Successfully.");
    }
  }

  class ConverterMag {
    String dir_path, fileName;
    int mag_id;


    public String getDir_path() {
      return dir_path;
    }

    public void setDir_path(String dir_path) {
      this.dir_path = dir_path;
    }

    public String getFileName() {
      return fileName;
    }

    public void setFileName(String fileName) {
      this.fileName = fileName;
    }

    public int getMag_id() {
      return mag_id;
    }

    public void setMag_id(int mag_id) {
      this.mag_id = mag_id;
    }
  }


}
