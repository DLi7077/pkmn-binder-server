package com.pkmn.binder.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pkmn.binder.configuration.CustomJsonDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("cards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
  @Id
  String id;
  String name;
  String cardName;
  String cardId;
  String rarity;
  String artist;
  String set;
  String setId;
  Integer setCardId;
  Integer setCardCount;
  String imgUrl;
  String language;
  @JsonDeserialize(using = CustomJsonDateDeserializer.class)
  LocalDateTime expansionReleaseDate;
}
