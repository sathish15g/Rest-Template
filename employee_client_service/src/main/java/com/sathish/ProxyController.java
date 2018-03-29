package com.sathish;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/proxyservice")
public class ProxyController {

	@Value("${employee.base.url}")
	private String employeebaseUrl;

	@Value("${country.base.url}")
	private String countrybaseUrl;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	private ResponseEntity<Object[]> getEmployees() {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(employeebaseUrl.concat("/employees"));
		ResponseEntity<Object[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				Object[].class);
		return result;
	}

	/*
	 * @RequestMapping(value = "/employees/{id}", method = RequestMethod.PUT)
	 * private void updateEmployee() {
	 * 
	 * Map<String, String> params = new HashMap<String, String>(); params.put("id",
	 * "2");
	 * 
	 * EmployeeVO updatedEmployee = new EmployeeVO(2, "New Name", "Gilly",
	 * "test@email.com");
	 * 
	 * UriComponentsBuilder builder =
	 * UriComponentsBuilder.fromUriString(employeebaseUrl.concat("employees/{id}"));
	 * restTemplate.put(builder.buildAndExpand(params).toUri(), updatedEmployee); }
	 */

	@RequestMapping(value = "/employees/{id}")
	private EmployeeVO getEmployeeById(@PathVariable("id") String id) {

		// URI (URL) parameters
		Map<String, String> params = new HashMap<String, String>();
		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(employeebaseUrl.concat("/employees/{id}"));
		EmployeeVO result = restTemplate.getForObject(builder.buildAndExpand(params).encode().toUri(),
				EmployeeVO.class);
		return result;
	}

	@RequestMapping(value = "/employees/{id}", method = RequestMethod.DELETE)
	private void deleteEmployee(@PathVariable("id") String id) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(employeebaseUrl.concat("/employees/{id}"));
		restTemplate.delete(builder.buildAndExpand(params).toUri());
	}

	@RequestMapping(value = "/employees", method = RequestMethod.POST)
	private void createEmployee(@RequestBody EmployeeVO newEmployee) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(employeebaseUrl.concat("/employees"));
		EmployeeVO result = restTemplate.postForObject(builder.toUriString(), newEmployee, EmployeeVO.class);
		System.out.println(result);
	}

	@RequestMapping(value = "/countries", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object[]> getAllCountries() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(countrybaseUrl.concat("/countries"));
		ResponseEntity<Object[]> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				Object[].class);
		return result;
	}

	@RequestMapping(value = "/country")
	public ResponseEntity<Country> getCountryByCode(
			@RequestParam(required = true, value = "countryCode") String countryCode) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(countrybaseUrl.concat("/country"))
				.queryParam("countryCode", countryCode);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ResponseEntity<Country> country = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				Country.class);
		return country;
	}

	@RequestMapping(value = "/country", method = RequestMethod.POST)
	public ResponseEntity<Object> createCountry(@RequestBody Country country) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Object> entity = new HttpEntity<Object>(country,headers);	
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(countrybaseUrl.concat("/country"));
		ResponseEntity<Object> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
				Object.class);
		return new ResponseEntity<Object>(response, HttpStatus.CREATED);
	}

	RequestCallback requestCallback(final Country country,final String countryCode) {
		return clientHttpRequest -> {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(countrybaseUrl.concat("/country"))
					.queryParam("countryCode", countryCode);

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

			ResponseEntity<Country> cntr = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					Country.class);
			Country updatedInstance = cntr.getBody();
			updatedInstance.setCountryDesc(country.getCountryDesc());
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(clientHttpRequest.getBody(), updatedInstance);
			clientHttpRequest.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		};
	}
	
	@RequestMapping(value = "/country/{countryCode}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateCountry(@PathVariable("countryCode") String countryCode, @RequestBody Country country) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("countryCode", countryCode);
		

		UriComponentsBuilder builderSec = UriComponentsBuilder.fromUriString(countrybaseUrl.concat("/country/{countryCode}"));
		ResponseEntity<Object> response = restTemplate.execute(
				builderSec.buildAndExpand(params).toUri(), 
				  HttpMethod.PUT, 
				  requestCallback(country,countryCode), 
				  clientHttpResponse -> null);
		return new ResponseEntity<Object>(response, HttpStatus.ACCEPTED);
	}
}
