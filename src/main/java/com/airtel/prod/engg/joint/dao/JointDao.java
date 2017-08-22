package com.airtel.prod.engg.joint.dao;

import com.airtel.prod.engg.joint.model.Manhole;
import com.airtel.prod.engg.joint.model.ManholeDuctInfo;

public interface JointDao {

	String saveInfo(Manhole manhole) throws Exception;
	
	Manhole getManholeInfo(String manhole) throws Exception;

}