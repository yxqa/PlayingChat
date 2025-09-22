package com.anjiah.playingchat.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Characters {
    private String id; // 如: "harry-potter"
    private String name; // 如: "哈利·波特"
    private String description;
    private String background;
    private String speechStyle;
    private String initialMessage;
    private String avatarUrl;
    private String tags; // 逗号分隔的标签
    private Integer popularity = 0;
    private Boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
