package dbimport;

import dbimport.config.Config;
import dbimport.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(Config.class)
@SpringBootApplication
public class Application implements CommandLineRunner {
    private Config config;
    @Autowired
    private JobService jobService;

    @Autowired
    public Application(Config config) {
        this.config = config;
    }

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        jobService.startImport();
    }
}
