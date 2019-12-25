package org.folio.rest.config;

import java.util.ArrayList;
import java.util.List;

import org.folio.rest.resolver.ExtractorStreamingResponseArgumentResolver;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
public class RepositoryRestConfig extends RepositoryRestMvcConfiguration {

  @Autowired
  private AsyncTaskExecutor taskExecutor;

  @Autowired
  private ExtractorStreamingResponseArgumentResolver extractorStreamingResponseArgumentResolver;
  
  @Value("${spring.mvc.async.request-timeout:172800000}")
  private long asyncRequestTimeout;

  public RepositoryRestConfig(ApplicationContext context, ObjectFactory<ConversionService> conversionService) {
    super(context, conversionService);
  }

  @Override
  public RequestMappingHandlerAdapter repositoryExporterHandlerAdapter() {
    RequestMappingHandlerAdapter requestMappingHandlerAdapter = super.repositoryExporterHandlerAdapter();
    requestMappingHandlerAdapter.setAsyncRequestTimeout(asyncRequestTimeout);
    requestMappingHandlerAdapter.setTaskExecutor(taskExecutor);
    return requestMappingHandlerAdapter;
  }

  @Override
  @ConfigurationProperties(prefix = "spring.data.rest")
  public RepositoryRestConfiguration repositoryRestConfiguration() {
    return super.repositoryRestConfiguration();
  }

  @Override
  protected List<HandlerMethodArgumentResolver> defaultMethodArgumentResolvers() {
    List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>(super.defaultMethodArgumentResolvers());
    resolvers.add(extractorStreamingResponseArgumentResolver);
    return resolvers;
  }

}
