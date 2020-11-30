package com.venosyd.open.repository.lib;

import com.venosyd.open.commons.util.ConfigReader;

/**
 * @author sergio lisan <sels@venosyd.com>
 */
public class RepositoryConfig extends ConfigReader {

    public static final RepositoryConfig INSTANCE = new RepositoryConfig();

    private RepositoryConfig() {
        super("repository");
    }
}