# GandalfBot
This is a Telegram bot integrated with ChatGPT. As access to the OpenAI API is paid, the bot has a whitelist of users who are authorized to use it.

### Comand's
- /add - add the user to the white list (only for admin)
- /remove - remove a user from the whitelist (only for admin)

### Bot launch
For the bot to work, you need to create an application.properties file with the following content in the srt/main/resources directory.

***application.properties***
```
bot.token=<telegram-bot-token>
bot.username=<telegram-bot-username>

spring.data.mongodb.uri=<mongodb-uri>

admin=<admin-username> (without '@')

bot.text.model=gpt-3.5-turbo
bot.text.temperature=0.5
bot.text.max-tokens=1000
bot.text.top-p=1
bot.text.frequency-penalty=0
bot.text.presence-penalty=0


api.token=Bearer <openai-token>
api.url.completions=https://api.openai.com/v1/chat/completions
```
