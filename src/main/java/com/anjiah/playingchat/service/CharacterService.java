package com.anjiah.playingchat.service;

import com.anjiah.playingchat.pojo.Characters;

import java.util.List;

public interface CharacterService {
    Characters findById(String name);

    List<Characters> findAllActive();

    void addCharacter(Characters character);
}
