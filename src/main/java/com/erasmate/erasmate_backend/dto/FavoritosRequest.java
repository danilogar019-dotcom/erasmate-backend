package com.erasmate.erasmate_backend.dto;

import java.util.List;

// Datos que recibe el PUT de favoritos: {"idsLugares": [1, 3, 5]}
public record FavoritosRequest(List<Long> idsLugares) {
}