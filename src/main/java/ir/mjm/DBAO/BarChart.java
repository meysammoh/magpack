package ir.mjm.DBAO;

import ir.mjm.entities.SessionSharedData;
import ir.mjm.util.FaceUtil;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * Created by Serap on 6/24/14.
 */
@ManagedBean
@RequestScoped
public class BarChart {

  int currentMagUploaded = -2;
  int maxSesiersRevenues = 10;

  private BarChartModel barModel;
  private int downloadsThisWeek, downloadsThisMonth, downloadsThisYear;

  public BarChartModel getRevenusBarModel() {
    if (((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId() != currentMagUploaded) {
      init();
    }
    return revenusBarModel;
  }

  public void setRevenusBarModel(BarChartModel revenusBarModel) {
    this.revenusBarModel = revenusBarModel;
  }

  private BarChartModel revenusBarModel;


  @PostConstruct
  public void init() {
    currentMagUploaded = ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId();
    createBarModels();
  }

  public BarChartModel getBarModel() {
    if (((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId() != currentMagUploaded) {
      init();
    }
    return barModel;
  }

  private void addLablesToDownloadChart(BarChartModel barmo) {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    Axis yAxis = barmo.getAxis(AxisType.Y);
    yAxis.setLabel(strings.getString("downloads.text"));
    yAxis.setMin(0);

  }

  public void updateDownloadsChart(String type) {
    if (type.equals("y")) {
      barModel = initBarModelYear();
    } else if (type.equals("m")) {
      barModel = initBarModelMonth();
    } else {
      barModel = initBarModelWeek();
    }
    addLablesToDownloadChart(barModel);
  }

  private BarChartModel initBarModelWeek() {

    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    BarChartModel model = new BarChartModel();

    ChartSeries downloads = new ChartSeries();
    downloads.setLabel(strings.getString("downloads.text"));
    downloads.setLabel("");
    int maxdl = 0;
    for (int day = 0; day <= 6; day++) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -day);
      Date startdate = cal.getTime();
      cal.add(Calendar.DATE, 1);
      Date enddate = cal.getTime();
      String date1 = Statistics.nonFullDateFormat.format(startdate);
      String date2 = Statistics.nonFullDateFormat.format(enddate);
      int dls = HiberDBFacad.getInstance().getTotalDownloadsOfPublisherInRange(
          ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedPublisherID(),
          date1, date2);
      downloads.set(date1, dls);
      if (maxdl < dls) {
        maxdl = dls;
      }
    }

    model.addSeries(downloads);
    Axis yAxis = model.getAxis(AxisType.Y);
    yAxis.setMax(maxdl + maxdl / 3 + 1);
    addLablesToDownloadChart(model);
    return model;
  }

  private BarChartModel initBarModelMonth() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    BarChartModel model = new BarChartModel();

    ChartSeries downloads = new ChartSeries();
    downloads.setLabel(strings.getString("downloads.text"));
    downloads.setLabel("");
    int maxdl = 0;
    for (int day = 0; day <= 29; day++) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -day);
      Date startdate = cal.getTime();
      cal.add(Calendar.DATE, 1);
      Date enddate = cal.getTime();
      String date1 = Statistics.nonFullDateFormat.format(startdate);
      String date2 = Statistics.nonFullDateFormat.format(enddate);
      int dls = HiberDBFacad.getInstance().getTotalDownloadsOfPublisherInRange(
          ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedPublisherID(),
          date1, date2);
      downloads.set(day + 1, dls);
      if (maxdl < dls) {
        maxdl = dls;
      }
    }

    model.addSeries(downloads);
    Axis yAxis = model.getAxis(AxisType.Y);
    yAxis.setMax(maxdl + maxdl / 3 + 1);
    addLablesToDownloadChart(model);
    return model;
  }

  private BarChartModel initBarModelYear() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    BarChartModel model = new BarChartModel();

    ChartSeries downloads = new ChartSeries();
    downloads.setLabel(strings.getString("downloads.text"));
    downloads.setLabel("");
    int maxdl = 0;
    for (int month = 1; month <= 12; month++) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -month * 29);
      Date startdate = cal.getTime();
      cal.add(Calendar.DATE, 32);
      Date enddate = cal.getTime();
      String date1 = Statistics.nonFullDateFormat.format(startdate);
      String date2 = Statistics.nonFullDateFormat.format(enddate);

      int dls = HiberDBFacad.getInstance().getTotalDownloadsOfPublisherInRange(
          ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedPublisherID(),
          date1, date2);
      downloads.set(month, dls);
      if (maxdl < dls) {
        maxdl = dls;
      }
    }

    model.addSeries(downloads);
    Axis yAxis = model.getAxis(AxisType.Y);
    yAxis.setLabel(strings.getString("users.text"));
    yAxis.setMin(0);

    yAxis.setMax(maxdl + maxdl / 3 + 1);
    addLablesToDownloadChart(model);
    return model;

  }

  private BarChartModel initBarModel2() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());

    BarChartModel model = new BarChartModel();
    int pubId = ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedPublisherID();

    double thisWeekRev = HiberDBFacad.getInstance().getThisDaysAgoRevenue(pubId, 7);
    double thisMonthRev = HiberDBFacad.getInstance().getThisDaysAgoRevenue(pubId, 30);
    maxSesiersRevenues = (int) thisMonthRev;
    ChartSeries newreaders = new ChartSeries();
    newreaders.setLabel(strings.getString("revenue.week.text"));
    newreaders.set("Time", thisWeekRev);
    ChartSeries oldreaders = new ChartSeries();
    oldreaders.setLabel(strings.getString("revenue.month.text"));
    oldreaders.set("Time", thisMonthRev);
    model.addSeries(newreaders);
    model.addSeries(oldreaders);

    return model;
  }

  private void createBarModels() {
    createBarModel();
    createBarModel2();
  }

  private void createBarModel() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    barModel = initBarModelWeek();

    barModel.setTitle(strings.getString("downloads.text") + " / " + strings.getString("range.text"));
    barModel.setLegendPosition("ne");

    //        Axis xAxis = barModel.getAxis(AxisType.X);
    //        xAxis.setLabel("Month");
    //
    //        Axis yAxis = barModel.getAxis(AxisType.Y);
    //        yAxis.setLabel("Users");
    //        yAxis.setMin(0);
    //        yAxis.setMax(200);
  }

  private void createBarModel2() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    revenusBarModel = initBarModel2();

    revenusBarModel.setTitle(strings.getString("revenues.text"));
    revenusBarModel.setLegendPosition("ne");

    Axis xAxis = revenusBarModel.getAxis(AxisType.X);
    xAxis.setLabel(strings.getString("time.text"));

    Axis yAxis = revenusBarModel.getAxis(AxisType.Y);
    yAxis.setLabel(strings.getString("currency.text"));
    yAxis.setMin(0);
    yAxis.setMax(maxSesiersRevenues + (maxSesiersRevenues / 5));

  }


  public int getDownloadsThisWeek() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -7);
    Date startdate = cal.getTime();
    cal.add(Calendar.DATE, 8);
    Date enddate = cal.getTime();
    String date1 = Statistics.nonFullDateFormat.format(startdate);
    String date2 = Statistics.nonFullDateFormat.format(enddate);
    downloadsThisWeek = HiberDBFacad.getInstance().getTotalDownloadsOfPublisherInRange(
        ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedPublisherID(),
        date1, date2);
    return downloadsThisWeek;
  }

  public int getDownloadsThisMonth() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -29);
    Date startdate = cal.getTime();
    cal.add(Calendar.DATE, 30);
    Date enddate = cal.getTime();
    String date1 = Statistics.nonFullDateFormat.format(startdate);
    String date2 = Statistics.nonFullDateFormat.format(enddate);
    downloadsThisMonth = HiberDBFacad.getInstance().getTotalDownloadsOfPublisherInRange(
        ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedPublisherID(),
        date1, date2);
    return downloadsThisMonth;
  }

  public int getDownloadsThisYear() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -365);
    Date startdate = cal.getTime();
    cal.add(Calendar.DATE, 366);
    Date enddate = cal.getTime();
    String date1 = Statistics.nonFullDateFormat.format(startdate);
    String date2 = Statistics.nonFullDateFormat.format(enddate);
    downloadsThisYear = HiberDBFacad.getInstance().getTotalDownloadsOfPublisherInRange(
        ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedPublisherID(),
        date1, date2);
    return downloadsThisYear;
  }

}
