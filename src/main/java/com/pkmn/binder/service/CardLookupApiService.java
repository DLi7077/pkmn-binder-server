package com.pkmn.binder.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkmn.binder.model.entity.Card;
import com.pkmn.binder.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardLookupApiService {
  // Create an HttpClient instance
  final HttpClient client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_1_1) // Specify HTTP version
    .connectTimeout(Duration.ofSeconds(10)) // Set connection timeout
    .build();
  final ObjectMapper objectMapper = new ObjectMapper();
  final CardRepository cardRepository;


  public List<String> pullPokemonNames() {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("http://localhost:3001/card-lookup/names")) // Set the target URI
      .GET() // Specify the GET method
      .build();

    try {
      // Send the request synchronously and get the response
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      List<String> pokemonNames = objectMapper.readValue(response.body(), new TypeReference<>() {
      });

      log.info("Pulled {} pokemon", pokemonNames.size());
      return pokemonNames;

    } catch (Exception e) {
      log.error("Something went wrong", e);
    }
    return List.of();
  }

  public List<Card> pullPokemonCards(String pokemonName) {
    MDC.put("pokemon", pokemonName);
    List<String> existingIds = cardRepository.findByName(pokemonName)
      .stream()
      .map(Card::getCardId)
      .toList();
    log.info("{} existing cards in db", existingIds.size());

    String pokemonNameEncoded = URLEncoder.encode(pokemonName, StandardCharsets.UTF_8);
    String existingIdsEncoded = URLEncoder.encode(String.join(",", existingIds), StandardCharsets.UTF_8);
    URI link = URI.create("http://localhost:3001/card-lookup/jp?name=" + pokemonNameEncoded + "&existingIds=" + existingIdsEncoded);
    log.info("{}", link);
    HttpRequest request = HttpRequest.newBuilder()
      .uri(link) // Set the target URI
      .GET() // Specify the GET method
      .build();

    // Todo: bug with Eevee, Mewtwo, Chansey, Pikachu
    try {
      // Send the request synchronously and get the response
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      List<Card> cards = objectMapper.readValue(response.body(), new TypeReference<>() {
      });

      Set<String> existingCardsInDB = cardRepository.findAll()
        .stream()
        .map(Card::getCardId)
        .collect(Collectors.toSet());

      cardRepository.saveAll(cards
        .stream()
        .filter(card -> !existingCardsInDB.contains(card.getCardId()))
        .toList()
      );

      log.info("Added {} cards", cards.size());
      return cards;

    } catch (Exception e) {
      log.error("Something went wrong", e);
    }
    MDC.remove("pokemon");
    return List.of();

  }
}
