package com.copyleft.snippos.repository;

import com.copyleft.snippos.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    Authority findOneByName(String name);

}