package ir.mjm.charts;

import ir.mjm.DBAO.ReportingFacad;
import ir.mjm.entities.SessionSharedData;
import ir.mjm.util.FaceUtil;
import org.primefaces.model.chart.PieChartModel;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * Created by mayssam on 7/15/14.
 */
@ManagedBean
@RequestScoped
public class GeoChart {
  int currentMagUploaded;

  @PostConstruct
  public void init() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    geoPieChart = new PieChartModel();
    currentMagUploaded = ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId();
    HashMap<String, Float> result = ReportingFacad.getInstance().getGeoDistribution(0, currentMagUploaded);
    geoPieChart = new PieChartModel();
    if (result != null && result.size() > 0) {
      for (Map.Entry<String, Float> keyvalue : result.entrySet()) {
        geoPieChart.set(keyvalue.getKey(), keyvalue.getValue());
      }
    } else {
      geoPieChart.set(strings.getString("data.not.found.text"), 100);
    }

    geoPieChart.setTitle(strings.getString("readers.geo.dist.text"));
    geoPieChart.setSliceMargin(2);
    geoPieChart.setLegendPosition("b");
    geoPieChart.setFill(true);

    geoPieChart.setShowDataLabels(true);
  }

  private PieChartModel geoPieChart;


  public PieChartModel getGeoPieChart() {
    if (((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId() != currentMagUploaded) {
      init();
    }
    return geoPieChart;
  }

  public void setGeoPieChart(PieChartModel ageChart) {
    this.geoPieChart = ageChart;
  }
}
