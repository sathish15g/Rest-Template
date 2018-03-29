package com.sathish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sathish.country.model.Country;
import com.sathish.country.repo.CountryRepo;

@Service
public class CountryService {

	@Autowired
	private CountryRepo countryRepo;

	public List<Country> getAllCountry() {
		return countryRepo.findAll();
	}

	public Country getCountryByCode(String countryCode) {
		return countryRepo.findOne(countryCode);
	}

	public void deleteByCountryCode(String countryCode) {
		countryRepo.delete(countryCode);
	}

	public void createCountry(Country country) {
		countryRepo.save(country);
	}

	public void updateCountry(Country country) {
		countryRepo.save(country);
	}

}
