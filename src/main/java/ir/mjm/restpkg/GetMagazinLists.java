package ir.mjm.restpkg;

import com.google.gson.Gson;
import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.ImageFacade;
import ir.mjm.DBAO.Magazine_tblTable;
import ir.mjm.DBAO.Statistics;
import ir.mjm.DBAO.UserImageDLAction;
import ir.mjm.DBAO.hiber.BannersTblHiberEntity;
import ir.mjm.DBAO.hiber.CategoriesTblHiberEntity;
import ir.mjm.DBAO.hiber.IssuepagerequestHiberEntity;
import ir.mjm.DBAO.hiber.MobileuserTblHiberEntity;
import ir.mjm.restBeans.Banner;
import ir.mjm.restBeans.Category;
import ir.mjm.restBeans.Suggestion;
import ir.mjm.util.Pair;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.server.ContainerRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;

/**
 * Created by Serap on 5/29/14.
 */
//@ApplicationPath("rest")
@Path("magazine")
public class GetMagazinLists extends MobileUserAuth {
  static final Logger log = Logger.getLogger(GetMagazinLists.class);



  // The Java method will process HTTP GET requests
  @GET
  // The Java method will produce content identified by the MIME Media type "text/plain"
  @Path("catImage/{id}")
  @Produces("application/json")
  public Response catImage(@PathParam("id") String id) {
    int user_id = isLogrdInGetMags(request, req);
    if (user_id < 0) {
      return Response.ok(
          "Please Login by API " + uriInfo.getAbsolutePath()
                                          .toString()
                                          .substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
          "userman/login?user= &pass=  then set header WWW-Authenticate value").build();
    }
    try {
      int catId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      return Response.ok("NOK: Category id should be a number.").build();
    }

    final CategoriesTblHiberEntity categoriesTblHiberEntity = HiberDBFacad.getInstance().category(id);
    if (categoriesTblHiberEntity == null) {
      return Response.ok("NOK: Category id not found.").build();
    }
    final File catFile = new File(
        Statistics.suggestionImageUrl(categoriesTblHiberEntity));
    StreamingOutput fileStream = new StreamingOutput() {
      @Override
      public void write(java.io.OutputStream output) throws IOException, WebApplicationException {

        try {
          byte[] data = Files.readAllBytes(
              catFile.toPath());
          output.write(data);
          output.flush();
        } catch (Exception e) {
          throw new WebApplicationException("File Not Found !!");
        }
      }
    };

    CacheControl cachCtrl = new CacheControl();
    cachCtrl.setPrivate(false);
    cachCtrl.setMaxAge(36000000);

    String mt = new MimetypesFileTypeMap().getContentType(catFile);
    return Response
        .ok(fileStream, mt)
        .header(
            "content-disposition",
            "attachment; filename = " + catFile.getName()).build();
  }


  @GET
  // The Java method will produce content identified by the MIME Media type "text/plain"
  @Path("getCats")
  @Produces("application/json")
  public String getCats() {
    int user_id = isLogrdInGetMags(request, req);
    if (user_id < 0) {
      return "Please Login by API " + uriInfo.getAbsolutePath()
                                             .toString()
                                             .substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
             "userman/login?user= &pass=  then set header WWW-Authenticate value";
    }

    // Return some cliched textual content
    //        log.debug("in getMagazin");
    List<Category> categories = null;
    try {
      categories = HiberDBFacad.getInstance().getAllCategories(uriInfo.getAbsolutePath() + "/" + "getmags");
      //            categories=DBFacade.getInstance().getAllCategories();
      if (categories.size() <= 0) {
        return "Can not found Categories.";
      }

    } catch (Exception e) {
      log.error("Exception in user_id: " + user_id, e);
      return "Can't not found Categories.";
    }

    return new Gson().toJson(categories);
  }

  @GET
  @Path("getCats/getmags/{id}")
  @Produces("application/json")
  public String getMags(@PathParam(value = "id") String magId) {
    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      return "NOK: Please Charge your Account.";
    }
    if (user_id < 0) {
      return "Please Login by API " + uriInfo.getAbsolutePath()
                                             .toString()
                                             .substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
             "userman/login?user= &pass=  then set header WWW-Authenticate value";
    }

    StringBuilder stringBuilder = new StringBuilder();
    int id = -1;
    if (magId.length() <= 3) {
      return "ID " + magId + " IS NOT VALID";
    }
    id = Integer.parseInt(magId.substring(3));
    if (id < 0) {
      return "ID " + magId + " IS NOT VALID";
    }
    Magazine_tblTable[] magazinelst = null;
    try {
      magazinelst = HiberDBFacad.getInstance().getMagazinesTitlteInCategory(id, true);
    } catch (Exception e) {
      log.error("Exception in user_id: " + user_id, e);
    }
    if (magazinelst == null || magazinelst.length <= 0) {
      return "Magazine Not Found.";
    }

    stringBuilder.append("{ ");
    String path = uriInfo.getAbsolutePath().toString();
    path = path.substring(0, path.indexOf("getCats/getmags/id=")) + "getImage";
    for (Magazine_tblTable mgt : magazinelst) {
      stringBuilder.append(" [ ");
      stringBuilder.append("Publisher: \"" + mgt.getPublisher_name() + "\" , ");
      stringBuilder.append("Title: \"" + mgt.getTitle() + "\" , ");
      stringBuilder.append("URL: \"" + path + "/imgid=" + mgt.getMagazine_id() + "\" ");
      stringBuilder.append(" ] , ");


    }
    stringBuilder.replace(stringBuilder.lastIndexOf(","), stringBuilder.lastIndexOf(",") + 1, "");

    stringBuilder.append("}");

    return stringBuilder.toString();
  }

  @GET
  @Path("getImage/{imgid}")
  @Produces("image/*")
  public Response getImage(@PathParam(value = "imgid") String imageId) {

    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      return Response.ok("NOK: Please Charge your Account.").build();
    }
    if (user_id < 0) {
      return Response.ok(
          "Please Login by API " +
          uriInfo.getAbsolutePath().toString().substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
          "userman/login?user= &pass=  then set header WWW-Authenticate value", MediaType.TEXT_PLAIN_TYPE).build();
    }

    int id = -1;

    try {
      id = Integer.parseInt(imageId.trim());
    } catch (Exception e) {
      log.error(e);
      return Response.ok("NOK: Please fill correct imgid.").type(MediaType.TEXT_PLAIN_TYPE).build();

    }
    if (id < 0) {
      return Response.noContent().build();
    }
    Pair<Image, String> image = null;
    try {
      image = ImageFacade.getInstance().getFirstImage(id);
    } catch (Exception e) {
      log.error("Exception in user_id: " + user_id, e);
      return Response.noContent().build();


    }
    //        Image image = null;

    if (image != null) {

      // resize the image to fit the GUI's image frame

      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      try {
        ImageIO.write((BufferedImage) image.getKey(), image.getValue(), out);

        final byte[] imgData = out.toByteArray();

        final InputStream bigInputStream =
            new ByteArrayInputStream(imgData);
        CacheControl cachCtrl = new CacheControl();
        cachCtrl.setPrivate(false);
        cachCtrl.setMaxAge(36000000);
        return Response.ok(bigInputStream).
            cacheControl(cachCtrl).type("image/" + image.getValue()).build();
      } catch (final IOException e) {
        return Response.noContent().build();
      }
    }

    return Response.ok("NOK: Can not find image of magazine.").type(MediaType.TEXT_PLAIN_TYPE).build();

  }

  //    @Produces({"application/json","image/png","image/png","image/png","image/png",
  //            "application/json","image/png","image/png","image/png","image/png",
  //            "application/json","image/png","image/png","image/png","image/png",
  //            "application/json","image/png","image/png","image/png","image/png",
  //    })

  @GET
  @Path("getsuggestion")
  @Produces("multipart/mixed")
  public MultiPart getSug() {
    MultiPart entity = new MultiPart();
    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      BodyPart part1 = new BodyPart();

      part1.setMediaType(MediaType.TEXT_PLAIN_TYPE);

      part1.setEntity("NOK: Please Charge your Account.");
      entity.bodyPart(part1);
      return entity;
    }
    if (user_id < 0) {
      BodyPart part1 = new BodyPart();

      part1.setMediaType(MediaType.TEXT_PLAIN_TYPE);

      part1.setEntity(
          "Please Login by API " +
          uriInfo.getAbsolutePath().toString().substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
          "userman/login?user= &pass=  then set header WWW-Authenticate value");
      entity.bodyPart(part1);
      return entity;
    }
    String path = uriInfo.getAbsolutePath().toString();

    HashMap<Suggestion, Image> imgs = ImageFacade.getInstance().getLastSuggestion(3, 4, ImageFacade.ImageSize.Small);


    if (imgs.size() < 0) {
      BodyPart part1 = new BodyPart();

      part1.setMediaType(MediaType.TEXT_PLAIN_TYPE);

      part1.setEntity("There Are No Magazine.Please First Upload Magazine.");
      entity.bodyPart(part1);
      return entity;
    }


    for (Map.Entry<Suggestion, Image> mag : imgs.entrySet()) {
      BodyPart part1 = new BodyPart();
      //         part1.getHeaders().add("Content-Disposition", "form-data; name=\"pics\"; filename=\"file1.txt\"");

      part1.setMediaType(MediaType.APPLICATION_JSON_TYPE);
      Gson gson = new Gson();
      part1.setEntity(gson.toJson(mag.getKey()));

      BodyPart part2 = new BodyPart();
      part2.setMediaType(MediaType.valueOf("image/" + mag.getKey().nameAndExt[1] + ";"));
      //         part1.getHeaders().add("Content-Type", "image/png;");

      //         part1.getHeaders().add("Content-Disposition", "form-data; name=\"field1\"");
      part2.setEntity(mag.getValue());

      entity.bodyPart(part1);
      entity.bodyPart(part2);

    }
    return entity;


  }

  @GET
  @Path("getfirst/{num}")
  @Produces("multipart/mixed")
  public MultiPart getFirst(@PathParam(value = "num") String number) {

    MultiPart entity = new MultiPart();
    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      BodyPart part1 = new BodyPart();

      part1.setMediaType(MediaType.TEXT_PLAIN_TYPE);

      part1.setEntity("NOK: Please Charge your Account.");
      entity.bodyPart(part1);
      return entity;
    }
    if (user_id < 0) {
      BodyPart part1 = new BodyPart();

      part1.setMediaType(MediaType.TEXT_PLAIN_TYPE);

      part1.setEntity(
          "Please Login by API " +
          uriInfo.getAbsolutePath().toString().substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
          "userman/login?user= &pass=  then set header WWW-Authenticate value");
      entity.bodyPart(part1);
      return entity;
    }
    int num;

    //        log.debug("in getfirst");
    try {
      num = Integer.parseInt(number.substring(4));
      //            log.debug("in getfirst pars int "+num);
    } catch (Exception e) {
      BodyPart part1 = new BodyPart();

      part1.setMediaType(MediaType.TEXT_PLAIN_TYPE);

      part1.setEntity("num parameter Format Not Correct ");
      entity.bodyPart(part1);
      return entity;


    }
    String path = uriInfo.getAbsolutePath().toString();
    //        log.debug("go to      HashMap<SugPOJO, Image> imgs=ImageFacade.getInstance().getLastFirstPage(num);\n");
    HashMap<Suggestion, Image> imgs;
    try {
      imgs = ImageFacade.getInstance().getFirstPagesOfLastMagazines(num);
    } catch (Exception e) {
      log.error("Exception in user_id: " + user_id, e);
      BodyPart part1 = new BodyPart();

      part1.setMediaType(MediaType.TEXT_PLAIN_TYPE);

      part1.setEntity("Server Error " + e.getCause());
      entity.bodyPart(part1);
      return entity;
    }
    //        log.debug(" HashMap<SugPOJO, Image> imgs=ImageFacade.getInstance().getLastFirstPage(num); return "+imgs);


    if (imgs.size() < 0) {

      BodyPart part1 = new BodyPart();

      part1.setMediaType(MediaType.TEXT_PLAIN_TYPE);

      part1.setEntity("There Are No Magazine.Please First Upload Magazine.");
      entity.bodyPart(part1);
      //            log.debug("            part1.setEntity(\"There Are No Magazine.Please First Upload Magazine.\");\n added");
      return entity;
    }


    for (Map.Entry<Suggestion, Image> mag : imgs.entrySet()) {
      BodyPart part1 = new BodyPart();
      //         part1.getHeaders().add("Content-Disposition", "form-data; name=\"pics\"; filename=\"file1.txt\"");

      part1.setMediaType(MediaType.APPLICATION_JSON_TYPE);
      Gson gson = new Gson();
      part1.setEntity(gson.toJson(mag.getKey()));

      BodyPart part2 = new BodyPart();
      part2.setMediaType(MediaType.valueOf("image/" + mag.getKey().nameAndExt[1] + ";"));

      part2.setEntity(mag.getValue());

      entity.bodyPart(part1);
      entity.bodyPart(part2);

    }
    return entity;


  }


  @GET
  @Path("getlastmags/{num}")
  //    @RolesAllowed("pulisherrole")
  @Produces("application/json")
  public String getLastMags(
      @Context SecurityContext sc,
      @PathParam(value = "num") String number) {
    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      return "NOK: Please Charge your Account.";
    }
    if (user_id < 0) {
      return "Please Login by API " + uriInfo.getAbsolutePath()
                                             .toString()
                                             .substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
             "userman/login?user= &pass=  then set header WWW-Authenticate value";
    }

    String ret = "";
    int num;
    try {

      num = Integer.parseInt(number);
      //            log.debug("\n\n\n\n\n"+num);
    } catch (Exception e) {


      return "num parameter Format Not Correct ";


    }

    //        log.error(((ContainerRequest) request).getSecurityContext().getAuthenticationScheme());
    Map<String, Cookie> cookies = ((ContainerRequest) request).getCookies();
    //        for(Map.Entry<String, Cookie> cookiePair:cookies.entrySet())
    //            log.debug("User: "+ user_id +" has cookie: "+cookiePair.getKey()+" value: "+cookiePair.getValue());
    //        for(Map.Entry<String, List<String>> co: ((ContainerRequest)request).getRequestHeaders().entrySet()){
    //            log.debug("\t\t"+co.getKey());
    //
    //            for(String s:co.getValue()){
    //                log.debug(co.getKey()+" ===>> "+s);
    //
    //            }
    //        }
    try {
      Class.forName("org.glassfish.jersey.server.internal.process.SecurityContextInjectee");
    } catch (ClassNotFoundException e) {
      log.error("Exception in user_id: " + user_id, e);
    }
    //        try {
    Principal princ = sc.getUserPrincipal();
    log.debug("User Logged in and SecurityContext.getAuthenticationScheme(): " + sc.getAuthenticationScheme());
    //        } catch (ServletException e) {
    //            log.error("Exception in: ",e);
    //        }
    ArrayList<Suggestion> pojos = ImageFacade.getInstance().getLastMagazines(num, ImageFacade.ImageSize.Small);

    ArrayList<Suggestion> pojosret = new ArrayList<Suggestion>();
    if (pojos == null || pojos.size() <= 0) {
      return "Can not find new magazine";
    }
    String path = uriInfo.getAbsolutePath().toString();
    path = path.substring(0, path.indexOf("getlastmags/")) + "getImg";
    for (Suggestion sugPOJO : pojos) {
      //TODO Should change for image name
      sugPOJO.setUrl(
          path + "/" + sugPOJO.getPub_id() + "/" + sugPOJO.getMag_id() + "/" + "sg_1." + sugPOJO.nameAndExt[1]);

    }

    return (new Gson()).toJson(pojos);
  }

  @GET
  @Path("findmag")
  //    @RolesAllowed("pulisherrole")
  @Produces("application/json")
  public String findMagazine(
      @Context SecurityContext sc,
      @DefaultValue("") @QueryParam(value = "title") String title) {
    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      return "NOK: Please Charge your Account.";
    }
    if (user_id < 0) {
      return "Please Login by API " + uriInfo.getAbsolutePath()
                                             .toString()
                                             .substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
             "userman/login?user= &pass=  then set header WWW-Authenticate value";
    }

    String ret = "";
    int num;
    if (title.equalsIgnoreCase("")) {


      return "Fill title parameter";


    }


    ArrayList<Suggestion> pojos = HiberDBFacad.getInstance().searchMagazineByTitleOrKeywords(title, true);


    if (pojos == null || pojos.size() <= 0) {
      return "Can not find new magazine";
    }
    String path = uriInfo.getAbsolutePath().toString();
    path = path.substring(0, path.indexOf("findmag") - 1) + "/getImg";
    for (Suggestion sugPOJO : pojos) {
      //TODO Should change for image name
      String pathOfImg = ImageFacade.getInstance().getImageAddress(sugPOJO.getMag_id(), "sg_1");
      if (pathOfImg != null) {
        int lind = pathOfImg.lastIndexOf("/");
        if (lind > 0) {
          pathOfImg = pathOfImg.substring(lind + 1);
          sugPOJO.setUrl(path + "/" + sugPOJO.getPub_id() + "/" + sugPOJO.getMag_id() + "/" + pathOfImg);
        }
      }

    }

    return (new Gson()).toJson(pojos);
  }

  @GET
  @Path("getImg/{pub_id}/{mag_id}/{name}")
  @Produces("image/*")
  public Response getThisMagsImage(
      @PathParam(value = "pub_id") String pub_id,
      @PathParam(value = "mag_id") String msg_id,
      @PathParam(value = "name") String imageName
                                  ) {
    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      return Response.ok("NOK: Please Charge your Account.").build();
    }
    if (user_id < 0) {
      return Response.ok(
          "Please Login by API " +
          uriInfo.getAbsolutePath().toString().substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
          "userman/login?user= &pass=  then set header WWW-Authenticate value", MediaType.TEXT_PLAIN_TYPE).build();
    }

    int pub_idNum;
    int msg_idNum;
    try {
      pub_idNum = Integer.parseInt(pub_id);
      msg_idNum = Integer.parseInt(msg_id);
      //            log.debug("in getfirst pars int "+pub_id);
    } catch (Exception e) {


      return Response.ok("num parameter Format Not Correct ", MediaType.TEXT_PLAIN_TYPE).build();


    }

    Pair<File, String> pagesFiles = null;
    try {
      pagesFiles = ImageFacade.getInstance().getFileFor(pub_idNum, msg_idNum, imageName);
    } catch (Exception e) {
      log.error("Exception in user_id: " + user_id, e);
      return Response.noContent().build();


    }
    //        Image pagesFiles = null;

    if (pagesFiles != null) {

      // resize the pagesFiles to fit the GUI's pagesFiles frame


      final Pair<File, String> finalPagesFiles = pagesFiles;
      StreamingOutput fileStream = new StreamingOutput() {
        @Override
        public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
          try {
            byte[] data = Files.readAllBytes(finalPagesFiles.getKey().toPath());
            output.write(data);
            output.flush();
          } catch (Exception e) {
            throw new WebApplicationException("File Not Found !!");
          }
        }
      };

      CacheControl cachCtrl = new CacheControl();
      cachCtrl.setPrivate(false);
      cachCtrl.setMaxAge(36000000);

      String mt = new MimetypesFileTypeMap().getContentType(finalPagesFiles.getKey());
      return Response
          .ok(fileStream, mt)
          .header(
              "content-disposition",
              "attachment; filename = " + imageName + "." + finalPagesFiles.getValue()).build();

    }

    return Response.noContent().build();


  }


  @GET
  @Path("getCustImg/{mag_id}/{pageNumber}/{pageSize}")
  @Produces("application/*")
  public Response getLastMagsStream(
      @PathParam(value = "mag_id") String mag_id,
      @PathParam(value = "pageNumber") String pageNumber,
      @PathParam(value = "pageSize") String pageSize
                                   ) {

    MobileuserTblHiberEntity user_id = isLogrdInGetMags(req, request);
    if (user_id == null) {
      throw new WebApplicationException(
          "Please Login by API " +
          uriInfo.getAbsolutePath().toString().substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
          "userman/login?user= &pass=  then set header WWW-Authenticate value", Response.Status.FORBIDDEN);
    }
    //
    //              if((user_id.getChargeennd().compareTo(Statistics.FullDateFormat.format(new Date()))<0)&&pageSize.toLowerCase().contains("l"))
    //                return Response.ok("NOK: Please Charge your Account.").build();

    int mg_idNum;
    int pageNum;
    pageSize = pageSize.toLowerCase();

    try {
      mg_idNum = Integer.parseInt(mag_id);
      pageNum = Integer.parseInt(pageNumber);
      if (!(pageSize.equals("s") || pageSize.equals("m") || pageSize.equals("l"))) {
        throw new WebApplicationException("Last Parameter should be 's' OR 'm' OR 'l' ", Response.Status.BAD_REQUEST);
      }

      //            log.debug("in getfirst pars int "+mag_id);
    } catch (Exception e) {


      throw new WebApplicationException("num parameter Format Not Correct ", Response.Status.BAD_REQUEST);


    }
    log.debug(
        "((user_id.getChargeennd().compareTo(Statistics.FullDateFormat.format(new Date()))<0)&&pageSize.toLowerCase().contains(\"l\"))" +
        user_id.getChargeennd() + " compare to " + new Date() + " is " +
        ((user_id.getChargeennd().compareTo(Statistics.FullDateFormat.format(new Date())) < 0)));
    log.debug("pageSize.equals(\"l\") " + pageSize.equals("l"));
    Pair<File, String> fileOfPage = null;
    try {
      fileOfPage = ImageFacade.fileAndTypeFor(
          mg_idNum, pageNum,
          pageSize.equals("s") ? ImageFacade.ImageSize.Small :
              pageSize.equals("m") ? ImageFacade.ImageSize.Medium :
                  ImageFacade.ImageSize.Larg,
          ((user_id.getChargeennd().compareTo(Statistics.FullDateFormat.format(new Date())) < 0) &&
           pageSize.equals("l")));
    } catch (Exception e) {
      log.error("Exception in sending fileOfPage may be client abort Exception");
      log.error("Exception in user_id: " + user_id, e);
      throw new WebApplicationException("Server Error!!!", Response.Status.INTERNAL_SERVER_ERROR);


    }
    //        Image fileOfPage = null;

    if (fileOfPage != null) {
      if (fileOfPage.getKey() == null) {
        throw new WebApplicationException("NOK: Please Charge your Account.", Response.Status.FORBIDDEN);
      } else {

        if (pageSize.equals("l")) {
          IssuepagerequestHiberEntity issuepagerequestHiberEntity = new IssuepagerequestHiberEntity();
          issuepagerequestHiberEntity.setUserId(user_id.getUserId());
          issuepagerequestHiberEntity.setMagId(mg_idNum);
          issuepagerequestHiberEntity.setPage(pageNum);
          issuepagerequestHiberEntity.setRequestTime(Statistics.FullDateFormat.format(new Date()));

          UserImageDLAction.getInstance().addToActions(issuepagerequestHiberEntity);


        }

        final Pair<File, String> finalFileOfPage = fileOfPage;
        StreamingOutput fileStream = new StreamingOutput() {
          @Override
          public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
            try {
              byte[] data = Files.readAllBytes(finalFileOfPage.getKey().toPath());
              output.write(data);
              output.flush();
            } catch (Exception e) {
              throw new WebApplicationException("File Not Found !!");
            }
          }
        };

        CacheControl cachCtrl = new CacheControl();
        cachCtrl.setPrivate(false);
        cachCtrl.setMaxAge(36000000);

        String mt = new MimetypesFileTypeMap().getContentType(fileOfPage.getKey());
        return Response
            .ok(fileStream, mt)
            .header(
                "content-disposition",
                "attachment; filename = " + mag_id + "-" + pageNum + "." + fileOfPage.getValue()).build();
      }
    }

    throw new WebApplicationException(
        "Can't Find fileOfPage for mag_id:" + mag_id + ". please check mag_id and max page number",
        Response.Status.BAD_REQUEST);


  }

  @GET
  @Path("getsugs")
  //    @RolesAllowed("pulisherrole")
  @Produces("application/json")
  public String getLastMagsBaa(@DefaultValue("0") @QueryParam("num") int num) {
    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      return "NOK: Please Charge your Account.";
    }

    if (user_id < 0) {
      return "Please Login by API " + uriInfo.getAbsolutePath()
                                             .toString()
                                             .substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
             "userman/login?user= &pass=  then set header WWW-Authenticate value";
    }

    if (num <= 0) {
      return "please fill num query parameter.(getsugs?num=)";

    }

    HashMap<String, ArrayList<Suggestion>> pojos = null;
    try {
      pojos = ImageFacade.getInstance().getSugMagazines(num);

    } catch (SQLException e) {
      log.error("Exception in user_id: " + user_id, e);

      return "System Error.";
    }


    return (new Gson()).toJson(pojos);
  }


  @GET
  @Path("getthissug")
  @Produces("application/json")
  public String getThisIssue(
      @DefaultValue("") @QueryParam("cats") String cats,
      @DefaultValue("0") @QueryParam("num") int num) {


    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      return "NOK: NOK: Please Charge your Account.";
    }
    if (user_id < 0) {
      return "Please Login by API " + uriInfo.getAbsolutePath()
                                             .toString()
                                             .substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
             "userman/login?user= &pass=  then set header WWW-Authenticate value";
    }
    if (cats.equals("")) {
      return "Please Fill cats Parameter.";
    }

    if (num == 0) {
      return "Please Fill num Parameter.";
    }

    String[] catss = cats.split(",");
    HashMap<String, ArrayList<Suggestion>> pojos = null;
    try {
      pojos = ImageFacade.getInstance().getLastMagazinesInCats(catss, num);
    } catch (SQLException e) {
      log.error("Exception in user_id: " + user_id, e);

      return "System Error.";
    }


    return (new Gson()).toJson(pojos);

  }

  @GET
  @Path("banners")
  @Produces("application/json")
  public String banners() {
    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      return "NOK: NOK: Please Charge your Account.";
    }
    if (user_id < 0) {
      return "Please Login by API " + uriInfo.getAbsolutePath()
                                             .toString()
                                             .substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
             "userman/login?user= &pass=  then set header WWW-Authenticate value";
    }


    List<Banner> banners =new ArrayList<>();
    for(BannersTblHiberEntity bannersTblHiberEntity:HiberDBFacad.getInstance().banners()){
      banners.add(new Banner(bannersTblHiberEntity));
    }

    return (new Gson()).toJson(banners);

  }

  @GET
  @Path("banner/{id}")
  @Produces("application/*")
  public Response banner(@PathParam("id") int id) {
    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      return Response.ok("NOK: NOK: Please Charge your Account.").build();
    }
    if (user_id < 0) {
      return Response.ok(
          "Please Login by API " + uriInfo.getAbsolutePath()
                                          .toString()
                                          .substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
          "userman/login?user= &pass=  then set header WWW-Authenticate value").build();
    }


    BannersTblHiberEntity banner = HiberDBFacad.getInstance().banner(id);
    if (banner == null) {
      return Response.ok("NOK: can not find banner for id: " + id).build();
    }
    final File file = Statistics.bannerFile(banner);
    if (!file.exists()) {
      return Response.ok("NOK: can not find banner for id: " + id).build();
    }
    StreamingOutput fileStream = new StreamingOutput() {
      @Override
      public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
        try {
          byte[] data = Files.readAllBytes(file.toPath());
          output.write(data);
          output.flush();
        } catch (Exception e) {
          throw new WebApplicationException("File Not Found !!");
        }
      }
    };

    CacheControl cachCtrl = new CacheControl();
    cachCtrl.setPrivate(false);
    cachCtrl.setMaxAge(36000000);

    return Response
        .ok(fileStream, "application/octet-stream")
        .header(
            "content-disposition",
            "attachment; filename = " + id).build();
  }


  @GET
  @Path("getissues")
  @Produces("application/json")
  public String getLastIssues(
      @DefaultValue("-1") @QueryParam("mag_id") int mag_id,
      @DefaultValue("0") @QueryParam("num") int num) {
    int user_id = isLogrdInGetMags(request, req);
    if (user_id == -100) {
      return "NOK: Please Charge your Account.";
    }
    if (user_id < 0) {
      return "Please Login by API " + uriInfo.getAbsolutePath()
                                             .toString()
                                             .substring(0, uriInfo.getAbsolutePath().toString().indexOf("magazine/")) +
             "userman/login?user= &pass=  then set header WWW-Authenticate value";
    }
    if (mag_id == -1) {
      return "Please Fill mag_id Parameter.";
    }
    if (num == 0) {
      return "Please Fill num Parameter.";
    }
    ArrayList<Suggestion> pojos = null;
    try {
      pojos = ImageFacade.getInstance().getLastIssues(mag_id, num);
    } catch (SQLException e) {
      log.error("Exception in user_id: " + user_id, e);

      return "System Error.";
    }

    return (new Gson()).toJson(pojos);
  }

/*  */
}
