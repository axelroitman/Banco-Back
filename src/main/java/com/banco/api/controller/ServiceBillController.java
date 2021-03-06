package com.banco.api.controller;

import com.banco.api.dto.others.ServiceCreatedDTO;
import com.banco.api.dto.others.ServiceCsvDTO;
import com.banco.api.dto.others.ServiceDTO;
import com.banco.api.model.ServicePayment;
import com.banco.api.service.billService.BillService;
import com.banco.api.service.billService.CreateBillServiceResult;
import com.banco.api.utils.DateUtils;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/service-bill")
public class ServiceBillController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBillController.class);

	@Autowired
    BillService billService;
	
	@PostMapping("/create")
	public ResponseEntity createServiceBills(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("vendorUsername") String vendorUsername, @RequestParam("vendorAccountType") String vendorAccountType){
		Collection<ServicePayment> services = new ArrayList<ServicePayment>();
		ArrayList<String> repeatedIds;
		ServiceCreatedDTO serviceCreatedDTO = new ServiceCreatedDTO();
		if(file.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		else {
			try {
				Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				CsvToBean<ServiceCsvDTO> csvToBean = new CsvToBeanBuilder(reader)
						.withType(ServiceCsvDTO.class)
						.withIgnoreLeadingWhiteSpace(true)
						.build();
				List<ServiceCsvDTO> servicesCsv = csvToBean.parse();

				CreateBillServiceResult createServiceResult = billService.createService(servicesCsv, name, vendorUsername,
						vendorAccountType);

				serviceCreatedDTO.setVendorId(createServiceResult.getVendorId());
				serviceCreatedDTO.setIds(createServiceResult.getRepeatedIds());
				if (createServiceResult.getRepeatedIds().size() == servicesCsv.size()) {
					return new ResponseEntity<>(HttpStatus.IM_USED); //Todos los ids pasados existen
				}
				return new ResponseEntity<>(serviceCreatedDTO, HttpStatus.CREATED);
				
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		}
		
	}

	@GetMapping("/search")
    public ResponseEntity<ServiceDTO> getServiceBill(@RequestParam String servicePaymentId, @RequestParam String vendorId, @RequestParam String dueDate) {
		ServicePayment servicePayment = billService.searchNotPayedServiceBill(servicePaymentId, vendorId, dueDate);
	        if (servicePayment != null) {
	            return new ResponseEntity<>(servicePayment.toView(), HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	        }	
    }
	
}
