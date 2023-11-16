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

            }
        }
        catch (Exception  e) {
          	 model.addAttribute("status", false);
           	 model.addAttribute("message",e.getMessage());
        }
        return "file-upload-status";

    }    
}
