package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class DirProcessor implements ItemProcessor<Dir, Dir> {

    private static final Logger log = LoggerFactory.getLogger(DirProcessor.class);

    @Override
    public Dir process(final Dir dir) throws Exception {
    	System.out.println("aaa");
//        final String firstName = person.getFirstName().toUpperCase();
//        final String lastName = person.getLastName().toUpperCase();
//
//        final Person transformedPerson = new Person(firstName, lastName);
//
//        log.info("Converting (" + person + ") into (" + transformedPerson + ")");
//
//        return transformedPerson;
    	return null;
    }
    
}
