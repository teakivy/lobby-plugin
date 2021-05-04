package me.teakivy.lobby.utils;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import javax.print.DocFlavor;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Mojang {

    public String getID(String name) {
        String url = "https://api.mojang.com/users/profiles/minecraft/"+name;
        try {
            @SuppressWarnings("deprecation")
            String UUIDJson = IOUtils.toString(new URL(url));
            if(UUIDJson.isEmpty()) return "invalid name";
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
            return UUIDObject.get("id").toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return "error";
    }

    public String getName(String uuid) {
        String url = "https://api.mojang.com/user/profiles/"+uuid.replace("-", "")+"/names";
        try {
            @SuppressWarnings("deprecation")
            String nameJson = IOUtils.toString(new URL(url));
            JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
            String playerSlot = nameValue.get(nameValue.size()-1).toString();
            JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
            return nameObject.get("name").toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public String getName(UUID uuid) {
        return getName(uuid.toString());
    }

    public String getStringUUID(String name) {
        String id = getID(name);
        if (id.equalsIgnoreCase("error")) return "error";
        String uuid1 = id.substring(0, 8);
        String uuid2 = id.substring(8, 12);
        String uuid3 = id.substring(12, 16);
        String uuid4 = id.substring(16, 20);
        String uuid5 = id.substring(20);
        return uuid1 + "-" + uuid2 + "-" + uuid3 + "-" + uuid4 + "-" + uuid5;
    }

    public String getUUID(String name) {
        return getStringUUID(name);
    }

    public static String insert(String bag, String marble, int index) {
        String bagBegin = bag.substring(0,index);
        String bagEnd = bag.substring(index);
        return bagBegin + marble + bagEnd;
    }
}
