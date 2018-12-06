package io.rsocket.r2dbc.demo.ranking;

import io.netifi.proteus.Proteus;
import io.rsocket.r2dbc.demo.RankingService;
import io.rsocket.r2dbc.demo.RankingServiceServer;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.util.Optional;

@SpringBootApplication
public class RankingServiceApplication {
  public static void main(String... args) {
    SpringApplication.run(RankingServiceApplication.class, args);
  }

  @Bean
  Proteus proteus() {
    return Proteus.builder()
        .group("io.rsocket.r2dbc.demo.ranking")
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
  CommandLineRunner run(Proteus proteus, RankingService rankingService) {
    return args -> {
      proteus.addService(new RankingServiceServer(rankingService, Optional.empty(), Optional.empty()));

      proteus.onClose().block();
    };
  }
}
