package com.nk.blog.enums;

public enum BlogPostShortBy {
    
    CREATED_AT("createdAt"),
    CREATED_BY("createdBy");

    private String orderBy;

	BlogPostShortBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderBy() {
		return this.orderBy;
	}
}
