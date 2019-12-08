package com.example.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entities.Compte;
import com.example.entities.Operation;
import com.example.metier.IBanqueMetier;

@Controller
public class BanqueController {

	@Autowired
	private IBanqueMetier banqueMetier;
	
	
	@RequestMapping("/operations")
	public String index () {
		
		return "comptes";
	} 
	
	@RequestMapping("/consulterCompte")
	public String consulter(Model model, String codeCompte,
			@RequestParam(name="page",defaultValue="0")int page,
			@RequestParam(name="size",defaultValue="5")int size
) {
		
		model.addAttribute("codeCompte", codeCompte);
		try {
			
			Page<Operation> pageoperations=banqueMetier.listOperation(codeCompte,page,size);
			model.addAttribute("listeOperation", pageoperations.getContent());
			
			int [] pages=new int [pageoperations.getTotalPages()];
			model.addAttribute("pages", pages);
			
			Compte cp=banqueMetier.consulterCompte(codeCompte);
			model.addAttribute("compte", cp);
			
		} catch (Exception e) {
			
			model.addAttribute("exception", e);
		}
		
		return "comptes";
	} 
	
	@RequestMapping (value="/saveOperation",method=RequestMethod.POST)
	public String saveOperation (Model model,String typeOeration,String codeCompte,double montant,String codeCompte2) {
		
		String message = null;
		try {
			if (typeOeration.equals("VERS")) {message=banqueMetier.verser(codeCompte, montant);}
			if (typeOeration.equals("RET")) {message=banqueMetier.retirer(codeCompte, montant);}
			if (typeOeration.equals("VIR")) {message=banqueMetier.virement(codeCompte, codeCompte2, montant);}
		
		 	
		
		} catch (Exception ex) {
			
			model.addAttribute("message",ex);
			return "redirect:/consulterCompte?codeCompte="+codeCompte
					+"&message="+ex.getMessage();
		}
		

		return "redirect:/consulterCompte?codeCompte="+codeCompte+"&message="+message;
		
	} 
	
}
