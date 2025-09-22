package com.anjiah.playingchat.service.Impl;

import com.anjiah.playingchat.mapper.CharacterMapper;
import com.anjiah.playingchat.pojo.Characters;
import com.anjiah.playingchat.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class CharacterImpl implements CharacterService {

    @Autowired
    private CharacterMapper characterMapper;

    @Override
    public Characters findById(String name) {
        return characterMapper.findById(name);
    }

    @Override
    public List<Characters> findAllActive() {
        return characterMapper.findAllActive();
    }

    @Override
    public void addCharacter(Characters character) {
        character.setUpdatedAt(LocalDateTime.now());
        character.setUpdatedAt(LocalDateTime.now());
        if(character.getPopularity()==null){
            character.setPopularity(0);
        }
        if(character.getActive()==null){
            character.setActive(true);
        }
        characterMapper.addCharacter(character);
    }
}
