package org.example.doc.repository;


import org.example.doc.model.DocOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocOperationRepo extends JpaRepository<DocOperation, UUID> {
    List<DocOperation> findByDocIdAndVersionGreaterThanOrderByVersionAsc(UUID docId, long version);
    boolean existsByDocIdAndClientOpId(UUID docId, String clientOpId);
}

