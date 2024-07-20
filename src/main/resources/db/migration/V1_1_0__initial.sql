/* Blog Service DB */
/* create the blog_post table */
create table `blog_post` (
   `id` bigint not null auto_increment,
   `title` varchar(255) not null,
   `description` varchar(500) not null,
   `content` clob not null,
   `image_url` varchar(255),
   `status` VARCHAR(10) not null,
   `created_by` bigint,
   `created_at` DATETIME,
   `last_updated_by` bigint,
   `last_updated_at` DATETIME,
   primary key (`id`),
   unique (`title`)
);



