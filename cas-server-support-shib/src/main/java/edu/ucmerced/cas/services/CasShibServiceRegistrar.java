/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package edu.ucmerced.cas.services;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.authentication.principal.Service;
import javax.validation.constraints.NotNull;

/**
 * The CASShib service registrar is responsible for reading in the casshib
 * registrations file (normally casshib-service-registrations.xml) and
 * registering the service with the ServicesManager.
 * 
 * @author Brian Koehmstedt
 * @version $Revision$ $Date$
 * @since 3.3.1a
 */
public class CasShibServiceRegistrar {
    /** Log instance for logging events, info, warnings, errors, etc. */
    private final Log log = LogFactory.getLog(this.getClass());

    /**
     * An exception indicating an error with loading and parsing the casshib
     * registrations XML file.
     * 
     * @author Brian Koehmstedt
     * @since 3.3.1a
     */
    public static class CasShibServiceRegistrarException extends Exception {
        public CasShibServiceRegistrarException(String msg) {
            super(msg);
        }

        public CasShibServiceRegistrarException(Throwable cause) {
            super(cause);
        }

        public CasShibServiceRegistrarException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    @NotNull
    private String casShibServiceRegistrationsResourceName;

    @NotNull
    private ServicesManager servicesManager;

    private boolean isInitialized = false;

    protected synchronized void initialize()
        throws CasShibServiceRegistrarException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream is = getClass().getClassLoader().getResourceAsStream(
                casShibServiceRegistrationsResourceName);
            if (is == null) {
                throw new CasShibServiceRegistrarException("Couldn't find "
                    + casShibServiceRegistrationsResourceName
                    + " in the classpath");
            }
            try {
                Document regDoc = builder.parse(is);
                addEntries(regDoc.getDocumentElement());
            } finally {
                is.close();
            }
        } catch (ParserConfigurationException e) {
            throw new CasShibServiceRegistrarException(e);
        } catch (SAXException e) {
            throw new CasShibServiceRegistrarException(e);
        } catch (IOException e) {
            throw new CasShibServiceRegistrarException(e);
        }

        this.isInitialized = true;
    }

    public CasShibRegisteredService findService(Service service)
        throws CasShibServiceRegistrarException {
        if (!isInitialized) {
            // this shouldn't happen here, but just in case
            initialize();
        }

        RegisteredService registeredService = servicesManager
            .findServiceBy(service);
        if (registeredService instanceof CasShibRegisteredService) {
            return (CasShibRegisteredService) registeredService;
        }
        return null;
    }

    public CasShibRegisteredService findServiceById(String serviceId)
        throws CasShibServiceRegistrarException {
        if (!isInitialized) {
            // this shouldn't happen here, but just in case
            initialize();
        }

        for (RegisteredService entry : servicesManager.getAllServices()) {
            if (entry instanceof CasShibRegisteredService
                && entry.getServiceId().equals(serviceId)) {
                return ((CasShibRegisteredService) entry);
            }
        }

        return null;
    }

    public CasShibRegisteredService findServiceByAppName(String appName)
        throws CasShibServiceRegistrarException {
        if (!isInitialized) {
            // this shouldn't happen here, but just in case
            initialize();
        }

        for (RegisteredService entry : servicesManager.getAllServices()) {
            if (entry instanceof CasShibRegisteredService
                && entry.getName().equals(appName)) {
                return ((CasShibRegisteredService) entry);
            }
        }

        return null;
    }

    protected void addEntries(Element element) {
        if (element.getTagName().equals("service")) {
            String id = element.getAttribute("id");
            String passcode = element.getAttribute("passcode");
            String appname = element.getAttribute("appname");

            if (id != null && passcode != null) {
                registerServiceWithCAS(id, appname, passcode);
                log.info("registered service " + id + " with an appname of "
                    + appname + " to the services manager");
            }
        }

        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                addEntries((Element) nl.item(i));
            }
        }
    }

    public void setCasShibServiceRegistrationsResourceName(String resourceName)
        throws CasShibServiceRegistrarException {
        this.casShibServiceRegistrationsResourceName = resourceName;
        // initialize once Spring sets all the attributes we need
        if (!this.isInitialized
            && this.casShibServiceRegistrationsResourceName != null
            && this.servicesManager != null) {
            initialize();
        }
    }

    public void setServicesManager(ServicesManager servicesManager)
        throws CasShibServiceRegistrarException {
        this.servicesManager = servicesManager;
        // initialize once Spring sets all the attributes we need
        if (!this.isInitialized
            && this.casShibServiceRegistrationsResourceName != null
            && this.servicesManager != null) {
            initialize();
        }
    }

    public void registerServiceWithCAS(String id, String appName,
        String passcode) {
        CasShibRegisteredService rs = new CasShibRegisteredService();

        rs.setServiceId(id);
        rs.setName(appName);
        rs.setPasscode(passcode);

        rs.setEnabled(true);
        rs.setDescription(appName);
        rs.setSsoEnabled(true); // ?
        rs.setAnonymousAccess(false);
        rs.setAllowedToProxy(true); // ?

        // don't let ServicesManager manage attribute release policy. Shibboleth
        // is doing this for us.
        rs.setIgnoreAttributes(true);

        // other attributes:
        // allowedAttributes - not relevant because of IgnoreAttributes
        // id - autogenerated
        // theme, evaluationOrder- can be left at default

        servicesManager.save(rs);
    }
}
