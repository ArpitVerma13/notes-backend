package com.example.notes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NoteController {

    private final NoteRepository repo;

    // @Value("${app.cors.allowedOrigin:*}")
    // private String allowedOrigin;

    public NoteController(NoteRepository repo) {
        this.repo = repo;
    }

    // Simple health check
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    // @CrossOrigin(origins = "*")
    @GetMapping("/notes")
    public List<Note> all() {
        return repo.findAll();
    }

    // @CrossOrigin(origins = "*")
    @PostMapping("/notes")
    public ResponseEntity<Note> create(@RequestBody NoteDto dto) {
        Note n = new Note();
        n.setTitle(dto.title);
        n.setContent(dto.content);
        Note saved = repo.save(n);
        URI location = URI.create("/api/notes/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    // @CrossOrigin(origins = "*")
    @GetMapping("/notes/{id}")
    public ResponseEntity<Note> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // @CrossOrigin(origins = "*")
    @PutMapping("/notes/{id}")
    public ResponseEntity<Note> update(@PathVariable Long id, @RequestBody NoteDto dto) {
        return repo.findById(id).map(existing -> {
            existing.setTitle(dto.title);
            existing.setContent(dto.content);
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // @CrossOrigin(origins = "*")
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Enable sharing for a note: generate a publicId (idempotent)
    // @CrossOrigin(origins = "*")
    @PostMapping("/notes/{id}/share")
    public ResponseEntity<String> share(@PathVariable Long id) {
        return repo.findById(id).map(n -> {
            n.ensurePublicId();
            repo.save(n);
            String base = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String url = base + "/api/share/" + n.getPublicId();
            return ResponseEntity.ok(url);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Public endpoint to fetch shared note (no auth)
    // @CrossOrigin(origins = "*")
    @GetMapping("/share/{publicId}")
    public ResponseEntity<Note> shared(@PathVariable String publicId) {
        return repo.findByPublicId(publicId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
