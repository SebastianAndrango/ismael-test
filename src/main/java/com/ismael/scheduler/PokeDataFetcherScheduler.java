package com.ismael.scheduler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import io.quarkus.scheduler.Scheduled;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PokeDataFetcherScheduler {

    private static final Logger LOGGER = Logger.getLogger(PokeDataFetcherScheduler.class);

    @ConfigProperty(name = "poke.api.url")
    String apiEndPoint;

    @Inject
    EventBus event;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Scheduled(cron = "${poke.cron}", identity = "pokeDataFetcher", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void execute() {
        fetchData()
                .ifPresentOrElse(this::publishEvent,
                        () -> LOGGER.error("[HTTP-Scheduler] No data receievd from PokeApi"));
    }

    private Optional<String> fetchData() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiEndPoint))
                    .GET()
                    .header("User-Agent", "Quarkus-PokeClient/1.0")
                    .header("Accept-Encoding", "identity")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return Optional.ofNullable(response.body());
        } catch (Exception exception) {
            LOGGER.error("[HTTP-Scheduler] Error fetching PokeApi data: ", exception);
            return Optional.empty();
        }
    }

    private void publishEvent(String data) {
        LOGGER.infof("[HTTP-Scheduler] Publishing data: %s", data);
        event.publish("poke-api", data);
    }
}
