package wowData.business.wowapi.auction;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuctionList 
{
	@JsonProperty("auctions")
	private List<Auction> auction;
	
	public List<Auction> getAuctions() {
		return auction;
	}

	public void setAuctions(List<Auction> auction) {
		this.auction = auction;
	}
}
