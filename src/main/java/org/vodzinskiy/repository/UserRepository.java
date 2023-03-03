package org.vodzinskiy.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vodzinskiy.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUserName(String userName);
}
