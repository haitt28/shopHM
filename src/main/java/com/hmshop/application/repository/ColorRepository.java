package com.hmshop.application.repository;

import com.hmshop.application.entity.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    @Query(value = "SELECT * FROM color b " +
            "WHERE b.id LIKE CONCAT('%',?1,'%')  " +
            "AND b.name LIKE CONCAT('%',?2,'%') ", nativeQuery = true)
    Page<Color> adminGetListColor(String id, String name, Pageable pageable);
    Color findByName(String name);
}
