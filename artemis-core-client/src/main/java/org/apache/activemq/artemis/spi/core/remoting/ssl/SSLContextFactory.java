/*
 * Copyright 2020 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.spi.core.remoting.ssl;

import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.activemq.artemis.api.config.ActiveMQDefaultConfiguration;
import org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants;
import org.apache.activemq.artemis.core.remoting.impl.ssl.SSLSupport;
import org.apache.activemq.artemis.utils.ConfigurationHelper;
import org.jboss.logging.Logger;

/**
 *
 */
public interface SSLContextFactory extends Comparable<SSLContextFactory> {
   Logger LOGGER = Logger.getLogger(SSLContextFactory.class);

   SSLContext getSSLContext(Map<String, Object> configuration) throws Exception;

   default SSLContext createSSLContext(Map<String, Object> configuration) throws Exception {
      final StringBuilder builder = new StringBuilder();
      configuration.forEach((k,v) -> builder.append("\r\n").append(k).append("=").append(v));
      LOGGER.debugf("Creating SSL context with configuration %s", builder.toString());
      String keyStoreProvider = ConfigurationHelper.getStringProperty(TransportConstants.KEYSTORE_PROVIDER_PROP_NAME, TransportConstants.DEFAULT_KEYSTORE_PROVIDER, configuration);
      String keyStorePath = ConfigurationHelper.getStringProperty(TransportConstants.KEYSTORE_PATH_PROP_NAME, TransportConstants.DEFAULT_KEYSTORE_PATH, configuration);
      String keyStorePassword = ConfigurationHelper.getPasswordProperty(TransportConstants.KEYSTORE_PASSWORD_PROP_NAME, TransportConstants.DEFAULT_KEYSTORE_PASSWORD, configuration, ActiveMQDefaultConfiguration.getPropMaskPassword(), ActiveMQDefaultConfiguration.getPropPasswordCodec());
      String trustStoreProvider = ConfigurationHelper.getStringProperty(TransportConstants.TRUSTSTORE_PROVIDER_PROP_NAME, TransportConstants.DEFAULT_TRUSTSTORE_PROVIDER, configuration);
      String trustStorePath = ConfigurationHelper.getStringProperty(TransportConstants.TRUSTSTORE_PATH_PROP_NAME, TransportConstants.DEFAULT_TRUSTSTORE_PATH, configuration);
      String trustStorePassword = ConfigurationHelper.getPasswordProperty(TransportConstants.TRUSTSTORE_PASSWORD_PROP_NAME, TransportConstants.DEFAULT_TRUSTSTORE_PASSWORD, configuration, ActiveMQDefaultConfiguration.getPropMaskPassword(), ActiveMQDefaultConfiguration.getPropPasswordCodec());
      String crlPath = ConfigurationHelper.getStringProperty(TransportConstants.CRL_PATH_PROP_NAME, TransportConstants.DEFAULT_CRL_PATH, configuration);
      boolean trustAll = ConfigurationHelper.getBooleanProperty(TransportConstants.TRUST_ALL_PROP_NAME, TransportConstants.DEFAULT_TRUST_ALL, configuration);
      String trustManagerFactoryPlugin = ConfigurationHelper.getStringProperty(TransportConstants.TRUST_MANAGER_FACTORY_PLUGIN_PROP_NAME, TransportConstants.DEFAULT_TRUST_MANAGER_FACTORY_PLUGIN, configuration);
      return new SSLSupport()
            .setKeystoreProvider(keyStoreProvider)
            .setKeystorePath(keyStorePath)
            .setKeystorePassword(keyStorePassword)
            .setTruststoreProvider(trustStoreProvider)
            .setTruststorePath(trustStorePath)
            .setTruststorePassword(trustStorePassword)
            .setTrustAll(trustAll)
            .setCrlPath(crlPath)
            .setTrustManagerFactoryPlugin(trustManagerFactoryPlugin)
            .createContext();
   }

   int getPriority();

   @Override
   default int compareTo(SSLContextFactory other) {
      return this.getPriority() - other.getPriority();
   }
}
