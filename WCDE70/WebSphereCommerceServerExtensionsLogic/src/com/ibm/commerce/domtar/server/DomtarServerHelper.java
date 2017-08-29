package com.ibm.commerce.domtar.server;

import com.ibm.commerce.server.WcsApp;

public class DomtarServerHelper {
	private static boolean isDevEnv;
	
	private static final String INST_KEY = "Instance/InstanceName";
	
	static {
        isDevEnv = "demo".equals(WcsApp.configProperties.getValue(INST_KEY));
    }

    /**
     *  Empty constructor
     */
    private DomtarServerHelper() {
        // So as to not inadvertently instantiate this class
    }

    /**
     * Is this a development environment
     * @return boolean
     */
    public static boolean isDevelopmentEnvironment() {
        return isDevEnv;
    }
}
