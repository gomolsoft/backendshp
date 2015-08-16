package de.moso.config;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private static final Cache restShopApiAuthTokenCache = CacheManager.getInstance().getCache("restShopApiAuthTokenCache");
    public static final int HALF_AN_HOUR_IN_MILLISECONDS = 3 * 60 * 1000;

    @Scheduled(fixedRate = HALF_AN_HOUR_IN_MILLISECONDS)
    public void evictExpiredTokens() {
        logger.info("Evicting expired tokens");
        restShopApiAuthTokenCache.evictExpiredElements();
    }

    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    public void store(String token, Authentication authentication) {
        restShopApiAuthTokenCache.put(new Element(token, authentication));
    }

    public boolean contains(String token) {
        return restShopApiAuthTokenCache.get(token) != null;
    }

    public Authentication retrieve(String token) {
        return (Authentication) restShopApiAuthTokenCache.get(token).getObjectValue();
    }
}
