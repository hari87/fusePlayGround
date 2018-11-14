package nl.devoteam;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;


@Configuration
public class SharedDBConfig {
	  @Value("${spring.datasource.url}")
	  private String mysqlUrl;
	  
	  @Value("${spring.datasource.username}")
	  private String db_username;
	  
	  @Value("${spring.datasource.password}")
	  private String db_password;
	  
	  @Value("${mysql.service.driverclassName}")
	  private String mysqlDriverName;
	  
	public DataSource setupDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUsername(db_username);
        ds.setPassword(db_password);
        ds.setUrl(mysqlUrl);
        ds.setInitialSize(1);
        return ds;
    }
	
	
	public SimpleRegistry registerDataSource() {
		SimpleRegistry registry = new SimpleRegistry();
		registry.put("myDataSource", setupDataSource());
		registry.put("jpaEntityManagerFactory", factory); 
		return registry;
	}
	
	//----------JPA SPECIFIC - BEGINS-----------------------------
	//Map<String, String> entityMgrValues = new HashMap<String, String>();
	
	/**
	 * @author Devoteam
	 *The datasource defined here are meant for configuring entityManager & 
	 *transaction manager. Though the datasource are repititive in nature, it 
	 *is also possible to do away with this by commenting the following line of code
	 *jpaComponent.setEntityManagerFactory(sharedDBConfig.factory);
	 * jpaComponent.setTransactionManager(sharedDBConfig.ptManager());
	 *	which is created in route (at present is available in ProcessTimer.java file)
	 *However the need and best way to use it is somthing that should be discussed. 
	 *Meanwhile, I have checked the total number of connections created and could see
	 *that for JPA it creates single connection object and same for camel-sql datasource
	 *placed in registry so the impact of these datasources if at present needs
	 *to be evaluated. 
	 * 
	 */
	
	Map<String, String> entityMgrProps() {
		 Map<String, String> props = new HashMap<String, String>();
		 
		   
		 props.put("javax.persistence.jdbc.url", mysqlUrl);
		 props.put("javax.persistence.jdbc.user", db_username);
		 props.put("javax.persistence.jdbc.password", db_password);
		 props.put("javax.persistence.jdbc.driver", mysqlDriverName);
		   
		   return props;
	   }
	  
	EntityManagerFactory factory =
			Persistence.createEntityManagerFactory("camel1", entityMgrProps());
	

	  
	  @Bean
	   public DataSource dataSource(){
	      DriverManagerDataSource dataSource = new DriverManagerDataSource();
	      dataSource.setDriverClassName(mysqlDriverName);
	      dataSource.setUrl(mysqlUrl);
	      dataSource.setUsername(db_username);
	      dataSource.setPassword( db_password );
	      return dataSource;
	   }
	  
	  public org.springframework.orm.jpa.JpaTransactionManager txMgr() {
		  JpaTransactionManager txMgr = new JpaTransactionManager();
		  txMgr.setDataSource(setupDataSource());
		  /*Map<String, String> jpaProperties = new HashMap<String, String>();
		  jpaProperties.put("propagationBehaviorName", "PROPAGATION_REQUIRES_NEW");
		  txMgr.setJpaPropertyMap(jpaProperties);*/
		  return txMgr;
	  }
	  
		  public PlatformTransactionManager ptManager() {
			  PlatformTransactionManager platformManager = new PlatformTransactionManager() {
				
				@Override
				public void rollback(TransactionStatus arg0) throws TransactionException {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public TransactionStatus getTransaction(TransactionDefinition arg0) throws TransactionException {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public void commit(TransactionStatus arg0) throws TransactionException {
					// TODO Auto-generated method stub
					
				}
			};
			return platformManager;
		  }
	  

	  

		//===============================NOT REQUIRED -- ENDS========================================================================
    @Bean
    public CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            public void beforeApplicationStart(CamelContext context) {
            	((org.apache.camel.impl.DefaultCamelContext) context).setRegistry(registerDataSource());
                context.setUseMDCLogging(true);
            }

            public void afterApplicationStart(CamelContext camelContext) {
            }
        };
    }
	
	

}
