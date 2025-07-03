package com.example.pastebox.repository;

import com.example.pastebox.entity.Pastebox;
import com.example.pastebox.entity.PasteboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasteboxRepository extends JpaRepository<Pastebox,Long> {
    Optional<Pastebox> getByHash(String hash);

    List<Pastebox> getByPasteboxStatus(PasteboxStatus pasteboxStatus);

    List<Pastebox> getByUserUsername(String userUsername);

    void deleteByHash(String hash);
}
