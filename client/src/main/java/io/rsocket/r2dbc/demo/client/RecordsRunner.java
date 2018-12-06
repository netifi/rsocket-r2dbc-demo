package io.rsocket.r2dbc.demo.client;

import com.google.protobuf.util.JsonFormat;
import io.netifi.proteus.Proteus;
import io.rsocket.r2dbc.demo.Record;
import io.rsocket.r2dbc.demo.RecordsRequest;
import io.rsocket.r2dbc.demo.RecordsServiceClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordsRunner {
	private static final Logger logger = LogManager.getLogger(RecordsRunner.class);

	private RecordsServiceClient recordsService;

	public RecordsRunner(@Autowired Proteus proteus) {
		this.recordsService = new RecordsServiceClient(proteus.group("io.rsocket.r2dbc.demo.records"));
	}

	public void run(String... args) throws Exception {
		RecordsRequest request =
				RecordsRequest.newBuilder()
						.setMaxResults(1000)
						.build();

		Iterable<Record> result = recordsService.records(request).toIterable();

		for (Record record: result) {
			logger.info(JsonFormat.printer().print(record));
		}

		// Exit
		System.exit(0);
	}
}
