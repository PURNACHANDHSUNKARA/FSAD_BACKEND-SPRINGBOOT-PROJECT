package com.fsad.scm.controller;

import com.fsad.scm.entity.Waitlist;
import com.fsad.scm.repository.WaitlistRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waitlist")
@CrossOrigin(origins = "*")
public class WaitlistController {

    private final WaitlistRepository repo;

    public WaitlistController(WaitlistRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Waitlist> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{email}")
    public List<Waitlist> getByEmail(@PathVariable String email) {
        return repo.findByStudentEmail(email);
    }
}
