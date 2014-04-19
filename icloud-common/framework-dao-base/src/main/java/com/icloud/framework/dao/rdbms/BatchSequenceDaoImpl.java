package com.icloud.framework.dao.rdbms;

import java.util.HashMap;

import com.ibatis.sqlmap.client.SqlMapClient;

public class BatchSequenceDaoImpl {
   
	private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    public SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }
    
    public long getNextSeq(String key, int allotment) throws Exception{
    	HashMap<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("sequenceName", key);
    	parameters.put("allotment", allotment);
    	Long result = (Long)sqlMapClient.queryForObject("batch_sequence.getNextSequence", parameters);
    	return result;
    }
}
