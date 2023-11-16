package com.itx.clientes;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.interaxa.contactos.BulkContacts;

@Controller
public class FilesController {

	// inject via application.properties
	
    @GetMapping("/")
    public String main(Model model) {


        return "welcome"; //view
    }
    
    @PostMapping("/upload-csv")
    public String upload(@RequestParam("file") MultipartFile file, 
    		@RequestParam(value = "optAdd", required = false) String opAdd, 
    		@RequestParam(value = "typeContactAdd", required = false) String typeContact,
    		Model model) {
    	
        try {
            if (file.isEmpty()) {
                  model.addAttribute("message", "Seleccione el archivo para importar los contacto.");
                  model.addAttribute("status", false);
            }
            else {
      		  model.addAttribute("status", true);
        	  BulkContacts cont = new BulkContacts(file.getInputStream());
        	  cont.setEsquema(typeContact);
        	  cont.setMaxContactosToProcess(1);
        	  cont.createContact();
    		  model.addAttribute("status", true);
    		 // Resultado res = new Resultado(1,1, 0, 1,0);
    		  Resultado res = new Resultado(cont.getResults().getTotalContactos(),
    				  						cont.getResults().getTotalContactosOk(),
    				  						cont.getResults().getTotalContactosError(), 
    				  						cont.getResults().getTotalContactosOk_API(), 
    				  						cont.getResults().getTotalContactosError_API() );
    		  model.addAttribute("resultado", res);
    //		  logger.info("Resultado de la operacion");
    //		  logger.info("Total Contactos: " + cont.getResults().getTotalContactos());
    //		  logger.info("Total Contactos OK (csv): " + cont.getResults().getTotalContactosOk());
    //		  logger.info("Total Contactos Error(csv): " + cont.getResults().getTotalContactosError());
    //		  logger.info("Total Contactos OK(API): " + cont.getResults().getTotalContactosOk_API());
    //		  logger.info("Total Contactos Error(API): " + cont.getResults().getTotalContactosError_API());
            }
        }
        catch (Exception  e) {
          	 model.addAttribute("status", false);
           	 model.addAttribute("message",e.getMessage());
        }
        return "file-upload-status";

    }    
}
