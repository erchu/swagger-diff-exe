package com.github.erchu.swaggerdiffexe;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public enum SwaggerVersion {

    V2("v2"),

    V1("v1");

    private final String stringRepresentation;

    public static Optional<SwaggerVersion> findByStringRepresentation(String stringRepresentation) {
        return Arrays.stream(SwaggerVersion.values())
                .filter((SwaggerVersion element) -> Objects.equals(element.stringRepresentation, stringRepresentation))
                .findAny();
    }
}
