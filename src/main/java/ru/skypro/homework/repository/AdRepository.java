package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Ad;

import java.util.List;
import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Integer> {
    Optional<Ad> findById(Long id);

    List<Ad> findByAuthor_Id(Long authorId);

    List<Ad> findAll();
}

