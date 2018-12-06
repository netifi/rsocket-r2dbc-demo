package io.rsocket.r2dbc.demo.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.netifi.proteus.Proteus;
import io.rsocket.r2dbc.demo.RecordsRequest;
import io.rsocket.r2dbc.demo.RoundResult;
import io.rsocket.r2dbc.demo.TournamentServiceClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.BaseSubscriber;

import org.springframework.stereotype.Service;

@Service
public class TournamentRunner {
  private static final Logger logger = LogManager.getLogger(TournamentRunner.class);

  private TournamentServiceClient tournamentService;

  public TournamentRunner(@Autowired Proteus proteus) {
    this.tournamentService = new TournamentServiceClient(proteus.group("io.rsocket.r2dbc.demo.tournament"));
  }

  public void run(String... args) throws Exception {
    RecordsRequest request = RecordsRequest.newBuilder().setMaxResults(40).build();

    tournamentService
        .tournament(request)
        .subscribe(new BaseSubscriber<RoundResult>() {
          @Override
          protected void hookOnSubscribe(Subscription subscription) {
            subscription.request(1);
          }

          @Override
          protected void hookOnNext(RoundResult record) {
            if (record.getRound() == 1){
              logger.info(
                  "\n=----------------------------------------------------------="+
                  "\n< @_@ > SUPER WINNER < @_@ >  ===> "+record.getWinner().getSuperName()+
                  "\n=----------------------------------------------------------=");
            }
            else {
              try {
                logger.info(JsonFormat.printer().print(record));
              }
              catch (InvalidProtocolBufferException e) { }
            }

            request(1);
          }
        });

    Thread.currentThread().join();
  }
}
