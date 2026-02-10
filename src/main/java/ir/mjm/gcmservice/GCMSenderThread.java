package ir.mjm.gcmservice;

import ir.mjm.DBAO.HiberDBFacad;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Created by Serap on 11/29/2014.
 */
public class GCMSenderThread implements Runnable {
  static final Logger log = Logger.getLogger(GCMSenderThread.class);

  int mag_id;
  String message;

  public GCMSenderThread(int mag_id, String message) {
    this.mag_id = mag_id;
    this.message = message;
  }

  @Override
  public void run() {
    List<String> reg_ids = HiberDBFacad.getInstance().getSubscribeGCMUsersForMagazine(mag_id);
    if (reg_ids != null && reg_ids.size() > 0) {
      try {
        GCMSender.getInstance().sendMessage(message, reg_ids);

        log.error("GCM Sender Oked in mag=".concat(mag_id + ""));
      } catch (IOException e) {
        log.error("GCM Sender Exception in mag=".concat(mag_id + ""), e);
      }
    }

  }
}
