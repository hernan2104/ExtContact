package com.itx.clientes;




import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.interaxa.contactos.BulkContacts;
import com.interaxa.contactos.ClienteExport;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import jakarta.servlet.http.HttpServletResponse;

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
    
    @GetMapping("/download-csv")
    public void exportCSV(HttpServletResponse response,
       		@RequestParam(value = "typeContactExp", required = false) String typeContact) 
          throws Exception {
    	 String filename;
    	if (typeContact.equalsIgnoreCase("puri"))
    		filename = "purina_EXTContact.csv";
    	else
    		filename = "professional_EXTContact.csv";
       // set file name and content type

       response.setContentType("text/csv");
       response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
             "attachment; filename=\"" + filename + "\"");
 	  BulkContacts cont = new BulkContacts();
 	  ArrayList<ClienteExport> clientes = cont.scanContacts(typeContact);
      // create a csv writer/*
 	 HeaderColumnNameMappingStrategy<ClienteExport> strategy = new HeaderColumnNameMappingStrategy<>();
     strategy.setType(ClienteExport.class);
     String headerLine = Arrays.stream(ClienteExport.class.getDeclaredFields())
    	        .map(field -> field.getAnnotation(CsvBindByName.class))
    	        .filter(Objects::nonNull)
    	        .map(CsvBindByName::column)
    	        .collect(Collectors.joining(","));
     try (StringReader reader = new StringReader(headerLine)) {
         CsvToBean<ClienteExport> csv = new CsvToBeanBuilder<ClienteExport>(reader)
                 .withType(ClienteExport.class)
                 .withMappingStrategy(strategy)
                 .build();
         for (ClienteExport csvCli : csv) {}
     }
 	  StatefulBeanToCsv<ClienteExport> writer = 
            new StatefulBeanToCsvBuilder<ClienteExport>
                 (response.getWriter())
                 .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                   .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                   .withMappingStrategy(strategy) 
            .withOrderedResults(false).build();
      

      if ((clientes != null) && (clientes.size() > 0)){
    	  System.out.println(clientes.get(0).getIDGenesys());
    		  writer.write(clientes);
      }
      
    } 
    
    @PostMapping("/upload-deletecsv")
    public String upload(@RequestParam("file_del") MultipartFile file, Model model)
    {
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.registerModule(new JavaTimeModule());
    	try {
          if (file.isEmpty()) {
    //    	  	logger.error("Seleccione el archivo para importar los contacto");
                model.addAttribute("message", "Seleccione el archivo para importar los contacto.");
                model.addAttribute("status", false);
          }
          else {
        	  BulkContacts cont = new BulkContacts(file.getInputStream());
        	  cont.setMaxContactosToProcess(2);
        	  cont.deleteContact();
    		  model.addAttribute("status", true);
    		 // Resultado res = new Resultado(1,1, 0, 1,0);
    		  Resultado res = new Resultado(cont.getResults().getTotalContactos(),
    				  						cont.getResults().getTotalContactosOk(),
    				  						cont.getResults().getTotalContactosError(), 
    				  						cont.getResults().getTotalContactosOk_API(), 
    				  						cont.getResults().getTotalContactosError_API() );
    		  model.addAttribute("resultado", res);
    //		  logger.info("Resultado de la operacion");
    	//	  logger.info("Total Contactos: " + cont.getResults().getTotalContactos());
    		//  logger.info("Total Contactos OK (csv): " + cont.getResults().getTotalContactosOk());
    	//	  logger.info("Total Contactos Error(csv): " + cont.getResults().getTotalContactosError());
   // 		  logger.info("Total Contactos OK(API): " + cont.getResults().getTotalContactosOk_API());
  //  		  logger.info("Total Contactos Error(API): " + cont.getResults().getTotalContactosError_API());

          }
       }
       catch (IOException  e) {
       //	  logger.error(e.getMessage());
       	  model.addAttribute("status", false);
       	  model.addAttribute("message",e.getMessage());
         }
         catch (Exception  e) {
        // 	logger.error(e.getMessage());
         	 model.addAttribute("status", false);
           	 model.addAttribute("message",e.getMessage());
         }
    	return "file-upload-status";
    }

}
