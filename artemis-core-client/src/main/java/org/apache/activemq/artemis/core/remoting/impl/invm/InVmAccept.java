/*
 * Copyright 2019 The Apache Software Foundation.
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
package org.apache.activemq.artemis.core.remoting.impl.invm;

import java.util.concurrent.Executor;
import org.apache.activemq.artemis.spi.core.remoting.BufferHandler;
import org.apache.activemq.artemis.utils.ExecutorFactory;

public interface InVmAccept {

   void connect(String connectionID, BufferHandler remoteHandler, InVMConnector connector, Executor clientExecutor);

   void disconnect(String connectionID);

   default boolean canConnect() {
      return this.getConnectionsAllowed() == -1 || this.getConnectionCount() < this.getConnectionsAllowed();
   }

   long getConnectionsAllowed();

   int getConnectionCount();

   BufferHandler getHandler();

   ExecutorFactory getExecutorFactory();
}
