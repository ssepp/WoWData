package wowData.business.wowapi.auction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuctionData {

    @JsonProperty("auctions")
    private AuctionList auctionList;
    
    private Realm realm;

	public AuctionList getAuctionList() {
		return auctionList;
	}

	public void setAuctionList(AuctionList auctionList) {
		this.auctionList = auctionList;
	}

	public Realm getRealm() {
		return realm;
	}

	public void setRealm(Realm realm) {
		this.realm = realm;
	}


}