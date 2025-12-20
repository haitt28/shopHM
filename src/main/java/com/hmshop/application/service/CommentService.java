package com.hmshop.application.service;

import com.hmshop.application.entity.Comment;
import com.hmshop.application.model.request.CreateCommentPostRequest;
import com.hmshop.application.model.request.CreateCommentProductRequest;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    Comment createCommentPost(CreateCommentPostRequest createCommentPostRequest,long userId);
    Comment createCommentProduct(CreateCommentProductRequest createCommentProductRequest, long userId);
}
