package ir.mjm.charts;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.DBAO.PubUserDet;
import ir.mjm.util.FaceUtil;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * Created by Serap on 8/5/14.
 */
@ManagedBean
@RequestScoped
public class SpecIssuePageCharts {
  int currentMagUploaded;
  private HorizontalBarChartModel readerThisMonths;
  private BarChartModel downloadsReachReichweit;
  private BarChartModel revenuesMonthYear;


  @PostConstruct
  public void init() {
    currentMagUploaded = ((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedCurrentMag_id();
    createPageViewHorizontalBarModel();
    createRevenuesHorizontalBarModel();
    createDownloadReachBarModel();

  }


  private void createPageViewHorizontalBarModel() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    readerThisMonths = new HorizontalBarChartModel();

    ChartSeries oldViews = new ChartSeries();
    ChartSeries newViews = new ChartSeries();

    int[] res = HiberDBFacad.getInstance().getNewAndOldReadersInMonthForMAgazine(currentMagUploaded, 30);
    oldViews.set(strings.getString("readers.returning.text"), res[0]);
    newViews.set(strings.getString("readers.new.text"), res[1]);
    ////            if(maxview<dls)
    ////                maxview=dls;
    //        }


    readerThisMonths.setLegendPosition("e");
    readerThisMonths.setStacked(true);

    Axis xAxis = readerThisMonths.getAxis(AxisType.X);
    xAxis.setLabel(strings.getString("births.text"));
    xAxis.setMin(0);
    xAxis.setMax((res[0] > res[1]) ? (res[0] + (res[0] / 3) + 1) : (res[0] + (res[0]) / 3 + 1));

    Axis yAxis = readerThisMonths.getAxis(AxisType.Y);

    readerThisMonths.setTitle(strings.getString("readers.month.text"));
    readerThisMonths.setLegendPosition("e");
    oldViews.setLabel(strings.getString("readers.returning.text"));
    newViews.setLabel(strings.getString("readers.new.text"));
    //        Axis yAxis = readerThisMonths.getAxis(AxisType.Y);
    //        yAxis.setLabel("Users");
    //        yAxis.setMin(0);
    readerThisMonths.addSeries(newViews);
    readerThisMonths.addSeries(oldViews);
    //yAxis.setMax(maxview+maxview/3+1);
  }

  private void createRevenuesHorizontalBarModel() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    revenuesMonthYear = new BarChartModel();
    revenuesMonthYear.setTitle(strings.getString("revenues.text"));
    ChartSeries oldViews = new ChartSeries();
    ChartSeries newViews = new ChartSeries();

    //        int maxview=0;
    //        for(int week=1;week<=2;week++){
    //            Calendar cal=Calendar.getInstance();
    //            cal.add(Calendar.DATE,-week*6);
    //            Date startdate=cal.getTime();
    //            cal.add(Calendar.DATE,7);
    //            Date enddate=cal.getTime();
    //            String date1= Statistics.FullDateFormat.format(startdate);
    //            String date2=Statistics.FullDateFormat.format(enddate);
    //            int[] dls= ReportingFacad.getInstance().getTotalAdPageViewsInDatesNewAndOld(((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedCurrentMag_id(),
    //                    date1, date2);
    //            oldViews.set("Week "+week, dls[0]);
    //            newViews.set("Week "+week, dls[1]);
    ////            if(maxview<dls)
    ////                maxview=dls;
    //        }
    double[] monthReve = HiberDBFacad.getInstance().getTotalRevenuesEarnedBeforAfter(currentMagUploaded, 31, 0);
    double[] yearReve = HiberDBFacad.getInstance().getTotalRevenuesEarnedBeforAfter(currentMagUploaded, 364, 0);
    oldViews.set(strings.getString("revenue.month.text"), monthReve[0]);
    newViews.set(strings.getString("revenue.month.text"), monthReve[1]);
    oldViews.setLabel(strings.getString("existing.text"));
    newViews.setLabel(strings.getString("readers.new.text"));

    oldViews.set(strings.getString("revenue.year.text"), yearReve[0]);
    newViews.set(strings.getString("revenue.year.text"), yearReve[1]);

    revenuesMonthYear.addSeries(oldViews);
    revenuesMonthYear.addSeries(newViews);
    revenuesMonthYear.setLegendPosition("sw");

    //        Axis yAxis = readerThisMonths.getAxis(AxisType.Y);
    //        yAxis.setLabel("Users");
    //        yAxis.setMin(0);

    //yAxis.setMax(maxview+maxview/3+1);
  }

  private void createDownloadReachBarModel() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    downloadsReachReichweit = new BarChartModel();
    downloadsReachReichweit.setTitle(strings.getString("downloads.text"));
    ChartSeries existingUsers = new ChartSeries();
    ChartSeries newUsers = new ChartSeries();


    //        double[] week1Earned= HiberDBFacad.getInstance().getTotalRevenuesEarnedBeforAfter(((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedCurrentMag_id(),
    //                ((PubUserDet) FaceUtil.findBean("pubUserDet")).getId(), 7,0);
    //        double[] towWeekAgo= HiberDBFacad.getInstance().getTotalRevenuesEarnedBeforAfter(((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedCurrentMag_id(),
    //                ((PubUserDet) FaceUtil.findBean("pubUserDet")).getId(), 14,7);

    long[] totalweek = HiberDBFacad.getInstance()
                                   .getTotalDownloadsNewAndOld(
                                       ((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedCurrentMag_id(),
                                       7,
                                       0);
    long[] totalMonth = HiberDBFacad.getInstance()
                                    .getTotalDownloadsNewAndOld(
                                        ((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedCurrentMag_id(),
                                        31,
                                        0);

    existingUsers.set(strings.getString("downloads.week.text"), totalweek[0]);
    newUsers.set(strings.getString("downloads.week.text"), totalweek[1]);
    existingUsers.setLabel(strings.getString("existing.text"));
    newUsers.setLabel(strings.getString("readers.new.text"));

    existingUsers.set(strings.getString("downloads.month.text"), totalMonth[0]);
    newUsers.set(strings.getString("downloads.month.text"), totalMonth[1]);


    downloadsReachReichweit.addSeries(existingUsers);
    downloadsReachReichweit.addSeries(newUsers);
    downloadsReachReichweit.setLegendPosition("sw");

    //        existingUsers.setLabel("Download this week");
    //        newUsers.setLabel("Download this Year");

  }


  public HorizontalBarChartModel getReaderThisMonths() {
    if (((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedCurrentMag_id() != currentMagUploaded) {
      init();
    }
    return readerThisMonths;
  }


  public void setReaderThisMonths(HorizontalBarChartModel readerThisMonths) {
    this.readerThisMonths = readerThisMonths;
  }

  public BarChartModel getDownloadsReachReichweit() {
    if (((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedCurrentMag_id() != currentMagUploaded) {
      init();
    }
    return downloadsReachReichweit;
  }

  public void setDownloadsReachReichweit(BarChartModel downloadsReachReichweit) {
    this.downloadsReachReichweit = downloadsReachReichweit;
  }

  public BarChartModel getRevenuesMonthYear() {
    if (((PubUserDet) FaceUtil.findBean("pubUserDet")).getSelectedCurrentMag_id() != currentMagUploaded) {
      init();
    }
    return revenuesMonthYear;
  }

  public void setRevenuesMonthYear(BarChartModel revenuesMonthYear) {
    this.revenuesMonthYear = revenuesMonthYear;
  }
}
