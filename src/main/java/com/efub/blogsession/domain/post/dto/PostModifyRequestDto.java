package com.efub.blogsession.domain.post.dto;

import lombok.Getter;

@Getter
public class PostModifyRequestDto {
    private Long accountId;
    private String title;
    private String content;
}