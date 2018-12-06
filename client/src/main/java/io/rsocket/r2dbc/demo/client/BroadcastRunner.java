package io.rsocket.r2dbc.demo.client;

import io.netifi.proteus.Proteus;
import io.rsocket.r2dbc.demo.RankingRequest;
import io.rsocket.r2dbc.demo.RankingServiceClient;
import io.rsocket.r2dbc.demo.Record;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BroadcastRunner {
	private static final Logger logger = LogManager.getLogger(BroadcastRunner.class);

	private RankingServiceClient rankingService;

	public BroadcastRunner(@Autowired Proteus proteus) {
		this.rankingService = new RankingServiceClient(proteus.group("io.rsocket.r2dbc.demo.ranking"));
	}

	public void run(String... args) throws Exception {
		RankingRequest request =
				RankingRequest.newBuilder()
						.addRecords(Record.newBuilder()
								.setId(0)
								.setName("Ryland Degnan")
								.setDescription("Co-founder and CTO, Netifi")
								.setThumbnail("https://attendease-event-content.s3.us-west-2.amazonaws.com/events/521abb61-6216-487e-a721-db53fa7003ac/upload/content/f5060af654de642b167c.jpg"))
						.addRecords(Record.newBuilder()
								.setId(1)
								.setName("Stephane Maldini")
								.setDescription("Reactive Engineering Cook, Pivotal")
								.setThumbnail("https://attendease-event-content.s3.us-west-2.amazonaws.com/events/521abb61-6216-487e-a721-db53fa7003ac/upload/content/aa207287b449bdd02dee.jpg"))
						.build();

		Iterable<Record> result = rankingService.rank(Mono.just(request)).toIterable();

		for (Record record: result) {
			System.out.println(record);
		}

		// Exit
		System.exit(0);
	}
}
