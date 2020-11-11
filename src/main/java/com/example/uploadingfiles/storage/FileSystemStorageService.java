package com.example.uploadingfiles.storage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.example.uploadingfiles.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void store(List<User> userList) {


	}
}

