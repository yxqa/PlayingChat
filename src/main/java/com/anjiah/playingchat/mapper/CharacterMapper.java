package com.anjiah.playingchat.mapper;

import com.anjiah.playingchat.pojo.Characters;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface CharacterMapper {

    //查询指定角色
    @Select("select * from characters where name = #{id} and is_active = true")
    Characters findById(String name);

    //查询所有角色
    @Select("SELECT * FROM characters WHERE is_active = true ORDER BY popularity DESC")
    List<Characters> findAllActive();

    @Insert("INSERT INTO characters(id, name, description, background, speech_style, " +
            "initial_message, avatar_url, tags, popularity, is_active, created_at, updated_at) " +
            "VALUES(#{id}, #{name}, #{description}, #{background}, #{speechStyle}, " +
            "#{initialMessage}, #{avatarUrl}, #{tags}, #{popularity}, #{active}, #{createdAt}, #{updatedAt})")
    void addCharacter(Characters character);
}
