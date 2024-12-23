package org.shipping.service;

import java.util.UUID;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SecurityService {

    @Inject
    SecurityIdentity securityIdentity;

    public UUID getCurrentUserId() {
        return UUID.fromString(securityIdentity.getPrincipal().getName());
    }
}
