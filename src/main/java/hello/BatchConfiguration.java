package hello;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    // tag::readerwriterprocessor[]
    @Bean
    public StaxEventItemReader<Dir> reader() {
    	String[] node = {"dir", "file"};
    	
    	StaxEventItemReader<Dir> reader2 = new StaxEventItemReader<Dir>();
    	reader2.setResource(new ClassPathResource("test.xml"));
    	reader2.setFragmentRootElementNames(node);
//    	reader2.setFragmentRootElementName("record");
    	Jaxb2Marshaller hoge = new Jaxb2Marshaller();
    	hoge.setClassesToBeBound(Dir.class);
    	reader2.setUnmarshaller(hoge);
    	
    	
    	System.out.println();
    	
    	return reader2;
    }

    @Bean
    public DirProcessor processor() {
        return new DirProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Dir> writer() {

        return null;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Dir, Dir> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
        
    }
    // end::jobstep[]
}
