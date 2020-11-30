package com.venosyd.open.repository.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
@ApplicationPath(RepositoryRS.REPOSITORY_BASE_URI)
public class RepositoryRESTfulAPI extends Application {

    public Set<Class<?>> getClasses() {
        var classes = new HashSet<Class<?>>();
        classes.add(RepositoryRSImpl.class);

        return classes;
    }
}
