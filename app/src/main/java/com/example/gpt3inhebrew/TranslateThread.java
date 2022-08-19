package com.example.gpt3inhebrew;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

public class TranslateThread extends Thread
{
    String text;
    String translateToLang;
    Handler handler;

    public TranslateThread(String text, String translateToLang, Handler handler)
    {
        this.text = text;
        this.translateToLang = translateToLang;
        this.handler = handler;
    }

    @Override
    public void run()
    {
        super.run();

        Message message = new Message();

        Translate translate = new Translate(translateToLang);
        try {
            message.obj = translate.Post(text);
            message.what = 200;
            handler.sendMessage(message);
        } catch (IOException e)
        {
            message.obj = e.getMessage();
            message.what = 400;
            handler.sendMessage(message);
        }
    }
}
