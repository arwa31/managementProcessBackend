package com.sid.manage.metier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.sid.manage.util.EmailUtil;
import com.sid.manage.util.OptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sid.manage.dao.UtilisateurRepo;
import com.sid.manage.entities.*;

import javax.mail.MessagingException;

@Service
@Transactional
public class UtilisateurService {

	@Autowired
	private UtilisateurRepo utilisateurRepository;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private OptUtil optUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Utilisateur findByUsername(String username) {

		return utilisateurRepository.findByUsername(username);
	}


    //------------------------ Registry---------------------------
	public boolean isOrganisationNameUnique(String username) {
		Optional<Utilisateur> user = Optional.ofNullable(utilisateurRepository.findByUsername(username));
		return user.isEmpty();
	}

	public Utilisateur save(Utilisateur appUser) {

		return utilisateurRepository.save(appUser);
	}



	/*public Utilisateur save(Utilisateur appUser) throws MessagingException {
		String opt=optUtil.generateOpt();
		try {
			emailUtil.sendOptEmail(appUser.getEmail(),opt);
		}catch (MessagingException e ){
			throw new MessagingException("Error while saving opt , please try again");
		}
		Utilisateur user = new Utilisateur();
		user.setUsername(appUser.getUsername());
		user.setEmail(appUser.getEmail());
		user.setPassword(appUser.getPassword());
		user.setOpt(opt);
		user.setOptGeneratedTime(LocalDateTime.now());
		return utilisateurRepository.save(user);

	}
*/
	//----------------------------- OTP --------------------------------------

	public String verifyAccount(String email, String opt) {
		Utilisateur user =utilisateurRepository.findUSerByEmail(email)
				.orElseThrow(()->new RuntimeException("User not found with this email: "+email));
		if (user.getOpt().equals(opt) && Duration.between(user.getOptGeneratedTime(),
				LocalDateTime.now()).getSeconds() < (5 * 60)) {
			user.setActive(true);
			utilisateurRepository.save(user);
			return "OTP verified, you can login now";
		}
		return "Please regenerate otp and try again";
	}

	public String regenerateOpt(String email) {
		Utilisateur user =utilisateurRepository.findUSerByEmail(email)
				.orElseThrow(()->new RuntimeException("User not found with this email: "+email));
		String opt=optUtil.generateOpt();
		try {
			emailUtil.sendOptEmail(user.getEmail(),opt);
		}catch (MessagingException e ){
			throw new RuntimeException("Unable to regenerate opt, please try again");
		}
		user.setOpt(opt);
		user.setOptGeneratedTime(LocalDateTime.now());
		utilisateurRepository.save(user);
		return "Email sent.. You can verify you're Email within 1 min ";
	}

//--------------------------------------CRUD-------------------------------------------
	public String deleteUser(long id) {		
		utilisateurRepository.deleteById(id);
			return "User removed !!"+id;
		}

	public List<Utilisateur> getUsers() {
		return utilisateurRepository.findAll();
	}
	public ArrayList<AdminMTC> getAdminMtc() {

		return utilisateurRepository.findByTypeU("adminMTC");
	}

	public Utilisateur getUserById(long id) {
		return utilisateurRepository.findById(id).orElse(null);
	}

	//------------------------------Forget Password----------------------------------------
	public String forgetPassword(String email) {
		Utilisateur user =utilisateurRepository.findUSerByEmail(email)
				.orElseThrow(()->new RuntimeException("User not found with this email: "+email));
        try {
            emailUtil.sendSetPassword(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send set new password email, please try again");
        }
        return "Please Check Your Email To Set Your Password";
	}

	public String setPassword(String email, String newPassword) {
		Utilisateur user = utilisateurRepository.findUSerByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));

		// Encode the new password
		String encodedPassword = passwordEncoder.encode(newPassword);

		user.setPassword(encodedPassword);
		utilisateurRepository.save(user);

		return "New Password Set Successfully, You Can Login Now!";
	}


}
