package io.rsocket.r2dbc.demo.tournament;

import io.netifi.proteus.Proteus;
import io.rsocket.r2dbc.demo.TournamentService;
import io.rsocket.r2dbc.demo.TournamentServiceServer;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.util.Optional;

@SpringBootApplication
public class TournamentServiceApplication {
  public static void main(String... args) {
    SpringApplication.run(TournamentServiceApplication.class, args);
  }

  @Bean
  Proteus proteus() {
    return Proteus.builder()
        .group("io.rsocket.r2dbc.demo.tournament")
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
  CommandLineRunner run(Proteus proteus, TournamentService tournamentService) {
    return args -> {
      proteus.addService(new TournamentServiceServer(tournamentService, Optional.empty(), Optional.empty()));

      proteus.onClose().block();
    };
  }
}
