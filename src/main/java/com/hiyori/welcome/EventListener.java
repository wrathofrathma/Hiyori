package com.hiyori.welcome;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by rathma on 1/2/18.
 */
public class EventListener extends ListenerAdapter {

    public long SERVER_ID=0;
    public long CHANNEL_ID=0;
    public String FOLDER="";
    public File[] banners;
    public EventListener(long server_id, long channel_id, String folder)
    {
        SERVER_ID=server_id;
        CHANNEL_ID=channel_id;
        FOLDER=folder;
        loadFolderList();
    }
    public void loadFolderList()
    {
        System.out.println("Finding banners...");
        File dir = new File(FOLDER);
        banners = dir.listFiles(new FilenameFilter()
        {
            public boolean accept(File dir, String name) {
                System.out.println(name);
                return name.endsWith(".png");
            }
        });
        System.out.println("Number of banners found: " + banners.length);
    }
    public File selectBanner()
    {
        int random = new Random().nextInt(banners.length);
        return banners[random];
    }
    @Override public void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        JDA jda = event.getJDA();
        Guild guild = event.getGuild();
        long guild_id = guild.getIdLong();
        //If the server is the one we're setting up for.
        if(guild_id==SERVER_ID)
        {
            TextChannel channel = jda.getTextChannelById(CHANNEL_ID);
            User user = event.getUser();

            /* Downloading user's Avatar */
            String avatar_url = user.getAvatarUrl();
            String username = user.getName();

            try{
                /* Loading Welcome Banner */
                BufferedImage banner = ImageIO.read(selectBanner());

                URL url = new URL(avatar_url);

                /* We have to spoof a web browser or we'll get hit with 403 access denied messages */
                HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                httpcon.addRequestProperty("User-Agent","Mozilla/4.0");
                InputStream input_stream = httpcon.getInputStream();
                BufferedImage img = ImageIO.read(input_stream);

                /* Creating our scaled avatar */
                BufferedImage scaled_avatar = new BufferedImage(95,
                        95, img.getType());
                // scales the input image to the output image
                Graphics2D g2d = scaled_avatar.createGraphics();
                g2d.drawImage(img, 0, 0, 95, 95, null);
                g2d.dispose();
                input_stream.close();


                /* Let's add the user's name to the image */
                Graphics graphics = banner.getGraphics();
                if(username.length()<=14) {
                    graphics.setFont(new Font("TimesRoman", Font.PLAIN, 40));
                    graphics.drawString(username, 240, 100);
                }
                else if(username.length()>14 && username.length()<=20) {
                    graphics.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                    graphics.drawString(username, 240, 100);
                }
                else if(username.length()>20) {
                    graphics.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                    graphics.drawString(username, 240, 95);
                }

                /* Let's add the user's avatar now */
                graphics.drawImage(scaled_avatar, 720, 12, null);
                graphics.dispose();

                /* I really don't like the idea of writing to disk, so we're just going to convert this to a byte array and call it a night */
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(banner, "png",baos);
                byte[] banner_out = baos.toByteArray();
                baos.close();

                /* Sending our banner to the channel */
                channel.sendFile(banner_out,"welcome.png").queue();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };
}
