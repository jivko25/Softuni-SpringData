package com.example.football.repository;

import com.example.football.models.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query("Select p FROM Player p  WHERE p.birthDay > :after AND p.birthDay < :before " +
            "ORDER BY p.stat.shooting desc, p.stat.passing desc," +
            " p.stat.endurance desc, p.lastName")
    List<Player> findAllByBirthDay(@Param(value = "after")LocalDate after,
                                   @Param(value = "before")LocalDate before);
}
