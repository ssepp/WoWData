package wowData.business.db;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table( name = "DerivedItemData")
public class DerivedItemData {

	// generated PK
	private Long id;
	
	private Integer itemId;

	private Integer ownerRealmId;
	private Integer regionId;
	
	private Long pricePer1;
	private Long pricePer10;
	private Long pricePer100;
	private Long pricePer1000;
	private Integer numberOfAuctions;
	
	private Date lastModified;
	
	@GenericGenerator(name="table-hilo-generator", strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters={  @Parameter(name = "increment_size", value = "100")
							, @Parameter(name = "sequence_name", value = "DerivedItemDataSequence")
							, @Parameter(name = "initial_value", value = "1")
	})
	@GeneratedValue(generator="table-hilo-generator")
	@Id
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getNumberOfAuctions() {
		return numberOfAuctions;
	}

	public void setNumberOfAuctions(Integer numberOfAuctions) {
		this.numberOfAuctions = numberOfAuctions;
	}

	public Long getPricePer1() {
		return pricePer1;
	}

	public void setPricePer1(Long pricePer1) {
		this.pricePer1 = pricePer1;
	}

	public Long getPricePer10() {
		return pricePer10;
	}

	public void setPricePer10(Long pricePer10) {
		this.pricePer10 = pricePer10;
	}

	public Long getPricePer100() {
		return pricePer100;
	}

	public void setPricePer100(Long pricePer100) {
		this.pricePer100 = pricePer100;
	}

	public Long getPricePer1000() {
		return pricePer1000;
	}

	public void setPricePer1000(Long pricePer1000) {
		this.pricePer1000 = pricePer1000;
	}

	public Integer getOwnerRealmId() {
		return ownerRealmId;
	}

	public void setOwnerRealmId(Integer ownerRealmId) {
		this.ownerRealmId = ownerRealmId;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}


}
