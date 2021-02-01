package com.survivingcodingbootcamp.blog.controller;

import com.survivingcodingbootcamp.blog.storage.HashtagStorage;
import com.survivingcodingbootcamp.blog.storage.PostStorage;
import com.survivingcodingbootcamp.blog.storage.TopicStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TopicController {
    private TopicStorage topicStorage;
    private PostStorage postStorage;
    private HashtagStorage hashtagStorage;

    @Autowired
    public TopicController(TopicStorage topicStorage, PostStorage postStorage, HashtagStorage hashtagStorage) {
        this.topicStorage = topicStorage;
        this.postStorage = postStorage;
        this.hashtagStorage = hashtagStorage;
    }

    public TopicController(TopicStorage topicStorage) {
        this.topicStorage = topicStorage;
    }

    @GetMapping("/topic/{id}")
    public String displaySingleTopic(@PathVariable long id, Model model) {
        model.addAttribute("topic", topicStorage.retrieveSingleTopic(id));
        model.addAttribute("allPostsForThisTopic", topicStorage.retrieveSingleTopic(id).getPosts());
        return "single-topic-template";
    }

    @RequestMapping("/topics")
    public String displayAllTopics(Model model){
        model.addAttribute("allTopics", topicStorage.retrieveAllTopics());
        return "all-topics-template";
    }
}
