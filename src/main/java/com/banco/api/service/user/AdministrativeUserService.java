package com.banco.api.service.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import com.banco.api.exception.DuplicatedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.api.utils.DateUtils;
import com.banco.api.dto.user.AdministrativeUserDTO;
import com.banco.api.dto.user.request.AdministrativeUserRequest;
import com.banco.api.model.user.Administrative;
import com.banco.api.repository.user.AdministrativeRepository;

@Service
public class AdministrativeUserService extends UserService<Administrative, AdministrativeUserDTO, AdministrativeUserRequest>{
	
	@Autowired
	AdministrativeRepository administrativeRepository;
	@Autowired
	private PhysicalUserService physicalUserService;
	@Autowired
	private LegalUserService legalUserService;
	
	
	
	public AdministrativeUserDTO createUser(AdministrativeUserRequest request) {
        if (existsUser(request.getUsername()) || physicalUserService.existsUser(request.getUsername())
				|| legalUserService.existsByUsername(request.getUsername())) {
            throw new DuplicatedUserException("El nombre de usuario ya existe");
        }

		if (existsByDni(request.getDni()) || physicalUserService.existsByDni(request.getDni())) {
			throw new DuplicatedUserException("Ya existe un usuario con el mismo DNI");
		}

        Administrative user = new Administrative();
        this.mapCommonUser(user, request);
        user.setActive(true);
        user.setBirthdate(DateUtils.parse(request.getBirthDate()));
        user.setDni(request.getDni());
        user.setMobilePhone(request.getMobilePhone());
        user.setPhone(request.getPhone());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        AdministrativeUserDTO result = user.toView();
		result.setPassword(user.getPassword());
		user.hashPassword(user.getPassword());
        Administrative saved = administrativeRepository.save(user);
        result.setId(saved.getId());
        return result;
    }
	
	public boolean existsUser(String username) {
        Administrative result = findByUsername(username);
        return result != null;
    }
	
	public Administrative findByUsername(String username) {
		Administrative administrativeUser = administrativeRepository.findByUsername(username);
		return administrativeUser;
    }
	
    public Administrative findByActiveUsername(String username) {
    	Administrative administrativeUser = administrativeRepository.findByUsernameAndUserTypeNumberAndActive(username, 2, true);
        return administrativeUser;
    }

    
    public AdministrativeUserDTO update(Administrative administrative) {
    	Administrative administrativeUser = administrativeRepository.save(administrative);
        return administrativeUser.toView();
    }
    
    public byte login(String username, String password) { // 1= Logued ; 2= Error ; 3= FirstLogin (Logued, but different code)
    	Administrative user = findByActiveUsername(username);
		byte result = 2;
		
		String hashedPass = null;
    	MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
	        byte[] digest = md.digest();
	        hashedPass = DatatypeConverter.printHexBinary(digest).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(user.getPassword().equals(hashedPass)) {
			if(user.isFirstLogin()) {
				user.setFirstLogin(false);
				update(user);
				result = 3;
			}
			else {
				result = 1;
			}
		}
		
		return result;
	}

	public boolean existsByDni(String dni) {
		return administrativeRepository.existsByDni(dni);
	}
}
