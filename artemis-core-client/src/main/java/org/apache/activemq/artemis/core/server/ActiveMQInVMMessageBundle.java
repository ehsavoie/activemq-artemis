/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.core.server;

import org.jboss.logging.Messages;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

/**
 * Logger Code 22
 * <p>
 * Each message id must be 6 digits long starting with 22, the 3rd digit should be 9. So the range
 * is from 229000 to 229999.
 * <p>
 * Once released, methods should not be deleted as they may be referenced by knowledge base
 * articles. Unused methods should be marked as deprecated.
 */
@MessageBundle(projectCode = "AMQ")
public interface ActiveMQInVMMessageBundle {

   ActiveMQInVMMessageBundle BUNDLE = Messages.getBundle(ActiveMQInVMMessageBundle.class);


   @Message(id = 229061, value = "Connection already exists with id {0}", format = Message.Format.MESSAGE_FORMAT)
   IllegalArgumentException connectionExists(Object id);

   @Message(id = 229062, value = "Acceptor with id {0} already registered", format = Message.Format.MESSAGE_FORMAT)
   IllegalArgumentException acceptorExists(Integer id);

   @Message(id = 229063, value = "Acceptor with id {0} not registered", format = Message.Format.MESSAGE_FORMAT)
   IllegalArgumentException acceptorNotExists(Integer id);
}
