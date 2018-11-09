package nl.devoteam;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jpa.JpaComponent;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
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
        ds.setInitialSize(3);
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
	
	Map<String, String> entityMgrProps() {
		 Map<String, String> props = new HashMap<String, String>();
		 
		/* Cannot use properites as they cannot be referenced from yaml file. 
		 *    Properties props = new Properties();
		   props.setProperty("javax.persistence.jdbc.url", "jdbc:mysql://192.168.99.100:43306/devoteam");
		   props.setProperty("javax.persistence.jdbc.user", "root");
		   props.setProperty("javax.persistence.jdbc.password", "example");
		   props.setProperty("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");*/
		   
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
		  txMgr.setDataSource(dataSource());
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
