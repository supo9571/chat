package   com.pangugle.modules.boot.im;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.pangugle.framework.spring.SpringBootManager;
import com.pangugle.framework.spring.beans.SimpleNameGenerator;

/**
 * 
 * @author Administrator
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = { 
		// framework
		"com.pangugle.framework.spring",
		
		// component
		"com.pangugle.passport",
		"com.pangugle.im",
		
		// im init
		"com.pangugle.modules.boot.im",
}, nameGenerator = SimpleNameGenerator.class)
@EnableTransactionManagement
public class Bootstrap {
	
	public static void main(String[] args) throws Exception {
        // run spring
        SpringBootManager.run(Bootstrap.class, "bootstrap.global.im.server.port", "global_im", args);
    }
}
