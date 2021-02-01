package com.survivingcodingbootcamp.blog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.survivingcodingbootcamp.blog.model.Hashtag;
import com.survivingcodingbootcamp.blog.model.Post;
import com.survivingcodingbootcamp.blog.storage.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Controller
public class HashtagController {
    private TopicStorage topicStorage;
    private PostStorage postStorage;
    private HashtagStorageJpaImpl hashtagStorage;

    @Autowired
    public HashtagController(TopicStorage topicStorage, PostStorage postStorage, HashtagStorageJpaImpl hashtagStorage) {
        this.topicStorage = topicStorage;
        this.postStorage = postStorage;
        this.hashtagStorage = hashtagStorage;
    }


    @GetMapping("/hashtag/{id}")
    public String oneHashtagsPosts(Model model, @PathVariable Long id){
        model.addAttribute("topics", topicStorage.retrieveAllTopics());
        model.addAttribute("thisHashtagsName", hashtagStorage.retrieveHashtagById(id).getHashtagName());
        model.addAttribute("thisHashtagsPosts", hashtagStorage.retrieveHashtagById(id).getThisHashtagsPosts());
        return "single-hashtag-template";
    }

//    @GetMapping("/hashtag/{inHashtagName}")
//    public String oneHashtagByName(Model model, @PathVariable String inHashtagName){
//        model.addAttribute("thisHashtagsItems", hashtagStorage.findByHashtagName(inHashtagName));
//        System.out.println("testing find by hashtagName: " + hashtagStorage.findByHashtagName("tests"));
//        return "single-hashtag-template";
//    }

    @GetMapping("/hashtag/allHashtags")
    public String allHashtags(Model model){
        model.addAttribute("topics", topicStorage.retrieveAllTopics());
        model.addAttribute("allPosts", postStorage.retrieveAllPosts());
        model.addAttribute("allHashtags", hashtagStorage.retrieveAllHashtags());
//        model.addAttribute("thisHashtagsItems", hashtagStorage.findByHashtagName(inHashtagName));
        System.out.println("testing find by hashtagName: " + hashtagStorage.findByHashtagName("java"));
        return "all-hashtags-template";
    }

    @PostMapping(value="/post/addHashtag")
    public String addHashtagByName(Model model, @RequestParam Long id, @RequestParam String newHashtagsToAdd){
        addHashtags(id, newHashtagsToAdd);
        return "redirect:/hashtag/allHashtags";
    }


    private void addHashtags(Long id, String inOneRawStringOfAllHashtagsToAdd){
        ArrayList<String> listOfTagsToAdd = new ArrayList<>();
        ArrayList<String> listOfLowerCaseHashtagNamesToAdd = new ArrayList<>();
        String[] newStrings = inOneRawStringOfAllHashtagsToAdd.split("#|,| "); //makes array
        for (String i: newStrings){ //loops through all the new strings
            if (  //tests for and skips empty and non-hashtag strings
                i.equalsIgnoreCase("") ||
                    i.equalsIgnoreCase(" ") ||
                    i.equalsIgnoreCase(",") ||
                    i.equalsIgnoreCase(".") ||
                    i.equalsIgnoreCase("#") )
            {continue;} // skips non-hashtag strings
            else {
                i = i.replaceAll("[^a-zA-Z0-9]", ""); //removes all non alpha-numeric characters
                listOfTagsToAdd.add(i); //adds strings to new hashtag list
                listOfLowerCaseHashtagNamesToAdd.add(i.toLowerCase());
            }
        }
        Post thisPost = postStorage.retrievePostById(id);
        for (String i: listOfTagsToAdd){
            Hashtag newHashtag = new Hashtag(i);
            if (thisPost.checkForExistingHashtag(thisPost, newHashtag.getHashtagName())){continue;} //skips redundant hashtags
            thisPost.addHashtagToThisPost(newHashtag);
            hashtagStorage.save(newHashtag);
            newHashtag.addPostToThisHashtag(thisPost);
        }
    }
}
