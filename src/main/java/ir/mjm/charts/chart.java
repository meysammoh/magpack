package ir.mjm.charts;

import ir.mjm.DBAO.HiberDBFacad;
import ir.mjm.entities.SessionSharedData;
import ir.mjm.util.FaceUtil;
import org.primefaces.model.chart.PieChartModel;

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
public class chart {


  private PieChartModel pieModel2;

  @PostConstruct
  public void init() {
    createPieModels();
  }

  public void setPieModel2(PieChartModel pieModel2) {
    this.pieModel2 = pieModel2;
  }

  public PieChartModel getPieModel2() {
    return pieModel2;
  }

  private void createPieModels() {
    createPieModel2();
  }


  private void createPieModel2() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    pieModel2 = new PieChartModel();
    int puId = ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedPublisherID();
    int[] newAndOldReadersCount = HiberDBFacad.getInstance().getNewAndOldReadersInMonthForPublisher(puId);
    pieModel2.set(strings.getString("readers.new.text") + " " + newAndOldReadersCount[0], newAndOldReadersCount[0]);

    pieModel2.set(
        strings.getString("readers.returning.text") + " " + newAndOldReadersCount[1],
        newAndOldReadersCount[1]);

    pieModel2.setTitle(strings.getString("readers.month.text"));
    pieModel2.setSliceMargin(2);
    pieModel2.setLegendPosition("b");
    pieModel2.setFill(true);

    pieModel2.setShowDataLabels(true);
    //        pieModel2.setDiameter(150);
  }
}
