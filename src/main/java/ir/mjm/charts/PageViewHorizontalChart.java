package ir.mjm.charts;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.ReportingFacad;
import ir.mjm.DBAO.Statistics;
import ir.mjm.entities.SessionSharedData;
import ir.mjm.util.FaceUtil;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

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
public class PageViewHorizontalChart {
  int currentMagUploaded;
  private HorizontalBarChartModel pageViewHorizontalBarModel;
  private HorizontalBarChartModel revenueEarnedHorizental;
  private HorizontalBarChartModel adPageViewHorizontalBarModel;
  private HorizontalBarChartModel totalSessionHorizental;
  private HorizontalBarChartModel averageSessionHorizental;
  private HorizontalBarChartModel totalDownloadsHorizental;

  public HorizontalBarChartModel getTotalDownloadsHorizental() {
    if (((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId() != currentMagUploaded) {
      init();
    }
    return totalDownloadsHorizental;
  }

  public void setTotalDownloadsHorizental(HorizontalBarChartModel totalDownloadsHorizental) {
    this.totalDownloadsHorizental = totalDownloadsHorizental;
  }

  public HorizontalBarChartModel getAverageSessionHorizental() {
    if (((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId() != currentMagUploaded) {
      init();
    }
    return averageSessionHorizental;
  }

  public void setAverageSessionHorizental(HorizontalBarChartModel averageSessionHorizental) {
    this.averageSessionHorizental = averageSessionHorizental;
  }


  public HorizontalBarChartModel getTotalSessionHorizental() {
    if (((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId() != currentMagUploaded) {
      init();
    }
    return totalSessionHorizental;
  }

  public void setTotalSessionHorizental(HorizontalBarChartModel totalSessionHorizental) {
    this.totalSessionHorizental = totalSessionHorizental;
  }


  public HorizontalBarChartModel getAdPageViewHorizontalBarModel() {
    if (((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId() != currentMagUploaded) {
      init();
    }
    return adPageViewHorizontalBarModel;
  }

  public void setAdPageViewHorizontalBarModel(HorizontalBarChartModel adPageViewHorizontalBarModel) {
    this.adPageViewHorizontalBarModel = adPageViewHorizontalBarModel;
  }


  @PostConstruct
  public void init() {
    createPageViewHorizontalBarModel();
    createAdPageViewHorizontalBarModel();
    createRevenueEarnedViewHorizontalBarModel();
    createTotalSessionHorizontalBarModel();
    createAverageSessionHorizontalBarModel();
    createDownloadsHorizontalBarModel();
  }

  public HorizontalBarChartModel getPageViewHorizontalBarModel() {
    if (((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId() != currentMagUploaded) {
      init();
    }
    return pageViewHorizontalBarModel;
  }

  public void setPageViewHorizontalBarModel(HorizontalBarChartModel pageViewHorizontalBarModel) {
    this.pageViewHorizontalBarModel = pageViewHorizontalBarModel;
  }

  private void createPageViewHorizontalBarModel() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    pageViewHorizontalBarModel = new HorizontalBarChartModel();
    pageViewHorizontalBarModel.setTitle(strings.getString("page.view.week.text"));
    ChartSeries oldViews = new ChartSeries();
    ChartSeries newViews = new ChartSeries();

    //        int maxview=0;
    for (int week = 1; week <= 2; week++) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -week * 6);
      Date startdate = cal.getTime();
      cal.add(Calendar.DATE, 7);
      Date enddate = cal.getTime();
      String date1 = Statistics.FullDateFormat.format(startdate);
      String date2 = Statistics.FullDateFormat.format(enddate);
      currentMagUploaded = ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId();
      int[] dls = ReportingFacad.getInstance().getTotalPageViewsInDatesNewAndOld(
          currentMagUploaded,
          date1, date2);
      oldViews.set(strings.getString("week.text") + " " + week, dls[0]);
      newViews.set(strings.getString("week.text") + " " + week, dls[1]);
      //            if(maxview<dls)
      //                maxview=dls;
    }

    pageViewHorizontalBarModel.addSeries(oldViews);
    pageViewHorizontalBarModel.addSeries(newViews);
    pageViewHorizontalBarModel.setLegendPosition("sw");

    oldViews.setLabel(strings.getString("existing.text"));
    newViews.setLabel(strings.getString("readers.new.text"));
    //        Axis yAxis = pageViewHorizontalBarModel.getAxis(AxisType.Y);
    //        yAxis.setLabel("Users");
    //        yAxis.setMin(0);

    //yAxis.setMax(maxview+maxview/3+1);
  }

  private void createAdPageViewHorizontalBarModel() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    adPageViewHorizontalBarModel = new HorizontalBarChartModel();
    adPageViewHorizontalBarModel.setTitle(strings.getString("adpage.view.week.text"));
    ChartSeries oldViews = new ChartSeries();
    ChartSeries newViews = new ChartSeries();

    //        int maxview=0;
    for (int week = 1; week <= 2; week++) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -week * 6);
      Date startdate = cal.getTime();
      cal.add(Calendar.DATE, 7);
      Date enddate = cal.getTime();
      String date1 = Statistics.FullDateFormat.format(startdate);
      String date2 = Statistics.FullDateFormat.format(enddate);
      int[] dls = ReportingFacad.getInstance().getTotalAdPageViewsInDatesNewAndOld(
          ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId(),
          date1, date2);
      oldViews.set(strings.getString("week.text") + " " + week, dls[0]);
      newViews.set(strings.getString("week.text") + " " + week, dls[1]);
      //            if(maxview<dls)
      //                maxview=dls;
    }

    adPageViewHorizontalBarModel.addSeries(oldViews);
    adPageViewHorizontalBarModel.addSeries(newViews);
    adPageViewHorizontalBarModel.setLegendPosition("sw");

    oldViews.setLabel(strings.getString("existing.text"));
    newViews.setLabel(strings.getString("readers.new.text"));
    //        Axis yAxis = pageViewHorizontalBarModel.getAxis(AxisType.Y);
    //        yAxis.setLabel("Users");
    //        yAxis.setMin(0);

    //yAxis.setMax(maxview+maxview/3+1);
  }

  private void createRevenueEarnedViewHorizontalBarModel() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    revenueEarnedHorizental = new HorizontalBarChartModel();
    revenueEarnedHorizental.setTitle(strings.getString("revenue.earned.total.text"));
    ChartSeries existingUsers = new ChartSeries();
    ChartSeries newUsers = new ChartSeries();


    double[] week1Earned = HiberDBFacad.getInstance()
                                       .getTotalRevenuesEarnedBeforAfter(
                                           ((SessionSharedData) FaceUtil.findBean(
                                               "sessionSharedData")).getSelectedMagazineId(), 7, 0);
    double[] towWeekAgo = HiberDBFacad.getInstance()
                                      .getTotalRevenuesEarnedBeforAfter(
                                          ((SessionSharedData) FaceUtil.findBean(
                                              "sessionSharedData")).getSelectedMagazineId(), 14, 7);

    existingUsers.set(strings.getString("week.text") + " 1", week1Earned[0]);
    newUsers.set(strings.getString("week.text") + " 1", week1Earned[1]);
    existingUsers.setLabel(strings.getString("existing.text"));
    newUsers.setLabel(strings.getString("readers.new.text"));

    existingUsers.set(strings.getString("week.text") + " 2", towWeekAgo[0]);
    newUsers.set(strings.getString("week.text") + " 2", towWeekAgo[1]);


    revenueEarnedHorizental.addSeries(existingUsers);
    revenueEarnedHorizental.addSeries(newUsers);
    revenueEarnedHorizental.setLegendPosition("sw");

    existingUsers.setLabel(strings.getString("existing.text"));
    newUsers.setLabel(strings.getString("readers.new.text"));

  }

  public HorizontalBarChartModel getRevenueEarnedHorizental() {
    if (((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId() != currentMagUploaded) {
      init();
    }
    return revenueEarnedHorizental;
  }

  public void setRevenueEarnedHorizental(HorizontalBarChartModel revenueEarnedHorizental) {
    this.revenueEarnedHorizental = revenueEarnedHorizental;
  }

  private void createTotalSessionHorizontalBarModel() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    totalSessionHorizental = new HorizontalBarChartModel();
    totalSessionHorizental.setTitle(strings.getString("reading.session.total.text"));
    ChartSeries oldViews = new ChartSeries();
    ChartSeries newViews = new ChartSeries();
    for (int week = 1; week <= 2; week++) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -week * 6);
      Date startdate = cal.getTime();
      cal.add(Calendar.DATE, 7);
      Date enddate = cal.getTime();
      String date1 = Statistics.FullDateFormat.format(startdate);
      String date2 = Statistics.FullDateFormat.format(enddate);
      int[] dls = ReportingFacad.getInstance().getTotalReadingSessionsNewAndOld(
          ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId(),
          date1, date2);
      oldViews.set(strings.getString("week.text") + " " + week, dls[0]);
      newViews.set(strings.getString("week.text") + " " + week, dls[1]);
    }

    totalSessionHorizental.addSeries(oldViews);
    totalSessionHorizental.addSeries(newViews);
    totalSessionHorizental.setLegendPosition("sw");

    oldViews.setLabel(strings.getString("existing.text"));
    newViews.setLabel(strings.getString("readers.new.text"));
  }

  private void createAverageSessionHorizontalBarModel() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    averageSessionHorizental = new HorizontalBarChartModel();
    averageSessionHorizental.setTitle(strings.getString("reading.length.avg.text"));
    ChartSeries oldViews = new ChartSeries();
    ChartSeries newViews = new ChartSeries();
    for (int week = 1; week <= 2; week++) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -week * 6);
      Date startdate = cal.getTime();
      cal.add(Calendar.DATE, 7);
      Date enddate = cal.getTime();
      String date1 = Statistics.FullDateFormat.format(startdate);
      String date2 = Statistics.FullDateFormat.format(enddate);
      double[] dls = ReportingFacad.getInstance().getAverageReadingSessionsNewAndOld(
          ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId(),
          date1, date2);
      oldViews.set(strings.getString("week.text") + " " + week, dls[0]);
      newViews.set(strings.getString("week.text") + " " + week, dls[1]);
    }

    averageSessionHorizental.addSeries(oldViews);
    averageSessionHorizental.addSeries(newViews);
    averageSessionHorizental.setLegendPosition("sw");

    oldViews.setLabel(strings.getString("existing.text"));
    newViews.setLabel(strings.getString("readers.new.text"));
  }

  private void createDownloadsHorizontalBarModel() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    totalDownloadsHorizental = new HorizontalBarChartModel();
    totalDownloadsHorizental.setTitle(
        strings.getString("download.total.text") + "/ " + strings.getString("range.total.text"));
    ChartSeries oldViews = new ChartSeries();
    ChartSeries newViews = new ChartSeries();
    //        for(int week=1;week<=2;week++){
    //            Calendar cal=Calendar.getInstance();
    //            cal.add(Calendar.DATE,-week*6);
    //            Date startdate=cal.getTime();
    //            cal.add(Calendar.DATE,7);
    //            Date enddate=cal.getTime();
    //            String date1= Statistics.nonFullDateFormat.format(startdate);
    //            String date2=Statistics.nonFullDateFormat.format(enddate);
    long[] dlsweek1 = HiberDBFacad.getInstance().getTotalDownloadsNewAndOld(
        ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId(),
        7, 0);
    long[] dlsweek2 = HiberDBFacad.getInstance().getTotalDownloadsNewAndOld(
        ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId(),
        14, 7);
    oldViews.set(strings.getString("week.text") + " 1", dlsweek1[0]);
    newViews.set(strings.getString("week.text") + " 1", dlsweek1[1]);
    oldViews.set(strings.getString("week.text") + " 2", dlsweek2[0]);
    newViews.set(strings.getString("week.text") + " 2", dlsweek2[1]);
    //        }

    totalDownloadsHorizental.addSeries(oldViews);
    totalDownloadsHorizental.addSeries(newViews);
    totalDownloadsHorizental.setLegendPosition("sw");

    oldViews.setLabel(strings.getString("existing.text"));
    newViews.setLabel(strings.getString("readers.new.text"));
  }
}
