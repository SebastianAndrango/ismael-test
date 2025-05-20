package com.ismael.event;

import org.jboss.logging.Logger;

import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventMessageListener {

    private static final Logger logger = Logger.getLogger(EventMessageListener.class);

    @ConsumeEvent(value = "concatenate-text", blocking = true)
    public void hanldeTextConcatenation(String message) {
        logger.infof("[BLOCKING] Handling text concatenation with message: %s", message);
    }

    @ConsumeEvent(value = "poke-api")
    public void handlePokeApiRequest(String message) {
        logger.infof("[NON BLOCKING] Handling PokeApi request with message: %s", message);
    }

}
