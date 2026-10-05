package com.nexturn.lms.dto;

public class RecommendRequest {
    private Integer officerId;
    private boolean recommend;
    private String comments;

    public Integer getOfficerId() { return officerId; }
    public void setOfficerId(Integer officerId) { this.officerId = officerId; }
    public boolean isRecommend() { return recommend; }
    public void setRecommend(boolean recommend) { this.recommend = recommend; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}