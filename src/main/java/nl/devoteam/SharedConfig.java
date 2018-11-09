package nl.devoteam;

import org.springframework.context.annotation.Configuration;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import javax.jms.ConnectionFactory;
import javax.sql.DataSource;


@Configuration
public class SharedConfig {
	  @Value("${amq.url}") // TODO Failover URL
	    private String amqUrl;
	  
	  @Value("${spring.datasource.url}")
	  private String mysqlUrl;

	    @Bean
	    public ActiveMQConnectionFactory coreConnectionFactory() {
	        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
	        connectionFactory.setBrokerURL(amqUrl);
	        // the below setting is to be checked with peers & mohammed.!!!
	        connectionFactory.setTrustAllPackages(true);
	        return connectionFactory;
	    }

	    @Bean
	    public JmsConfiguration jmsConfiguration(ActiveMQConnectionFactory coreConnectionFactory) {
	        JmsConfiguration jmsConfiguration = new JmsConfiguration();
	        ConnectionFactory connectionFactory = new PooledConnectionFactory(coreConnectionFactory);
	        jmsConfiguration.setConnectionFactory(connectionFactory);
	        return jmsConfiguration;
	    }

	    @Bean(name = "activemq")
	    public ActiveMQComponent activeMQComponent(JmsConfiguration jmsConfiguration) {
	        ActiveMQComponent component = new ActiveMQComponent();
	        component.setConfiguration(jmsConfiguration);
	        return component;
	    }


	    @Bean
	    public CamelContextConfiguration contextConfiguration() {
	        return new CamelContextConfiguration() {
	            public void beforeApplicationStart(CamelContext context) {
	                context.setUseMDCLogging(true);
	            }

	            public void afterApplicationStart(CamelContext camelContext) {
	            }
	        };
	    }

}
