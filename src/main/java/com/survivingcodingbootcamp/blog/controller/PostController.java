package com.survivingcodingbootcamp.blog.controller;

import com.survivingcodingbootcamp.blog.model.Hashtag;
import com.survivingcodingbootcamp.blog.model.Post;
import com.survivingcodingbootcamp.blog.model.Topic;
import com.survivingcodingbootcamp.blog.storage.HashtagStorage;
import com.survivingcodingbootcamp.blog.storage.PostStorage;
import com.survivingcodingbootcamp.blog.storage.TopicStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/posts")
public class PostController {
    private TopicStorage topicStorage;
    private PostStorage postStorage;
    private HashtagStorage hashtagStorage;


    @Autowired
    public PostController(TopicStorage topicStorage, PostStorage postStorage, HashtagStorage hashtagStorage) {
        this.topicStorage = topicStorage;
        this.postStorage = postStorage;
        this.hashtagStorage = hashtagStorage;
    }
    public PostController(PostStorage postStorage){
        this.postStorage = postStorage;
    }


    @RequestMapping("")
    public String displayAllPosts(Model model){
        model.addAttribute("allPosts", postStorage.retrieveAllPosts());
        return "allPosts";
    }

    @GetMapping("/{id}")
    public String displaySinglePost(@PathVariable long id, Model model) {
        model.addAttribute("post", postStorage.retrievePostById(id));
        return "single-post-template";
    }

    @PostMapping(value="addPost")
    public String addPost(Model model,
                          @RequestParam Long id,
                          @RequestParam String newPostTitle,
                          @RequestParam String newPostAuthor,
                          @RequestParam String newPostContent,
                          @RequestParam String newPostHashtags
    ){
        Topic thisTopic = topicStorage.retrieveSingleTopic(id);
        Post newPost = new Post(newPostTitle, newPostAuthor, thisTopic, newPostContent);
        postStorage.save(newPost);
        thisTopic.addPostToThisTopic(newPost);

        ArrayList<String> listOfTagsToAdd = new ArrayList<>();
        ArrayList<String> listOfLowerCaseHashtagNamesToAdd = new ArrayList<>();
        String[] newStrings = newPostHashtags.split("#|,| "); //makes array
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
        for (String i: listOfTagsToAdd){
            Hashtag newHashtag = new Hashtag(i);
            if (newPost.checkForExistingHashtag(newPost, newHashtag.getHashtagName())){continue;} //skips redundant hashtags
            newPost.addHashtagToThisPost(newHashtag);
            hashtagStorage.save(newHashtag);
            newHashtag.addPostToThisHashtag(newPost);
        }
        return "redirect:/";
    }
}
