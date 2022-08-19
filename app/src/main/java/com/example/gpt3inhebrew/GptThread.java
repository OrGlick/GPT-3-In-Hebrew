package com.example.gpt3inhebrew;

import android.os.Handler;
import android.os.Message;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

public class GptThread extends Thread {
    String prompt,temperature, top_p, frequency_penalty, presence_penalty, maximum_length;
    Handler handler;

    public GptThread(String prompt, String temperature, String top_p, String frequency_penalty, String presence_penalty, String maximum_length, Handler handler)
    {
        this.prompt = prompt;
        this.temperature = temperature;
        this.top_p = top_p;
        this.frequency_penalty = frequency_penalty;
        this.presence_penalty = presence_penalty;
        this.maximum_length = maximum_length;
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();



        Message message = new Message();
        String token = Helper.open_ai_api_key;

        OpenAiService service = new OpenAiService(token);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .temperature(Double.valueOf(temperature))
                .topP(Double.valueOf(top_p))
                .frequencyPenalty(Double.valueOf(frequency_penalty))
                .presencePenalty(Double.valueOf(presence_penalty))
                .maxTokens(Integer.valueOf(maximum_length))
                .echo(true)
                .build();
        try
        {
            message.obj = service.createCompletion("text-davinci-002", completionRequest).getChoices();
            message.what = 200;
            handler.sendMessage(message);
        }
        catch (Exception e)
        {
            message.obj = e.getMessage();
            message.what = 400;
            handler.sendMessage(message);
        }

    }
}