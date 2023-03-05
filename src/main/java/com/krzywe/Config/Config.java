package com.krzywe.Config;

import java.beans.PropertyVetoException;
import java.net.InetAddress;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.hibernate.resource.jdbc.spi.PhysicalConnectionHandlingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages="com.krzywe.Repository",
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "hibernateTransactionManager"
		)
public class Config {
	
	@Value("${database.address}")
	private InetAddress serverAddress;
	
	@Value("${database.port}")
	private String serverPort;
	
	@Value("${databaseLogin}")
	private String databaseLogin;
	
	@Value("${databasePassword}")
	private String databasePassword;
	
	
	
	@Bean
	@Primary//tutaj tez zmienono z DataSource javax na PooledDataSorce
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
		return builder
	    		  .dataSource(dataSource)
	    		  .properties(
	    				  additionalProperties()
	    				  .entrySet()
	    				  .stream()
	    				  .map(e -> e)
	    				  .collect(
	    						  Collectors
	    						  .toMap( k -> k.getKey().toString(), v-> v.getValue().toString())
	    						  )
	    				  )
	    		  .packages("com.krzywe.Model")
	    		  .build();
	   }
	
	@Bean
    @Primary//zmieniono z data source na pooledData source
    public DataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();
		
		pooledDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
		pooledDataSource.setUser(databaseLogin);
		pooledDataSource.setPassword(databasePassword);
		pooledDataSource.setJdbcUrl("jdbc:mysql://"+serverAddress.getHostAddress()+":"+serverPort+"/default_schema");
		pooledDataSource.setMaxPoolSize(5);
		pooledDataSource.setMinPoolSize(2);
		pooledDataSource.setMaxIdleTime(100);
        return pooledDataSource;
    }
	
	@Bean
	@Primary
	public PlatformTransactionManager hibernateTransactionManager(LocalContainerEntityManagerFactoryBean emf) {
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(emf.getObject());

	    return transactionManager;
	}
	
	@Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
	
	private Properties additionalProperties() {
        Properties properties = new Properties();
        
        
        
        //properties.setProperty("javax.persistence.schema-generation.scripts.create-target", "drop.sql");
        properties.setProperty("hibernate.hbm2ddl.auto", "none");//create-drop
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");

        //properties.setProperty("hibernate.hbm2ddl.delimiter",";");
        //properties.setProperty("hibernate.hbm2dll.create_namespaces", "true");
        //properties.setProperty("javax.persistence.schema-generation.scripts.action", "drop-and-create");
        //properties.setProperty("javax.persistence.schema-generation.scripts.create-target", "users-create.sql");
        //properties.setProperty("javax.persistence.schema-generation.scripts.drop-target", "users-drop.sql");
        
        properties.put(Environment.FORMAT_SQL, true);
	    properties.put(Environment.SHOW_SQL, true);
	    //properties.put(Environment.cac, properties)
	    //properties.put(Environment.USE_QUERY_CACHE, true);
	    //properties.setProperty("hibernate.cache.region.factory_class","org.hibernate.cache.ehcache.EhCacheRegionFactory");
	    //properties.put(Environment.USE_SECOND_LEVEL_CACHE, true);
	    properties.put(Environment.CONNECTION_HANDLING, PhysicalConnectionHandlingMode.DELAYED_ACQUISITION_AND_RELEASE_AFTER_TRANSACTION);
	    //properties.put(Environment.POOL_SIZE, 15);
	   
	    //properties.put(Environment.AUTOCOMMIT, true);
	    //properties.put(Environment, properties)
	    //properties.put(Environment.C3P0_MIN_SIZE,2);
	    //properties.put(Environment.C3P0_MAX_SIZE,10);
        return properties;
    }

}
