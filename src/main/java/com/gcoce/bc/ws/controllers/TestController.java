package com.gcoce.bc.ws.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public ResponseEntity<?> allAccess() {
        return ResponseEntity.ok("Servidor funcionado correctamente");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminAccess() {
        return ResponseEntity.ok("Admin Content.");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> userAccess() {
        return ResponseEntity.ok("User Content.");
    }

    @GetMapping("/beneficio")
    @PreAuthorize("hasRole('BENEFICIO')")
    public ResponseEntity<?> beneficioAccess() {
        return ResponseEntity.ok("Beneficio Content.");
    }

    @GetMapping("/agricultor")
    @PreAuthorize("hasRole('AGRICULTOR')")
    public ResponseEntity<?> agricultorAccess() {
        return ResponseEntity.ok("Agricultor Content.");
    }

    @GetMapping("/peso-cabal")
    @PreAuthorize("hasRole('PESO_CABAL')")
    public ResponseEntity<?> pesoCabalAccess() {
        return ResponseEntity.ok("Peso Cabal Content.");
    }
}
