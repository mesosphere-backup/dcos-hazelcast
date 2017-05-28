/*
 * Copyright 2017 Johannes Unterstein (junterstein@mesosphere.io)
 *
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
package mesosphere.marathon.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

public class MarathonHazelcastApp {

  public static void main(String[] args) {
    Config config = new XmlConfigBuilder(MarathonHazelcastApp.class.getClassLoader().getResourceAsStream("dcos-hazelcast.xml")).build();
    setPropertyIfPresent(config, "hazelcast.rest.enabled", "HAZELCAST_REST_ENABLED", "true");
    setPropertyIfPresent(config, "hazelcast.memcache.enabled", "HAZELCAST_MEMCACHE_ENABLED", "false");

    Hazelcast.newHazelcastInstance(config);
  }

  private static void setPropertyIfPresent(Config config, String configParam, String envParam, String defaultValue) {
    String env = System.getenv(envParam);
    if (StringUtils.isNotEmpty(env)) {
      config.setProperty(configParam, env);
    } else {
      config.setProperty(configParam, defaultValue);
    }
  }
}
