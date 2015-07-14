package wowData.business.db;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wowData.business.support.TimeLeftTranslator;
import wowData.business.wowapi.auction.Auction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table( name = "Auction" )
@JsonIgnoreProperties(ignoreUnknown = true)
public class DbAuction implements Comparable<DbAuction> {

	static final Logger logger = LoggerFactory.getLogger(DbAuction.class);
	
	private static final Long NO_BUYOUT = new Long(0);

	// Data directly from auction JSON
	private String auc;
	private Integer item;
	private String owner;
	private Long bid;
	private Long buyout;
	private Integer quantity;
	private Integer rand;
	private Long seed;
	private Integer context;

	// Data normalized from auction JSON
	private Integer ownerRealmId;
	private Integer timeLeftId;

	// Data from other sources
	private Date lastModified;
	private Integer regionId;

	// generated PK
	private Long id;

	DbAuction(Auction auction) {
		this.auc = auction.getAuc();
		this.item = auction.getItem();
		this.owner = auction.getOwner();
		this.bid = auction.getBid();
		this.buyout = auction.getBuyout();
		this.quantity = auction.getQuantity();
		this.rand = auction.getRand();
		this.seed = auction.getSeed();
		this.context = auction.getContext();
		this.lastModified = auction.getLastModified();

		this.timeLeftId = TimeLeftTranslator.valueOf(auction.getTimeLeft()).getId();

	}

	@Override
	public int compareTo(DbAuction otherDbAuction) {
		
		return determinePrice().compareTo(otherDbAuction.determinePrice());
	}
	
	@Override
	public String toString() {
		return "itemId: " + item
				+ "\n quantity: " + quantity
				+ "\n buyout: " + buyout
				+ "\n price: " + determinePrice()
				;
	}
	
	public Long determinePrice() {
		return buyout / new Long(quantity);
	}
	
	public boolean determineHasBuyout() {
		return !NO_BUYOUT.equals(buyout);
	}

	@GenericGenerator(name="table-hilo-generator", strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters={  @Parameter(name = "increment_size", value = "100")
							, @Parameter(name = "sequence_name", value = "DbAuctionSequence")
							, @Parameter(name = "initial_value", value = "133215020")
	})
	@GeneratedValue(generator="table-hilo-generator")
	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getRand() {
		return rand;
	}

	public void setRand(Integer rand) {
		this.rand = rand;
	}

	public Integer getContext() {
		return context;
	}

	public void setContext(Integer context) {
		this.context = context;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getAuc() {
		return auc;
	}

	public void setAuc(String auc) {
		this.auc = auc;
	}

	public Long getBid() {
		return bid;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}

	public Long getBuyout() {
		return buyout;
	}

	public void setBuyout(Long buyout) {
		this.buyout = buyout;
	}

	public Long getSeed() {
		return seed;
	}

	public void setSeed(Long seed) {
		this.seed = seed;
	}


	public Integer getOwnerRealmId() {
		return ownerRealmId;
	}


	public void setOwnerRealmId(Integer ownerRealmId) {
		this.ownerRealmId = ownerRealmId;
	}


	public Integer getTimeLeftId() {
		return timeLeftId;
	}


	public void setTimeLeftId(Integer timeLeftId) {
		this.timeLeftId = timeLeftId;
	}


	public Integer getRegionId() {
		return regionId;
	}


	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}


}

