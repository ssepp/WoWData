package wowData.business.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wowData.business.support.Realm;
import wowData.business.support.RegionSelection;
import wowData.business.wowapi.auction.Auction;

public class DbAuctionList extends ArrayList<DbAuction>{
	
	static final Logger log = LoggerFactory.getLogger(DbAuctionList.class);
	
	public DbAuctionList(List<Auction> auctionList) {
		
		for (Auction auction : auctionList) {
			this.add(new DbAuction(auction));
		}
		
	}

	public void fillLastModified(Date lastModified) {
		
		for (DbAuction dbAction : this) {
			dbAction.setLastModified(lastModified);
		}
	}

	public void fillRegion(RegionSelection regionSelection) {

		for (DbAuction dbAction : this) {
			dbAction.setRegionId(regionSelection.getId());
		}
		
	}

	public void fillRealm(String realmName) {
		
		for (DbAuction dbAction : this) {
			
			Integer realmId = Realm.realmNameIdMap.get(realmName);
			
			if (realmId == null) {
				log.error("RealmId not found for realmname: '{}'", realmName);
			}
			
			dbAction.setOwnerRealmId(realmId);
		}
		
	}

}
