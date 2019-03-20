package dbimport.service;

import dbimport.CatalogParser;
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
        try {
            catalogParser.listFiles(inputCatalog).forEach(resource -> {
                try {
                    JobParameters jobParameters = new JobParametersBuilder()
                            .addString("resource", resource.toFile().getAbsolutePath())
                            .toJobParameters();
                    Job job = jobRegistry.getJob("csvToDbUpload");
                    jobLauncher.run(job, jobParameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
