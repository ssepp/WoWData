package wowData.logic;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import wowData.business.db.DbAuctionList;
import wowData.business.db.RealmData;
import wowData.business.support.Realm;
import wowData.business.wowapi.auction.AuctionData;

public class WowDataReader {

	private String url;

	private String realmName;

	private DbAuctionList dbAuctionList;

	static final Logger log = LoggerFactory.getLogger(WowDataReader.class);

	public boolean readAuctionData() {

		RestTemplate restTemplate = new RestTemplate();
		AuctionData auctionData = null;

		log.info("Reading from url: {}", url);
		
		try {
			auctionData = restTemplate.getForObject(url, AuctionData.class);
		} catch (Exception e) {
			log.error("Exception for restTemplate",e);
			log.error("Exception for restTemplate with URL {}. Trying again...", url);

			try {
				auctionData = restTemplate.getForObject(url, AuctionData.class);
				log.info("Second invocation of resttemplate was succesful ^_^");
			} catch (Exception e2) {
				log.error("Exception for restTemplate again!");
				log.error("Exception for restTemplate", e2);
				return false;
			}
		} 

		dbAuctionList = new DbAuctionList(auctionData.getAuctionList().getAuctions());
		realmName = auctionData.getRealm().getName();

		return true;
	}
	

	public Date determinepreviousLastModified(Realm realm) {
		
		Configuration cfg = new Configuration();
		cfg.configure();

		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();

		SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);
		
		RealmData realmData;
		
		try {

			Session session = sessionFactory.openSession();
			session.beginTransaction();
			
			String realmNameRegion = realm.getName() + realm.getRegionSelection();
			realmData = (RealmData)  session.get(RealmData.class, realmNameRegion);
			
			session.getTransaction().rollback();
			session.close();

		} finally {
			if ( sessionFactory != null ) {
				sessionFactory.close();
			}
		}
		
		if (realmData != null) {
			return realmData.getLastModified();
		} 
		
		return null;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public DbAuctionList getDbAuctionList() {
		return dbAuctionList;
	}

	public void setDbAuctionList(DbAuctionList dbAuctionList) {
		this.dbAuctionList = dbAuctionList;
	}

	public String getRealmName() {
		return realmName;
	}


}
