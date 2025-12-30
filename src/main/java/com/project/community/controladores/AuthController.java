package com.project.community.controladores;
//
//import java.util.HashMap;
//
//
//import java.util.Map;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.nimbusds.oauth2.sdk.TokenResponse;
//import com.project.community.DTO.LoginDTO;
//import com.project.community.servicios.AuthService;
//
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
public class AuthController {
	
//	private final AuthService service;
	
//	@PostMapping("/register")
//	public ResponseEntity<TokenResponse> register(@RequestBody final RegisterRequest request) {
//		final TokenResponse token = service.register(request);
//		return ResponseEntity.ok(token);
//	}
//
//	@PostMapping("/login")
//	public ResponseEntity<TokenResponse> authenticate(@RequestBody final LoginRequest request) {
//		final TokenResponse token = service.register(request);
//		return ResponseEntity.ok(token);
//	}
//
//	@PostMapping("/refresh")
//	public ResponseEntity<TokenResponse> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
//
//		return service.refreshToken(authHeader);
//	}
//	private final AuthenticationManager authenticationManager;
//	
//	public AuthController(AuthenticationManager authenticationManager) {
//		this.authenticationManager = authenticationManager;
//	}
//	
//	@PostMapping("/login")
//	public ResponseEntity<?> login(@RequestBody LoginDTO login, HttpServletRequest request){
//		UsernamePasswordAuthenticationToken authToken = 
//				new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
//		Authentication auth = authenticationManager.authenticate(authToken);
//		SecurityContextHolder.getContext().setAuthentication(auth);
//		HttpSession session = request.getSession(true);
//		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
//		Map<String, String>response = new HashMap<>();
//		response.put("message", "Login Successful");
//		return ResponseEntity.ok(response);
//	}
//	
//	@GetMapping("/me")
//	public ResponseEntity<?> me() {
//	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//	    return ResponseEntity.ok(auth.getName());
//	}
	
	
}
