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
public class AgeChart {
  int currentMagUploaded;

  @PostConstruct
  public void init() {
    ResourceBundle strings = ResourceBundle.getBundle(
        "ir.mjm.strings",
        FacesContext.getCurrentInstance().getViewRoot().getLocale());
    agePieChart = new PieChartModel();
    currentMagUploaded = ((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId();
    HashMap<String, Float> result = ReportingFacad.getInstance().getAgeDistribution(0, currentMagUploaded);
    agePieChart = new PieChartModel();
    if (result != null && result.size() > 0) {
      for (Map.Entry<String, Float> keyvalue : result.entrySet()) {
        agePieChart.set(keyvalue.getKey(), keyvalue.getValue());
      }
    } else {
      agePieChart.set(strings.getString("data.not.found.text"), 100);
    }
    agePieChart.setTitle(strings.getString("readers.age.dist.text"));
    agePieChart.setSliceMargin(2);
    agePieChart.setLegendPosition("b");
    agePieChart.setFill(true);

    agePieChart.setShowDataLabels(true);
  }

  private PieChartModel agePieChart;


  public PieChartModel getAgePieChart() {
    if (((SessionSharedData) FaceUtil.findBean("sessionSharedData")).getSelectedMagazineId() != currentMagUploaded) {
      this.init();
    }
    return agePieChart;
  }

  public void setAgePieChart(PieChartModel ageChart) {
    this.agePieChart = ageChart;
  }
}
