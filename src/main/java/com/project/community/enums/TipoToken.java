package com.project.community.enums;

public enum TipoToken {
	ACCESS_TYPE("ACCESS"),
	REFRESH_TYPE("REFRESH");
	
	
	private final String value;
	TipoToken(String value){
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	
	public static TipoToken fromValue(String value) {
		for (TipoToken tipo : TipoToken.values()) {
			if(tipo.getValue().equalsIgnoreCase(value)) {
				return tipo;
			}
		}
		throw new IllegalArgumentException("Token no soportado: " + value);
	}
}
