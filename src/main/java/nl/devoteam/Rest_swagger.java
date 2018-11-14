package nl.devoteam;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Rest_swagger extends RouteBuilder{
	@Value("${swagger.port}")
	private int swagger_port;
	@Override
	public void configure() throws Exception {
		// configure we want to use servlet as the component for the rest DSL
        // and we enable json binding mode
        restConfiguration().component("netty4-http").bindingMode(RestBindingMode.auto)
            // and output using pretty print
            .dataFormatProperty("prettyPrint", "true")
            // setup context path and port number that netty will use
            .contextPath("/").port(swagger_port)
            .host("localhost")
            //CORs when enabled helps to access applications hosted from remote servers
            // from swagger
            .enableCORS(true)
            // add swagger api-doc out of the box
            .apiContextPath("/api-doc")
                .apiProperty("api.title", "User API").apiProperty("api.version", "1.0");
                // and enable CORS
               // .apiProperty("cors", "true");
        rest("/user").description("User rest service")
        .produces("application/json")
        .consumes("application/json")
        /*
         * Creates a GET service for fetching user information
         * @inputParam:
         * Takes in id as input (user id)
         * @OutputParam
         * Returns User object (as json) in response
         */
        
        .get("/getUser/{id}").description("get individual user").outType(User.class)
        .param().name("id").type(RestParamType.path).description("The id of the user to get").dataType("integer").endParam()
        .to("direct:readData")
        
        /*
         * Creates a GET service for fetching user information
         * @inputParam:
         * Takes in id as input (user id)
         * @OutputParam
         * Returns User object (as json) in response
         */
        
        .post("/create").description("to create an user")
        .param().name("body").type(RestParamType.body).description("The user to create").endParam()
        .to("direct:createUser");
        
        from("direct:readData")
        //activemq cannot have null body hence such the below setBody.
        .setBody().simple("${header.id}")
        .to("activemq:queue_find_user");
        
        /*
         * Usually we can have a single direct component and can dynamically
         * route messages to different queues based on CamelRestHttpPath and 
         * other header options. Since that might be bit confusing, i am not 
         * or skipping that implementation here. 
         */
        from("direct:createUser")
        .marshal().json(JsonLibrary.Jackson)
        .to("activemq:queue_create_user");
	
	}

}
