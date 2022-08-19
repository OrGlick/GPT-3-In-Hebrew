package com.example.gpt3inhebrew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.theokanning.openai.completion.CompletionChoice;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    EditText editText;
    TextView textViewEn, textViewHe;
    Button button, btnCopy;
    String translatedText;
    String englishPrompt;


    Button btn_close_setting;
    EditText et_temperature, et_top_p, et_frequency_penalty, et_presence_penalty, et_maximum_length;

    Dialog settingDialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFindView();
        createSettingDialog();

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(editText.getText().toString().length() != 0)
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    String textToTranslate = editText.getText().toString();
                    translate(textToTranslate, "en");
                }
            }
        });

        btnCopy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", translatedText);
                manager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity.this, "הועתק", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFindView()
    {
        textViewEn = findViewById(R.id.text_view_english);
        textViewHe = findViewById(R.id.text_view_hebrew);
        editText = findViewById(R.id.edit_text);
        button = findViewById(R.id.button);
        btnCopy = findViewById(R.id.button_copy);
        btnCopy.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void createSettingDialog()
    {
        settingDialog = new Dialog(this);
        settingDialog.setContentView(R.layout.setting_layout);
        settingDialog.setTitle("אפשרויות");
        settingDialog.setCancelable(false);

        btn_close_setting = settingDialog.findViewById(R.id.btn_close_setting);
        et_temperature = settingDialog.findViewById(R.id.edit_text_temperature);
        et_maximum_length = settingDialog.findViewById(R.id.edit_text_max_length);
        et_top_p = settingDialog.findViewById(R.id.edit_text_top_p);
        et_frequency_penalty = settingDialog.findViewById(R.id.edit_text_frequency_penalty);
        et_presence_penalty = settingDialog.findViewById(R.id.edit_text_presence_penalty);

        btn_close_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Double.parseDouble(et_temperature.getText().toString()) >= 0 && Double.parseDouble(et_temperature.getText().toString()) <= 1
                && Double.parseDouble(et_top_p.getText().toString()) >= 0 && Double.parseDouble(et_top_p.getText().toString()) <= 1
            && Integer.parseInt(et_maximum_length.getText().toString()) >=1 && Integer.parseInt(et_maximum_length.getText().toString()) <= 4000
                && Double.parseDouble(et_presence_penalty.getText().toString()) >= 0
                        && Double.parseDouble(et_presence_penalty.getText().toString()) <=2
                && Double.parseDouble(et_frequency_penalty.getText().toString()) >= 0
                        && Double.parseDouble(et_frequency_penalty.getText().toString()) <=2)
                {
                    settingDialog.dismiss();
                }
            }
        });
    }

    private void translate(String textToTranslate, String translateToLang)
    {
        Handler handler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(@NonNull Message message)
            {
                if(message.what == 200) {
                    translatedText = ((String) message.obj);

                    if (translateToLang.equals("en"))
                    {
                        GPT3(translatedText);
                    }
                    else
                    {
                        progressDialog.dismiss();
                        updateTextOnUI(translatedText);
                    }
                }
                else
                {
                    progressDialog.dismiss();
                    Helper.showError((String)message.obj, MainActivity.this);
                }
                return false;
            }
        });
        TranslateThread thread = new TranslateThread(textToTranslate, translateToLang, handler);
        if(translateToLang.equals("en"))
        {
            progressDialog.setMessage("מתרגם לאנגלית...");
            progressDialog.show();
        }
        else
            progressDialog.setMessage("מתרגם בחזרה לעברית...");
        thread.start();
    }

    private void GPT3(String prompt)
    {
        Handler handler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(@NonNull Message message)
            {
                if(message.what == 200)
                {
                    List<CompletionChoice> choiceList = (List<CompletionChoice>) message.obj;
                    String result = choiceList.get(0).getText();

                    englishPrompt = prompt;

                    result = result.substring(prompt.length());
                    backToHebrew(result);
                }
                else if(message.what == 400)
                {
                    progressDialog.dismiss();
                    Helper.showError((String)message.obj, MainActivity.this);
                }
                return false;
            }
        });

        GptThread gptThread = new GptThread(prompt, et_temperature.getText().toString(), et_top_p.getText().toString(),
                et_frequency_penalty.getText().toString(), et_presence_penalty.getText().toString(), et_maximum_length.getText().toString()
                ,handler);
        progressDialog.setMessage("... GPT 3 ממתין לעיבוד הטקסט על ידי  ");
        gptThread.start();
    }

    private void backToHebrew(String text)
    {
        translate(text, "he");
    }

    private void updateTextOnUI(String translatedText)
    {
        textViewEn.setText(englishPrompt);
        textViewHe.setText(translatedText);
        btnCopy.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.setting_item)
            settingDialog.show();
        return super.onOptionsItemSelected(item);
    }
}
