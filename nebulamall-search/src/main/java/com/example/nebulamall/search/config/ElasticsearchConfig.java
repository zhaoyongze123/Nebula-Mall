package com.example.nebulamall.search.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch Configuration
 * Configures Elasticsearch client for connecting to ES cluster
 */
@Configuration
@EnableElasticsearchRepositories(
    basePackages = "com.example.nebulamall.search.repository"
)
public class ElasticsearchConfig {

    @Bean
    @ConditionalOnMissingBean
    public org.elasticsearch.client.RestHighLevelClient restHighLevelClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("127.0.0.1:9200")
                .withConnectTimeout(5000)
                .withSocketTimeout(60000)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

}
