package com.mycompany.myapp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.User.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.Aml.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ApplicationType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ApplicationType.class.getName() + ".securityInterviews");
            createCache(cm, com.mycompany.myapp.domain.HostingType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.HostingType.class.getName() + ".hostingPlatforms");
            createCache(cm, com.mycompany.myapp.domain.HostingPlatform.class.getName());
            createCache(cm, com.mycompany.myapp.domain.HostingPlatform.class.getName() + ".hostings");
            createCache(cm, com.mycompany.myapp.domain.PersonalDataRegion.class.getName());
            createCache(cm, com.mycompany.myapp.domain.PersonalDataRegion.class.getName() + ".personalDataTypes");
            createCache(cm, com.mycompany.myapp.domain.PersonalDataType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.PersonalDataType.class.getName() + ".personalData");
            createCache(cm, com.mycompany.myapp.domain.IcrfStatus.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Icrf.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SensitiveDataType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SensitiveDataType.class.getName() + ".sensitiveData");
            createCache(cm, com.mycompany.myapp.domain.Psat.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SecurityInterview.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SecurityInterview.class.getName() + ".hostings");
            createCache(cm, com.mycompany.myapp.domain.SecurityInterview.class.getName() + ".personalData");
            createCache(cm, com.mycompany.myapp.domain.SecurityInterview.class.getName() + ".icrfs");
            createCache(cm, com.mycompany.myapp.domain.SecurityInterview.class.getName() + ".sensitiveData");
            createCache(cm, com.mycompany.myapp.domain.SecurityInterview.class.getName() + ".complementaryQuestions");
            createCache(cm, com.mycompany.myapp.domain.Hosting.class.getName());
            createCache(cm, com.mycompany.myapp.domain.PersonalData.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SensitiveData.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Availability.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ComplementaryQuestion.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
