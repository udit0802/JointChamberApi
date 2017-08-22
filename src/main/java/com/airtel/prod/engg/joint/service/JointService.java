package com.airtel.prod.engg.joint.service;

import com.airtel.prod.engg.joint.model.Manhole;

public interface JointService {

	String saveInfo(Manhole manhole) throws Exception;
	
	Manhole getManholeInfo(String manholeNumber) throws Exception;

}