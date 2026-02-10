package ir.mjm.DBAO;

import ir.mjm.DBAO.hiber.IssuepagerequestHiberEntity;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by Serap on 7/10/14.
 */
public class UserImageDLAction {
  static final Logger log = Logger.getLogger(UserImageDLAction.class);

  static AddUserDLActions addUserDLActions = null;
  ArrayList<IssuepagerequestHiberEntity> issuepagerequestHiberEntities = new ArrayList<IssuepagerequestHiberEntity>();
  boolean stopThread = false;
  Object syncAob = new Object();
  private static UserImageDLAction ourInstance = new UserImageDLAction();

  public static UserImageDLAction getInstance() {

    return ourInstance;
  }

  private UserImageDLAction() {
    addUserDLActions = new AddUserDLActions();

    addUserDLActions.start();
  }

  public void addToActions(IssuepagerequestHiberEntity issuepagerequestHiberEntity) {
    if (issuepagerequestHiberEntity != null) {
      synchronized (issuepagerequestHiberEntities) {
        issuepagerequestHiberEntities.add(issuepagerequestHiberEntity);
        if (addUserDLActions == null) {
          addUserDLActions = new AddUserDLActions();
          addUserDLActions.start();

        }


      }
    }
    if (!addUserDLActions.isAlive()) {
      addUserDLActions.start();
    }
  }

  public boolean isStopThread() {
    boolean ret;
    synchronized (syncAob) {
      ret = stopThread;
    }
    return ret;
  }

  public void setStopThread(boolean stopThread) {
    synchronized (syncAob) {
      this.stopThread = stopThread;
    }
  }

  class AddUserDLActions extends Thread {
    private ArrayList<IssuepagerequestHiberEntity> issuepagerequestHiber = new ArrayList<IssuepagerequestHiberEntity>();
    int cachedSize = 0;

    @Override
    public void run() {
      log.debug("issuepagerequest Add Process Start.");
      while (!isStopThread()) {
        synchronized (issuepagerequestHiberEntities) {
          if (issuepagerequestHiberEntities != null) {
            cachedSize = issuepagerequestHiberEntities.size();
            if (cachedSize > 0) {
              issuepagerequestHiber.addAll(issuepagerequestHiberEntities);
              issuepagerequestHiberEntities.clear();
            }
          }


        }
        for (int i = 0; i < cachedSize; i++) {
          IssuepagerequestHiberEntity tmp = issuepagerequestHiber.remove(0);
          try {
            HiberDBFacad.getInstance().addIssuepagerequest(tmp);
          } catch (Exception e) {
            log.error("Exception in: ", e);
          }
          try {
            HiberDBFacad.getInstance().addOwnedIssueForMobileUser(tmp.getMagId(), tmp.getUserId());
          } catch (Exception e) {
            log.error("Exception in: ", e);
          }
        }
        cachedSize = 0;
        try {
          sleep((20000 / (cachedSize + 1)));
        } catch (InterruptedException e) {
          log.error("Exception in: ", e);
        }

      }

    }
  }
}
