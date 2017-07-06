package mesosphere.dcos.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Map;
import java.util.UUID;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan
@RestController("/")
public class SampleApp extends WebMvcConfigurerAdapter {

  // stores the current nodeId of this service - current implemented as uuid
  private final String nodeId;

  private Map<String, String> content;

  public SampleApp() {
    nodeId = UUID.randomUUID().toString();

    // init hazelcast
    ClientConfig clientConfig = new ClientConfig();
    clientConfig.getNetworkConfig().addAddress("hazelcast.marathon.autoip.dcos.thisdcos.directory");
    HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
    content = client.getMap("content");
  }

  @RequestMapping("/")
  public Object getContent() {
    return new ContentResponse(content, nodeId);
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  public Object addContent(@RequestBody ContentRequest request) {
    content.put(request.getKey(), request.getValue());
    return new ContentResponse(content, nodeId);
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(SampleApp.class, args);
  }

}
