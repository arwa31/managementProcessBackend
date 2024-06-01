package com.sid.manage.controller;




import com.sid.manage.entities.AdminMTC;
import com.sid.manage.entities.Membre;
import com.sid.manage.entities.Utilisateur;
import com.sid.manage.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import com.sid.manage.metier.UtilisateurService;
import com.sid.manage.model.LoginCredentials;
import com.sid.manage.util.JWTUtil;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/api/auth")
public class AuthMetier {

    @Autowired
    private UtilisateurService userService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailUtil emailUtil;

    //---------------------------------Registry---------------------------------------

    //---------------------------------Registry---------------------------------------
    @PostMapping("/registerMember")
    public ResponseEntity<String> registerHandler(@RequestBody Membre membre) throws MessagingException {
        if (membre.getPassword() == null || membre.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le champ de mot de passe ne peut pas être vide.");
        }
        String encodedPass = passwordEncoder.encode(membre.getPassword());
        membre.setPassword(encodedPass);
        membre.setEtat(0);
        membre = (Membre) userService.save(membre);

        ArrayList<AdminMTC> ListAdmin = userService.getAdminMtc();
      boolean emailSent = false;
        for (AdminMTC adminMTC : ListAdmin) {
            System.out.println("Sending email to: " + adminMTC.getEmail());
            emailSent = emailUtil.sendNotifEmail(adminMTC.getEmail(), membre.getUsername());

        }
      
        if (emailSent) {
            return ResponseEntity.status(HttpStatus.OK).body("Member registered and notification sent successfully.");
           // return ResponseEntity.ok("Member registered and notification sent successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Member registered but failed to send notification.");
        }
    }

    @GetMapping("/exists")
    public Map<String, Boolean> checkOrganisationName(@RequestParam String name) {
        boolean isUnique = userService.isOrganisationNameUnique(name);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", !isUnique);
        return response;
    }


  @PostMapping("/registerAdmin")
    public ResponseEntity<String> register(@RequestBody AdminMTC appUser) throws MessagingException {
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUser = (AdminMTC) userService.save(appUser);
        String responseMessage = "User registered successfully with ID: " + appUser.getId();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
   //------------------------------------OTP--------------------------------------------

    @PutMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestParam String email, @RequestParam String opt) {
        return new ResponseEntity<>(userService.verifyAccount(email, opt), HttpStatus.OK);
    }

    @PutMapping("/regenerate-OPT")
    public ResponseEntity<?> regenerateOPT(@RequestParam String email)  {
        return new ResponseEntity<>(userService.regenerateOpt(email), HttpStatus.OK);
    }

    //------------------------------login--------------------------------------------------
   /* @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body){
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
            authManager.authenticate(authInputToken);
            String token = jwtUtil.generateToken(body.getUsername());
            return Collections.singletonMap("jwt-token", token);
        }catch (AuthenticationException authExc){
            throw new RuntimeException("Invalid Login Credentials");
        }
    }*/

  @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
            authManager.authenticate(authInputToken);

           Utilisateur user = userService.findByUsername(body.getUsername());
            if (user == null) {
                throw new RuntimeException("User not found");
            }

            else if (!user.isActive()) {
                return Collections.singletonMap("message", "Your Account is not verified");
            } else {

                String token = jwtUtil.generateToken(body.getUsername());
                return Collections.singletonMap("jwt-token", token);
            }
        } catch (AuthenticationException authExc) {
            throw new RuntimeException("Invalid Login Credentials");
        }
    }

    //---------------------------------Set Password--------------------------------------

    @PutMapping("/forget-password")
    public ResponseEntity<Map<String, String>> forgetPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String result = userService.forgetPassword(email);
        return new ResponseEntity<>(Map.of("message", result), HttpStatus.OK);
    }


   /*

   @PutMapping("/set-password")
   public ResponseEntity<String> setNewPassword(@RequestBody Map<String, String> requestBody) {
       String email = requestBody.get("email");
       String newPassword = requestBody.get("newPassword");
       String result = userService.setPassword(email, newPassword);
       return new ResponseEntity<>(result, HttpStatus.OK);
   }*/

    @PutMapping("/set-password")
    public ResponseEntity<Map<String, String>> setNewPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String newPassword = requestBody.get("newPassword");
        String result = userService.setPassword(email, newPassword);
        Map<String, String> response = Map.of("message", result);
        return ResponseEntity.ok().body(response);
    }

}