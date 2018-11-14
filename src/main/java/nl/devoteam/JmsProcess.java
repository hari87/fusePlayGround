package nl.devoteam;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class JmsProcess extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("activemq:queue_find_user")
		.routeId("jms_find_user")
		.bean(Database.class, "findUser(${header.id})")
		.log(LoggingLevel.INFO, "fetched_user_name", "${body.user_name}");
		
		
		from("activemq:queue_create_user")
		.routeId("jms_route_create_user")
		.unmarshal().json(JsonLibrary.Jackson, User.class)
        .to("jpa:nl.devoteam.User?persistenceUnit=camel1&sharedEntityManager=true&joinTransaction=false")
        .log(LoggingLevel.INFO, "confirm_user_creation", "${body.id}");
        
		
	}

}
