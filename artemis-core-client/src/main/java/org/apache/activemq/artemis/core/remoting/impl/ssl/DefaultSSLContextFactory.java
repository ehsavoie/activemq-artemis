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
package org.apache.activemq.artemis.core.remoting.impl.ssl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants;
import org.apache.activemq.artemis.spi.core.remoting.ssl.SSLContextFactory;
import org.apache.activemq.artemis.utils.ConfigurationHelper;

/**
 * Default SSLContextFactory providing a cache using the SSL_CONTEXT_PROP_NAME value as key.
 */
public class DefaultSSLContextFactory implements SSLContextFactory {

   private static final Map<String, SSLContext> SSL_CONTEXTS = Collections.synchronizedMap(new HashMap<>());

   @Override
   public void clearSSLContexts() {
      SSL_CONTEXTS.clear();
   }

   @Override
   public SSLContext getSSLContext(Map<String, Object> configuration,
           String keystoreProvider, String keystorePath, String keystorePassword,
           String truststoreProvider, String truststorePath, String truststorePassword,
           String crlPath, String trustManagerFactoryPlugin, boolean trustAll) throws Exception {
      String sslContextName = getSSLContextName(configuration);
      if (!SSL_CONTEXTS.containsKey(sslContextName)) {
         SSL_CONTEXTS.put(sslContextName, createSSLContext(configuration,
              keystoreProvider, keystorePath, keystorePassword,
              truststoreProvider, truststorePath, truststorePassword,
              crlPath, trustManagerFactoryPlugin, trustAll));
      }
      return SSL_CONTEXTS.get(sslContextName);
   }

   protected SSLContext createSSLContext(Map<String, Object> configuration,
           String keystoreProvider, String keystorePath, String keystorePassword,
           String truststoreProvider, String truststorePath, String truststorePassword,
           String crlPath, String trustManagerFactoryPlugin, boolean trustAll) throws Exception {
      final StringBuilder builder = new StringBuilder();
      configuration.forEach((k, v) -> builder.append("\r\n").append(k).append("=").append(v));
      LOGGER.debugf("Creating SSL context with configuration %s", builder.toString());
      boolean useDefaultSslContext = ConfigurationHelper.getBooleanProperty(TransportConstants.USE_DEFAULT_SSL_CONTEXT_PROP_NAME, TransportConstants.DEFAULT_USE_DEFAULT_SSL_CONTEXT, configuration);
      if (useDefaultSslContext) {
         return SSLContext.getDefault();
      }
      return new SSLSupport()
               .setKeystoreProvider(keystoreProvider)
               .setKeystorePath(keystorePath)
               .setKeystorePassword(keystorePassword)
               .setTruststoreProvider(truststoreProvider)
               .setTruststorePath(truststorePath)
               .setTruststorePassword(truststorePassword)
               .setTrustAll(trustAll)
               .setCrlPath(crlPath)
               .setTrustManagerFactoryPlugin(trustManagerFactoryPlugin)
               .createContext();
   }

   protected String getSSLContextName(Map<String, Object> configuration) {
      String sslContextName = ConfigurationHelper.getStringProperty(TransportConstants.SSL_CONTEXT_PROP_NAME, null, configuration);
      if (sslContextName == null) {
         String keyStorePath = ConfigurationHelper.getStringProperty(TransportConstants.KEYSTORE_PATH_PROP_NAME, TransportConstants.DEFAULT_KEYSTORE_PATH, configuration);
         if (keyStorePath != null) {
            return keyStorePath + '_' + ConfigurationHelper.getStringProperty(TransportConstants.KEYSTORE_PROVIDER_PROP_NAME, TransportConstants.DEFAULT_KEYSTORE_PROVIDER, configuration);
         }
         return ConfigurationHelper.getStringProperty(TransportConstants.TRUSTSTORE_PATH_PROP_NAME, TransportConstants.DEFAULT_TRUSTSTORE_PATH, configuration) + '_' + ConfigurationHelper.getStringProperty(TransportConstants.TRUSTSTORE_PROVIDER_PROP_NAME, TransportConstants.DEFAULT_TRUSTSTORE_PROVIDER, configuration);
      }
      return sslContextName;
   }

   @Override
   public int getPriority() {
      return 10;
   }
}
