package com.project.community.enums;

public enum StorageFolder {
	USUARIOS("usuarios"),
	EVENTOS("eventos");
	private final String path;
	StorageFolder(String path){
		this.path = path;
	}
	public String getPath() {
		return path;
	}
}
