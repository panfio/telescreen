package ru.panfio.telescreen.handler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.panfio.telescreen.handler.service.MinioService;

@SpringBootApplication
public class HandlerApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context =SpringApplication.run(HandlerApplication.class, args);
		//Dirty hack since @PostConstruct doesn't work
		context.getBean(MinioService.class).createFolderStructure();
	}

//	@Bean
//	public BeanPostProcessor legacyTesterBeanPostProcessor() {
//		return new BeanPostProcessor() {
//			@Override
//			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//				return new LegacyTesterBeanPostProcessor(bean, beanName).createProxy();
//			}
//		};
//	}

}
