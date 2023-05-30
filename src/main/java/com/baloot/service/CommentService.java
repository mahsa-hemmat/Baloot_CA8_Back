package com.baloot.service;


import com.baloot.exception.CommentNotFoundException;
import com.baloot.model.Comment;
import com.baloot.model.Commodity;
import com.baloot.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository cRepo;
    @Autowired
    public CommentService(CommentRepository commentRepository){
        this.cRepo = commentRepository;
    }
    public void saveComment(Comment comment){
        cRepo.save(comment);
    }
    public void saveAllComments(List<Comment> commentList){
        cRepo.saveAll(commentList);
    }
    public Comment findCommentById(Long id) throws CommentNotFoundException {
        Optional<Comment> comment = cRepo.findById(id);
        if(comment.isPresent())
            return comment.get();
        throw new CommentNotFoundException(id.toString());
    }

    public List<Comment> findByCommodity(int commodity) {
        return cRepo.findByCommodity(commodity);
    }
}
