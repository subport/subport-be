package subport.batch.scheduler;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig implements AsyncConfigurer {

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(50);
		executor.setMaxPoolSize(50);
		executor.setThreadNamePrefix("asyncExecutor-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setTaskDecorator(runnable -> {
			Map<String, String> mdcContext = MDC.getCopyOfContextMap();
			return () -> {
				try {
					if (mdcContext != null) {
						MDC.setContextMap(mdcContext);
					}
					runnable.run();
				} finally {
					MDC.clear();
				}
			};
		});
		executor.initialize();
		return executor;
	}
}
