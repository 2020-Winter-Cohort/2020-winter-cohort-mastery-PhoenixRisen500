package com.survivingcodingbootcamp.blog.controller;

import com.survivingcodingbootcamp.blog.storage.HashtagStorage;
import com.survivingcodingbootcamp.blog.storage.HashtagStorageJpaImpl;
import com.survivingcodingbootcamp.blog.storage.PostStorage;
import com.survivingcodingbootcamp.blog.storage.TopicStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private TopicStorage topicStorage;
    private PostStorage postStorage;
    private HashtagStorageJpaImpl hashtagStorageJpaImpl;
    private HashtagStorage hashtagStorage;

    @Autowired
    public HomeController(TopicStorage topicStorage, PostStorage postStorage, HashtagStorage hashtagStorage) {
        this.topicStorage = topicStorage;
        this.postStorage = postStorage;
        this.hashtagStorage = hashtagStorage;
    }

    public HomeController(TopicStorage topicStorage) {
        this.topicStorage = topicStorage;
    }

    @GetMapping("/")
    public String displayHomePage(Model model) {
        model.addAttribute("topics", topicStorage.retrieveAllTopics());
        model.addAttribute("allPosts", postStorage.retrieveAllPosts());
        model.addAttribute("allHashtags", hashtagStorage.retrieveAllHashtags());
    return "home-template";
    }
}
