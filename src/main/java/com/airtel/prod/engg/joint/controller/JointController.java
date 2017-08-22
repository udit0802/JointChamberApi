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
import com.airtel.prod.engg.joint.service.JointService;

@RestController
@RequestMapping(value = "/joint/chamber")
public class JointController {
	
	@Autowired
	private JointService jointService;

	@RequestMapping(value = "/save/manhole", method = RequestMethod.POST,consumes = "application/json")
	public ResponseWrapper<String> saveInfo(@RequestBody Manhole manhole){
		ResponseWrapper<String> wrapper = null;
		try{
		Response<String> response = new Response<String>();
		response.setResponse(jointService.saveInfo(manhole));
		wrapper = new ResponseWrapper<String>(JointConstants.SUCCESS, 200, response, null);
		}catch(Exception e){
			wrapper = new ResponseWrapper<String>(JointConstants.FAILURE, 500, null, e.getMessage());
		}
			return wrapper;
	}
	
	@RequestMapping(value = "/get/manhole", method = RequestMethod.GET,produces = "application/json")
	public ResponseWrapper<Manhole> saveInfo(@RequestParam String manholeNumber){
		ResponseWrapper<Manhole> wrapper = null;
		try{
		Response<Manhole> response = new Response<Manhole>();
		response.setResponse(jointService.getManholeInfo(manholeNumber));
		wrapper = new ResponseWrapper<Manhole>(JointConstants.SUCCESS, 200, response, null);
		}catch(Exception e){
			wrapper = new ResponseWrapper<Manhole>(JointConstants.FAILURE, 500, null, e.getMessage());
		}
			return wrapper;
	}
}
