package fi.haagahelia.bzot.web;

import java.io.Console;
import java.sql.SQLException;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import fi.haagahelia.bzot.domain.Record;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@EnableAutoConfiguration
@Controller
public class WebController {				
	List<String> directions = new ArrayList<String>(Arrays.asList(new String[]{"En-Fi", "Fi-En"}));
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
    @RequestMapping(value={"/start"}, method=RequestMethod.GET)
    public String _startGet(Model model) {
    	log.info("--------------method GET");
    	
    	//stub for first starting
    	Record myRecord = new Record();
    	myRecord.setWord("");
    	myRecord.setDirection("En-Fi");
    	myRecord.setContent("");
    	
        model.addAttribute("record", myRecord);   	
        return "start";
    }
    
    @RequestMapping(value={"/start"}, method=RequestMethod.POST)
    public String _startPost(@ModelAttribute Record myRecord, Model model) {
    	log.info("--------------method POST");	
    	
    	myRecord.setContent(translationRetrieving(myRecord.getWord(), myRecord.getDirection()));
        model.addAttribute("record", myRecord);       
        
        String info = String.format("%s record retrieved: question = %s, answer = %s", myRecord.getDirection(), myRecord.getWord(), myRecord.getContent());
        log.info(info);
        
        return "start";
    }

    @RequestMapping(value="/login")
	public String _login() {
		return "login";
	}
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value="/add")
	public String _add(Model model) {
    	model.addAttribute("record", new Record());
    	model.addAttribute("directions", directions);
		return "addcontent";
	}
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String _save(Record record){
    	translationSaving(record.getWord(), record.getContent(), record.getDirection());

    	return "redirect:start";
    }    
    
	private static SessionFactory sessionFactory = null;  
	private static ServiceRegistry serviceRegistry = null; 
	private String s = "";
	  
	private static SessionFactory configureSessionFactory(String direction) throws HibernateException {  
	    Configuration configuration = new Configuration();  
	    configuration.configure();
	    
	    Properties properties = configuration.getProperties();
	    
	    if (direction.equals("Fi-En")) {
	    	//System.out.print("CASE Fi-En\r");	
	    	configuration.setProperty("hibernate.connection.url","jdbc:sqlite:fien.db");
	    }
	    else {
	    	//System.out.print("CASE En-Fi\r");	
	    	configuration.setProperty("hibernate.connection.url","jdbc:sqlite:enfi.db");
	    }
	    
		serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();          
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);  
	    
	    return sessionFactory;  
	}
	
	public String translationRetrieving(String word, String direction)
	{
		// Configure the session factory
		configureSessionFactory(direction);
		
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
	
	public String translationSaving(String word, String content, String direction)
	{
		// Configure the session factory
		configureSessionFactory(direction);
		
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sessionFactory.openSession();
			s = "";
			
			tx = session.beginTransaction();

			//Saving the data
			Record record = new Record();
			record.setWord(word);
			record.setContent(content);
			
			//Integer myID = (Integer) session.save(record); //for testing
			session.save(record);

			session.getTransaction().commit();
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
		return "";
	}
}