package com.pkmn.binder.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkmn.binder.model.entity.Card;
import com.pkmn.binder.service.CardLookupApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardDataLoaderService {
  final CardLookupApiService cardLookupApiService;
  final ObjectMapper objectMapper;

  @Scheduled(cron = "0 9 * * *") // Runs every day at 9 AM
  public void loadCards() throws JsonProcessingException {
    // Pulls cards from card lookup server and loads cards to MongoDB
    List<String> names = cardLookupApiService.pullPokemonNames();

    Map<String, Integer> cardCount = new HashMap<>();
    for (String name : names) {
      MDC.put("pokemon", name);
      log.info("Pulling cards for {}", name);
      long startMs = System.currentTimeMillis();
      List<Card> cards = cardLookupApiService.pullPokemonCards(name);
      cardCount.put(name, cards.size());
      long endMs = System.currentTimeMillis();

      log.info("Pulled {} {} cards in {}ms", cards.size(), name, endMs - startMs);
      MDC.clear();
    }

    log.info("{}", objectMapper.writeValueAsString(cardCount));
  }

}
