package com.robotica.kohasimulator.repository;

import com.robotica.kohasimulator.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, String> {
    Optional<Branch> findByBranchcode(String branchcode);
}
