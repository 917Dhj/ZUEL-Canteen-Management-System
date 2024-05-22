package com.example.miniprogrammanagement.Bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ImageResponse {

    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("status")
    private Integer status;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("url_viewer")
        private String urlViewer;
        @JsonProperty("url")
        private String url;
        @JsonProperty("display_url")
        private String displayUrl;
        @JsonProperty("width")
        private String width;
        @JsonProperty("height")
        private String height;
        @JsonProperty("size")
        private String size;
        @JsonProperty("time")
        private String time;
        @JsonProperty("expiration")
        private String expiration;
        @JsonProperty("image")
        private ImageDTO image;
        @JsonProperty("thumb")
        private ThumbDTO thumb;
        @JsonProperty("medium")
        private MediumDTO medium;
        @JsonProperty("delete_url")
        private String deleteUrl;

        @NoArgsConstructor
        @Data
        public static class ImageDTO {
            @JsonProperty("filename")
            private String filename;
            @JsonProperty("name")
            private String name;
            @JsonProperty("mime")
            private String mime;
            @JsonProperty("extension")
            private String extension;
            @JsonProperty("url")
            private String url;
        }

        @NoArgsConstructor
        @Data
        public static class ThumbDTO {
            @JsonProperty("filename")
            private String filename;
            @JsonProperty("name")
            private String name;
            @JsonProperty("mime")
            private String mime;
            @JsonProperty("extension")
            private String extension;
            @JsonProperty("url")
            private String url;
        }

        @NoArgsConstructor
        @Data
        public static class MediumDTO {
            @JsonProperty("filename")
            private String filename;
            @JsonProperty("name")
            private String name;
            @JsonProperty("mime")
            private String mime;
            @JsonProperty("extension")
            private String extension;
            @JsonProperty("url")
            private String url;
        }
    }
}
