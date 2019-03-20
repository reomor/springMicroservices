package dbimport.config;

import dbimport.domain.TestObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${catalogs.input}/*.csv")
    private Resource[] resources;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor bpp = new JobRegistryBeanPostProcessor();
        bpp.setJobRegistry(jobRegistry);
        return bpp;
    }

    @Bean
    public MultiResourceItemReader<TestObject> csvMultiResourceReader() {
        MultiResourceItemReader<TestObject> resourceItemReader = new MultiResourceItemReader<>();
        resourceItemReader.setResources(resources);
        resourceItemReader.setDelegate(csvReader());
        return resourceItemReader;
    }

    @Bean
    public FlatFileItemReader<TestObject> csvReader() {
        FlatFileItemReader<TestObject> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
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
            System.out.println("s" + jobExecution.getJobId());
            final Integer rowsCount = jdbcTemplate.queryForObject("SELECT count(*) FROM db", Integer.class);
            System.out.println("Number of records: " + rowsCount);
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            System.out.println("e" + jobExecution.getJobId());
            final Integer rowsCount = jdbcTemplate.queryForObject("SELECT count(*) FROM db", Integer.class);
            System.out.println("Number of records: " + rowsCount);
        }
    }

    @Bean
    public Step csvLoadBatchStep() {
        return stepBuilderFactory.get("csvLoadBatchStep")
                .<TestObject, TestObject>chunk(2)
                .reader(csvMultiResourceReader())
                .processor(csvProcessor())
                .writer(csvWriter())
                .build();
    }

    @Bean
    public Step csvMoveAfterProcessStep() {
        final CsvFileMovingAfterJob csvFileMovingAfterJob = new CsvFileMovingAfterJob();
        return stepBuilderFactory.get("csvMoveAfterProcessStep")
                .tasklet(csvFileMovingAfterJob)
                .build();
    }

    @Bean
    public Job csvToDbUpload(CsvLoadJobNotificationListener listener) {
        return jobBuilderFactory.get("csvToDbUpload")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(csvLoadBatchStep())
                .next(csvMoveAfterProcessStep())
                .end()
                .build();
    }
}
