package com.example.metier;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.CompteRepository;
import com.example.dao.OperationRepository;
import com.example.entities.Compte;
import com.example.entities.CompteCourant;
import com.example.entities.Operation;
import com.example.entities.Retrait;
import com.example.entities.Versement;

@Service
@Transactional
public class BanqueMetierImpl implements IBanqueMetier {

	@Autowired
	private CompteRepository compteRepository;
	
	@Autowired
	private OperationRepository operationRepository;
	
	
	@Override
	public Compte consulterCompte(String codeCpte) {

		Optional<Compte> cp1=compteRepository.findById(codeCpte);
		Compte cp=cp1.get();

		if (cp==null) throw new RuntimeException("Compte introuvable");
		return cp;
	}

	
	
	@Override
	public String verser(String codeCpte, double montant) {

	String message;
	Compte cp=consulterCompte(codeCpte);
	Operation v=new Versement(new Date(), montant, cp);
	operationRepository.save(v);
	cp.setSolde(cp.getSolde()+montant);
	compteRepository.save(cp);
	
	message="Operation effectuer avec succees";
	return message;
		
	}

	@Override
	public String retirer(String codeCpte, double montant) {

	String message;
	double facilitesCaisse = 0;
	Optional<Compte> cp1=compteRepository.findById(codeCpte);
	Compte cp=cp1.get();
	
	if (cp instanceof CompteCourant) 
		facilitesCaisse=((CompteCourant) cp).getDecouvert();
	
	if (cp.getSolde()+facilitesCaisse <montant) 
	   throw new RuntimeException("Solde insuffisant");
	
	Retrait r=new Retrait(new Date(), montant, cp);
	operationRepository.save(r);
	cp.setSolde(cp.getSolde()-montant);
	compteRepository.save(cp);
	
	message="Operation effectuer avec succees";
	return message;
		
		
	}

	@Override
	public String virement(String codeCpte1, String codeCpte2, double montant) {
		
		String message;
		if (codeCpte1.equals(codeCpte2)) 
			throw new RuntimeException("Impossibile d effectuer un virement sur le meme compte");
		
		retirer(codeCpte1, montant);
		verser(codeCpte2, montant);
		
		message="Operation effectuer avec succees";
		return message;
		
	}

	@Override
	public Page<Operation> listOperation(String codeCpte, int page, int size) {

		return operationRepository.listOperation(codeCpte, new PageRequest(page, size));

	}


}
