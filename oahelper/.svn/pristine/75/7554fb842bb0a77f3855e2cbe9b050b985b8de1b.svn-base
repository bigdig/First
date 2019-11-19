package com.tfzq.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfzq.pr.dao.generator.MyMapper;
import com.tfzq.pr.model.generator.MyInform;
import com.tfzq.pr.model.generator.MyVote;

/**
 * @author yuzhitao
 */
@Service
public class MyService implements MyMapper {
	@Autowired
	MyMapper myMapper;

	public List<MyVote> selectByVoteGroupId(String id) {
		return myMapper.selectByVoteGroupId(id);
	}

	public List<MyVote> selectUserVote(String id, String userId) {
		return myMapper.selectUserVote(id, userId);
	}

	public List<MyVote> selectUserAllVote(String isActivity, String userId) {
		return myMapper.selectUserAllVote(isActivity, userId);
	}

	public void deleteUserVote(String id, String userId) {
		myMapper.deleteUserVote(id, userId);
	}

	public List<MyInform> selectUserInform(String userId) {
		return myMapper.selectUserInform(userId);
	}

	@Override
	public List<MyInform> selectInformByCondition(Map<String, Object> params) {
		return myMapper.selectInformByCondition(params);
	}

}
