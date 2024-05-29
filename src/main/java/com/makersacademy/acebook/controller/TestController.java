package com.makersacademy.acebook.controller;

import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.cloudinary.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.beam.sdk.schemas.utils.JsonUtils;

@RestController
public class TestController {

    @GetMapping("/test")
    public ModelAndView test() {
        return new ModelAndView("/test");
    }

    @PostMapping("/test")
    public ModelAndView handleImageUpload(@RequestParam("imageInfo") String imageInfo) throws IOException {
        ModelAndView modelAndView = new ModelAndView("/test");
        String result = imageInfo;
        System.out.println("----------------?????????????");
        modelAndView.addObject("result", result);
        System.out.println(imageInfo);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(result, Map.class);
        String thumbnail_url = map.get("thumbnail_url");
        modelAndView.addObject("thumbnail_url", thumbnail_url);
        System.out.println(map.get("url"));
        System.out.println(map.get("thumbnail_url"));
        return modelAndView;
    }
}
