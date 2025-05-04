package ru.miniprog.minicrmapp.config;

import org.springframework.modulith.core.ApplicationModuleDetectionStrategy;
import org.springframework.modulith.core.JavaPackage;

import java.util.stream.Stream;

public class ModuleDetection implements ApplicationModuleDetectionStrategy {

    @Override
    public Stream<JavaPackage> getModuleBasePackages(JavaPackage basePackage) {
        return basePackage.getDirectSubPackages()
                .stream()
                .filter(pkg -> !pkg.getName().endsWith(".config"));
    }
}
