package dbimport.config;

import dbimport.CatalogParser;
import dbimport.domain.TestObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Paths;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
    public static final String CSV_UPLOAD_JOB = "csvToDbUpload";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CatalogParser catalogParser;

    // injected by csvReader!
    private Resource resource;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor bpp = new JobRegistryBeanPostProcessor();
        bpp.setJobRegistry(jobRegistry);
        return bpp;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<TestObject> csvReader(@Value("#{jobParameters['resource']}") String fileName) {
        FlatFileItemReader<TestObject> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        resource = new FileSystemResource(fileName);
        reader.setResource(resource);
        reader.setLineMapper(new DefaultLineMapper<TestObject>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("id", "name", "value");
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<TestObject>() {
                    {
                        setTargetType(TestObject.class);
                    }
                });
            }
        });
        return reader;
    }

    @Bean
    public ItemProcessor<TestObject, TestObject> csvProcessor() {
        return testObject -> new TestObject(testObject.getId(), testObject.getName(), testObject.getValue());
    }

    @Bean
    public JdbcBatchItemWriter<TestObject> csvWriter() {
        JdbcBatchItemWriter<TestObject> csvWriter = new JdbcBatchItemWriter<>();
        csvWriter.setDataSource(dataSource);
        csvWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        csvWriter.setSql("INSERT INTO db (id, name, value) VALUES (:id, :name, :value)");
        return csvWriter;
    }

    @Component
    class CsvLoadJobNotificationListener extends JobExecutionListenerSupport {

        @Override
        public void beforeJob(JobExecution jobExecution) {
            logger.info("Start job(id=" + jobExecution.getJobId() + ") for: " + jobExecution.getJobParameters().getString("resource"));
            final Integer rowsCount = jdbcTemplate.queryForObject("SELECT count(*) FROM db", Integer.class);
            logger.info("Number of records in DB before job: " + rowsCount);
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
                logger.info("Completed job(id=" + jobExecution.getJobId() + ") for: " + jobExecution.getJobParameters().getString("resource"));
            }
            final Integer rowsCount = jdbcTemplate.queryForObject("SELECT count(*) FROM db", Integer.class);
            logger.info("Number of records in DB after job: " + rowsCount);
            try {
                logger.info("Moving file to processed catalog");
                catalogParser.moveFile(Paths.get(resource.getURI()));
            } catch (IOException e) {
                logger.warn("Error during moving file: " + e.getMessage());
            }
        }
    }

    @Bean
    public Step csvLoadBatchStep() {
        return stepBuilderFactory.get("csvLoadBatchStep")
                .<TestObject, TestObject>chunk(2)
                .reader(csvReader(null)) // will be injected in step
                .processor(csvProcessor())
                .writer(csvWriter())
                .build();
    }


    @Bean
    public Job csvToDbUpload(CsvLoadJobNotificationListener listener) {
        return jobBuilderFactory.get(CSV_UPLOAD_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(csvLoadBatchStep())
                .end()
                .build();
    }
}
