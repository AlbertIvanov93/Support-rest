package com.albert.supportbackend.repository;

import com.albert.supportbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query("from User u where u.name like concat('%', :name, '%')")
    List<User> findAllByName(@Param("name") String name);
}
