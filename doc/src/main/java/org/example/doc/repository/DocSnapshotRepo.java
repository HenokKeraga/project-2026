package org.example.doc.repository;


import org.example.doc.model.DocSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocSnapshotRepo extends JpaRepository<DocSnapshot, UUID> {
    Optional<DocSnapshot> findTopByDocIdOrderByVersionDesc(UUID docId);
}
