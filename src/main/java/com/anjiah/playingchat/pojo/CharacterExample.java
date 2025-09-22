package com.anjiah.playingchat.pojo;

import lombok.Data;

@Data
public class CharacterExample {
    private Long id;
    private String characterId;
    private String userInput;
    private String characterResponse;
    private Integer order = 0;
}
