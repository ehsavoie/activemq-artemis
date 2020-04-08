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

import org.apache.activemq.artemis.spi.core.remoting.ssl.SSLContextFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants;
import org.apache.activemq.artemis.utils.ConfigurationHelper;

/**
 *
 */
public class DefaultSSLContextFactory implements SSLContextFactory {

   private static final Map<String, SSLContext> SSL_CONTEXTS = Collections.synchronizedMap(new HashMap<>());

   @Override
   public SSLContext getSSLContext(Map<String, Object> configuration) throws Exception {
      boolean useDefaultSslContext = ConfigurationHelper.getBooleanProperty(TransportConstants.USE_DEFAULT_SSL_CONTEXT_PROP_NAME, TransportConstants.DEFAULT_USE_DEFAULT_SSL_CONTEXT, configuration);

      String sslContextName = ConfigurationHelper.getStringProperty(TransportConstants.SSL_CONTEXT_PROP_NAME, ConfigurationHelper.getStringProperty(TransportConstants.KEYSTORE_PATH_PROP_NAME, TransportConstants.DEFAULT_KEYSTORE_PATH, configuration), configuration);
      if (useDefaultSslContext) {
         return SSLContext.getDefault();
      }
      if (!SSL_CONTEXTS.containsKey(sslContextName)) {
         SSL_CONTEXTS.put(sslContextName, createSSLContext(configuration));
      }
      return SSL_CONTEXTS.get(sslContextName);
   }

   @Override
   public int getPriority() {
      return 10;
   }
}
