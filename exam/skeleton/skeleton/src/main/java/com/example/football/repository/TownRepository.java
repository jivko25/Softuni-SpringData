package com.example.football.repository;


import com.example.football.models.entity.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {
    @Query("Select t FROM Town t WHERE t.name = :name")
    Town findTownByName(@Param(value = "name") String name);
}
