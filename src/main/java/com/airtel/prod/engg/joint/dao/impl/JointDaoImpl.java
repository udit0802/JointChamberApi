package com.airtel.prod.engg.joint.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.airtel.prod.engg.joint.constant.JointConstants;
import com.airtel.prod.engg.joint.constant.Query;
import com.airtel.prod.engg.joint.dao.JointDao;
import com.airtel.prod.engg.joint.model.Cable;
import com.airtel.prod.engg.joint.model.CableDb;
import com.airtel.prod.engg.joint.model.Connection;
import com.airtel.prod.engg.joint.model.ConnectionDb;
import com.airtel.prod.engg.joint.model.Duct;
import com.airtel.prod.engg.joint.model.End;
import com.airtel.prod.engg.joint.model.Joint;
import com.airtel.prod.engg.joint.model.Manhole;
import com.airtel.prod.engg.joint.model.ManholeDuctInfo;

@Component
public class JointDaoImpl implements JointDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/* (non-Javadoc)
	 * @see com.airtel.prod.engg.dao.impl.JointDao#saveInfo(com.airtel.prod.engg.model.Manhole, java.lang.String)
	 */
	@Override
	public String saveInfo(Manhole manhole) throws Exception{
		String manholeId = manhole.getManholeId();
		getAndDeleteManholeInfo(manholeId);
		Set<String> loopCable = new HashSet<>();
		Map<String,Map<String,Integer>> ductCableMap = new HashMap<String, Map<String,Integer>>();
		Map<String,Set<String>> cableIds = new HashMap<>();
		for(ManholeDuctInfo manholeDuctInfo : manhole.getManholeDuctInfo()){
			String direction = manholeDuctInfo.getDirection();
			String manHolePrimaryKey = manholeId + "_" + direction;
			jdbcTemplate.update(Query.MANHOLE_TABLE_INSERT_QUERY, new Object[]{manHolePrimaryKey,manholeId,manholeDuctInfo.getNoOfDucts(),direction});
			for(Duct duct : manholeDuctInfo.getDucts()){
				String ductId = duct.getDuctId();	
				String ductPrimaryKey = manHolePrimaryKey + "_" + ductId;
				ductCableMap.put(ductPrimaryKey, duct.getCableLoopMap());
				Set<String> ductCableIds = duct.getCableLoopMap().keySet();
				cableIds.put(ductPrimaryKey, ductCableIds);
				jdbcTemplate.update(Query.DUCT_TABLE_INSERT_QUERY, new Object[]{ductPrimaryKey,manHolePrimaryKey,ductId,duct.getNoOfCables(),duct.getColor()});
			}
		}
		Set<String> connectionCableIds = new HashSet<>();
		for(Joint joint : manhole.getJoints()){
			String jointId = manholeId + "_J" + joint.getJointOrder();
			jdbcTemplate.update(Query.JOINT_TABLE_INSERT_QUERY, new Object[]{jointId,joint.getNoOfCables(),manholeId,joint.getJointOrder()});
			for(Connection connection : joint.getConnections()){
				String cablePrimaryKey1 = fetchCableInfo(joint,connection,ductCableMap,1,loopCable,manholeId);
				String cablePrimaryKey2 = fetchCableInfo(joint,connection,ductCableMap,2,loopCable,manholeId);
				connectionCableIds.add(connection.getEnd1().getCableId());
				connectionCableIds.add(connection.getEnd2().getCableId());
				jdbcTemplate.update(Query.CONNECTION_TABLE_INSERT_QUERY, new Object[]{cablePrimaryKey1,cablePrimaryKey2,jointId});
			}
		}
		for(Map.Entry<String, Set<String>> entry : cableIds.entrySet()){
			for(String entry1 : connectionCableIds){
				if(entry.getValue().contains(entry1)){
					entry.getValue().remove(entry1);
				}
			}
		}
		for(Map.Entry<String, Set<String>> entry : cableIds.entrySet()){
			for(String cableId : entry.getValue()){
				Integer loopDistance = ductCableMap.get(entry.getKey()).get(cableId);
				if(loopDistance != null){
					String cablePrimaryKey = entry.getKey() + "_" + cableId;
					jdbcTemplate.update(Query.CABLE_TABLE_INSERT_QUERY, new Object[]{cablePrimaryKey,entry.getKey(),cableId,null,null,null,null,-1});
					jdbcTemplate.update(Query.LOOP_TABLE_INSERT_QUERY, new Object[]{cableId,loopDistance,manholeId});
				}
			}
		}
		return JointConstants.SUCCESS;
	}
	
	private void getAndDeleteManholeInfo(String manholeId){
		List<String> jointIds = jdbcTemplate.queryForList(Query.JOINT_TABLE_SELECT_IDS, new Object[]{manholeId}, String.class);
		for(String jointId : jointIds){
			List<ConnectionDb> connectionsInJoint = jdbcTemplate.query(Query.CONNECTION_TABLE_SELECT_QUERY, new Object[]{jointId}, new BeanPropertyRowMapper(ConnectionDb.class));
			List<String> cableIds = new ArrayList<>();
			for(ConnectionDb connection : connectionsInJoint ){
				cableIds.add(connection.getEnd1());
				cableIds.add(connection.getEnd2());
				
			}
			jdbcTemplate.update(Query.CONNECTION_MASTER_DELETE,new Object[]{jointId});
			for(String cableId : cableIds){
				jdbcTemplate.update(Query.CABLE_MASTER_DELETE,new Object[]{cableId});
			}
			jdbcTemplate.update(Query.JOINT_MASTER_DELETE,new Object[]{jointId});
		}
		
		List<String> manholePrimaryKeys = jdbcTemplate.queryForList(Query.MANHOLE_MASTER_FETCH_PK, new Object[]{manholeId}, String.class);
		if(manholePrimaryKeys.isEmpty()){
			return;
		}
		for(String manholePrimaryKey : manholePrimaryKeys){
			List<String> ductIds = jdbcTemplate.queryForList(Query.DUCT_MASTER_FETCH_PK, new Object[]{manholePrimaryKey}, String.class);
			for(String ductId : ductIds){
				List<String> cableIds = jdbcTemplate.queryForList(Query.CABLE_MASTER_FETCH_PK, new Object[]{ductId}, String.class);
				for(String cableId : cableIds){
					jdbcTemplate.update(Query.CABLE_MASTER_DELETE,new Object[]{cableId});
				}
				jdbcTemplate.update(Query.DUCT_MASTER_DELETE_PK,new Object[]{ductId});
			}
			jdbcTemplate.update(Query.MANHOLE_MASTER_DELETE_PK,new Object[]{manholePrimaryKey});
		}
	}
	
	private String fetchCableInfo(Joint joint,Connection connection,Map<String,Map<String,Integer>> ductCableMap,int endNumber,Set<String> cableMap,String manholeNumber)throws Exception{
		String cableId = null;
		String tubeId = null;
		String color = null;
		if(endNumber == 1){
			cableId = connection.getEnd1().getCableId();
			tubeId = connection.getEnd1().getTubeId();
			color = connection.getEnd1().getColor();
		}else if(endNumber ==2){
			cableId = connection.getEnd2().getCableId();
			tubeId = connection.getEnd2().getTubeId();
			color = connection.getEnd2().getColor();
		}
		String direction = null;
		int type = 0;
		int cableOrder = 0;
		for(Cable cable : joint.getCableInfo()){
			if(cable.getCableId().equalsIgnoreCase(cableId)){
				direction = cable.getCableDirection();
				type = cable.getCableType();
				cableOrder = cable.getCableOrder();
				break;
			}
		}
		Integer loopDistance = null;
		String ductPrimaryKey = null;
		for(Map.Entry<String, Map<String,Integer>> entry : ductCableMap.entrySet()){
			loopDistance = entry.getValue().get(cableId);
			if(loopDistance != null){
				ductPrimaryKey = entry.getKey();
				break;
			}
		}
		String cablePrimaryKey = null;
		if(loopDistance != null){
			cablePrimaryKey = ductPrimaryKey + "_" + cableId + "_" + tubeId + "_" + color;
			jdbcTemplate.update(Query.CABLE_TABLE_INSERT_QUERY, new Object[]{cablePrimaryKey,ductPrimaryKey,cableId,direction,type,tubeId,color,cableOrder});
			if(!cableMap.contains(cableId)){
				jdbcTemplate.update(Query.LOOP_TABLE_INSERT_QUERY, new Object[]{cableId,loopDistance,manholeNumber});
				cableMap.add(cableId);
			}
			
		}
		return cablePrimaryKey;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Manhole getManholeInfo(String manholeNumber) throws Exception {
		Manhole manhole = new Manhole();
		manhole.setManholeId(manholeNumber);
		List<Joint> joints = jdbcTemplate.query(Query.JOINT_TABLE_SELECT_QUERY, new BeanPropertyRowMapper(Joint.class),new Object[]{manholeNumber});
		for(Joint joint : joints){
			List<ConnectionDb> connectionsInJoint = jdbcTemplate.query(Query.CONNECTION_TABLE_SELECT_QUERY, new Object[]{joint.getJointId()}, new BeanPropertyRowMapper(ConnectionDb.class));
			List<Connection> connections = new ArrayList<>();
			List<Cable> cables = new ArrayList<>();
			Set<String> cableIds = new HashSet<>();
			for(ConnectionDb connectionDb : connectionsInJoint){
				CableDb cableDbEnd1 = (CableDb)jdbcTemplate.queryForObject(Query.CABLE_TABLE_SELECT_QUERY, new Object[]{connectionDb.getEnd1()}, new BeanPropertyRowMapper(CableDb.class));
				CableDb cableDbEnd2 = (CableDb)jdbcTemplate.queryForObject(Query.CABLE_TABLE_SELECT_QUERY, new Object[]{connectionDb.getEnd2()}, new BeanPropertyRowMapper(CableDb.class));
				Connection conn = new Connection();
//				End end1 = new End();
//				end1.setCableId(cableDbEnd1.getCableId());
//				end1.setTubeId(cableDbEnd1.getTubeId());
//				end1.setColor(cableDbEnd1.getColor());
//				End end2 = new End();
//				end2.setCableId(cableDbEnd2.getCableId());
//				end2.setTubeId(cableDbEnd2.getTubeId());
//				end2.setColor(cableDbEnd2.getColor());
				
//				End end1 = getConnectionEnd(cableDbEnd1);
//				End end2 = getConnectionEnd(cableDbEnd2);
				
				conn.setEnd1(setConnectionEnd(cableDbEnd1));
				conn.setEnd2(setConnectionEnd(cableDbEnd2));
				connections.add(conn);
				
				if(!cableIds.contains(cableDbEnd1.getCableId())){
//					Cable cable1 = new Cable();
//					cable1.setCableId(cableDbEnd1.getCableId());
//					cable1.setCableDirection(cableDbEnd1.getCableDirection());
//					cable1.setCableType(cableDbEnd1.getCableType());
//					cable1.setCableOrder(cableDbEnd1.getCableOrder());
					cables.add(setCableProperties(cableDbEnd1));
					cableIds.add(cableDbEnd1.getCableId());
				}
				
				if(!cableIds.contains(cableDbEnd2.getCableId())){
//					Cable cable2 = new Cable();
//					cable2.setCableId(cableDbEnd2.getCableId());
//					cable2.setCableDirection(cableDbEnd2.getCableDirection());
//					cable2.setCableType(cableDbEnd2.getCableType());
//					cable2.setCableOrder(cableDbEnd2.getCableOrder());
					cables.add(setCableProperties(cableDbEnd2));
					cableIds.add(cableDbEnd2.getCableId());
				}
			}
			joint.setCableInfo(cables);
			joint.setConnections(connections);
		}
		List<ManholeDuctInfo> ductInfos = jdbcTemplate.query(Query.MANHOLE_TABLE_SELECT_QUERY, new Object[]{manholeNumber}, new BeanPropertyRowMapper(ManholeDuctInfo.class));
		for(ManholeDuctInfo ductInfo : ductInfos){
			List<Duct> ducts = new ArrayList<>();
			for(int i = 1;i <= ductInfo.getNoOfDucts();i++){
				ducts = jdbcTemplate.query(Query.DUCT_TABLE_SELECT_QUERY, new Object[]{ductInfo.getId()},new BeanPropertyRowMapper(Duct.class));
				for(Duct duct : ducts){
					Map<String,Integer> cableLoopMap = new HashMap<>();
					List<String> cableIdsInLoop = jdbcTemplate.queryForList(Query.CABLE_TABLE_CABLE_LOOP_MAP_SELECT_QUERY, new Object[]{duct.getId()}, String.class);
					for(String cableId : cableIdsInLoop){
						Integer loopDistance = jdbcTemplate.queryForObject(Query.LOOP_MASTER_SELECT_QUERY, new Object[]{cableId,manholeNumber}, Integer.class);
						cableLoopMap.put(cableId, loopDistance);
					}
					duct.setCableLoopMap(cableLoopMap);
				}
				
			}
			ductInfo.setDucts(ducts);
		}
		manhole.setManholeDuctInfo(ductInfos);
		manhole.setJoints(joints);
		manhole.setNoOfJoints(joints.size());
		return manhole;
	}
	
	private End setConnectionEnd(CableDb cable){
		End end = new End();
		end.setCableId(cable.getCableId());
		end.setTubeId(cable.getTubeId());
		end.setColor(cable.getColor());
		return end;
	}
	
	private Cable setCableProperties(CableDb cableDb){
		Cable cable = new Cable();
		cable.setCableId(cableDb.getCableId());
		cable.setCableDirection(cableDb.getCableDirection());
		cable.setCableType(cableDb.getCableType());
		cable.setCableOrder(cableDb.getCableOrder());
		return cable;
	}
}
