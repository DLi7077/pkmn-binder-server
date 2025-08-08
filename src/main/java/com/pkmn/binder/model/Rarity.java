package com.pkmn.binder.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Rarity {
  // English Rarities
  SPECIAL_ILLUSTRATION_RARE("Special Illustration Rare"),
  ILLUSTRATION_RARE("Illustration Rare"),
  TRAINER_GALLERY_HOLO_RARE("Trainer Gallery Rare Holo"),
  PROMO("Promo"),
  V_HOLO_RARE("Rare Holo V"),
  VSTAR_HOLO_RARE("Rare Holo VSTAR"),
  ULTRA_RARE("Ultra Rare "),
  RAINBOW_RARE("Rainbow Rare"),
  SECRET_RARE("Secret Rare"),

  // Japanese Rarities
  SPECIAL_ART_RARE("Special Art Rare"),
  ART_RARE("Art Rare"),
  ;

  final String label;

  @Override
  public String toString() {
    return label;
  }
}
