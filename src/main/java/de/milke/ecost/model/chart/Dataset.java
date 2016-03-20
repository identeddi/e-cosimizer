package de.milke.ecost.model.chart;

import java.util.List;

public class Dataset {
    // label : "My First dataset",
    // fillColor : "rgba(220,220,220,0.5)",
    // strokeColor : "rgba(220,220,220,0.8)",
    // highlightFill : "rgba(220,220,220,0.75)",
    // highlightStroke : "rgba(220,220,220,1)",
    // data : [ 65, 59, 80, 81, 56, 55, 40, 59, 80, 81, 56, 55, 40 ]
    String label;
    String fillColor;
    String strokeColor;
    String highlightFill;
    String highlightStroke;
    List<Integer> data;

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public String getFillColor() {
	return fillColor;
    }

    public void setFillColor(String fillColor) {
	this.fillColor = fillColor;
    }

    public String getStrokeColor() {
	return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
	this.strokeColor = strokeColor;
    }

    public String getHighlightFill() {
	return highlightFill;
    }

    public void setHighlightFill(String highlightFill) {
	this.highlightFill = highlightFill;
    }

    public String getHighlightStroke() {
	return highlightStroke;
    }

    public void setHighlightStroke(String highlightStroke) {
	this.highlightStroke = highlightStroke;
    }

    public List<Integer> getData() {
	return data;
    }

    public void setData(List<Integer> data) {
	this.data = data;
    }

}
