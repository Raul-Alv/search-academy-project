package co.empathy.academy.search.configuration;

import co.empathy.academy.search.service.SearchService;
import co.empathy.academy.search.service.client.SearchEngineClient;
import co.empathy.academy.search.service.SearchServiceImpl;
import co.empathy.academy.search.service.client.SearchEngineClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// This is a Spring configuration class where beans can be defined
// Alternatively, instead of manually creating beans, classes can be marked with the @Component, @Service or @Repository annotations to be managed by Spring
// Remember that Spring beans are singletons by default, that means that only one instance of them will exist in a given Spring context (be careful about shared state and concurrency)
@Configuration
public class Config {

    @Bean
    public SearchEngineClient searchEngineClient() {
        return new SearchEngineClientImpl();
    }

    // Spring will handle wiring bean dependencies for us
    // In this case it will retrieve the SearchEngineClient from the context and pass it to our SearchService
    @Bean
    public SearchService searchService(SearchEngineClient searchEngine) {
        return new SearchServiceImpl(searchEngine);
    }
}
