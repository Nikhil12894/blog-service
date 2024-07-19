#### Blog Service
- **Endpoints**:
  - POST `/blogs/{userID}`: Create a new blog post
  - GET `/blogs`: Retrieve all blog previews
  - GET `/blogs/{userID}`: Retrieve all blog previews
  - GET `/blogs/{id}`: Retrieve a single blog post
  - PUT `/blogs/{id}/user/{userID}`: Update a blog post
  - DELETE `/blogs/{id}/user/{userID}`: Delete a blog post
- **Responsibilities**:
  - Managing blog content


**BlogService.java**
```java
public interface BlogService {
    Blog createBlog(Blog blog,Long userID);
    List<Blog> getAllBlogs();
    List<Blog> getAllBlogs(Long userID);
    Blog getBlogById(Long id);
    Blog updateBlog(Long id, Blog blog,Long userID);
    void deleteBlog(Long id,Long userID);
}
```



#### TODO List

- [x] Initialize Blog Service App
- [ ] Add Initially Required Models And DTOs
- [ ] Create Service layer for handling Blog
- [ ] Expose the blog management through a RestApi