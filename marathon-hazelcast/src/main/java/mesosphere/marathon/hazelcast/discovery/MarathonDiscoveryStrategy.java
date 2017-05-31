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
package mesosphere.marathon.hazelcast.discovery;

import com.hazelcast.logging.ILogger;
import com.hazelcast.nio.Address;
import com.hazelcast.spi.discovery.AbstractDiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.SimpleDiscoveryNode;
import mesosphere.marathon.hazelcast.MarathonHazelcastApp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.*;

public class MarathonDiscoveryStrategy extends AbstractDiscoveryStrategy {
  private static final Logger LOG = LoggerFactory.getLogger(MarathonHazelcastApp.class);

  private final String dnsName;

  MarathonDiscoveryStrategy(ILogger logger, Map<String, Comparable> properties) {
    super(logger, properties);

    String appId = System.getenv("MARATHON_APP_ID");
    List<String> appPathElements = Arrays.asList(StringUtils.split(appId, "/"));
    Collections.reverse(appPathElements);
    String dnsPart = StringUtils.join(appPathElements, "-");
    this.dnsName = String.format("%s.marathon.containerip.dcos.thisdcos.directory", dnsPart);
    LOG.info(String.format("Starting MarathonDiscoveryStrategy for app: '%s', dns name: '%s'", appId, dnsName));
  }

  @Override
  public Iterable<DiscoveryNode> discoverNodes() {
    List<DiscoveryNode> servers = new LinkedList<>();

    int calculations = 0;
    while (servers.size() < MarathonHazelcastApp.minMembers && calculations < 10) {
      try {
        Thread.sleep(2000); // let`s sleep a while to give DNS
      } catch (InterruptedException o_O) {
        LOG.error("Unable to sleep", o_O);
      }
      servers.clear();
      try {
        InetAddress[] inetAddresses = InetAddress.getAllByName(dnsName);
        for (InetAddress inetAddress : inetAddresses) {
          servers.add(new SimpleDiscoveryNode(new Address(inetAddress.getHostAddress(), 5701)));
        }
      } catch (Exception e) {
        LOG.warn(String.format("DNS name '%s' not resolvable", dnsName), e);
      }

      calculations++;
      LOG.info(String.format("During DNS resolving try %d got #servers %s", calculations, servers.size()));
    }
    return servers;
  }

  @Override
  public void destroy() {
  }
}
