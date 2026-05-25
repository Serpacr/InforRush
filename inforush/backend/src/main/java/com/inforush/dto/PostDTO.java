package com.inforush.dto;

import com.inforush.model.Post;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostDTO {

    public static class Request {
        @NotBlank(message = "Título é obrigatório")
        private String title;
        @NotBlank(message = "Conteúdo é obrigatório")
        private String content;
        private String excerpt;
        private String coverImage;
        private boolean published = false;
        private List<String> tagIds;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getExcerpt() { return excerpt; }
        public void setExcerpt(String excerpt) { this.excerpt = excerpt; }
        public String getCoverImage() { return coverImage; }
        public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
        public boolean isPublished() { return published; }
        public void setPublished(boolean published) { this.published = published; }
        public List<String> getTagIds() { return tagIds; }
        public void setTagIds(List<String> tagIds) { this.tagIds = tagIds; }
    }

    public static class UpdateRequest {
        private String title;
        private String content;
        private String excerpt;
        private String coverImage;
        private Boolean published;
        private List<String> tagIds;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getExcerpt() { return excerpt; }
        public void setExcerpt(String excerpt) { this.excerpt = excerpt; }
        public String getCoverImage() { return coverImage; }
        public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
        public Boolean getPublished() { return published; }
        public void setPublished(Boolean published) { this.published = published; }
        public List<String> getTagIds() { return tagIds; }
        public void setTagIds(List<String> tagIds) { this.tagIds = tagIds; }
    }

    public static class Response {
        private String id, title, slug, content, excerpt, coverImage;
        private boolean published;
        private LocalDateTime createdAt, updatedAt;
        private AuthDTO.UserDTO author;
        private Set<TagDTO> tags;

        public static Response from(Post p) {
            Response r = new Response();
            r.id = p.getId(); r.title = p.getTitle(); r.slug = p.getSlug();
            r.content = p.getContent(); r.excerpt = p.getExcerpt();
            r.coverImage = p.getCoverImage(); r.published = p.isPublished();
            r.createdAt = p.getCreatedAt(); r.updatedAt = p.getUpdatedAt();
            r.author = AuthDTO.UserDTO.from(p.getAuthor());
            r.tags = p.getTags().stream().map(TagDTO::from).collect(Collectors.toSet());
            return r;
        }
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getSlug() { return slug; }
        public String getContent() { return content; }
        public String getExcerpt() { return excerpt; }
        public String getCoverImage() { return coverImage; }
        public boolean isPublished() { return published; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public AuthDTO.UserDTO getAuthor() { return author; }
        public Set<TagDTO> getTags() { return tags; }
    }

    public static class TagDTO {
        private String id, name, slug;

        public static TagDTO from(com.inforush.model.Tag t) {
            TagDTO dto = new TagDTO();
            dto.id = t.getId(); dto.name = t.getName(); dto.slug = t.getSlug();
            return dto;
        }
        public String getId() { return id; }
        public String getName() { return name; }
        public String getSlug() { return slug; }
    }
}
