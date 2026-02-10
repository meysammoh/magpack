package ir.mjm.DBAO;

import ir.mjm.DBAO.hiber.MagazineTblHiberEntity;
import ir.mjm.restBeans.Suggestion;
import ir.mjm.util.Pair;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * Created by pcd iran on 6/14/14.
 */
public class ImageFacade {
  static final Logger log = Logger.getLogger(ImageFacade.class);

  private static ImageFacade ourInstance = new ImageFacade();

  public static ImageFacade getInstance() {
    return ourInstance;
  }

  private ImageFacade() {
  }

  public Pair<Image, String> getFirstImage(int magazine_id) throws Exception {
    //        String dirPath=DBFacade.getInstance().getMagazinePath(magazine_id);
    String dirPath = HiberDBFacad.getInstance().getMagazinePath(magazine_id);
    String imgname = "png";
    File pdfDir = new File(Statistics.appPath + dirPath + "/image/");
    File imageFile = getImageInDir(pdfDir, "page_0.png");
    if (imageFile == null) {
      imageFile = getImageInDir(pdfDir, "page_0.jpg");
      if (imageFile == null) {
        return null;
      }
      imgname = "jpg";
    }
    return new Pair<Image, String>(ImageIO.read((imageFile)), imgname);


  }

  public File getImageInDir(File dir, String imageName) {

    File[] flss = dir.listFiles();
    if (flss != null) {
      for (File f : flss) {
        if (f.getName().toLowerCase().contains(imageName.toLowerCase())) {
          ;
        }
        return f;
      }
    }
    return null;
  }

  public ArrayList<Image> getImage(File dir, String startBy, String endBy1, String endBy2, int startNum, int endNum) {
    ArrayList<Image> ret = new ArrayList<Image>();
    ArrayList<File> allfiless = new ArrayList<File>();
    log.debug("   line49     Collections.addAll(allfiless, dir.listFiles());\n dir: " + dir);
    if (dir == null) {
      return ret;
    }
    if (dir.getName().toLowerCase().endsWith(".pdf")) {
      dir = dir.getParentFile();
    }
    Collections.addAll(allfiless, dir.listFiles());
    while (!allfiless.isEmpty()) {
      File f = allfiless.remove(0);
      if (f.isDirectory()) {
        Collections.addAll(allfiless, f.listFiles());
        continue;
      }

      if (f.getName().toLowerCase().startsWith(startBy)) {
        for (int i = startNum; i <= endNum; i++) {
          if (f.getName().toLowerCase().endsWith("_" + i + endBy1) ||
              f.getName().toLowerCase().endsWith("_" + i + endBy2)) {
            try {
              ret.add(ImageIO.read(f));
            } catch (IOException e) {
              log.error("Exception in: ", e);
            }
          }
        }
      }

    }
    return ret;

  }

  public static Pair<Image, String> getImage(File dir, String startBy, int imagePage) {

    ArrayList<File> allfiless = new ArrayList<File>();
    String ext = "png";
    //        log.debug("  line 81      Collections.addAll(allfiless, dir.listFiles());\n dir: "+dir);
    if (dir == null) {
      return null;
    }
    if (dir.getName().toLowerCase().endsWith(".pdf")) {
      dir = dir.getParentFile();
    }
    String[] dirlist = dir.list();
    if (dirlist != null && dirlist.length > 0) {
      Collections.addAll(allfiless, dir.listFiles());
    }
    while (!allfiless.isEmpty()) {
      File f = allfiless.remove(0);
      if (f.isDirectory()) {
        File[] ldd = f.listFiles();
        if (ldd != null && ldd.length > 0) {
          Collections.addAll(allfiless, f.listFiles());
        }
        continue;
      }
      boolean tmpbool = f.getName().toLowerCase().endsWith("_" + imagePage + ".jpg");
      if (f.getName().toLowerCase().startsWith(startBy) &&
          (f.getName().toLowerCase().endsWith("_" + imagePage + ".png") || tmpbool)) {
        try {
          if (tmpbool) {
            ext = "jpg";
          }
          return new Pair<Image, String>(ImageIO.read(f), ext);
        } catch (IOException e) {
          log.error("Exception in: ", e);
        }
      }


    }
    return null;

  }

  public Pair<Image, String> getImage(File dir, String name) {
    Pair<Image, String> ret = null;
    ArrayList<File> allFiles = new ArrayList<>();
    log.debug("   line 113     Collections.addAll(allFiles, dir.listFiles());\n dir: " + dir);
    if (dir == null || (!dir.exists())) {
      return ret;
    }
    if (dir.getName().toLowerCase().endsWith(".pdf")) {
      dir = dir.getParentFile();
    }
    Collections.addAll(allFiles, dir.listFiles());
    while (!allFiles.isEmpty()) {
      File f = allFiles.remove(0);
      if (f.isDirectory()) {
        Collections.addAll(allFiles, f.listFiles());
        continue;
      }

      if (f.getName().toLowerCase().endsWith(name)) {

        try {
          String ext = "png";
          int index = f.getName().lastIndexOf(".");
          if (index > 0) {
            ext = f.getName().substring(index + 1);
          }
          ret = new Pair<Image, String>(ImageIO.read(f), ext);
          return ret;
        } catch (IOException e) {
          log.error("Exception in: ", e);
        }
      }

    }
    return ret;

  }

  public Pair<File, String> getFileEndWith(File dir, String endWith) {
    Pair<File, String> ret = null;
    ArrayList<File> allFiles = new ArrayList<>();
    log.debug("   line 113     Collections.addAll(allFiles, dir.listFiles());\n dir: " + dir);
    if (dir == null || (!dir.exists())) {
      return ret;
    }
    if (dir.getName().toLowerCase().endsWith(".pdf")) {
      dir = dir.getParentFile();
    }
    Collections.addAll(allFiles, dir.listFiles());
    while (!allFiles.isEmpty()) {
      File f = allFiles.remove(0);
      if (f.isDirectory()) {
        Collections.addAll(allFiles, f.listFiles());
        continue;
      }
      if (f.getName().toLowerCase().endsWith(endWith)) {
        String ext = "png";
        int index = f.getName().lastIndexOf(".");
        if (index > 0) {
          ext = f.getName().substring(index + 1);
        }
        ret = new Pair<File, String>(f, ext);
        return ret;
      }
    }
    return ret;
  }

  public HashMap<Magazine_tblTable, ArrayList<Image>> getLastSmallSuggestion(int numOfMag, int numOfPageEachMAg) {
    ArrayList<Magazine_tblTable> pdFiles = new ArrayList<Magazine_tblTable>();
    //        try {
    pdFiles = HiberDBFacad.getInstance().getLastMagazines(numOfMag, true);
    //        } catch (SQLException e) {
    //            log.error("Exception in: ", e);
    //        }
    HashMap<Magazine_tblTable, ArrayList<Image>> ret = new HashMap<Magazine_tblTable, ArrayList<Image>>();
    for (Magazine_tblTable magaznie : pdFiles) {
      ret.put(
          magaznie,
          getFirstSmallImage(
              Statistics.appPath.replaceAll("\\\\", "/") + magaznie.getDir_path(),
              numOfPageEachMAg));
    }

    return ret;

  }

  private ArrayList<Image> getFirstSmallImage(String dir, int nImage) {
    return getImage((new File(dir)), "s", ".png", ".jpg", 1, nImage);
  }


  public HashMap<Suggestion, Image> getLastSuggestion(int numOfMag, int numOfPageEachMag, ImageSize imageSize) {
    ArrayList<Magazine_tblTable> pdFiles = new ArrayList<Magazine_tblTable>();
    //        try {
    //            pdFiles=DBFacade.getInstance().getLastMagazines(numOfMag,true);
    pdFiles = HiberDBFacad.getInstance().getLastMagazines(numOfMag, true);
    //        } catch (SQLException e) {
    //            log.error("Exception in: ", e);
    //        }
    HashMap<Suggestion, Image> ret = new HashMap<Suggestion, Image>();

    if (imageSize == ImageSize.Small) {
      for (Magazine_tblTable magaznie : pdFiles) {
        Suggestion sugPOJO = getSugPojo(magaznie);
        if (sugPOJO != null) {
          for (Image img : getFirstSmallImage(
              Statistics.appPath.replaceAll("\\\\", "/") + magaznie.getDir_path(),
              numOfPageEachMag)) {
            ret.put(sugPOJO, img);
          }
        }
      }
    }
    if (imageSize == ImageSize.Medium) {
      for (Magazine_tblTable magaznie : pdFiles) {
        Suggestion sugPOJO = getSugPojo(magaznie);
        if (sugPOJO != null) {
          for (Image img : getFirstMediumemage(
              Statistics.appPath.replaceAll("\\\\", "/") + magaznie.getDir_path(),
              numOfPageEachMag)) {
            ret.put(sugPOJO, img);
          }
        }
      }
    }
    if (imageSize == ImageSize.Larg) {
      for (Magazine_tblTable magaznie : pdFiles) {
        Suggestion sugPOJO = getSugPojo(magaznie);
        if (sugPOJO != null) {
          for (Image img : getFirstLargeImage(
              Statistics.appPath.replaceAll("\\\\", "/") + magaznie.getDir_path(),
              numOfPageEachMag)) {
            ret.put(sugPOJO, img);
          }
        }

      }
    }


    return ret;

  }


  public ArrayList<Suggestion> getLastMagazines(int numOfMag, ImageSize imageSize) {
    ArrayList<Suggestion> ret = new ArrayList<Suggestion>();
    ArrayList<MagazineTblHiberEntity> mags;

    //            mags= DBFacade.getInstance().getLastMagazines(numOfMag,true);
    mags = HiberDBFacad.getInstance().getLastMagazinesHiber(numOfMag, true);


    if (imageSize == ImageSize.Small) {
      for (MagazineTblHiberEntity magaznie : mags) {
        Suggestion sugPOJO = getSugPojo(magaznie);
        if (sugPOJO != null) {
          ret.add(sugPOJO);
        }

      }
    } else if (imageSize == ImageSize.Medium) {
      for (MagazineTblHiberEntity magaznie : mags) {

        Suggestion sugPOJO = getSugPojo(magaznie);
        if (sugPOJO != null) {
          ret.add(sugPOJO);
        }
      }
    } else if (imageSize == ImageSize.Larg) {
      for (MagazineTblHiberEntity magaznie : mags) {
        Suggestion sugPOJO = getSugPojo(magaznie);
        if (sugPOJO != null) {
          ret.add(sugPOJO);
        }

      }
    }

    return ret;
  }

  public HashMap<String, ArrayList<Suggestion>> getSugMagazines(int limit) throws SQLException {
    HashMap<String, ArrayList<Suggestion>> ret = new HashMap<String, ArrayList<Suggestion>>();
    //        ArrayList<String> sugcats=DBFacade.getInstance().getSugCats();
    ArrayList<String> sugcats = HiberDBFacad.getInstance().getSugCatsByHibernate();
    for (String cat : sugcats) {
      ret.put(cat, getMagInCat(cat, limit));
    }
    return ret;

  }

  public HashMap<String, ArrayList<Suggestion>> getLastMagazinesInCats(String[] cats, int limit) throws SQLException {
    HashMap<String, ArrayList<Suggestion>> ret = new HashMap<String, ArrayList<Suggestion>>();
    for (String cat : cats) {
      if (cat != null && cat.length() > 0) {
        ret.put(cat, getMagInCat(cat, (limit / cats.length) + 1));
      }
    }
    return ret;

  }

  public ArrayList<Suggestion> getMagInCat(String cat, int limit) throws SQLException {
    ArrayList<Suggestion> pojos = new ArrayList<Suggestion>();

    //        ArrayList<Magazine_tblTable> magz=DBFacade.getInstance().getMagazineInCats(cat, limit,true );
    ArrayList<Magazine_tblTable> magz = HiberDBFacad.getInstance().getMagazineInCats(cat, limit, true);
    if (magz != null) {
      for (Magazine_tblTable magaznie : magz) {
        Suggestion sugPOJO = getSugPojo(magaznie);
        if (sugPOJO != null) {
          pojos.add(sugPOJO);
        }
      }


    }
    return pojos;

  }


  public HashMap<Suggestion, Image> getFirstPagesOfLastMagazines(int numOfMag) {
    ArrayList<Magazine_tblTable> pdFiles = new ArrayList<Magazine_tblTable>();
    //        try {
    pdFiles = HiberDBFacad.getInstance().getLastMagazines(numOfMag, true);
    //        } catch (SQLException e) {
    //            log.error("Exception in: ", e);
    //        }
    HashMap<Suggestion, Image> ret = new HashMap<Suggestion, Image>();

    for (Magazine_tblTable magaznie : pdFiles) {
      Suggestion sugPOJO = getSugPojo(magaznie);
      if (sugPOJO != null) {
        for (Image img : getFirstLargeImage(
            Statistics.appPath.replaceAll("\\\\", "/") + magaznie.getDir_path(),
            1)) {
          ret.put(sugPOJO, img);
        }
      }
    }
    return ret;
  }

  private Suggestion getSugPojo(Magazine_tblTable magaznie) {
     /*   String[] nameAndExt=getNameAndExt(magaznie.getMagazine_id(), "sg_1");
        if(nameAndExt==null)
            return null;
        SugPOJO sugPOJO=new SugPOJO();
        sugPOJO.setCat(magaznie.getCategory());
        sugPOJO.setPub_id(magaznie.getUser_id());
        sugPOJO.setTitle(magaznie.getTitle());
        sugPOJO.setPublishDate(magaznie.getIssue_appear());
        sugPOJO.setMag_id(magaznie.getMagazine_id());
        sugPOJO.setIssue_num(magaznie.getIssue_num());
        sugPOJO.setMaxPageNumber(Statistics.getMaxPageNumber(magaznie.getDir_path()));
        sugPOJO.setDownloadCount(magaznie.getDlcount());
        sugPOJO.setFavorCount(magaznie.getFavorcount());
        sugPOJO.setLanguage(magaznie.getLanguage());

        sugPOJO.setFree(Math.abs(magaznie.getPrice()) <= 0);

        sugPOJO.nameAndExt=nameAndExt;*/
    //log.error("go to converter ");
    return getSugPojo(ReportingFacad.convertMagazine_tblTableToMagazineTblHiberEntity(magaznie));
  }

  public static String[] getNameAndExt(int magId, String name) {
    String pathOfImg = ImageFacade.getInstance().getImageAddress(magId, name);
    String[] ret = null;
    if (pathOfImg != null) {
      int lind = pathOfImg.lastIndexOf("/");
      if (lind > 0) {
        pathOfImg = pathOfImg.substring(lind + 1);
        lind = pathOfImg.indexOf(".");
        ret = new String[]{pathOfImg.substring(0, lind), pathOfImg.substring(lind + 1)};

      }
    }
    return ret;
  }

  public static String[] getNameAndExt(MagazineTblHiberEntity magazineTblHiberEntity, String name) {
    String pathOfImg = ImageFacade.getInstance().getImageAddress(magazineTblHiberEntity, name);
    String[] ret = null;
    if (pathOfImg != null) {
      int lind = pathOfImg.lastIndexOf("/");
      if (lind > 0) {
        pathOfImg = pathOfImg.substring(lind + 1);
        lind = pathOfImg.indexOf(".");
        ret = new String[]{pathOfImg.substring(0, lind), pathOfImg.substring(lind + 1)};

      }
    }
    return ret;
  }

  public static Suggestion getSugPojo(MagazineTblHiberEntity magazineTblHiberEntity) {
    String[] nameAndExt = getNameAndExt(magazineTblHiberEntity, "sg_1");
    if (nameAndExt == null) {
      return null;
    }

    Suggestion sugPOJO = new Suggestion();
    sugPOJO.setCat(magazineTblHiberEntity.getCategory());
    sugPOJO.setDescription(magazineTblHiberEntity.getDescription());
    sugPOJO.setMagazineType(magazineTblHiberEntity.getMagType());
    sugPOJO.setPub_id(magazineTblHiberEntity.getUserId());
    sugPOJO.setTitle(magazineTblHiberEntity.getTitle());
    try {
      sugPOJO.setPublishDate(Statistics.nonFullDateFormat.parse(magazineTblHiberEntity.getIssueAppear()));
    } catch (ParseException e) {
      log.error("Exception in: ", e);
    }
    sugPOJO.setMag_id(magazineTblHiberEntity.getMagazineId());
    sugPOJO.setIssue_num(magazineTblHiberEntity.getIssueNum());
    sugPOJO.setMaxPageNumber(Statistics.getMaxPageNumber(magazineTblHiberEntity.getDirPath()));
    sugPOJO.setDownloadCount(magazineTblHiberEntity.getDlcount());
    sugPOJO.setFavorCount(magazineTblHiberEntity.getFavorcount());
    sugPOJO.setLanguage(magazineTblHiberEntity.getLang());
    sugPOJO.setBookmarkCount(magazineTblHiberEntity.getSumOfBookmark());
    sugPOJO.setFree(Math.abs(magazineTblHiberEntity.getPrice()) <= 0);
    sugPOJO.setNew(Statistics.isIssueAppearNew(magazineTblHiberEntity.getIssueAppear()));
    sugPOJO.nameAndExt = nameAndExt;

    return sugPOJO;
  }

  private ArrayList<Image> getFirstMediumemage(String dir, int nImage) {
    return getImage((new File(dir)), "m", ".png", ".jpg", 1, nImage);
  }


  public HashMap<Magazine_tblTable, ArrayList<Image>> getLastLargSuggestion(int numOfMag, int numOfPageEachMAg) {
    ArrayList<Magazine_tblTable> pdFiles = new ArrayList<Magazine_tblTable>();
    //        try {
    pdFiles = HiberDBFacad.getInstance().getLastMagazines(numOfMag, true);
    //        } catch (SQLException e) {
    //            log.error("Exception in: ", e);
    //        }
    HashMap<Magazine_tblTable, ArrayList<Image>> ret = new HashMap<Magazine_tblTable, ArrayList<Image>>();
    for (Magazine_tblTable magaznie : pdFiles) {
      ret.put(
          magaznie, getFirstLargeImage(
              Statistics.appPath.replaceAll("\\\\", "/") + magaznie.getDir_path(),
              numOfPageEachMAg));
    }

    return ret;

  }

  private ArrayList<Image> getFirstLargeImage(String dir, int nImage) {
    return getImage((new File(dir)), "l", ".png", ".jpg", 1, nImage);
  }

  public ArrayList<String> getListOfImageAddress(MagazineTblHiberEntity mag, String name) {
    ArrayList<String> res = new ArrayList<String>();

    File dir = new File(Statistics.appPath + mag.getDirPath());
    if (dir.getName().toLowerCase().endsWith(".pdf")) {
      dir = dir.getParentFile();
    }
    ArrayList<File> allfiless = new ArrayList<File>();
    File[] listOfff = dir.listFiles();
    if (listOfff != null) {
      Collections.addAll(allfiless, listOfff);
    }
    while (!allfiless.isEmpty()) {
      File f = allfiless.remove(0);
      if (f.isDirectory()) {
        File[] listof = f.listFiles();
        if (listof != null) {
          Collections.addAll(allfiless, listof);
        }
        continue;
      }

      if (f.getName().toLowerCase().startsWith(name) && (!f.getName().toLowerCase().endsWith("pdf"))) {
        String toSearch = Statistics.getApplicationPath();
        if (!toSearch.endsWith("/")) {
          toSearch = toSearch + "/";
        }
        String path = f.getPath();
        int indx = path.indexOf(toSearch);
        if (indx > -1) {
          path = path.substring(indx + toSearch.length() - 1);
        }
        res.add(path);
      }
    }
    Collections.sort(res);
    ArrayList<String> resTemp = new ArrayList<String>();

    for (String add : res) {
      if (add.indexOf("PDFs") > 1) {
        add = add.substring(add.indexOf("PDFs") - 1);

      }

      resTemp.add(add.replaceAll("\\\\", "/"));
    }
    return resTemp;
  }

  public ArrayList<String> getListOfImageAddress(int mag_id, String name) {
    ArrayList<String> res = new ArrayList<String>();
    MagazineTblHiberEntity mag = HiberDBFacad.getInstance().getMagazineById(mag_id);
    return getListOfImageAddress(mag, name);
  }

  public String getImageAddress(int mag_id, String name) {
    MagazineTblHiberEntity mag = HiberDBFacad.getInstance().getMagazineById(mag_id);
    if (mag == null) {
      return null;
    }
    return getImageAddress(mag, name);
  }

  public String getImageAddress(MagazineTblHiberEntity mag, String name) {
    if (mag == null) {
      return null;
    }
    String ret = null;
    File dir = new File(Statistics.appPath + mag.getDirPath());
    if (dir.getName().toLowerCase().endsWith(".pdf")) {
      dir = dir.getParentFile();
    }
    ArrayList<File> allfiless = new ArrayList<File>();
    File[] listOfff = dir.listFiles();
    if (listOfff != null) {
      Collections.addAll(allfiless, listOfff);
    }
    while (!allfiless.isEmpty()) {
      File f = allfiless.remove(0);
      if (f.isDirectory()) {
        File[] listof = f.listFiles();
        if (listof != null) {
          Collections.addAll(allfiless, listof);
        }
        continue;
      }

      if (f.getName().toLowerCase().startsWith(name) && !f.getName().toLowerCase().endsWith(".pdf")) {
        String toSearch = Statistics.getApplicationPath();
        if (!toSearch.endsWith("/")) {
          toSearch = toSearch + "/";
        }
        String path = f.getPath();
        int indx = path.indexOf(toSearch);
        if (indx > -1) {
          path = path.substring(indx + toSearch.length() - 1);
        }
        ret = path;
        break;
      }
    }
    if (ret != null) {

      if (ret.indexOf("PDFs") > 1) {
        ret = ret.substring(ret.indexOf("PDFs") - 1);

      }

      ret = ret.replaceAll("\\\\", "/");
    }

    return ret;
  }

  //    public HashMap getListOfImageAddresshashhhhhh(int mag_id,String name) {
  //        ArrayList<String> res = new ArrayList<String>();
  //        MagazineTblHiberEntity mag= HiberDBFacad.getInstance().getMagazineById(mag_id);
  //        HashMap hitCount=HiberDBFacad.getInstance().getEachPageViewPercent(mag_id);
  //        File dir=new File(Statistics.appPath+mag.getDirPath());
  //        if (dir.getName().toLowerCase().endsWith(".pdf")) {
  //            dir = dir.getParentFile();
  //        }
  //        ArrayList<File> allfiless=new ArrayList<File>();
  //        File[] listOfff=dir.listFiles();
  //        if(listOfff!=null)
  //            Collections.addAll(allfiless,listOfff );
  //        while (!allfiless.isEmpty()) {
  //            File f = allfiless.remove(0);
  //            if (f.isDirectory()) {
  //                File[] listof=f.listFiles();
  //                if(listof!=null)
  //                    Collections.addAll(allfiless, listof);
  //                continue;
  //            }
  //
  //            if (f.getName().toLowerCase().startsWith(name)) {
  //                String toSearch= Statistics.getApplicationPath();
  //                if(!toSearch.endsWith("/"))
  //                    toSearch=toSearch+"/";
  //                String path=f.getPath();
  //                int indx=path.indexOf(toSearch);
  //                if(indx>-1){
  //                    path=path.substring(indx+toSearch.length()-1);
  //                }
  //                res.add(path);
  //            }
  //        }
  //        Collections.sort(res);
  //        HashMap resTemp = new HashMap();
  //
  //        for(String add:res){
  //            if(add.indexOf("PDFs")>1){
  //                add=add.substring(add.indexOf("PDFs")-1);
  //
  //            }
  //            int index=add.lastIndexOf('-');
  //            int index2=add.lastIndexOf('.');
  //            int count=0;
  //            if(index>-1 && index2>-1){
  //                String pageNum=add.substring(index+1,index2-index-1);
  //                int page=Integer.valueOf(pageNum);
  //
  //                if(hitCount.containsKey(page)){
  //                    if(hitCount.get(page)!=null)
  //                        count=Integer.valueOf(hitCount.get(page).toString());
  //                }
  //            }
  //            resTemp.put(add.replaceAll("\\\\","/"),0);
  //        }
  //        return  resTemp;
  //    }
  public Pair<Image, String> getImagefor(int pub_idNum, int mag_idNum, String imageName) {

    Pair<Image, String> ret = null;
    ArrayList<Magazine_tblTable> magazines_tbl;
    //        try {
    //            magazines_tbl= DBFacade.getInstance().getMagazineByUserId(pub_idNum, mag_idNum,true);
    magazines_tbl = HiberDBFacad.getInstance().getMagazineByUserId(pub_idNum, mag_idNum, true);
    //        } catch (SQLException e) {
    //            log.error("Exception in: ", e);
    //            return null;
    //        }
    for (Magazine_tblTable magazine_tblTable : magazines_tbl) {
      ret = getImage(new File(Statistics.appPath.replaceAll("\\\\", "/") + magazine_tblTable.getDir_path()), imageName);
      break;
    }
    return ret;
  }

  public Pair<File, String> getFileOfMagazineFor(int pub_idNum, int mag_idNum, String imageName) {

    Pair<File, String> ret = null;
    ArrayList<Magazine_tblTable> magazines_tbl;
    //        try {
    //            magazines_tbl= DBFacade.getInstance().getMagazineByUserId(pub_idNum, mag_idNum,true);
    magazines_tbl = HiberDBFacad.getInstance().getMagazineByUserId(pub_idNum, mag_idNum, true);
    //        } catch (SQLException e) {
    //            log.error("Exception in: ", e);
    //            return null;
    //        }
    for (Magazine_tblTable magazine_tblTable : magazines_tbl) {
      ret = getFileEndWith(
          new File(Statistics.appPath.replaceAll("\\\\", "/") + magazine_tblTable.getDir_path()),
          imageName);
      break;
    }
    return ret;
  }

  public Pair<File, String> getFileFor(int pub_idNum, int mag_idNum, String imageName) {

    Pair<File, String> ret = null;
    ArrayList<Magazine_tblTable> magazines_tbl;
    //        try {
    //            magazines_tbl= DBFacade.getInstance().getMagazineByUserId(pub_idNum, mag_idNum,true);
    magazines_tbl = HiberDBFacad.getInstance().getMagazineByUserId(pub_idNum, mag_idNum, true);
    //        } catch (SQLException e) {
    //            log.error("Exception in: ", e);
    //            return null;
    //        }
    for (Magazine_tblTable magazine_tblTable : magazines_tbl) {
      ret = getFileEndWith(
          new File(Statistics.appPath.replaceAll("\\\\", "/") + magazine_tblTable.getDir_path()),
          imageName);
      break;
    }
    return ret;
  }

  /**
   * @param mag_idNum
   * @param pageNumber
   * @param imageSize
   * @param ifIsFree
   * @return if only premium magazine find and ifIsFree is true, return new Pair<>(null,null); else if cannot find return null and if find return image pair
   */
  public static Pair<Image, String> getImagefor(int mag_idNum, int pageNumber, ImageSize imageSize, boolean ifIsFree) {

    Pair<Image, String> ret = null;
    ArrayList<Magazine_tblTable> magazines_tbl;
    //        try {
    //            magazines_tbl= DBFacade.getInstance().getMagazineByUserId(-1, mag_idNum,true);
    magazines_tbl = HiberDBFacad.getInstance().getMagazineByUserId(-1, mag_idNum, true);

    //        } catch (SQLException e) {
    //            log.error("Exception in: ", e);
    //            return null;
    //        }
    String startBye = imageSize == ImageSize.Small ? "s" : imageSize == ImageSize.Medium ? "m" : "l";
    for (Magazine_tblTable magazine_tblTable : magazines_tbl) {
      if (ifIsFree && magazine_tblTable.getPrice() > 0) {
        ret = new Pair<>(null, null);
        log.debug(" ret=new Pair<>(null,null);");
        continue;
      }
      ret = getImage(
          new File(Statistics.appPath.replaceAll("\\\\", "/") + magazine_tblTable.getDir_path()),
          startBye,
          pageNumber);
      break;
    }
    return ret;
  }

  public ArrayList<Suggestion> getLastIssues(int mag_id, int num) throws SQLException {
    ArrayList<Suggestion> ret = new ArrayList<Suggestion>();
    ArrayList<MagazineTblHiberEntity> magazines_tbl;
    //        magazines_tbl=DBFacade.getInstance().getIssues(mag_id,num,true);
    magazines_tbl = HiberDBFacad.getInstance().getIssues1(mag_id, num, true);
    if (magazines_tbl != null) {
      for (MagazineTblHiberEntity magazine : magazines_tbl) {
        Suggestion sujp = getSugPojo(magazine);
        if (sujp != null) {
          ret.add(sujp);
        }
      }
    }
    return ret;
  }

  public static Pair<File, String> fileAndTypeFor(
      final int mg_idNum,
      final int pageNum,
      final ImageSize imageSize,
      final boolean checkMagazineShouldBeFree) {

    Pair<File, String> ret = null;
    ArrayList<Magazine_tblTable> magazines_tbl;
    //        try {
    //            magazines_tbl= DBFacade.getInstance().getMagazineByUserId(-1, mag_idNum,true);
    magazines_tbl = HiberDBFacad.getInstance().getMagazineByUserId(-1, mg_idNum, true);

    //        } catch (SQLException e) {
    //            log.error("Exception in: ", e);
    //            return null;
    //        }

    for (Magazine_tblTable magazine_tblTable : magazines_tbl) {
      if (checkMagazineShouldBeFree && magazine_tblTable.getPrice() > 0) {
        ret = new Pair<>(null, null);
        log.debug(" ret=new Pair<>(null,null);");
        continue;
      }
      ret = fileOfMagazine(
          new File(Statistics.appPath.replaceAll("\\\\", "/") + magazine_tblTable.getDir_path()),
          imageSize,
          pageNum);
      break;
    }
    return ret;
  }

  private static Pair<File, String> fileOfMagazine(File dir, final ImageSize imageSize, final int pageNum) {

    ArrayList<File> allfiless = new ArrayList<File>();
    if (dir == null) {
      return null;
    }
    if (dir.getName().toLowerCase().endsWith(".pdf")) {
      dir = dir.getParentFile();
    }
    String[] dirlist = dir.list();
    if (dirlist != null && dirlist.length > 0) {
      Collections.addAll(allfiless, dir.listFiles());
    }
    while (!allfiless.isEmpty()) {
      File f = allfiless.remove(0);
      if (f.isDirectory()) {
        File[] ldd = f.listFiles();
        if (ldd != null && ldd.length > 0) {
          Collections.addAll(allfiless, f.listFiles());
        }
        continue;
      }
      final String fileName = f.getName().toLowerCase();
      switch (imageSize) {
        case Larg: {
          if (fileName.toLowerCase().startsWith("l") && fileName.endsWith("_" + pageNum + ".pdf")) {
            return new Pair<>(f, "pdf");
          }
          break;
        }
        case Medium:
        case Small: {
          if ((fileName.startsWith("m") || fileName.startsWith("s")) &&
              (fileName.endsWith("_" + pageNum + ".jpg") || fileName.endsWith("_" + pageNum + ".png"))) {
            boolean isJpg = fileName.endsWith("_" + pageNum + ".jpg");
            return new Pair<>(f, isJpg ? "jpg" : "png");
          }
        }
      }
    }

    return null;
  }

  public static enum ImageSize {
    Small, Medium, Larg
  }
}
