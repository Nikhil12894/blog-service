package com.nk.blog.constants;

public abstract class PostConstents {

	private PostConstents() {}
	
	public static final Long SYSTEM_USER = 0L;


	public static final String DEFAULT_BLOG_STATUS = "DRAFT";
	public static final String ALLOWED_BLOG_STATUS = "DRAFT,PUBLISHED,DELETED";


	public static final Integer DEFAULT_PAGE = 1;

    public static final Integer DEFAULT_PAGE_SIZE = 10;

    public static final String DEFAULT_SORT_CREATED = "CREATED_AT";

    public static final String DEFAULT_SORT_ORDER = "ASC";

    public static final String PAGE = "page";

    public static final String PAGE_SIZE = "page_size";

    public static final String SORT_ORDER = "sort_order";

    public static final String SORT_BY = "sort_by";

    public static final String NO_SORT = "NONE";
}
