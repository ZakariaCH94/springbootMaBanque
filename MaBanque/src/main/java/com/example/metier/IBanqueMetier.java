package com.example.metier;

import org.springframework.data.domain.Page;

import com.example.entities.Compte;
import com.example.entities.Operation;

public interface IBanqueMetier {
	
	public Compte consulterCompte(String codeCpte);
	public String verser(String codeCpte,double montant);
	public String retirer(String codeCpte,double montant);
	public String virement(String codeCpte1,String codeCpte2,double montant);
	public Page<Operation> listOperation(String codeCpte,int page,int size);

}
