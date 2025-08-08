package com.pkmn.binder.repository;

import com.pkmn.binder.model.entity.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CardRepository extends MongoRepository<Card, String> {
  List<Card> findByName(String name);


  @Transactional
  long deleteByName(String name);

}
