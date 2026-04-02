package com.goormthon5backend.global.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeedDataInitializer {

    private final SeedDataService seedDataService;

    @PostConstruct
    public void init() {
        seedDataService.seed();
    }
}
