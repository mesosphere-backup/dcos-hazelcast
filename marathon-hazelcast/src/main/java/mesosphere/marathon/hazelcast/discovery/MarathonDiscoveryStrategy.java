/*
 * Copyright 2017 Johannes Unterstein (junterstein@mesosphere.io)
 *
 * Initial version by Luca Burgazzoli (https://github.com/lburgazzoli/hazelcast-discovery-dns)
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

import com.hazelcast.config.NetworkConfig;
import com.hazelcast.logging.ILogger;
import com.hazelcast.nio.Address;
import com.hazelcast.spi.discovery.AbstractDiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.SimpleDiscoveryNode;
import org.apache.commons.lang3.StringUtils;

import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MarathonDiscoveryStrategy extends AbstractDiscoveryStrategy {
  private static final String[] SRV_RECORD = new String[]{"SRV"};
  private static final Hashtable<String, String> NAMING_CONF;

  static {
    NAMING_CONF = new Hashtable<>();
    NAMING_CONF.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
    NAMING_CONF.put("java.naming.provider.url", "dns:");
  }

  private final String dnsName;
  private final ILogger log;

  MarathonDiscoveryStrategy(ILogger logger, Map<String, Comparable> properties) {
    super(logger, properties);
    log = logger;

    String appId = System.getenv("APP_ID");
    this.dnsName = appId + "marathon.mesos";
    log.info(String.format("Starting MarathonDiscoveryStrategy for app: '%s', dns name: '%s'", appId, dnsName));
  }

  @Override
  public Iterable<DiscoveryNode> discoverNodes() {
    List<DiscoveryNode> servers = new LinkedList<>();

    try {
      DirContext ctx = new InitialDirContext(NAMING_CONF);
      NamingEnumeration<?> resolved = ctx.getAttributes(dnsName, SRV_RECORD).get("srv").getAll();

      while (resolved.hasMore()) {
        Address address = resolveAddressFromSrvRecord((String) resolved.next());
        log.info(String.format("DNS name '%s' resolved to address '%s'", dnsName, address));
        servers.add(new SimpleDiscoveryNode(address));
      }
    } catch (Exception e) {
      throw new RuntimeException(String.format("DNS name '%s' not resolvable", dnsName), e);
    }

    return servers;
  }

  @Override
  public void destroy() {
  }

  private Address resolveAddressFromSrvRecord(String record) throws Exception {
    String[] split = StringUtils.split(record, " ");
    String hostName = ("" + split[3]).trim();
    String port = ("" + split[2]).trim();

    return new Address(
        hostName.trim(),
        port.length() > 0 ? Integer.parseInt(port) : NetworkConfig.DEFAULT_PORT
    );
  }
}
