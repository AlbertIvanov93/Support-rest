package com.albert.supportbackend.repository;

import com.albert.supportbackend.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long>{

    Page<Request> findAllByUserId(Pageable pageable, Long userId);

    @Query("from Request r " +
            "where r.status = 'SENT'")
    Page<Request> findAllByStatusSent(Pageable pageable);

    @Query("from Request r " +
            "where r.user.name like concat('%', :username, '%') " +
            "and r.status = 'SENT'")
    Page<Request> findAllByNameAndStatusSent(Pageable pageable, String username);
}