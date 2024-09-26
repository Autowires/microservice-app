package com.microservices.app.controller.admin;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.app.dto.UserStatus;
import com.microservices.app.dto.Userdto;

import ch.qos.logback.core.model.Model;


@Controller
public class UsersController {

	@Autowired
	private DiscoveryClient discoveryClient;
	@RequestMapping("/admin")
	public ModelAndView getUserAndView(@RequestParam(required = false) String role) {
	    String baseUrl = discoveryClient.getInstances("authentication").stream()
	                                    .findFirst()
	                                    .map(si -> si.getUri().toString() + "/admin/users")
	                                    .orElseThrow(() -> new RuntimeException("Authentication service is not available"));

	    try {
	        String response = new RestTemplate().exchange(baseUrl, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class).getBody();
	        List<Userdto> users = new ObjectMapper().readValue(response, new TypeReference<List<Userdto>>() {});
	        
	        if (role != null) {
	            users = users.stream().filter(u -> u.getUserType().equals(role)).toList();
	        }

	        return new ModelAndView("admin/users").addObject("users", users);
	        
	    } catch (HttpClientErrorException e) {
	        return new ModelAndView("error").addObject("message", "Client error: " + e.getStatusCode());
	    } catch (ResourceAccessException e) {
	        return new ModelAndView("error").addObject("message", "Service not reachable: " + e.getMessage());
	    } catch (Exception e) {
	        return new ModelAndView("error").addObject("message", "Failed to contact admin service: " + e.getMessage());
	    }
	}

	@GetMapping("/admin/reviewRequest")
	public String reviewRequests(Model model) {
		return "admin/reviewRequest";
		
	}

	@PutMapping("/admin/status")
	public ModelAndView putUserStatus(@RequestParam long userId, @RequestParam UserStatus status ) {
	    // Discover the "admin" service instances
//		long userId=1;
//		String status = "DEACTIVATED";
		
		
//		
//		
//		
//		// where is authentication service to update the users status how i get it?
//		var instances = discoveryClient.getInstances("authentication");
//		var service = instances.get(0);
//		// how do i get her address
//		
//		var address = service.getUri();
//		
//		var path = address+"?userId="+userId+"&status="+status;
//		
//		RestTemplate rt = new RestTemplate();
//		rt.put(path, null);
//		
//		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	    List<ServiceInstance> instances = discoveryClient.getInstances("authentication");

	    if (instances.isEmpty()) {
	        ModelAndView mv = new ModelAndView("error");
	        mv.addObject("message", "Service not available");
	        return mv;
	    }

	    ServiceInstance serviceInstance = instances.get(0);
	    String baseUrl = serviceInstance.getUri().toString() + "/admin/users/status?userId";

	    // Create the URL for PUT request with userId and status as parameters
	    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
	            .queryParam("userId", userId)
	            .queryParam("status", status);

	    // Debugging: Log the URL being called
	    System.out.println("Calling URL: " + uriBuilder.toUriString());

	    // Create a RestTemplate instance
	    RestTemplate restTemplate = new RestTemplate();

	    // Set up headers
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

	    // Create HTTP entity with the headers
	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    try {
	        // Send PUT request to update the user status
	        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, entity, String.class);
	    } catch (Exception e) {
	        // Handle the error gracefully
	        ModelAndView mv = new ModelAndView("error");
	        mv.addObject("message", "Failed to update user status: " + e.getMessage());
	        
	        return mv;
	    }

	    // Redirect back to the users page after the update
	    return new ModelAndView("redirect:/admin/users");
	}





    
    
  

}
