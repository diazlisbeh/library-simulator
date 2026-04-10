package com.robotica.kohasimulator.service;

import com.robotica.kohasimulator.dto.CheckoutRequest;
import com.robotica.kohasimulator.dto.CheckoutResponse;
import com.robotica.kohasimulator.model.Checkout;
import com.robotica.kohasimulator.repository.CheckoutRepository;
import com.robotica.kohasimulator.repository.ItemRepository;
import com.robotica.kohasimulator.repository.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CheckoutRepository checkoutRepository;
    private final PatronRepository patronRepository;
    private final ItemRepository itemRepository;

    @Value("${koha.checkout.default-loan-days}")
    private int defaultLoanDays;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest req) {
        var patron = patronRepository.findById(req.patronId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Patron not found: " + req.patronId()));

        if (!patron.getActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Patron account is inactive");
        }
        if (patron.getExpiryDate() != null && patron.getExpiryDate().isBefore(java.time.LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Patron account has expired");
        }

        var item = itemRepository.findById(req.itemId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Item not found: " + req.itemId()));

        if (!item.getAvailable()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Item is not available for checkout");
        }
        if (item.getLost() || item.getWithdrawn()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Item is marked as lost or withdrawn");
        }

        // Check patron doesn't already have this item
        checkoutRepository.findByItem_ItemIdAndReturnDateIsNull(item.getItemId())
            .ifPresent(c -> { throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Item is already checked out"); });

        LocalDateTime dueDate = req.dueDate() != null
            ? LocalDateTime.parse(req.dueDate(), DateTimeFormatter.ISO_DATE_TIME)
            : LocalDateTime.now().plusDays(defaultLoanDays);

        var checkout = new Checkout();
        checkout.setPatron(patron);
        checkout.setItem(item);
        checkout.setLibraryId(req.libraryId() != null ? req.libraryId() : item.getBranchcode());
        checkout.setCheckoutDate(LocalDateTime.now());
        checkout.setDueDate(dueDate);

        item.setAvailable(false);
        itemRepository.save(item);

        return CheckoutResponse.from(checkoutRepository.save(checkout));
    }

    public CheckoutResponse getById(String checkoutId) {
        return checkoutRepository.findById(checkoutId)
            .map(CheckoutResponse::from)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Checkout not found: " + checkoutId));
    }

    public List<CheckoutResponse> getActiveByPatron(String patronId) {
        if (!patronRepository.existsById(patronId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Patron not found: " + patronId);
        }
        return checkoutRepository.findByPatron_PatronIdAndReturnDateIsNull(patronId)
            .stream().map(CheckoutResponse::from).toList();
    }

    @Transactional
    public CheckoutResponse returnByCheckoutId(String checkoutId) {
        var checkout = checkoutRepository.findById(checkoutId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Checkout not found: " + checkoutId));

        if (checkout.getReturnDate() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Item already returned");
        }

        checkout.setReturnDate(LocalDateTime.now());
        checkout.getItem().setAvailable(true);
        itemRepository.save(checkout.getItem());

        return CheckoutResponse.from(checkoutRepository.save(checkout));
    }

    @Transactional
    public CheckoutResponse returnByBarcode(String barcode) {
        var item = itemRepository.findByBarcode(barcode)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Item not found with barcode: " + barcode));

        var checkout = checkoutRepository.findByItem_ItemIdAndReturnDateIsNull(item.getItemId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No active checkout found for barcode: " + barcode));

        return returnByCheckoutId(checkout.getCheckoutId());
    }
}
