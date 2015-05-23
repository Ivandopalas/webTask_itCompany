package com.epam.it_company.dao.util;

import java.util.ResourceBundle;

/**
 * Manager of data base info resources.
 */
public class DaoResourceManager {
    private final static DaoResourceManager instance = new DaoResourceManager();
    private ResourceBundle bundle;
    private static final String BUNDLE_NAME = "config";
    private DaoResourceManager(){
        bundle = ResourceBundle.getBundle(BUNDLE_NAME);
    }

    /**
     * Get instance of DBResourceManager
     * @return instance of current class
     */
    public static DaoResourceManager getInstance(){
        return instance;
    }

    /**
     * Getting info from resource file by key.
     * @return Current value of key
     */
    public String getValue(String key){
        return bundle.getString(key);
    }

}
