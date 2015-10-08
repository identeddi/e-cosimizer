package de.milke.ecost.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.milke.ecost.model.PowerSupply;

@Stateless
public class Check24SupplyResolver {

    String baseURL = "https://vergleich.check24.de";

    public List<PowerSupply> getPowerSupplies(int PLZ, int consumption) throws IOException {

	Document powerCalcDoc = getPowerCalculator();
	Document powerResultDoc = getPowerResultDocument(powerCalcDoc, PLZ, consumption);
	List<PowerSupply> supplies = parseResultDocument(powerResultDoc);
	return supplies;

    }

    Document getPowerCalculator() throws IOException {

	Connection conn = Jsoup
		.connect("https://a.check24.net/misc/click.php?pid=92489&aid=86&product_id=1");

	Document document = conn.get();
	return document;
    }

    Document getPowerResultDocument(Document calcDoc, int PLZ, int consumtion) throws IOException {
	Map<String, String> formElements = new HashMap<>();
	formElements.put("style", "check24-bluegrey");
	formElements.put("pid", "92489");
	formElements.put("tid", "");
	formElements.put("ref", "");

	formElements.put("considerdeposit", "no");
	formElements.put("considerdiscounts", "yes");
	formElements.put("paymentperiod", "month");
	formElements.put("priceguarantee", "yes");
	formElements.put("guidelinematch", "yes");
	formElements.put("packages", "no");
	formElements.put("eco", "no");
	formElements.put("c24api_rs_module_uri", "");
	formElements.put("c24api_rs_app", "form");
	formElements.put("c24api_rs_lang", "");
	formElements.put("c24api_style", "check24-bluegrey");
	formElements.put("c24api_pid", "92489");
	formElements.put("c24api_tid", "");
	formElements.put("c24api_ref", "");
	formElements.put("c24_controller", "form");
	formElements.put("c24_calculate", "x");
	formElements.put("c24api_onlinesubscription", "no");
	formElements.put("c24api_priceguarantee_months", "12");
	formElements.put("c24api_sortfield", "price");
	formElements.put("c24api_sortorder", "asc");
	formElements.put("c24api_user_has_pointplan", "");
	formElements.put("c24api_exclude_brand_provider_id", "0");
	formElements.put("c24api_zipcode", "10115");
	// formElements.put("c24api_city", "Hennef");
	// formElements.put("c24api_totalconsumption", "3500");
	formElements.put("c24api_contractperiod", "12");
	formElements.put("c24api_paymentperiod", "month");
	formElements.put("c24api_customertype", "private");
	formElements.put("c24api_eco", "no");
	formElements.put("c24api_eco_type", "normal");
	formElements.put("c24api_considerdiscounts", "yes");
	formElements.put("c24api_packages", "yes");
	formElements.put("c24api_priceguarantee", "yes");
	formElements.put("c24api_guidelinematch", "yes");
	// formElements.put("c24api_reference_provider_hash", "rwevertriebag");
	// formElements.put("c24api_reference_tariffversion_key",
	// "605930-base");
	formElements.put("c24_calculate", "jetzt+kostenlos+vergleiche");
	Element iframe = calcDoc.select("iframe").first();
	String iframeSrc = iframe.attr("src");

	Document document;

	document = Jsoup.connect(iframeSrc).get();

	String url = "";
	for (Element iinput : document.select("form")) {
	    url = iinput.attr("action");
	}
	Connection finalConn = Jsoup.connect(baseURL + url);

	formElements.put("c24api_zipcode", "40789");
	formElements.put("c24api_totalconsumption", "3500");

	for (Element iinput : document.select("input")) {

	    String name = iinput.attr("name");
	    String value = iinput.attr("value");
	    if (name.equals("fs")) {
		formElements.put(name, value);
	    } else if (name.equals("c24api_rs_session")) {
		formElements.put(name, value);
	    }
	    finalConn = finalConn.data(name, value);

	}

	String fullURL = baseURL + url;
	boolean first = true;
	for (String formKey : formElements.keySet()) {
	    finalConn = finalConn.data(formKey, formElements.get(formKey));
	    if (first) {
		fullURL += "?";
		first = false;
	    } else {
		fullURL += "&";

	    }
	    fullURL += formKey + "=" + formElements.get(formKey);

	}

	Document resultDocument = Jsoup.connect(fullURL).post();
	return resultDocument;
    }

    List<PowerSupply> parseResultDocument(Document resultDocument) {
	List<PowerSupply> powerSupplies = new ArrayList<>();

	Elements results = resultDocument.select("div[class=c24Frame]")
		.select("div[class=c24FrameContent]").select("div[class=c24FrameContentPadding]")
		.select("div[class=c24-content]").get(2).select("div[class=c24-result-row-energy]");
	for (Element row : results) {

	    Element iinput = row.select("div[class=c24-energy-result-row-detail c24-clearfix]")
		    .get(0);
	    Elements col = iinput.select("table[class=c24-energy-result-row-price-info-table]");
	    col = col.select("tbody").select("tr").select("td");
	    String price = col.get(1).text();

	    price = price.replaceAll("[^0-9]", "");

	    Double priceDouble = Integer.parseInt(price) / 100.;
	    col = iinput.select("div[class=c24-result-column2]");
	    Elements liselem = col.get(0).select("li");
	    String anbieter = "";
	    for (int i = liselem.size() - 1; i >= 0; i--) {
		if (!liselem.get(i).text().isEmpty()) {
		    anbieter += liselem.get(i).text();
		    anbieter += " - ";
		}
	    }

	    int kundenbewertungenInt = 0;
	    int weiterempfehlungInt = 0;
	    String urlWeiter = "";
	    try {
		Element elem = row.select("div[class=c24-result-tariff-details c24-clearfix]")
			.get(0).select("div[class=c24-result-column3 c24-result-column5").get(0)
			.select("div[class=c24-ajax-tab").select("a").get(0);

		String kundenbewertungen = elem.text();
		kundenbewertungen = kundenbewertungen.replaceAll("[^0-9]", "");
		kundenbewertungenInt = (int) Integer.parseInt(kundenbewertungen);

		String weitrempfehlung = elem.select("div[class=starempty14]")
			.select("div[class=starfull14]").get(0).attr("style");
		weitrempfehlung = weitrempfehlung.replaceAll("[^0-9]", "");
		weiterempfehlungInt = (int) ((Integer.parseInt(weitrempfehlung) / 70.) * 100);
	    } catch (Exception e) {
	    }

	    try {
		Elements elem = iinput.select("div[class=c24-result-button-aligning]");

		elem = elem.select("div[class=c24-button-container]");
		elem = elem.select("a");
		urlWeiter = elem.attr("href");
	    } catch (Exception e) {

	    }

	    if (!urlWeiter.isEmpty()) {
		urlWeiter = baseURL + urlWeiter;
	    }

	    powerSupplies.add(new PowerSupply(anbieter, priceDouble, weiterempfehlungInt,
		    kundenbewertungenInt, urlWeiter));

	}
	return powerSupplies;
    }
}
