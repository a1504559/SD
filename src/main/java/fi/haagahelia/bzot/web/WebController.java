package fi.haagahelia.bzot.web;

import java.util.List;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fi.haagahelia.bzot.domain.Record;

@EnableAutoConfiguration
@Controller
public class WebController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
    @RequestMapping(value="/start", method=RequestMethod.GET)
    public String customerForm(Model model) {
    	Record myRecord = new Record();
    	myRecord.setContent("");
        model.addAttribute("record", myRecord);
        return "start";
    }
    
	private static SessionFactory sessionFactory = null;  
	private static ServiceRegistry serviceRegistry = null; 
	private String s = "";
	  
	private static SessionFactory configureSessionFactory() throws HibernateException {  
	    Configuration configuration = new Configuration();  
	    configuration.configure();  
	    
	    Properties properties = configuration.getProperties();
	    
		serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();          
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);  
	    
	    return sessionFactory;  
	}
	
	public String translationRetrieving(String word)
	{
		// Configure the session factory
		configureSessionFactory();
		
		Session session = null;
		Transaction tx=null;
		
		try {
			session = sessionFactory.openSession();
			s = "";
			
			// Fetching the data
			String hql = "FROM Record WHERE word = :word";
			Query query = session.createQuery(hql);
			query.setParameter("word", word);
			
			List<Record> records = query.list();
			
			for (Record tempList : records) {
				s = records.get(0).getContent().replace("</font>');", "");
			}			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			// Rolling back the changes to make the data consistent in case of any failure 
			// in between multiple database write operations.
			tx.rollback();
		} finally{
			if(session != null) {
				session.close();
			}
		}
		return s;
	}

    @RequestMapping(value="/start", method=RequestMethod.POST)
    public String customerSubmit(@ModelAttribute Record myRecord, Model model) {
    	myRecord.setContent(translationRetrieving(myRecord.getWord()));
        model.addAttribute("record", myRecord);       
        
        String info = String.format("Record retrieved: question = %s, answer = %s", myRecord.getWord(), myRecord.getContent());
        log.info(info);
        
        return "start";
    }

}