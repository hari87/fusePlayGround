package nl.devoteam;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;


//@Configuration
public class  PersistenceJPAConfig{
	

	   public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	      LocalContainerEntityManagerFactoryBean em 
	        = new LocalContainerEntityManagerFactoryBean();
	      em.setDataSource(dataSource());
	      em.setPackagesToScan(new String[] { "nl.devoteam" });
	 
	      JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	      em.setJpaVendorAdapter(vendorAdapter);
	      em.setJpaProperties(additionalProperties());
	 
	      return em;
	   }
	
	 @Bean
	   public DataSource dataSource(){
	      DriverManagerDataSource dataSource = new DriverManagerDataSource();
	      dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	      dataSource.setUrl("jdbc:mysql://192.168.99.100:43306/devoteam");
	      dataSource.setUsername( "root" );
	      dataSource.setPassword( "example" );
	      return dataSource;
	   }
	 
	 
	 
	 @Bean
	   public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
	       return new PersistenceExceptionTranslationPostProcessor();
	   }
	 
	   Properties additionalProperties() {
	       Properties properties = new Properties();
	       properties.setProperty("hibernate.hbm2ddl.auto", "none");
	       properties.setProperty(
	         "hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
	        
	       return properties;
	   }
	   
	 
}
