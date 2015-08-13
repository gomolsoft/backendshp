package de.moso.service;

import de.moso.config.ExternalServiceAuthenticator;
import de.moso.domain.DomainUser;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.UUID;

public class SomeExternalServiceAuthenticator implements ExternalServiceAuthenticator {
    public final static String ROLE_DOMAIN_USER  =  "ROLE_DOMAIN_USER";
    public final static String ROLE_WEB_USER     =  "ROLE_WEB_USER";
    public final static String WEB_USER          =  "WebUser";

    @Override
    public AuthenticatedExternalWebService authenticate(String username, String password) {
        final ExternalWebServiceStub externalWebService = new ExternalWebServiceStub();

        // Do all authentication mechanisms required by external web service protocol and validated response.
        // Throw descendant of Spring AuthenticationException in case of unsucessful authentication. For example BadCredentialsException

        // ...
        // ...

        // If authentication to external service succeeded then create authenticated wrapper with proper Principal and GrantedAuthorities.
        // GrantedAuthorities may come from external service authentication or be hardcoded at our layer as they are here with ROLE_DOMAIN_USER

        final AuthenticatedExternalWebService authenticatedExternalWebService;

        if (WEB_USER.equals(username))
            authenticatedExternalWebService  = new AuthenticatedExternalWebService(new DomainUser(username, UUID.randomUUID().toString()), null, AuthorityUtils.commaSeparatedStringToAuthorityList(ROLE_WEB_USER));
        else
            authenticatedExternalWebService  = new AuthenticatedExternalWebService(new DomainUser(username, UUID.randomUUID().toString()), null, AuthorityUtils.commaSeparatedStringToAuthorityList(ROLE_DOMAIN_USER+","+ROLE_WEB_USER));

        authenticatedExternalWebService.setExternalWebService(externalWebService);

        return authenticatedExternalWebService;
    }
}
