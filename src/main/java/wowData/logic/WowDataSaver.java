package wowData.logic;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wowData.business.db.DbAuction;
import wowData.business.db.DbAuctionList;
import wowData.business.db.DerivedItemData;
import wowData.business.db.RealmData;
import wowData.business.support.Realm;

public class WowDataSaver {

	private DbAuctionList dbAuctionList;
	
	private List<DerivedItemData> derivedItemDataList;
	
	static final Logger log = LoggerFactory.getLogger(WowDataSaver.class);

	public boolean save() {

		Configuration cfg = new Configuration();
		cfg.configure();

		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();

		SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);

		try {

			Session session = sessionFactory.openSession();
			session.beginTransaction();

			log.info("Starting save of {} lines of dbAuctions", dbAuctionList.size());
			
			int dbAuctionNr = 0;
			
			for (DbAuction dbAuction : dbAuctionList) {
				session.save(dbAuction);
				dbAuctionNr++;
				
				if (dbAuctionNr % 100 == 0) {
					session.flush();
					session.clear();
				}
			}
			
			log.info("Starting save of {} lines of derivedItemData", derivedItemDataList.size());
			
			int derivedItemNr = 0;
			
			for (DerivedItemData derivedItemData : derivedItemDataList) {
				session.save(derivedItemData);
				derivedItemNr++;
				
				if (derivedItemNr % 100 == 0) {
					session.flush();
					session.clear();
				}
			}
			
			session.getTransaction().commit();
			session.close();

		} finally {
			if ( sessionFactory != null ) {
				sessionFactory.close();
			}
		}
		
		log.info("Save complete.");

		return true;

	}
	
	public void writePreviousLastModified(Realm realm, Date lastModified) {
		Configuration cfg = new Configuration();
		cfg.configure();
	
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
	
		SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);
	
		try {
	
			Session session = sessionFactory.openSession();
			session.beginTransaction();
	
			String realmNameRegion = realm.getName() + realm.getRegionSelection();
			RealmData realmData = new RealmData();
			realmData.setRealmNameRegion(realmNameRegion);
			realmData.setLastModified(lastModified);
			session.update(realmData);
			
			session.getTransaction().commit();
			session.close();
	
		} finally {
			if ( sessionFactory != null ) {
				sessionFactory.close();
			}
		}
	
	}

	public DbAuctionList getDbAuctionList() {
		return dbAuctionList;
	}

	public void setDbAuctionList(DbAuctionList dbAuctionList) {
		this.dbAuctionList = dbAuctionList;
	}

	public void setDerivedItemDataList(List<DerivedItemData> derivedItemDataList) {
		this.derivedItemDataList = derivedItemDataList;
	}

}
