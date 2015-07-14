package wowData.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import wowData.business.support.Realm;
import wowData.business.support.RegionSelection;

/**
 * Reads the data from the property. Does not cache data to allow for hot swapping of properties.
 */
public class WowDataConfiguration {
	
	private static final String REALM_EU_FILENAME = "realmsEU.txt";
	private static final String REALM_US_FILENAME = "realmsUS.txt";
	private static final String PROPERTIES_FILENAME = "wowdata.properties";
	private static final String TARGETTED_TRADE_ITEMS_FILENAME = "targettedTradeItems.txt";

	public List<Realm> determineRealmList() throws IOException {

		List<Realm> realmList = new ArrayList<Realm>();
		addRealmsToList(realmList, REALM_EU_FILENAME, RegionSelection.EU);
		addRealmsToList(realmList, REALM_US_FILENAME, RegionSelection.US);
		
		if (realmList.size() == 0) {
			throw new RuntimeException("Realm files can not all be empty. Must select at least one realm.");
		}

		return realmList;
	}
	
	private void addRealmsToList(List<Realm> realmList, String fileName, RegionSelection regionSelection) throws IOException {
		
		Resource resource = new ClassPathResource(fileName);
		File realmsFile = resource.getFile();
		
		if (!realmsFile.exists()) {
			throw new RuntimeException("Can not find file: '" + fileName + "'");
		}

		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(realmsFile));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				
				if (';' == line.charAt(0) || "--".equals(line.substring(0, 2))) {
					continue;
				}
				
				realmList.add(new Realm(line, regionSelection));
			}
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
	}

	public String determineApiKey() throws IOException {
		return (String)createProperties().get("apikey");
	}
	
	public boolean determineForceDownloadNewData() throws IOException {
		
		String s = (String)createProperties().get("forceDownloadNewData");
		
		if ("1".equals(s)) {
			return true;
		}
		
		return false;
	}
	
	private Properties createProperties() throws IOException {
		Properties properties = new Properties();
		
		Resource resource = new ClassPathResource(PROPERTIES_FILENAME);
		InputStream propertiesStream = resource.getInputStream();
		
		properties.load(propertiesStream);
		
		return properties;
	}
	
	public List<String> determineTargettedTradeItems() throws IOException {
	
		List<String> targettedTradeItems = new ArrayList<>();
	
		Resource resource = new ClassPathResource(TARGETTED_TRADE_ITEMS_FILENAME);
		File file = resource.getFile();
		
		if (!file.exists()) {
			throw new RuntimeException("Can not find file: '" + TARGETTED_TRADE_ITEMS_FILENAME + "'");
		}

		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				
				if (';' == line.charAt(0) || "--".equals(line.substring(0, 2))) {
					continue;
				}
				
				targettedTradeItems.add(line);
			}
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		
		return targettedTradeItems;
		
	}

	public int determineThreadPoolSize() throws NumberFormatException, IOException {
		return Integer.valueOf((String)createProperties().get("threadpoolsize"));
	}

	

}
