package cloudcode.guestbook.frontend.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import java.net.*;
import java.io.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@Controller
public class FrontendController {

    private String backendUri = String.format("http://%s/messages", System.getenv("GUESTBOOK_API_ADDR")); 

    @GetMapping("/")
    public String main(Model model) {
        try {
            URL url = new URL(backendUri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(100);

            InputStreamReader reader = new InputStreamReader(con.getInputStream(), "UTF-8");
            ArrayList<Map<String, String>> messageList = new Gson().fromJson(reader, new TypeToken<ArrayList<Map<String, String>>>(){}.getType());
            model.addAttribute("messages", messageList);
            return "home"; //view
        } catch (IOException e) {
            return "error";
        }
    }

    @RequestMapping(value="/post", method=RequestMethod.POST)
    @ResponseBody
    public String post(FormMessage formMessage) {
        return formMessage.toString();
        //return "redirect:/";
    }

}