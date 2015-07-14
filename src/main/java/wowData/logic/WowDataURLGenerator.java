package wowData.logic;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import wowData.business.support.Realm;
import wowData.business.wowapi.auctionDataStatus.AuctionDataStatus;

public class WowDataURLGenerator {

	private Date previousLastModified;
	private Date lastModified;
	private String url;

	private static final String hostEU = "https://eu.api.battle.net";
	private static final String hostUS = "https://us.api.battle.net";

	private static final String apiUrlMidsection = "/wow/auction/data";

	static final Logger log = LoggerFactory.getLogger(WowDataURLGenerator.class);

	public boolean determineURL(Realm realm) throws IOException {

		WowDataConfiguration wowDataConfiguration = new WowDataConfiguration();

		String statusURL = createStatusURL(realm);
		
		log.info("Reading from URL: {}", statusURL);
		
		RestTemplate restTemplate = new RestTemplate();
		AuctionDataStatus auctionDataStatus = null;
		try {
			auctionDataStatus = restTemplate.getForObject(statusURL, AuctionDataStatus.class);
		} catch(Exception e) {
			log.error("Exception for restTemplate",e);
			log.error("Exception for restTemplate with URL {}. Trying again...", statusURL);

			try { 
				auctionDataStatus = restTemplate.getForObject(statusURL, AuctionDataStatus.class);
				log.info("Second attempt succesful!");
			} catch (Exception e2) {
				log.error("Failed again");
				log.error("Exception for restTemplate",e2);
				log.error("Exception for restTemplate with URL {}", statusURL);
				return false;
			}

		}

		lastModified = new Date(Long.parseLong(auctionDataStatus.getFiles().get(0).getLastModified()));

		url = auctionDataStatus.getFiles().get(0).getUrl().concat("?apikey=" + wowDataConfiguration.determineApiKey());

		return true;

	}

	private String createStatusURL(Realm realm) throws IOException {

		StringBuilder statusUrl = new StringBuilder("");

		switch(realm.getRegionSelection()) {
		case EU: statusUrl.append(hostEU);
		break;
		case US: statusUrl.append(hostUS);
		break;
		}

		statusUrl.append(apiUrlMidsection);

		statusUrl.append("/" + realm.getName());

		WowDataConfiguration wowDataConfiguration = new WowDataConfiguration();

		statusUrl.append("?apikey=" + wowDataConfiguration.determineApiKey());

		return statusUrl.toString();
	}

	public boolean determineIsDataNew() {

		if (previousLastModified == null) {
			return true;
		}

		if (lastModified.compareTo(previousLastModified) > 0) {
			return true;
		}

		return false;
	}

	public static String getHosteu() {
		return hostEU;
	}

	public static String getHostus() {
		return hostUS;
	}

	public static String getApiurlmidsection() {
		return apiUrlMidsection;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getPreviousLastModified() {
		return previousLastModified;
	}

	public void setPreviousLastModified(Date previousLastModified) {
		this.previousLastModified = previousLastModified;
	}


}
