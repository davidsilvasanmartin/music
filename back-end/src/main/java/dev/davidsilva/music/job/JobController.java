package dev.davidsilva.music.job;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping("/import-albums")
    public ResponseEntity<Void> runImportAlbumsJob() {
        try {
            jobService.launchGetImportAlbumsJob();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // In a production scenario, you'd want to return a more meaningful error message.
            return ResponseEntity.status(500).build();
        }
    }
}