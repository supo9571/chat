package   com.pangugle.modules.boot.all;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.pangugle.framework.reflect.MyFieldFactory;
import com.pangugle.framework.spring.SpringBootManager;
import com.pangugle.framework.spring.beans.SimpleNameGenerator;

/**
 * 所有服务部署在一起,
 * @author Administrator
 *
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = { 
		// framework
		"com.pangugle.framework.spring",
		
		// load component
		"com.pangugle.passport",
		"com.pangugle.im",
		
		// modules
		"com.pangugle.modules.web",
		"com.pangugle.modules.kefu",
		"com.pangugle.modules.passport",
		"com.pangugle.modules.im",
}, nameGenerator = SimpleNameGenerator.class)
@EnableTransactionManagement
public class Bootstrap {

	public static void main(String[] args) throws Exception {
		// 自定义配置文件
//		System.setProperty("env", "test");
		
		MyFieldFactory.clear(); // 热更新会引起类信息异常，需要重新收集类信息
		
		// run spring
		SpringBootManager.run(Bootstrap.class, "bootstrap.global.all.server.port", "global_all", args);
	}

}
