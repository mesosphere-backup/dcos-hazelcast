/*
 * Copyright 2017 Johannes Unterstein (junterstein@mesosphere.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mesosphere.dcos.hazelcast.discovery;

import com.hazelcast.logging.ILogger;
import com.hazelcast.nio.Address;
import com.hazelcast.spi.discovery.AbstractDiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.SimpleDiscoveryNode;
import mesosphere.dcos.hazelcast.DcosHazelcastApp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DcosDiscoveryStrategy extends AbstractDiscoveryStrategy {
  private static final Logger LOG = LoggerFactory.getLogger(DcosHazelcastApp.class);

  DcosDiscoveryStrategy(ILogger logger, Map<String, Comparable> properties) {
    super(logger, properties);
  }

  @Override
  public Iterable<DiscoveryNode> discoverNodes() {
    List<DiscoveryNode> servers = new LinkedList<>();

    for (String node : StringUtils.split(System.getenv("HAZELCAST_INITIAL_MEMBERS"), System.lineSeparator())) {
      try {
        servers.add(new SimpleDiscoveryNode(new Address(node, 5701)));
      } catch (UnknownHostException e) {
        LOG.warn(String.format("DNS name '%s' not resolvable", node), e);
      }
    }
    return servers;
  }

  @Override
  public void destroy() {
  }
}
