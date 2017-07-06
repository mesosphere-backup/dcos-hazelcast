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
package mesosphere.dcos.hazelcast;

import java.util.Map;

public class ContentResponse {
  private Map<String, String> content;
  private String nodeId;

  public ContentResponse(Map<String, String> content, String nodeId) {
    this.content = content;
    this.nodeId = nodeId;
  }

  public Map<String, String> getContent() {
    return content;
  }

  public String getNodeId() {
    return nodeId;
  }

}
