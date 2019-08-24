package com.obaccelerator.portal;

import lombok.Value;

@Value
public class BotEvaluationResult {
    private boolean likelyABot;
    private String fullRequest;
}
