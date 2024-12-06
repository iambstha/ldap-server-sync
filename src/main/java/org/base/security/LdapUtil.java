package org.base.security;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.base.model.User;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

@Slf4j
@Singleton
public class LdapUtil {

    private final String ldapUrl;
    private final String baseDn;
    private final String bindDn;
    private final String bindCredential;

    public LdapUtil() {
        log.info("LdapUtil instance is created");
        Config config = ConfigProvider.getConfig();
        this.ldapUrl = config.getValue("quarkus.security.ldap.dir-context.url", String.class);
        this.baseDn = config.getValue("quarkus.security.ldap.identity-mapping.search-base-dn", String.class);
        this.bindDn = config.getValue("quarkus.security.ldap.bind-dn", String.class);
        this.bindCredential = config.getValue("quarkus.security.ldap.bind-credential", String.class);
    }

    public User getUserInformation(String username) {
        LdapContext context = null;
        User user = null;
        try {
            context = getLdapContext();

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> results = context.search(baseDn, "(uid=" + username + ")", searchControls);

            if (results.hasMore()) {
                SearchResult result = results.next();

                if (result.getAttributes() != null) {
                    log.info("Found user: {}", result.getNameInNamespace());
                    user = User
                            .builder()
                            .username(username)
                            .email(getAttributeValue(result.getAttributes(), "mail"))
                            .firstName(getAttributeValue(result.getAttributes(), "givenname"))
                            .lastName(getAttributeValue(result.getAttributes(), "sn"))
                            .displayName(getAttributeValue(result.getAttributes(), "mail"))
                            .department(getAttributeValue(result.getAttributes(), "departmentNumber"))
                            .mobile(getAttributeValue(result.getAttributes(), "mobile"))
                            .build();
                }else{
                    log.error("No attributes found for user: {}", username);
                }
            }
        } catch (Exception e) {
            log.error("Error fetching user information: {}", e.getMessage());
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (Exception e) {
                    log.error("Error closing LDAP context during user information fetching: {}", e.getMessage());
                }
            }
        }
        return user;
    }

    private LdapContext getLdapContext() throws Exception {

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_PRINCIPAL, bindDn);
        env.put(Context.SECURITY_CREDENTIALS, bindCredential);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        return new InitialLdapContext(env, null);
    }

    private String getAttributeValue(Attributes attributes, String attributeName) {
        try {
            if (attributes != null && attributes.get(attributeName) != null) {
                return (String) attributes.get(attributeName).get();
            }
        } catch (Exception e) {
            log.error("Error retrieving attribute {}: {}", attributeName, e.getMessage());
        }
        return null;
    }

    private String getUserDnByUid(String uid) {
        LdapContext context = null;
        try {
            context = getLdapContext();

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> results = context.search(baseDn, "(uid=" + uid + ")", searchControls);

            if (results.hasMore()) {
                SearchResult result = results.next();
                return result.getNameInNamespace();
            } else {
                log.error("No user found with uid: {}", uid);
            }
        } catch (Exception e) {
            log.error("Error fetching user DN by uid {}: {}", uid, e.getMessage());
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (Exception e) {
                    log.error("Error closing LDAP context during fetching user by user dn: {}", e.getMessage());
                }
            }
        }
        return null;
    }

    public boolean authenticateUser(String username, String password) {
        LdapContext context = null;
        try {
            String userDn = getUserDnByUid(username);
            if (userDn == null) {
                log.error("User DN not found for username: {}", username);
                return false;
            }

            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_PRINCIPAL, userDn);
            env.put(Context.SECURITY_CREDENTIALS, password);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");

            log.info("Attempting bind with DN: {}", userDn);

            context = new InitialLdapContext(env, null);
            return true;
        } catch (Exception e) {
            log.error("Authentication failed for username {}: {}", username, e.getMessage());
            return false;
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (Exception e) {
                    log.error("Error closing LDAP context: {}", e.getMessage());
                }
            }
        }
    }

}
