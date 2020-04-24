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
import org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants;
import org.apache.activemq.artemis.core.remoting.impl.ssl.SSLSupport;
import org.apache.activemq.artemis.utils.ConfigurationHelper;
import org.jboss.logging.Logger;

/**
 * Service interface to create a SSLContext for a configuration.
 */
public interface SSLContextFactory extends Comparable<SSLContextFactory> {
   Logger LOGGER = Logger.getLogger(SSLContextFactory.class);

   /**
    * Obtain a SSLContext from the configuration.
    * @param configuration
    * @param keystoreProvider
    * @param keystorePath
    * @param keystorePassword
    * @param truststoreProvider
    * @param truststorePath
    * @param truststorePassword
    * @param crlPath
    * @param trustManagerFactoryPlugin
    * @param trustAll
    * @return a SSLContext instance.
    * @throws Exception
    */
   SSLContext getSSLContext(Map<String, Object> configuration,
           String keystoreProvider, String keystorePath, String keystorePassword,
           String truststoreProvider, String truststorePath, String truststorePassword,
           String crlPath, String trustManagerFactoryPlugin, boolean trustAll) throws Exception;

   default void clearSSLContexts() {
   }

   default SSLContext createSSLContext(Map<String, Object> configuration,
           String keystoreProvider, String keystorePath, String keystorePassword,
           String truststoreProvider, String truststorePath, String truststorePassword,
           String crlPath, String trustManagerFactoryPlugin, boolean trustAll) throws Exception {
      final StringBuilder builder = new StringBuilder();
      configuration.forEach((k,v) -> builder.append("\r\n").append(k).append("=").append(v));
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

   int getPriority();

   @Override
   default int compareTo(SSLContextFactory other) {
      return this.getPriority() - other.getPriority();
   }
}
