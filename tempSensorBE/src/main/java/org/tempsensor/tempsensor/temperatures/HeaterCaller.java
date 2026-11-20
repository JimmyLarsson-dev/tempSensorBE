package org.tempsensor.tempsensor.temperatures;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.tempsensor.tempsensor.report.Mailer;

import java.time.Duration;

/**
 * Pratar med elementets API på det interna nätverket.
 *
 * GISSNING (korrigeras när vi vet hur API:t ser ut):
 *   POST {heater.base-url}/heater/on
 *   POST {heater.base-url}/heater/off
 */
@Slf4j
@Component
public class HeaterCaller {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(5);

    private final RestClient restClient;
    private final Mailer mailer;

    public HeaterCaller(@Value("${heater.base-url}") String baseUrl, Mailer mailer) {
        this.mailer = mailer;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    /**
     * @param onOrOff "on" eller "off"
     * @return true om elementet svarade OK, false om anropet misslyckades
     */
    public boolean callHeater(String onOrOff) {
        if (!"on".equals(onOrOff) && !"off".equals(onOrOff)) {
            throw new IllegalArgumentException("onOrOff must be \"on\" or \"off\", was: " + onOrOff);
        }
        try {
            restClient.post()
                    .uri("/heater/{state}", onOrOff)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Heater turned {}", onOrOff);
            return true;
        } catch (RestClientException e) {
            log.error("Failed to turn heater {}: {}", onOrOff, e.getMessage());
            mailer.sendHeaterFailureWarning(onOrOff, e.getMessage());
            return false;
        }
    }
}
