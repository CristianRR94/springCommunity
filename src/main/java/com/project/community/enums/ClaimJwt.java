package com.project.community.enums;

public enum ClaimJwt {
    USUARIO_ID("usuarioId"),
    NOMBRE("nombre"),
    TIPO_USO("tipo_uso"),
    ROLES("roles");

    private final String value;

    ClaimJwt(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}