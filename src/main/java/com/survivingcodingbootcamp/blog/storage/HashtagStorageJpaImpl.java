package com.survivingcodingbootcamp.blog.storage;

import com.survivingcodingbootcamp.blog.model.Hashtag;
import com.survivingcodingbootcamp.blog.storage.repository.HashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HashtagStorageJpaImpl implements HashtagStorage {
    private HashtagRepository hashtagRepo;

    @Autowired
    public HashtagStorageJpaImpl(HashtagRepository hashtagRepo) {
        this.hashtagRepo = hashtagRepo;
    }

    @Override
    public Iterable<Hashtag> retrieveAllHashtags() {
        return hashtagRepo.findAll();
    }

    @Override
    public Hashtag retrieveHashtagById(long id) {
        return hashtagRepo.findById(id).get();
    }

    @Override
    public void save(Hashtag hashtagToAdd) {
        hashtagRepo.save(hashtagToAdd);
    }
}
