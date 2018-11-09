package nl.devoteam;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jpa.JpaComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class ProcessTimer extends RouteBuilder{
	JpaComponent jpaComponent = new JpaComponent();
	
@Autowired
SharedDBConfig sharedDBConfig;
	
	
	@Override
	public void configure() throws Exception {
		jpaComponent.setEntityManagerFactory(sharedDBConfig.factory);
		jpaComponent.setTransactionManager(sharedDBConfig.ptManager());
		from("timer://myTimer?repeatCount=1")
		.routeId("generate-user")
		.bean(UserService.class, "generateUser")
		 .to("jpa:nl.devoteam.User?persistenceUnit=camel1&sharedEntityManager=true&joinTransaction=false")
		// .transacted("required")
		 .log("Inserted new order ${body.id}")
		.to("sql:select * from devoteam.users limit 1?dataSource=myDataSource")
		.log("${body}");
		
	}

}
