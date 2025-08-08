package com.pkmn.binder.controller;

import com.pkmn.binder.model.entity.Card;
import com.pkmn.binder.repository.CardRepository;
import com.pkmn.binder.service.CardLookupApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {
  final CardLookupApiService cardLookupApiService;
  final CardRepository cardRepository;

  @GetMapping("/data")
  public Map<String, Integer> seed() {
//    List<String> names = cardLookupApiService.pullPokemonNames();
    List<String> names = List.of("Zapdos");

    Map<String, Integer> cardCount = new HashMap<>();
    for (String name : names) {
      log.info("Pulling cards for {}", name);
      long startMs = System.currentTimeMillis();
      List<Card> cards = cardLookupApiService.pullPokemonCards(name);
      cardCount.put(name, cards.size());
      long endMs = System.currentTimeMillis();

      log.info("Pulled {} cards for {} in {}ms", cards.size(), name, endMs - startMs);
    }

    return cardCount;
  }

  @DeleteMapping("/delete")
  public long deleteCard(@RequestParam("name") String name) {
    return cardRepository.deleteByName(name);
  }
  @GetMapping("/test")
  public List<String> getCardIds(){
    return cardRepository.findAll().stream().map(Card::getCardId).toList();
  }
}
