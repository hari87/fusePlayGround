package nl.devoteam;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jpa.JpaComponent;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ch.qos.logback.classic.Logger;

/**
 * @author Devoteam
 *
 *This is a test class for practicing certain implementations. It is not documented
 *So please ignore this when you see this for reference.
 */
@Component
public class ProcessTimer extends RouteBuilder{
	JpaComponent jpaComponent = new JpaComponent();
	
@Autowired
SharedDBConfig sharedDBConfig;
	
	
	@Override
	public void configure() throws Exception {
		System.setProperty("log.name", "devoteam");
		jpaComponent.setEntityManagerFactory(sharedDBConfig.factory);
		jpaComponent.setTransactionManager(sharedDBConfig.ptManager());
		MDC.put("myProp", "myProp");
		from("timer://myTimer?repeatCount=1000")
		.routeId("generate-user")
		.bean(UserService.class, "generateUser")
		 .to("jpa:nl.devoteam.User?persistenceUnit=camel1&sharedEntityManager=true&joinTransaction=false")
		// .transacted("required")
		 .log("Inserted new order ${body.id}")
		 .bean(UserService.class, "generateUser")
		 .to("jpa:nl.devoteam.User?persistenceUnit=camel1&sharedEntityManager=true&joinTransaction=false")
		 .log("Inserted new order ${body.id}")
		 .bean(Database.class, "findUsers")
		 .log(LoggingLevel.INFO, "JPA-findUsersLog", "${body}")
		.to("sql:select * from devoteam.users limit 1?dataSource=myDataSource")
		.log("${body}")
		;
		
	}

}
