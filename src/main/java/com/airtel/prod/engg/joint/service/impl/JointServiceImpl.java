package com.airtel.prod.engg.joint.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airtel.prod.engg.joint.dao.JointDao;
import com.airtel.prod.engg.joint.model.Manhole;
import com.airtel.prod.engg.joint.service.JointService;

@Component
public class JointServiceImpl implements JointService {
	
	@Autowired
	private JointDao jointDao;

	/* (non-Javadoc)
	 * @see com.airtel.prod.engg.joint.service.impl.JointService#saveInfo(com.airtel.prod.engg.joint.model.Manhole, java.lang.String)
	 */
	@Override
	public String saveInfo(Manhole manhole) throws Exception{
		return jointDao.saveInfo(manhole);
	}
	
	@Override
	public Manhole getManholeInfo(String manholeNumber) throws Exception{
		return jointDao.getManholeInfo(manholeNumber);
	}
}
