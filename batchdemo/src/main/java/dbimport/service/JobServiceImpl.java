package dbimport.service;

import dbimport.CatalogParser;
import dbimport.config.BatchConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JobServiceImpl implements JobService {
    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Value("${catalogs.input}")
    private String inputCatalog;

    @Autowired
    private CatalogParser catalogParser;

    @Autowired
    public JobServiceImpl(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @Override
    public void startImport() {
        logger.info("Start job runner");
        try {
            catalogParser.listFiles(inputCatalog).forEach(resource -> {
                try {
                    logger.info("Start job for: " + resource.toFile().getAbsolutePath());
                    JobParameters jobParameters = new JobParametersBuilder()
                            .addString("resource", resource.toFile().getAbsolutePath())
                            .toJobParameters();
                    Job job = jobRegistry.getJob(BatchConfiguration.CSV_UPLOAD_JOB);
                    jobLauncher.run(job, jobParameters);
                } catch (Exception e) {
                    logger.info("Error during job for: " + resource.toFile().getAbsolutePath() + ": " + e.getMessage());
                }
            });
        } catch (IOException e) {
            logger.info("Error job runner: " + e.getMessage());
        }
    }
}
