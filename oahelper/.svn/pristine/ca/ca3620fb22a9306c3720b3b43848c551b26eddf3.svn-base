package com.tfzq.pr.dao.generator;

import java.util.List;
import java.util.Map;

import com.tfzq.pr.model.generator.MyInform;
import com.tfzq.pr.model.generator.MyVote;

/**
 * @author yuzhitao
 */
public interface MyMapper {
	public List<MyVote> selectByVoteGroupId(String id);

	public List<MyVote> selectUserVote(String id, String userId);

	public List<MyVote> selectUserAllVote(String isActivity, String userId);

	public void deleteUserVote(String id, String userId);

	public List<MyInform> selectUserInform(String userId);

	public List<MyInform> selectInformByCondition(Map<String, Object> params);

}