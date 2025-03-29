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
    public ResponseEntity<Void> runImportAlbumsJob() throws Exception {
        jobService.launchGetImportAlbumsJob();
        return ResponseEntity.ok().build();
    }
}