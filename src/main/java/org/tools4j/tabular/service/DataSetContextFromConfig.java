package org.tools4j.tabular.service;

import org.apache.log4j.Logger;
import org.tools4j.tabular.config.ConfigReader;
import org.tools4j.tabular.properties.PropertiesRepo;
import org.tools4j.tabular.properties.PropertiesRepoWithAdditionalTweakedContantStyleProperties;

/**
 * User: ben
 * Date: 31/10/17
 * Time: 4:36 PM
 */
public class DataSetContextFromConfig {
    private final static Logger LOG = Logger.getLogger(DataSetContextFromConfig.class);
    private final ConfigReader config;

    public DataSetContextFromConfig(final ConfigReader config) {
        this.config = config;
    }

    public DataSetContext load() {
        LOG.info("Loading DataSet properties");
        PropertiesRepo configFileProperties = new PropertiesRepo(config.getConfigPropertiesFiles());
        PropertiesRepo configLocalFileProperties = new PropertiesRepo(config.getLocalConfigPropertiesFiles());

        final PropertiesRepo allProperties = new PropertiesRepo();
        LOG.info("==================== config.properties file properties ====================");
        LOG.info(configFileProperties.toPrettyString());
        allProperties.putAll(configFileProperties);

        if(!configLocalFileProperties.isEmpty()) {
            LOG.info("==================== config-local.properties file properties ====================");
            LOG.info(configLocalFileProperties.toPrettyString());
            allProperties.putAll(configLocalFileProperties);
        }

        return load(configFileProperties);
    }

    private DataSetContext load(PropertiesRepo properties) {
        LOG.info("Loading DataSet CSV");
        final DataSetFromFiles dataSetFromFiles = new DataSetFromFiles(config.getTableCsvFiles());
        DataSet dataSet = dataSetFromFiles.get();
        final PropertiesRepo environmentVariables = new PropertiesRepoWithAdditionalTweakedContantStyleProperties(new EnvironmentVariables().load()).load();
        final PropertiesRepo systemProperties = new PropertiesRepoWithAdditionalTweakedContantStyleProperties(new SystemVariables().load()).load();
        return new DataSetContextLoader(dataSet, properties, environmentVariables, systemProperties).load();
    }
}
