package wowData.business.db;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "RealmData")
public class RealmData {

	// realmname + region
	@Id
	private String realmNameRegion;
	
	private Date lastModified;

	public String getRealmNameRegion() {
		return realmNameRegion;
	}

	public void setRealmNameRegion(String realmNameRegion) {
		this.realmNameRegion = realmNameRegion;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

}
