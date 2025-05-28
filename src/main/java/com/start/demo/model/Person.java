package com.start.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record Person(UUID id, String name, String email) {
    // Record automatically generates getters, equals, hashCode, and toString
}