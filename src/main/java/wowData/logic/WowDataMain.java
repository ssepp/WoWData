package wowData.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wowData.business.support.Realm;


// TODO: DI, unit-tests
// TODO: compressing database?
// TODO: javadoc
// TODO: One session per realm?
// TODO: Combine Realm and RealmData tables
// TODO: Seperate reading JSON data and translating JSON data to relational data.

public class WowDataMain {

	static final Logger log = LoggerFactory.getLogger(WowDataMain.class);

	public static void main(String[] args) throws Exception {

		log.info("========= STARTING PROGRAM =============.");

		try {

			WowDataConfiguration wowDataConfiguration = new WowDataConfiguration();
			List<Realm> realmList = wowDataConfiguration.determineRealmList();

			final ItemManager itemManager = new ItemManager();
			itemManager.initialize();

			ExecutorService executorService = null;
			executorService =  Executors.newFixedThreadPool(wowDataConfiguration.determineThreadPoolSize());
			List<Future<Boolean>> realmProcessorFutures = new ArrayList<Future<Boolean>>();

			for (Realm realm: realmList) {

				// Copy realm to a final variable so we can use it in the callable
				final Realm realmForCallable = realm;

				realmProcessorFutures.add(executorService.submit(new Callable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						RealmProcessor realmProcessor = new RealmProcessor(itemManager);
						return realmProcessor.process(realmForCallable);
					}

				}));

			}

			for(Future<Boolean> realmProcesserFuture : realmProcessorFutures) {
				realmProcesserFuture.get();
			}

		} catch(Exception e) {
			log.error("Fatal exception in WowDataMain", e);
			log.info("========= PROGRAM COMPLETED WITH FATAL EXCEPTION =============.");
			throw(e);
		}

		log.info("========= PROGRAM COMPLETED =============.");

	}

}
