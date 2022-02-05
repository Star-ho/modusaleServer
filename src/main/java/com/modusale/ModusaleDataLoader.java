package com.modusale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ModusaleDataLoader implements CommandLineRunner {
    private final ModusaleBatch modusaleBatch;

    public ModusaleDataLoader(@Autowired ModusaleBatch modusaleBatch) {
        this.modusaleBatch = modusaleBatch;
    }

    @Override
    public void run(String... args) {
        modusaleBatch.consume();
    }
}
