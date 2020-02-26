package com.upload.filedemo.job;

import com.upload.filedemo.model.AnagSAP;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

@EnableBatchProcessing
@Configuration
public class CsvToMongoJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public Job readCSVFile() {
        return jobBuilderFactory.get("readCSVFile").incrementer(new RunIdIncrementer()).start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<AnagSAP, AnagSAP>chunk(10).reader(reader())
                .writer(writer()).build();
    }

    @Bean
    public FlatFileItemReader<AnagSAP> reader() {
        FlatFileItemReader<AnagSAP> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("SAP.csv"));
        reader.setLineMapper(new DefaultLineMapper<AnagSAP>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"cf", "tipoUtente", "cidSAP", "societaZXP", "societaSAP", "societaHR", "matricolaHR", "cognome", "nome", "sesso", "dtNascita", "comuneNascita", "prov"});
                setDelimiter(";");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<AnagSAP>() {{
                setTargetType(AnagSAP.class);
            }});
        }});
        return reader;
    }

    @Bean
    public MongoItemWriter<AnagSAP> writer() {
        MongoItemWriter<AnagSAP> writer = new MongoItemWriter<AnagSAP>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("anagSAP");
        return writer;
    }
}
