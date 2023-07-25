package com.infinity.fashionity.posts.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostSaveDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotBlank
        @Builder.Default
        private ArrayList<MultipartFile> images = new ArrayList<>();
        @NotBlank
        private String content;
        @Builder.Default
        private ArrayList<String> hashtag = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private boolean success;
    }
}
