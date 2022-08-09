package com.example.marek.box;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {



}
