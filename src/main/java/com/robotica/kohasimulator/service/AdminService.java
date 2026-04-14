package com.robotica.kohasimulator.service;

import com.robotica.kohasimulator.dto.*;
import com.robotica.kohasimulator.model.Biblio;
import com.robotica.kohasimulator.model.Item;
import com.robotica.kohasimulator.model.Patron;
import com.robotica.kohasimulator.repository.BiblioRepository;
import com.robotica.kohasimulator.repository.ItemRepository;
import com.robotica.kohasimulator.repository.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PatronRepository patronRepository;
    private final BiblioRepository biblioRepository;
    private final ItemRepository   itemRepository;
    private final PasswordEncoder  passwordEncoder;

    // ── Patrons ──────────────────────────────────────────────────────────────

    public PatronResponse createPatron(AdminPatronRequest req) {
        if (req.cardnumber() == null || req.userid() == null
                || req.password() == null || req.surname() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "cardnumber, userid, password y surname son obligatorios.");
        }
        if (patronRepository.findByCardnumber(req.cardnumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Ya existe un patron con cardnumber: " + req.cardnumber());
        }
        if (patronRepository.findByUserid(req.userid()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Ya existe un patron con userid: " + req.userid());
        }

        Patron patron = new Patron();
        patron.setCardnumber(req.cardnumber());
        patron.setUserid(req.userid());
        patron.setPasswordHash(passwordEncoder.encode(req.password()));
        patron.setSurname(req.surname());
        patron.setFirstname(req.firstname());
        patron.setEmail(req.email());
        patron.setPhone(req.phone());
        patron.setAddress(req.address());
        patron.setCategorycode(req.categorycode() != null ? req.categorycode() : "PT");
        patron.setBranchcode(req.branchcode()   != null ? req.branchcode()   : "CPL");
        patron.setFlags(req.flags()             != null ? req.flags()        : 0L);
        patron.setActive(req.active()           != null ? req.active()       : true);

        return PatronResponse.from(patronRepository.save(patron));
    }

    public PatronResponse updatePatron(String patronId, AdminPatronRequest req) {
        Patron patron = patronRepository.findById(patronId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Patron not found: " + patronId));

        if (req.cardnumber() != null) patron.setCardnumber(req.cardnumber());
        if (req.userid()     != null) patron.setUserid(req.userid());
        if (req.password()   != null) patron.setPasswordHash(passwordEncoder.encode(req.password()));
        if (req.firstname()  != null) patron.setFirstname(req.firstname());
        if (req.surname()    != null) patron.setSurname(req.surname());
        if (req.email()      != null) patron.setEmail(req.email());
        if (req.phone()      != null) patron.setPhone(req.phone());
        if (req.address()    != null) patron.setAddress(req.address());
        if (req.categorycode() != null) patron.setCategorycode(req.categorycode());
        if (req.branchcode()   != null) patron.setBranchcode(req.branchcode());
        if (req.flags()        != null) patron.setFlags(req.flags());
        if (req.active()       != null) patron.setActive(req.active());

        return PatronResponse.from(patronRepository.save(patron));
    }

    // ── Biblios ──────────────────────────────────────────────────────────────

    public BiblioResponse createBiblio(AdminBiblioRequest req) {
        if (req.title() == null || req.title().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title es obligatorio.");
        }

        Biblio biblio = new Biblio();
        biblio.setTitle(req.title());
        biblio.setAuthor(req.author());
        biblio.setIsbn(req.isbn());
        biblio.setPublisher(req.publisher());
        biblio.setPublicationYear(req.publicationYear());
        if (req.language() != null) biblio.setLanguage(req.language());

        return BiblioResponse.from(biblioRepository.save(biblio));
    }

    public BiblioResponse updateBiblio(String biblioId, AdminBiblioRequest req) {
        Biblio biblio = biblioRepository.findById(biblioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Biblio not found: " + biblioId));

        if (req.title()           != null) biblio.setTitle(req.title());
        if (req.author()          != null) biblio.setAuthor(req.author());
        if (req.isbn()            != null) biblio.setIsbn(req.isbn());
        if (req.publisher()       != null) biblio.setPublisher(req.publisher());
        if (req.publicationYear() != null) biblio.setPublicationYear(req.publicationYear());
        if (req.language()        != null) biblio.setLanguage(req.language());

        return BiblioResponse.from(biblioRepository.save(biblio));
    }

    // ── Items ─────────────────────────────────────────────────────────────────

    public ItemResponse addItem(AdminItemRequest req) {
        if (req.biblioId() == null || req.barcode() == null || req.barcode().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "biblio_id y barcode son obligatorios.");
        }
        Biblio biblio = biblioRepository.findById(req.biblioId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Biblio not found: " + req.biblioId()));

        if (itemRepository.findByBarcode(req.barcode()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Ya existe un item con barcode: " + req.barcode());
        }

        Item item = new Item();
        item.setBiblio(biblio);
        item.setBarcode(req.barcode());
        item.setBranchcode(req.branchcode() != null ? req.branchcode() : "CPL");
        item.setLocation(req.location());
        item.setCallnumber(req.callnumber());
        item.setItype(req.itype() != null ? req.itype() : "BK");

        return ItemResponse.from(itemRepository.save(item));
    }
}
