package dbimport;

import dbimport.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(Config.class)
@SpringBootApplication
public class Application {
    private Config config;

    @Autowired
    public Application(Config config) {
        this.config = config;
    }

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
