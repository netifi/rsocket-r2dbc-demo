package io.rsocket.r2dbc.demo.client;

import io.netifi.proteus.Proteus;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;

@SpringBootApplication
public class ClientApplication {

  public static void main(String... args) {
    SpringApplication.run(ClientApplication.class, args);
  }

  @Bean
  Proteus proteus() {
    return Proteus.builder()
        .group("io.rsocket.r2dbc.demo.client")
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
  public CommandLineRunner commandLineRunner(
      TournamentRunner tournamentRunner,RecordsRunner recordsRunner, BroadcastRunner broadcastRunner) {
    //return tournamentRunner::run;
    //return broadcastRunner::run;
    return recordsRunner::run;
  }
}
