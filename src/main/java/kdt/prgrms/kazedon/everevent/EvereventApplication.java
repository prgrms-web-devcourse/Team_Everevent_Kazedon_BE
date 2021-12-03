package kdt.prgrms.kazedon.everevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EvereventApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvereventApplication.class, args);
    }

}
