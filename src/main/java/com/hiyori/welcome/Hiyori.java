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
import java.awt.*;
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
    public int ROUNDED=0;
    public int TEXT_X=0;
    public int TEXT_Y=0;
    public int AVATAR_X=0;
    public int AVATAR_Y=0;
    public Color TEXT_COLOR=Color.WHITE;

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
                    .addEventListener(new EventListener(SERVER_ID, CHANNEL_ID, FOLDER,
                            ROUNDED, AVATAR_X, AVATAR_Y,
                            TEXT_X, TEXT_Y, TEXT_COLOR))
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
            SERVER_ID = obj.get("Server_ID").getAsLong();
            CHANNEL_ID = obj.get("Channel_ID").getAsLong();
            FOLDER = obj.get("Folder").getAsString();
            System.out.println("#### Bot Settings ####\n" + "T: " + TOKEN + " | SID: " + SERVER_ID + " | CID: " + CHANNEL_ID + " | F: " + FOLDER);

            ROUNDED = obj.get("Rounded").getAsInt();
            TEXT_X= obj.get("Text_X").getAsInt();
            TEXT_Y= obj.get("Text_Y").getAsInt();
            AVATAR_X= obj.get("Avatar_X").getAsInt();
            AVATAR_Y= obj.get("Avatar_Y").getAsInt();

            /* Kind of annoying trying to convert strings to colors, but that's what's happening here */
            String color = obj.get("Text_Color").getAsString();
            System.out.println("Loading Text_Color: " + color);
            TEXT_COLOR=(Color) Class.forName("java.awt.Color").getField(color.toLowerCase()).get(null);
            System.out.println("#### Banner Settings ####\n" + "Text_X: " + TEXT_X + " | Text_Y: " + TEXT_Y + " | Ava_X: " + AVATAR_X + " | Ava_Y: " + AVATAR_Y + " | Rounded: " + ROUNDED + " | Text Color:  "+ TEXT_COLOR);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Couldn't locate config.json");
            return 1;
        }
        catch (NoSuchFieldException e)
        {
            System.out.println("Color in settings not found. Defaulting to white...");
            TEXT_COLOR=Color.WHITE;
            System.out.println("#### Banner Settings ####\n" + "Text_X: " + TEXT_X + " | Text_Y: " + TEXT_Y + " | Ava_X: " + AVATAR_X + " | Ava_Y: " + AVATAR_Y + " | Rounded: " + ROUNDED + " | Text Color:  "+ TEXT_COLOR);
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
