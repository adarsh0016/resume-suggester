package com.adarsh.resumed.service;

@FunctionalInterface
public interface LLMService {
    String getSuggestion(String promptText);
}
