package wowData.logic;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wowData.business.db.DbAuctionList;
import wowData.business.db.DerivedItemData;
import wowData.business.support.Realm;

public class RealmProcessor {

	static final Logger log = LoggerFactory.getLogger(RealmProcessor.class);
	
	private ItemManager itemManager;
	
	public RealmProcessor(ItemManager itemManager) {
		super();
		this.itemManager = itemManager;
	}

	public Boolean process(Realm realm) throws Exception {

		log.info("Starting realm: {}, in region: {}", realm.getName(), realm.getRegionSelection());
		
		WowDataConfiguration wowDataConfiguration = new WowDataConfiguration();
		boolean forceDownloadNewData = wowDataConfiguration.determineForceDownloadNewData();

		WowDataReader wowDataReader = new WowDataReader();
		Date previousLastModified = wowDataReader.determinepreviousLastModified(realm);

		WowDataURLGenerator wowDataURLGenerator = new WowDataURLGenerator();
		wowDataURLGenerator.setPreviousLastModified(previousLastModified);
		if (!wowDataURLGenerator.determineURL(realm)) {
			log.info("Fatal error in wowDataURLGenerator, skipping realm: {} in region: {}", realm.getName(), realm.getRegionSelection());
			return false;
		}
		if (!wowDataURLGenerator.determineIsDataNew() && !forceDownloadNewData) {
			log.info("No new data, skipping realm: {} in region: {}", realm.getName(), realm.getRegionSelection());
			return false;
		}

		String url = wowDataURLGenerator.getUrl();
		Date lastModified = wowDataURLGenerator.getLastModified();

		wowDataReader.setUrl(url);
		if (!wowDataReader.readAuctionData()) {
			log.info("Fatal error in wowDataReader, skipping realm: {} in region: {}", realm.getName(), realm.getRegionSelection());
			return false;
		}

		DbAuctionList dbAuctionList = wowDataReader.getDbAuctionList();
		dbAuctionList.fillLastModified(lastModified);
		dbAuctionList.fillRegion(realm.getRegionSelection());
		dbAuctionList.fillRealm(realm.getName());

		itemManager.filterTradeMaterials(dbAuctionList);
		
		DerivedItemDataCalculator derivedItemDataCalculator = new DerivedItemDataCalculator();
		List<DerivedItemData> derivedItemDataList = 
				derivedItemDataCalculator.calculateDerivedItemdData(dbAuctionList, itemManager.getTargettedTradeItemIds());

		WowDataSaver wowDataSaver = new WowDataSaver();
		wowDataSaver.setDbAuctionList(dbAuctionList);
		wowDataSaver.setDerivedItemDataList(derivedItemDataList);
		wowDataSaver.writePreviousLastModified(realm, lastModified);
		wowDataSaver.save();

		log.info("Finished realm: {}, in region: {}", realm.getName(), realm.getRegionSelection());

		return true;
	}



}