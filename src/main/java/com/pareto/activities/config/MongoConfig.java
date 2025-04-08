package com.pareto.activities.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.pareto.activities.config.converter.LocalDateTimeReadConverter;
import com.pareto.activities.config.converter.LocalDateTimeWriteConverter;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.jsr310.LocalDateTimeCodec;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(
                new LocalDateTimeReadConverter(),
                new LocalDateTimeWriteConverter()
        ));
    }

    @Bean
    @Qualifier("biscoMongoClient")
    public MongoClient mongoClient() {

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(new LocalDateTimeCodec())
        );

        MongoClientSettings settings = MongoClientSettings
                .builder()
                .codecRegistry(
                        codecRegistry
                )
                .applyConnectionString(new ConnectionString(connectionString))
                .build()
                ;

        return MongoClients.create(settings);
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory(@Qualifier("biscoMongoClient") MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(
                mongoClient,
                "activities"
        );
    }

    @Bean
    @Primary
    @Qualifier("biscoMongoTemplate")
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(
                mongoClient,
                "activities"
        );
    }
}

