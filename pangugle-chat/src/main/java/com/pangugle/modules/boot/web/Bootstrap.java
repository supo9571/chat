package   com.pangugle.modules.boot.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.pangugle.framework.spring.SpringBootManager;
import com.pangugle.framework.spring.beans.SimpleNameGenerator;

/**
 * 所有服务部署在一起, 生产环境请勿使用
 * @author Administrator
 *
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = { 
		// framework
		"com.pangugle.framework.spring",
		
		// component
		"com.pangugle.passport",
		"com.pangugle.im",
		
		// modules
		"com.pangugle.modules.web",
}, nameGenerator = SimpleNameGenerator.class)
@EnableTransactionManagement
public class Bootstrap {

	public static void main(String[] args) throws Exception {
		// run spring
		SpringBootManager.run(Bootstrap.class, "bootstrap.global.web.server.port", "global_web", args);
	}

}
