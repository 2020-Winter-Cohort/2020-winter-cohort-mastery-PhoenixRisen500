package com.survivingcodingbootcamp.blog.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Hashtag {
    @Id
    @GeneratedValue
    private Long id;
    private String hashtagName;
    @ManyToMany(mappedBy = "thisPostsHashtags")
    private Collection<Post> thisHashtagsPosts = new ArrayList<>();
    @ManyToMany
    private Collection<Topic> thisHashtagsTopics = new ArrayList<>();

    public Hashtag(String hashtagName) {
        this.hashtagName = hashtagName;
    }
    public Hashtag() {

    }

    public void addPostToThisHashtag(Post inPost){
        thisHashtagsPosts.add(inPost);
    }

    public Long getId() {
        return id;
    }
    public String getHashtagName() {
        return hashtagName;
    }
    public Collection<Post> getThisHashtagsPosts() {
        return thisHashtagsPosts;
    }
    public Object getThisHashtagsTopics(){
        return thisHashtagsTopics;
    }

}
