package de.milke.ecost.model;

public class PowerSupply {

    String supply;
    String provider;
    double price;
    int rating;
    int ratingCount;
    String url;

    public PowerSupply(String provider, String supply, double price, int rating, int ratingCount,
	    String url) {
	super();
	this.provider = provider;
	this.supply = supply;
	this.price = price;
	this.rating = rating;
	this.ratingCount = ratingCount;
	this.url = url;
    }

    public PowerSupply() {
	super();
    }

    public String getProvider() {
	return provider;
    }

    public void setProvider(String provider) {
	this.provider = provider;
    }

    public double getPrice() {
	return price;
    }

    public void setPrice(double price) {
	this.price = price;
    }

    public int getRating() {
	return rating;
    }

    public void setRating(int rating) {
	this.rating = rating;
    }

    public int getRatingCount() {
	return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
	this.ratingCount = ratingCount;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getSupply() {
	return supply;
    }

    public void setSupply(String supply) {
	this.supply = supply;
    }

}
