package io.rsocket.r2dbc.demo.tournament;

import java.math.RoundingMode;
import java.util.function.Function;

import com.google.common.math.IntMath;
import io.netifi.proteus.Proteus;
import io.netty.buffer.ByteBuf;
import io.rsocket.r2dbc.demo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;

@Service
public class DefaultTournamentService implements TournamentService {
  private static final Logger logger = LogManager.getLogger(DefaultTournamentService.class);
  private static final int WINDOW_SIZE = 2;

  private final RecordsServiceClient recordsService;
  private final RankingServiceClient rankingService;

  public DefaultTournamentService(@Autowired Proteus proteus) {
    this.recordsService = new RecordsServiceClient(proteus.group("io.rsocket.r2dbc.demo.records"));
    this.rankingService = new RankingServiceClient(proteus.group("io.rsocket.r2dbc.demo.ranking"));
  }

  @Override
  public Flux<RoundResult> tournament(RecordsRequest request, ByteBuf metadata) {
    int maxRounds = IntMath.log2(request.getMaxResults(), RoundingMode.UP);
    return tournament(recordsService.records(request), 1, maxRounds);
  }

  private Flux<RoundResult> tournament(Flux<Record> records, int round, int maxRounds) {
    ConnectableFlux<Record> winners = round(records).publish(WINDOW_SIZE);
    Flux<RoundResult> result = winners
        .map(winner -> RoundResult.newBuilder()
            .setRound(round)
            .setWinner(winner)
            .build());


    Flux<RoundResult> tournamentResult = (round < maxRounds)
      ? Flux.just(result, Flux.defer(() -> tournament(winners, round + 1, maxRounds)))
            .flatMap(Function.identity(), 1, 1)
      : result;

    winners.connect();

    return tournamentResult;
  }

  private Flux<Record> round(Flux<Record> records) {
    return records.buffer(WINDOW_SIZE)
        .map(buffer -> RankingRequest.newBuilder().addAllRecords(buffer).build())
        .log()
        .transform(rankingService::rank);
  }
}
