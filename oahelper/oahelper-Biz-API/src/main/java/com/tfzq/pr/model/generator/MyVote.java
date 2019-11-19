package com.tfzq.pr.model.generator;

public class MyVote {
	private String voteGroupName;
	private String voteName;
	private Integer sortNo;
	private String userName;
	private String voteItemContent;

	private String voteGroupId;
	private String voteId;
	private String voteItemId;
	private String userId;

	public String getVoteItemId() {
		return voteItemId;
	}

	public void setVoteItemId(String voteItemId) {
		this.voteItemId = voteItemId;
	}

	public String getVoteGroupId() {
		return voteGroupId;
	}

	public void setVoteGroupId(String voteGroupId) {
		this.voteGroupId = voteGroupId;
	}

	public String getVoteId() {
		return voteId;
	}

	public void setVoteId(String voteId) {
		this.voteId = voteId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVoteGroupName() {
		return voteGroupName;
	}

	public void setVoteGroupName(String voteGroupName) {
		this.voteGroupName = voteGroupName;
	}

	public String getVoteName() {
		return voteName;
	}

	public void setVoteName(String voteName) {
		this.voteName = voteName;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVoteItemContent() {
		return voteItemContent;
	}

	public void setVoteItemContent(String voteItemContent) {
		this.voteItemContent = voteItemContent;
	}
}
