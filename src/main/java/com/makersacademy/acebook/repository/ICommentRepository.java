package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepository extends CrudRepository<Comment, Long> {

    @Query(value = "SELECT c FROM Comment c JOIN c.post p WHERE p.id = :postId")
    Iterable<Comment> findCommentByPostId(@Param("postId") Long postId);
}


