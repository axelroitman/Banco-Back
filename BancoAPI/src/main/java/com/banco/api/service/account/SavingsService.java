package com.banco.api.service.account;

import com.banco.api.dto.account.SavingsDTO;
import com.banco.api.dto.account.request.SavingsRequest;
import com.banco.api.model.account.Savings;
import com.banco.api.repository.account.SavingsRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SavingsService extends AccountService<Savings, SavingsDTO, SavingsRequest> {

	private static final Float ANUAL_INTEREST_RATE = 15F;

	@Autowired
	SavingsRepository savingsRepository;

    @Override
    public Savings createAccount() {
        Savings account = new Savings(0, ANUAL_INTEREST_RATE);
        
        while(existsAccountNumber(account.getAccountNumber())) {
            account = new Savings(0, ANUAL_INTEREST_RATE);
        }
        
        Savings result = savingsRepository.save(account);
        return result;
    }
    
    public boolean existsAccountNumber(String accountNumber) {
    	boolean result;
    	
    	if(savingsRepository.findByAccountNumber(accountNumber) == null) {
    		result = false;
    	}
    	else {
    		result = true;
    	}
    	return result;
    }
    
    public Savings findByAccountNumber(String accountNumber) {
    	Savings result = savingsRepository.findByAccountNumber(accountNumber);
    	return result;
    }

    public List<Savings> findAll() {
    	return Lists.newArrayList(savingsRepository.findAll());
	}
    
    public boolean existsCbu(String cbu) {
    	boolean result;
    	
    	if(savingsRepository.findByCbu(cbu) == null) {
    		result = false;
    	}
    	else {
    		result = true;
    	}
    	return result;
    }
    
    public Savings findByCbu(String cbu) {
    	Savings result = savingsRepository.findByCbu(cbu);
    	return result;
    }
    
    public Savings update(Savings savings) {

    	Savings result = savingsRepository.save(savings);
		return result;
     }
    
	public Savings closeAccount(Savings savings) {
		
		if(Float.compare(savings.getBalance(), 0) == 0)
		{
			savings.setActive(false);
			Savings result = savingsRepository.save(savings);
			return result;
		}
		else
		{
			return savings;
		}
	}


}

    