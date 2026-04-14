package com.robotica.kohasimulator.controller;

import com.robotica.kohasimulator.dto.*;
import com.robotica.kohasimulator.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ── Patrons ──────────────────────────────────────────────────────────────

    @PostMapping("/patrons")
    @ResponseStatus(HttpStatus.CREATED)
    public PatronResponse createPatron(@RequestBody AdminPatronRequest req) {
        return adminService.createPatron(req);
    }

    @PutMapping("/patrons/{patronId}")
    public PatronResponse updatePatron(
            @PathVariable String patronId,
            @RequestBody AdminPatronRequest req) {
        return adminService.updatePatron(patronId, req);
    }

    // ── Biblios ──────────────────────────────────────────────────────────────

    @PostMapping("/biblios")
    @ResponseStatus(HttpStatus.CREATED)
    public BiblioResponse createBiblio(@RequestBody AdminBiblioRequest req) {
        return adminService.createBiblio(req);
    }

    @PutMapping("/biblios/{biblioId}")
    public BiblioResponse updateBiblio(
            @PathVariable String biblioId,
            @RequestBody AdminBiblioRequest req) {
        return adminService.updateBiblio(biblioId, req);
    }

    // ── Items ─────────────────────────────────────────────────────────────────

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse addItem(@RequestBody AdminItemRequest req) {
        return adminService.addItem(req);
    }
}
