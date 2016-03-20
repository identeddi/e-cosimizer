package de.milke.ecost.model.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartModel {

    List<String> labels = new ArrayList<>();
    List<Dataset> datasets = new ArrayList<>();

    public List<String> getLabels() {
	return labels;
    }

    public void setLabels(List<String> labels) {
	this.labels = labels;
    }

    public List<Dataset> getDatasets() {
	return datasets;
    }

    public void setDatasets(List<Dataset> datasets) {
	this.datasets = datasets;
    }

}
