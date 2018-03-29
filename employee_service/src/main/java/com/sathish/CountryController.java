package com.sathish;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sathish.country.model.Country;
import com.sathish.service.CountryService;

@RestController
@RequestMapping("/countryservice")
public class CountryController {

	@Autowired
	private CountryService countryService;

	@RequestMapping(value = "/countries", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<List<Country>> getAllCountries() {
		 List<Country> countries = countryService.getAllCountry();
		 return new ResponseEntity<List<Country>>(countries, HttpStatus.OK);
	}

	@RequestMapping(value = "/country")
	public ResponseEntity<Country> getCountryByCode(@RequestParam(required = true, name = "countryCode") String countryCode) {

		Country country = countryService.getCountryByCode(countryCode);
		if (country != null) {
			return new ResponseEntity<Country>(country, HttpStatus.OK);
		} else {
			return new ResponseEntity<Country>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/country/{countryCode}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteCountry(@PathVariable("countryCode") String countryCode) {
		countryService.deleteByCountryCode(countryCode);
		return new ResponseEntity<String>("Deleted country : "+countryCode,HttpStatus.OK);
	}

	@RequestMapping(value = "/country", method = RequestMethod.POST)
	public ResponseEntity<Country> createCountry(@RequestBody Country country) {
		countryService.createCountry(country);
		return new ResponseEntity<Country>(country,HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/country/{countryCode}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateCountry(@PathVariable("countryCode") String countryCode, @RequestBody Country country) {
		Country cntry = countryService.getCountryByCode(countryCode);
		if(cntry!=null) {
			cntry.setCountryDesc(country.getCountryDesc());
			countryService.updateCountry(cntry);
			return new ResponseEntity<Object>(cntry, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Object>("Country not found", HttpStatus.OK);	
		}
		
	}
	
}
