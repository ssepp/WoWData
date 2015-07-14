package wowData.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wowData.business.db.DbAuction;
import wowData.business.db.DbAuctionList;
import wowData.business.db.Item;

public class ItemManager {

	private static final Integer ITEM_CLASS_MATERIAL = Integer.valueOf(7);

	/** ItemIds of all trade-materials */
	private Set<Integer> tradeMaterialItemIds;
	
	/** ItemIds of all items that are of specific interest. */
	private List<Integer> targettedTradeItemIds; 
	
	static final Logger log = LoggerFactory.getLogger(ItemManager.class);
	
	public ItemManager() {
		
		targettedTradeItemIds = new ArrayList<Integer>();
		tradeMaterialItemIds = new HashSet<Integer>();
	}
	
	public void initialize() throws IOException {

		WowDataConfiguration wowDataConfiguration = new WowDataConfiguration();
		List<Item> materialItemList = loadMaterialItemList();

		initTargettedTradeItemIds(materialItemList, wowDataConfiguration);

		initTradeMaterialItemIds(materialItemList, wowDataConfiguration);

	}
	
	
	/**
	 * Fill instance variable targettedItemIds.
	 */
	private void initTargettedTradeItemIds(List<Item> materialItemList, WowDataConfiguration wowDataConfiguration) throws IOException {
		
		targettedTradeItemIds.clear();
		
		// Load the targetted materials from the configuration. 
		List<String> targettedTradeItemNames = wowDataConfiguration.determineTargettedTradeItems();
		
		// Make the names lowercase for case-insensitive matching.
		List<String> lowerCaseTargettedTradeItemNames = new ArrayList<>();
		for (String name : targettedTradeItemNames) {
			lowerCaseTargettedTradeItemNames.add(name.toLowerCase());
		}
		
		List<String> foundItemNames = new ArrayList<>();
		
		for (Item item: materialItemList) {
			
			if (lowerCaseTargettedTradeItemNames.contains(item.getName().toLowerCase())) {
				targettedTradeItemIds.add(item.getId());
				log.info("Added targetted item: \"{}\"", item.getName());
				
				if (!foundItemNames.contains(item.getName())) {
					foundItemNames.add(item.getName().toLowerCase());
				} else {
					log.error("Targetted item '{}' found multiple times in Item database!", item.getName());
				}
				
			}
		}
		
		// Did we fail to find any items in the item database?
		for (String targettedItem : lowerCaseTargettedTradeItemNames) {
			if (!foundItemNames.contains(targettedItem)) {
				log.error("Targetted item '{}' not found in Item database!", targettedItem);
			}
		}
		
	}
	
	/**
	 * Fill instance variable tradeMaterialItemIds.
	 */
	private void initTradeMaterialItemIds(List<Item> materialItemList, WowDataConfiguration wowDataConfiguration) {
		
		tradeMaterialItemIds.clear();
		
		// Every item in materialItemList is a material, so simply add all ids of these items.
		for (Item item: materialItemList) {
			tradeMaterialItemIds.add(item.getId());
		}
		
	}

	/**
	 * Retrieves a list of all items that are materials from the database.
	 */
	private List<Item> loadMaterialItemList() {
		
		List<Item> materialItemList;
		
		Configuration cfg = new Configuration();
		cfg.configure();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
		SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);

		try {

			Session session = sessionFactory.openSession();
			session.beginTransaction();

			Criteria criteria = session.createCriteria(Item.class)
					.add(Restrictions.eq("itemClass", ITEM_CLASS_MATERIAL));

			materialItemList = criteria.list();

			session.close();
			

		} finally {
			if ( sessionFactory != null ) {
				sessionFactory.close();
			}
		}
		
		return materialItemList;
	}


	private boolean determineIsTradeMaterial(Integer itemId) {
		return tradeMaterialItemIds.contains(itemId);
	}

	public void filterTradeMaterials(DbAuctionList dbAuctionList) {
		
		Iterator<DbAuction> iterator = dbAuctionList.iterator();
		
		while (iterator.hasNext()) {
			
			DbAuction dbAuction = iterator.next();
			
			if (!determineIsTradeMaterial(dbAuction.getItem())) {
				iterator.remove();
			}
			
		}
		
	}

	public Set<Integer> getTradeMaterialItemIds() {
		return tradeMaterialItemIds;
	}

	public List<Integer> getTargettedTradeItemIds() {
		return targettedTradeItemIds;
	}

}
