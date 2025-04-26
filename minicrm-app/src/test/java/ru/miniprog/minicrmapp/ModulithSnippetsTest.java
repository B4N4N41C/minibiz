package ru.miniprog.minicrmapp;


import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static org.junit.jupiter.api.Assertions.*;

class ModulithSnippetsTest {

    private final ApplicationModules modules = ApplicationModules.of(MinicrmAppApplication.class);

    @Test
    void listModules() {
        modules.forEach(System.out::println);
    }

    @Test
    void verifyModules() {
        modules.verify();
    }

    @Test
    void createDoc() {
        new Documenter(modules).writeDocumentation();
    }
}
