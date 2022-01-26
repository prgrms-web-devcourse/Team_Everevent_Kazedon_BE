package kdt.prgrms.kazedon.everevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ConfigurationPropertiesScan("kdt.prgrms.kazedon.everevent.configures.property")
public class EvereventApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvereventApplication.class, args);
    }

}
