
package com.example.marek.repository;

import com.example.marek.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
