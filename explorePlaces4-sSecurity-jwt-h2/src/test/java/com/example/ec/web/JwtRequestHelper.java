/**
 * 
 */
package com.example.ec.web;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.example.ec.domain.Role;
import com.example.ec.security.JwtProvider;

/**

 * Helper class for creating HTTP Headers before invoking an API with TestRestClient.
 * 
 * @author amit
 *
 */

@Component
public class JwtRequestHelper {

	@Autowired
	private static JwtProvider jwtProvider;

//	@Autowired
//	private JwtProvider applicationUnderTestJwtProvider;
//
//	@PostConstruct
//	private void initProvider() {
//		jwtProvider = applicationUnderTestJwtProvider;
//	}


	/**
	 * Build Request entity for logged in user with a payload.
	 *
	 * @param username
	 * @param withRoleId
	 * @param payload
	 * @return
	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static HttpEntity loggedInAs(String username, String withRoleId, Object payload) {
//		return new HttpEntity(payload, forUser(username, withRoleId));
//	}


	/**
	 * Build Request entity for logged in user, no payload.
	 *
	 * @param username
	 * @param withRoleId
	 * @return
	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static HttpEntity loggedInAs(String username, String withRoleId) {
//		return new HttpEntity(forUser(username, withRoleId));
//	}

	/**
	 * Generate the appropriate headers for JWT Authentication.
	 *
	 * @param username username
	 * @param withRoleId role identifier
	 * @return Http Headers for Content-Type and Authorization
	 */
//	private static HttpHeaders forUser(String username, String withRoleId) {
//
//		HttpHeaders headers = new HttpHeaders();
//
//		Role r = new Role();
//		r.setRoleName(withRoleId);
//
//		String token = jwtProvider.createToken(username, Arrays.asList(r));
//		headers.setContentType(APPLICATION_JSON);
//		headers.add("Authoriation", "Bearer " + token);
//
//		return headers;
//	}


	
	/**
	 * Generate the appropriate headers for JWT Authentication.
	 *
	 * @param username username
	 * @param withRoleId role identifier
	 * @return Http Headers for Content-Type and Authorization
	 */
	public HttpHeaders withRole(String roleName) {

		HttpHeaders headers = new HttpHeaders();

		Role r = new Role();
		r.setRoleName(roleName);

		String token = jwtProvider.createToken("anonymous", Arrays.asList(r));
		headers.setContentType(APPLICATION_JSON);
		headers.add("Authoriation", "Bearer " + token);

		return headers;
	}

}
