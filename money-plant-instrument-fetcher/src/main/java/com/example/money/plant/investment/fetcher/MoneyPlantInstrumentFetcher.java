package com.example.money.plant.investment.fetcher;

import com.example.money.plant.investment.fetcher.entity.KiteConnectEntity;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionFactory;
import org.apache.geode.cache.Scope;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnablePool;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.enabled;

@SpringBootApplication(scanBasePackages = { "com.example" })
@ClientCacheApplication(name = "MoneyPlantInstrumentFetcher")
@EnableLocator
@EnableEntityDefinedRegions(basePackageClasses = KiteConnectEntity.class,
		clientRegionShortcut = ClientRegionShortcut.PROXY)
public class MoneyPlantInstrumentFetcher {

	public static void main(String[] args) {
		SpringApplication.run(MoneyPlantInstrumentFetcher.class);
	}

	@Bean
	@Lazy
	ApplicationRunner runAdditionalClientCacheInitialization(GemFireCache gemfireCache) {

		return args -> {
			ClientCache clientCache = (ClientCache) gemfireCache;
		};
	}

	@Bean("LoginToken")
	@Lazy
	ReplicatedRegionFactoryBean myReplicateRegion(GemFireCache gemFireCache) {
		ReplicatedRegionFactoryBean regionFactoryBean = new ReplicatedRegionFactoryBean<>();
		regionFactoryBean.setCache(gemFireCache);
		regionFactoryBean.setScope(Scope.GLOBAL);
		return regionFactoryBean;
	}

}
