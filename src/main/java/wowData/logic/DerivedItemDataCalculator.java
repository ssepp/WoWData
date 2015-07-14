package wowData.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wowData.business.db.DbAuction;
import wowData.business.db.DbAuctionList;
import wowData.business.db.DerivedItemData;

public class DerivedItemDataCalculator {
	
	static final Logger log = LoggerFactory.getLogger(DerivedItemDataCalculator.class);

	public List<DerivedItemData> calculateDerivedItemdData(DbAuctionList dbAuctionList, List<Integer> targettedTradeItemId) throws IOException {

		// A map from itemId to the list of dbAuctions for that items. We only fill it for targetted items.
		Map<Integer, List<DbAuction>> targettedItemsMap = new HashMap<>();
		
		for (DbAuction dbAuction : dbAuctionList) {
			
			if (!dbAuction.determineHasBuyout()) {
				// No buyout. Skip it.
				continue;
			}
			
			if (!targettedTradeItemId.contains(dbAuction.getItem())) {
				// Not a targetted item. Skip it.
				continue;
			}
			
			if (targettedItemsMap.get(dbAuction.getItem()) == null) {
				targettedItemsMap.put(dbAuction.getItem(), new ArrayList<DbAuction>());
			}
			
			targettedItemsMap.get(dbAuction.getItem()).add(dbAuction);
			
		}
		
		// Sort the lists by price
		for (Integer itemId : targettedItemsMap.keySet()) {
			Collections.sort(targettedItemsMap.get(itemId));
		}
		
		List<DerivedItemData> derivedItemDataList = new ArrayList<DerivedItemData>();
		// Calculate the derived data
		for (Integer itemId : targettedItemsMap.keySet()) {
		
			List<DbAuction> dbAuctionListForItem = targettedItemsMap.get(itemId);
			
			DerivedItemData derivedItemData = new DerivedItemData();
			derivedItemDataList.add(derivedItemData);
			
			derivedItemData.setItemId(itemId);
			derivedItemData.setOwnerRealmId(dbAuctionList.get(0).getOwnerRealmId());
			derivedItemData.setRegionId(dbAuctionList.get(0).getRegionId());
			derivedItemData.setLastModified(dbAuctionList.get(0).getLastModified());
			
			derivedItemData.setNumberOfAuctions(determineNumberOfAuctions(dbAuctionListForItem));
			
			if (dbAuctionListForItem.size() > 0) {
				derivedItemData.setPricePer1(dbAuctionListForItem.get(0).determinePrice());
			}
			
			derivedItemData.setPricePer10(calculateAverage(dbAuctionListForItem, 10));
			
			derivedItemData.setPricePer100(calculateAverage(dbAuctionListForItem, 100));
			
			derivedItemData.setPricePer1000(calculateAverage(dbAuctionListForItem, 1000));
			
		}
		
		return derivedItemDataList;
	}
	
	private Integer determineNumberOfAuctions(List<DbAuction> dbAuctionListForItem) {
		
		int sum = 0;
		
		for (DbAuction dbAuction : dbAuctionListForItem) {
			sum += dbAuction.getQuantity();
		}
		
		return sum;
	}

	/**
	 * Calculates the average value of the buyouts of the first numAuctionsToAverage auctions in the inputList.
	 * NOTE: One row in the inputList can respresent multiple auctions. This is determined by dbauction.quantity!
	 * 
	 * The result is truncated to an integer.
	 * 
	 * Return null if #auctions < inputSize.
	 * 
	 */
	private Long calculateAverage(List<DbAuction> inputList, int numAuctionsToAverage) {
		
		long sum = 0;
		int auctionsAdded = 0;
		
		int inputListIndex = 0;
		
		while (auctionsAdded < numAuctionsToAverage && inputListIndex < inputList.size() ) {
			
			// Don't add more items than we still need for the average, and don't add more items than the quantity of the current auction.
			int auctionsToAdd = Math.min(numAuctionsToAverage - auctionsAdded, inputList.get(inputListIndex).getQuantity());
			
			sum += auctionsToAdd * inputList.get(inputListIndex).determinePrice();
			auctionsAdded += auctionsToAdd;
			
			inputListIndex++;
		}
		
		if (auctionsAdded < numAuctionsToAverage) {
			// Not enough auctions available to calculate the average.
			return null;
		}
		
		return sum/numAuctionsToAverage;
		
	}

}
