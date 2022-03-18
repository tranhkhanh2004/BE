package com.example.demo.API;

import com.example.demo.Convert.UsersConvert;
import com.example.demo.DTO.ChangProfile;
import com.example.demo.DTO.ChangsPassword;
import com.example.demo.DTO.UsersDTO;
import com.example.demo.Entity.RolesEntity;
import com.example.demo.Entity.User;
import com.example.demo.Repository.RolesRepository;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.Service.RolesService;
import com.example.demo.Service.UsersService;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.JwtTokenProvider;
import com.example.demopayload.JWTAuthResponse;

import springfox.documentation.service.ResponseMessage;

import java.util.Collections;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AuthController {
	
	
	//private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersService  usersService;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
	UsersConvert usersConvert;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@Validated @RequestBody User loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getusername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated @RequestBody UsersDTO dto){

        // add check for username exists in a DB
        if(usersRepository.existsByUsername(dto.getusername())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        // create user object
        dto.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
		User user = usersConvert.DtoToEntity(dto);
        RolesEntity roles = rolesRepository.findByName("ROLE_READER").get();
        user.setRoles(Collections.singleton(roles));
        user=usersService.saveUser(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }
    @PutMapping("/changeprofile")
    public ResponseEntity<?> changeProfile(@Validated HttpServletRequest request,@RequestBody ChangProfile cf){
    	  String token = JwtAuthenticationFilter.getJWTfromRequest(request);
    	  String username=tokenProvider.getUsernameFromJWT(token);
    	  User user;
    	  try {
    		  if(usersRepository.existsByUsername(cf.getusername())) {
    			  
    			  return new ResponseEntity<>("Username", HttpStatus.OK);
    		  }
    		  user= usersRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User", "Id", "id"));
    		  user.setFullname(cf.getFullname());
    		  user.setAddress(cf.getAddress());
    		  user.setBirth(cf.getBirth());
    		  user.setPhone(cf.getPhone());
    		  user.setEmail(cf.getEmail());
    		  user.setSex(cf.getSex()); 
    		 usersService.saveUser(user);
    		 return new ResponseEntity<>("Yes", HttpStatus.OK);
    		  }catch (UsernameNotFoundException exception) {
    			  return new ResponseEntity<>("No", HttpStatus.NOT_FOUND);
    		  }
    }
    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword(@Validated HttpServletRequest request,@RequestBody ChangsPassword cf){
    	  String token = JwtAuthenticationFilter.getJWTfromRequest(request);
    	  String username=tokenProvider.getUsernameFromJWT(token);
    	  
    	  try {
    		  if(usersRepository.existsByUsername(cf.getusername())) {
    			  
    			  return new ResponseEntity<>("Username", HttpStatus.OK);
    		  }
    	User   currentuser= usersRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User", "Id", "id"));
    		if(new BCryptPasswordEncoder().matches(cf.getOldpassword(), currentuser.getPassword()))
    		{ if(cf.getPassword().equals(cf.getRePassword()))
    		{return new ResponseEntity<>("No1", HttpStatus.NOT_FOUND);
    			
    		}else {  currentuser.setPassword(new BCryptPasswordEncoder().encode(cf.getPassword()));
			this.usersService.saveUser(currentuser); }
    		
    		} else {  return new ResponseEntity<>("No2", HttpStatus.NOT_FOUND); }
    		 return new ResponseEntity<>("Yes", HttpStatus.OK);
    		  }catch (UsernameNotFoundException exception) {
    			  return new ResponseEntity<>("No3", HttpStatus.NOT_FOUND);
    		  }
    }
    }
