package org.example.doc.controller;



import jakarta.validation.Valid;
import org.example.doc.dto.CreateDocRequest;
import org.example.doc.dto.DocResponse;
import org.example.doc.model.DocOperation;
import org.example.doc.repository.DocOperationRepo;
import org.example.doc.service.DocService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/docs")
public class DocController {

    private final DocService docService;
    private final DocOperationRepo opRepo;


    public DocController(DocService docService, DocOperationRepo opRepo) {
        this.docService = docService;
        this.opRepo = opRepo;
    }

    @PostMapping
    public DocResponse create(@RequestBody @Valid CreateDocRequest req) {
        return docService.createDoc(req.title());
    }

    @GetMapping("/{docId}")
    public DocResponse get(@PathVariable UUID docId) {
        return docService.getDoc(docId);
    }

    @GetMapping("/{docId}/ops")
    public List<DocOperation> ops(@PathVariable UUID docId, @RequestParam(defaultValue = "0") long fromVersion) {
        return opRepo.findByDocIdAndVersionGreaterThanOrderByVersionAsc(docId, fromVersion);
    }
}
