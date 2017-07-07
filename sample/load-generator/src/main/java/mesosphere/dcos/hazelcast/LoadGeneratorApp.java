package mesosphere.dcos.hazelcast;

import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public class LoadGeneratorApp {

  public static void main(String[] args) throws Exception {
    String vip = System.getenv("VIP_APP");
    Integer amount = Integer.valueOf(System.getenv("AMOUNT"));

    System.out.println("VIP: " + vip);
    System.out.println("Amount: " + amount);
    RestTemplate restTemplate = new RestTemplate();
    for (int i = 0; i < amount; i++) {
      try {
        System.out.println("Run #" + i);
        String response = restTemplate.postForObject(vip, new ContentRequest("k" + i, UUID.randomUUID().toString()), String.class);
        System.out.println("Response: " + response);
        Thread.sleep(500);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
