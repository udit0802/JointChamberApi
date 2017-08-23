package com.airtel.prod.engg.joint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airtel.prod.engg.joint.constant.JointConstants;
import com.airtel.prod.engg.joint.model.Manhole;
import com.airtel.prod.engg.joint.model.Response;
import com.airtel.prod.engg.joint.model.ResponseWrapper;
import com.airtel.prod.engg.joint.model.Status;
import com.airtel.prod.engg.joint.service.JointService;

@RestController
@RequestMapping(value = "/joint/chamber")
public class JointController {
	
	@Autowired
	private JointService jointService;

	@RequestMapping(value = "/save/manhole", method = RequestMethod.POST,consumes = "application/json")
	public ResponseWrapper<String> saveInfo(@RequestBody Manhole manhole){
		ResponseWrapper<String> wrapper = null;
		Status status = new Status();
		Response<String> response = new Response<String>();
		try{
		response.setResponse(jointService.saveInfo(manhole));
		status.setCode(200);
		wrapper = new ResponseWrapper<String>(status, response);
		}catch(Exception e){
			status.setCode(500);
			status.setMessage(e.getMessage());
			wrapper = new ResponseWrapper<String>(status, response);
		}
			return wrapper;
	}
	
	@RequestMapping(value = "/get/manhole", method = RequestMethod.GET,produces = "application/json")
	public ResponseWrapper<Manhole> saveInfo(@RequestParam String manholeNumber){
		ResponseWrapper<Manhole> wrapper = null;
		Status status = new Status();
		Response<Manhole> response = new Response<Manhole>();
		try{
		response.setResponse(jointService.getManholeInfo(manholeNumber));
		status.setCode(200);
		wrapper = new ResponseWrapper<Manhole>(status, response);
		}catch(Exception e){
			status.setCode(500);
			status.setMessage(e.getMessage());
			wrapper = new ResponseWrapper<Manhole>(status, response);
		}
			return wrapper;
	}
}
