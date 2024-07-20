#### Blog Service
- **Endpoints**:
  - POST `/blogs`: Create a new blog post
  - GET `/blogs`: Retrieve all blog previews
  - GET `/blogs/user`: Retrieve all blog previews
  - GET `/blogs/{id}`: Retrieve a single blog post
  - PUT `/blogs/{id}`: Update a blog post
  - DELETE `/blogs/{id}`: Delete a blog post
- **Responsibilities**:
  - Managing blog content


**BlogService.java**
```java
public interface BlogPostService {
    BlogPostDTO createBlog(BlogPostRequest blogRequest);
    BlogPostListDTO getAllBlogsCurrentUser(Integer page, Integer pageSize, SortOrder sort, BlogPostShortBy sortBy);
    BlogPostListDTO getAllBlogs(Integer page, Integer pageSize, SortOrder sort, BlogPostShortBy sortBy);
    BlogPostDTO getBlogById(Long id);
    BlogPostDTO updateBlog(Long id, BlogPostRequest blog);
    void deleteBlog(Long id);
}
```



#### TODO List

- [x] ~~Initialize Blog Service App.~~
- [x] ~~Add Initially Required Models And DTOs.~~
- [x] ~~Create Service layer for handling Blog.~~
- [x] ~~Expose the blog management through a RestAPI.~~
- [x] ~~Integrate Swagger for api documentation.~~
- [x] ~~Integrate flyway for db migration.~~
- [ ] Write Unittest with expectation of **100% code coverage** for services and apis attest.
- [ ] Integrate vault for secrete management.
- [ ] Write Github Action pipeline for automated Docker Image Build and generating kubernetes deployment yml file.
- [ ] Configure the app into ArgoCD for kubernetes deployment.
- [ ] Create A user management service using [Kycloak](https://www.keycloak.org/).
- [ ] Create a authenticate lib so that other service can validate and authenticate user.
- [ ] Integrate the api with frontend app.
