package com.wdf.fudoc.spring;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author wangdingfu
 * @date 2022-08-25 11:40:45
 */
public interface SpringConfigFileConstants {


    String RESOURCE = "resources";

    String YAML = "yaml";
    String YML = "yml";
    String PROPERTIES = "properties";


    String APPLICATION = "application";
    String BOOTSTRAP = "bootstrap";

    Set<String> EXTENSIONS = Sets.newHashSet(YAML, YML, PROPERTIES);
    Set<String> CONFIG_FILE_NAMES = Sets.newHashSet(BOOTSTRAP, APPLICATION);


    String SPLIT = "-";

    String ENV_KEY = "spring.profiles.active";
    String PROFILES = "spring.profiles";
    String SERVER_PORT_KEY = "server.port";
    String CONTEXT_PATH_KEY = "server.servlet.context-path";


    String MAVEN_PROFILES = "@profiles.active@";


    String DEFAULT_ENV = "application";

    int DEFAULT_SERVER_PORT = 8080;
}
