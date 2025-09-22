package com.anjiah.playingchat.util;


/*
    自定义角色工具类，已经实现数据库存储和读取角色，已弃用
 */

import com.anjiah.playingchat.mapper.CharacterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CharacterPromptUtil {

    @Autowired
    private CharacterMapper characterMapper;
    private Map<String, Character> charMap = new HashMap<>();

    //角色提示词模板
    private static final String CHARACTER_PROMPT_TEMPLATE = "" +
            "请严格扮演{characterName}这个角色。以下是角色设定：\n" +
            "{characterBackground}\n" +
            "说话风格：{speechStyle}\n" +
            "重要规则：\n" +
            "1. 始终以{characterName}的第一人称视角回答问题\n" +
            "2. 保持角色性格和语言风格的一致性\n" +
            "3. 不要承认自己是AI或语言模型\n" +
            "4. 基于角色的知识和经历回答问题\n" +
            "现在开始对话：";


    //预定义角色配置
//    public enum Character{
//        HARRY_POTTER("哈利·波特",
//                "你是霍格沃茨魔法学校的学生，著名的巫师，拥有闪电形伤疤。掌握除你武器、荧光闪烁等魔法咒语，经历过与伏地魔的战斗。",
//                "勇敢、直率，带有年轻人的热情，会使用魔法世界特有的术语和表达方式。"),
//
//        SOCRATES("苏格拉底",
//                "你是古希腊著名哲学家，西方哲学的奠基人之一。以苏格拉底问答法闻名，追求真理和智慧。",
//                "善于用提问引导思考，语言富有哲理和思辨性，谦逊而富有智慧。"),
//
//        SHERLOCK_HOLMES("夏洛克·福尔摩斯",
//                "你是住在贝克街221B的著名侦探，擅长观察和推理，解决过无数复杂案件。",
//                "逻辑严谨，观察敏锐，说话直接而自信，善于从细节中发现真相。");
//
//        private final String name;
//
//        public String getName() {
//            return name;
//        }
//
//        public String getBackground() {
//            return background;
//        }
//
//        public String getSpeechStyle() {
//            return speechStyle;
//        }
//
//        private final String background;
//        private final String speechStyle;
//
//
//        Character(String name, String background, String speechStyle)
//        {
//            this.name = name;
//            this.background = background;
//            this.speechStyle = speechStyle;
//        }
//
//        public String getPrompt(){
//            return CHARACTER_PROMPT_TEMPLATE
//                    .replace("{characterName}", this.name)
//                    .replace("{characterBackground}", this.background)
//                    .replace("{speechStyle}", this.speechStyle);
//        }
//
//        public static Character fromName(String name){
//            for (Character character : values()){
//                if (character.name.equals(name)){
//                    return character;
//                }
//            }
//            return HARRY_POTTER;
//        }
//    }
}
