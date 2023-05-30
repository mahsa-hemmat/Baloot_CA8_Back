package com.baloot.controller;

import com.baloot.exception.CommentNotFoundException;
import com.baloot.exception.CommodityNotFoundException;
import com.baloot.exception.UserNotFoundException;
import com.baloot.info.AbstractCommentInfo;
import com.baloot.service.BalootSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {
    private final BalootSystem balootSystem;
    @Autowired
    public CommentController(BalootSystem balootSystem){
        this.balootSystem = balootSystem;
    }

    @PostMapping ("")
    public ResponseEntity<Object> addComment(@RequestBody AbstractCommentInfo commentInfo) {
        if (!balootSystem.hasAnyUserLoggedIn())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in. Please login first");
        try {
            balootSystem.addComment(commentInfo.getText(), commentInfo.getCommodityId());
            return ResponseEntity.status(HttpStatus.OK).body("Comment added successfully.");
        } catch (CommodityNotFoundException|UserNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/{commentId}")
    public ResponseEntity<Object> voteComment(@PathVariable(value = "commentId") Long commentId,
                                              @RequestParam(value = "vote") int vote) {
        if (!balootSystem.hasAnyUserLoggedIn())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in. Please login first");
        try {
            balootSystem.voteComment(commentId, vote);
            return ResponseEntity.status(HttpStatus.OK).body("vote added successfully.");
        } catch (CommentNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
