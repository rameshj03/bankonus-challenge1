package com.example.uploadingfiles.dao;


import com.example.uploadingfiles.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

    public interface DBStore extends MongoRepository<User, Long> {

    }

