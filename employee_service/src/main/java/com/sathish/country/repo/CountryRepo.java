package com.sathish.country.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sathish.country.model.Country;

public interface CountryRepo extends JpaRepository<Country, String>{

}
