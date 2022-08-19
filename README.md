# GPT_3_In_Hebrew

Chat and use the GPT 3 model from Open AI, in hebrew!, through an android application
<br/><br/>
How It Works:

1. You write a prompt in hebrew

2. The prompt is sent for translation to english in Azure

3. The english prompt is sent to Open AI

4. The answer from Open AI is sent for translation to hebrew in Azure 

5. You get an answer in hebrew to your hebrew prompt!
<br/><br/>

What do you need to run this?

1. Android studio installed

2. An Azure account 
<br/><br/>

 
Build instarctions:

1. Go to https://beta.openai.com and sign up

2. Go to https://portal.azure.com and create a translation resource

3. Download the repository as a zip file, extract it, and open it in Android Studio

4. Open the Helper class, and paste your Azure translation api key, the region of the translation resource, and your Open AI key, in the appropriate variables

5. Thats it! run the application on your android phone or an emulator.
