package com.anjiah.playingchat.controller;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import com.anjiah.playingchat.pojo.Characters;
import com.anjiah.playingchat.service.CharacterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class ChatController {

    @Autowired
    private CharacterService characterService;


    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

//    private final ChatClient dashScopeChatClient;
    private final ChatClient chatClient;
    private final MessageWindowChatMemory messageWindowChatMemory;

    private static final String CHARACTER_PROMPT_TEMPLATE =
            "请严格扮演{characterName}这个角色。以下是角色设定：\n" +
            "{characterBackground}\n" +
            "说话风格：{speechStyle}\n" +
            "重要规则：\n" +
            "1. 始终以{characterName}的第一人称视角回答问题\n" +
            "2. 保持角色性格和语言风格的一致性\n" +
            "3. 不要承认自己是AI或语言模型\n" +
            "4. 基于角色的知识和经历回答问题\n" +
            "现在开始对话：";


    public ChatController(ChatClient.Builder chatClientBuilder,
                          MysqlChatMemoryRepository mysqlChatMemoryRepository) {



        //创建记忆窗口
        this.messageWindowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(mysqlChatMemoryRepository)
                .maxMessages(100)
                .build();

        //构建带记忆功能的ChatClient
        this.chatClient = chatClientBuilder
                .defaultSystem(DEFAULT_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(messageWindowChatMemory).build(),
                        new SimpleLoggerAdvisor()
                )
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(
                        DashScopeChatOptions.builder().withTopP(0.7).build()
                ).build();


//        this.dashScopeChatClient = chatClientBuilder
//                .defaultSystem(DEFAULT_PROMPT)
//                // 实现 Logger 的 Advisor
//                .defaultAdvisors(
//                        new SimpleLoggerAdvisor()
//                )
//                // 设置 ChatClient 中 ChatModel 的 Options 参数
//                .defaultOptions(
//                        DashScopeChatOptions.builder()
//                                .withTopP(0.7)
//                                .build()
//                )
//                .build();
    }

    /**
     * ChatClient 简单调用
     */
    @GetMapping("/simple/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？") String query,
                             @RequestParam(defaultValue = "default_user") String conversation_id) {

        return chatClient.prompt(query)
                .advisors(a->a.param(CONVERSATION_ID,conversation_id)).call().content();
    }


    /**
     * 角色扮演
     * @param name
     * @param message
     * @return
     */
    @PostMapping("/character")
    public String characterChat(@RequestParam(value = "name", defaultValue = "哈利·波特") String name,
                                @RequestParam String message,
                                @RequestParam(defaultValue = "default_user") String userId) {

        String conversation_id = name + "_" + userId;


        //获取角色配置
        Characters character = characterService.findById(name);
        if(character == null){
            throw new IllegalArgumentException("角色不存在:" + name);
        }

        //构建角色提示词
        String characterPrompt = CHARACTER_PROMPT_TEMPLATE
                .replace("{characterName}", character.getName())
                .replace("{characterBackground}", character.getBackground())
                .replace("{speechStyle}", character.getSpeechStyle());

        //构建完整对话提示
        String fullPrompt = characterPrompt + "\n用户提问" + message;

        //使用带记忆的ChatClient

        //调用AI使用角色特定的系统提示
        return chatClient.prompt()
                .system(characterPrompt)
                .user(message)
                .advisors(a->a.param(CONVERSATION_ID,conversation_id))
                .call()
                .content();

    }


    /**
     * 获取所有角色
     * @return
     */
    @GetMapping("/characters")
    public List<Map<String, Object>> getAvailableCharacters(){

        // 需要先在CharacterMapper中添加搜索方法
        List<Characters> charactersList= characterService.findAllActive();
        return charactersList.stream()
                .map(characters -> {
                    Map<String, Object> charInfo = new HashMap<>();
                    charInfo.put("id", characters.getId());
                    charInfo.put("name", characters.getName());
                    charInfo.put("description", characters.getDescription());
                    charInfo.put("initialMessage", characters.getInitialMessage());
                    charInfo.put("tags", characters.getTags() != null ?
                            List.of(characters.getTags().split(",")):List.of());
                    charInfo.put("popularity", characters.getPopularity());
                    return charInfo;

                }).collect(Collectors.toList());
    }


    /**
     * 添加自定义角色
     * @param character
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<String> addCharacter(@RequestBody Characters character){

        if(characterService.findById(character.getName()) != null){
            return ResponseEntity.badRequest().body(character.getName() + "---已存在");
        }

        try {
            characterService.addCharacter(character);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("创建失败: " + e.getMessage());
        }


    }

}
