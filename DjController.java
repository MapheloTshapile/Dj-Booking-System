package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.DJ;
import za.ac.cput.service.IDjService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;

import java.util.List;

@RestController
@RequestMapping("/api/dj")
public class DjController {

    private final IDjService djService;


    @Autowired
    public DjController(IDjService djService) {
        this.djService = djService;
    }

    // Admin can create DJ
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<DJ> create(@RequestBody DJ dj) {
        DJ createdDj = djService.create(dj);
        return new ResponseEntity<>(createdDj, HttpStatus.CREATED);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<DJ> read(@PathVariable Long id) {
        DJ dj = djService.read(id);
        if (dj != null) {
            return new ResponseEntity<>(dj, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Admin can update DJ
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<DJ> update(@RequestBody DJ dj) {
        DJ updatedDj = djService.update(dj);
        if (updatedDj != null) {
            return new ResponseEntity<>(updatedDj, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Admin can delete DJ
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = djService.delete(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DJ>> getAll() {
        List<DJ> djs = djService.getAll();
        return new ResponseEntity<>(djs, HttpStatus.OK);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        DJ dj = djService.read(id);
        if (dj == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        byte[] img = dj.getImageData();
        if (img == null || img.length == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        String contentType = dj.getImageContentType();
        if (contentType == null || contentType.isEmpty()) contentType = "image/jpeg";

        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .body(img);
    }

    /**
     * Accept raw bytes in the request body. Useful when multipart/form-data isn't available.
     * Content-Type should be an image media type (image/jpeg, image/png) or application/octet-stream.
     * Admin only - to upload DJ images
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/{id}/upload-image-raw", consumes = {"application/octet-stream", "image/jpeg", "image/png", "image/*"})
    public ResponseEntity<DJ> uploadImageRaw(@PathVariable Long id, @RequestBody byte[] body, @RequestHeader(value = "Content-Type", required = false) String contentType) {
        DJ existing = djService.read(id);
        if (existing == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (body == null || body.length == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (contentType == null || contentType.isEmpty()) contentType = "application/octet-stream";

        DJ updated = new DJ.Builder().copy(existing)
                .setImageData(body)
                .setImageContentType(contentType)
                .build();

        DJ saved = djService.update(updated);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }
}
