package com.hiyori.welcome;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.MalformedJsonException;
import com.sun.javafx.fxml.expression.Expression;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by rathma on 1/2/18.
 */
public class Hiyori{
    public long SERVER_ID=0;
    public long CHANNEL_ID=0;
    public String TOKEN="";
    public String FOLDER="";
    private JDA jda;
    public Hiyori()
    {
        System.out.println("Starting Hiyori...");
        loadConfig();
    }
    public int run()
    {
        System.out.println("Attempting to login...");
        try
        {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(TOKEN)
                    .addEventListener(new EventListener(SERVER_ID, CHANNEL_ID, FOLDER))
                    .buildBlocking();
        }
        catch (LoginException e)
        {
            e.printStackTrace();
        }
        catch (RateLimitedException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Logged in...");
        return 0;
    }
    public int loadConfig()
    {
        System.out.println("Loading configuration...");
        try
        {
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(new FileReader("config.json"));

            TOKEN = obj.get("Token").getAsString();
            SERVER_ID = obj.get("ServerID").getAsLong();
            CHANNEL_ID = obj.get("ChannelID").getAsLong();
            FOLDER = obj.get("Folder").getAsString();

            System.out.println("T: " + TOKEN + " | SID: " + SERVER_ID + " | CID: " + CHANNEL_ID + " | F: " + FOLDER);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Couldn't locate config.json");
            return 1;
        }
        /*catch (MalformedJsonException e)
        {
            System.out.println("Your config.json is fucked. Fix it");
            return 1;
        }*/
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Configuration loaded successfully...");
        return 0;
    }

}
