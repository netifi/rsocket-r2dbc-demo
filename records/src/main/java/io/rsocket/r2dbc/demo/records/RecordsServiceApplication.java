package io.rsocket.r2dbc.demo.records;

import io.netifi.proteus.Proteus;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;

import io.rsocket.r2dbc.demo.RecordsService;
import io.rsocket.r2dbc.demo.RecordsServiceServer;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.util.Optional;

@SpringBootApplication
public class RecordsServiceApplication {
  public static void main(String... args) {
    SpringApplication.run(RecordsServiceApplication.class, args);
  }

  @Bean
  PostgresqlConnectionFactory connectionFactory() {
    return new PostgresqlConnectionFactory(
        PostgresqlConnectionConfiguration
            .builder() //
            .host("localhost")
            .database("marvel")
            .username("captain")
            .password("america")
            .build()
    );
  }

  @Bean
  Proteus proteus() {
    return Proteus.builder()
        .group("io.rsocket.r2dbc.demo.records")
        .accessKey(602046955788162L)
        .accessToken("ZYABDgKU6zp2VrYr4TSlgCYbkqQ=")
        .host("ws.proteus-broker.apps.bluelake.cf-app.com")
        .port(80)
        //.accessKey(9007199254740991L)
        //.accessToken("kTBDVtfRBO4tHOnZzSyY5ym2kfY=")
        //.host("localhost")
        //.port(8101)
        .clientTransportFactory(addr ->
            WebsocketClientTransport.create((InetSocketAddress) addr))
        .build();
  }

  @Bean
  CommandLineRunner run(Proteus proteus, RecordsService recordsService) {
    return args -> {
      proteus.addService(new RecordsServiceServer(recordsService, Optional.empty(), Optional.empty()));

      proteus.onClose().block();
    };
  }
}
