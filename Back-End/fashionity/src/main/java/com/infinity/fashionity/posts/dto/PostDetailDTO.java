package com.infinity.fashionity.posts.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDetailDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotBlank
        @JsonIgnore
        @JsonAlias(value = "post_seq")
        private long postSeq;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        @Builder.Default
        private List<Post> post = new ArrayList<>();
    }

    public static class Post{
        @JsonAlias(value = "post_seq")
        private long postSeq;
        private String[] images;
        private String content;
        private String name;
        @JsonAlias(value = "profile_img")
        private String profileImg;
        private boolean liked;
        @JsonAlias(value = "like_count")
        private int likeCount;
        @JsonAlias(value = "comment_count")
        private int commentCount;
        @JsonAlias(value = "created_at")
        @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonAlias(value = "updated_at")
        @JsonFormat(pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;
        private boolean following;
    }
}
