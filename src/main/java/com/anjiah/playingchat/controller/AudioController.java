package com.anjiah.playingchat.controller;


import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@RestController
public class AudioController {

    @Autowired
    private DashScopeSpeechSynthesisModel synthesisModel;

    private static final String TEXT = "我叫毋晨琪，是西北师范大学研一在读学生";
    private static final String PATH = "src/main/resources/tts/";

    @GetMapping("/tts")
    public void tts(){
        //创建SpeechSynthesisOptions对象
        DashScopeSpeechSynthesisOptions options = DashScopeSpeechSynthesisOptions.builder()
                .build();

        SpeechSynthesisResponse response = synthesisModel.call(
                new SpeechSynthesisPrompt(TEXT, options)
        );

        File file = new File(PATH+"/output.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)){
            ByteBuffer byteBuffer = response.getResult().getOutput().getAudio();
            fos.write(byteBuffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
