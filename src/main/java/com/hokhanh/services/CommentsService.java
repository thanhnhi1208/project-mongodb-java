package com.hokhanh.services;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hokhanh.model.Comments;
import com.hokhanh.repositories.CommentsRepository;

@Service
public class CommentsService {
	@Autowired
	private CommentsRepository commentsRepository;

	public List<Object> getAllComments() {
		return commentsRepository.getComments();
	}

	public String insertComment(Map<String, Object> comment) {
		Document document = new Document(comment);
		return this.commentsRepository.insertComment(document);
	}

	public String updateComment(Map<String, Object> comment) {
		Document document = new Document(comment);
		String id = document.getString("_id");
		document.remove("_id");
		return this.commentsRepository.updateComment(id, document);
	}

	public String deleteComment(String id) {
		return this.commentsRepository.deleteComment(id);
	}
	
	public Comments getCommentById(String id) {
		return this.commentsRepository.getCommentById(id);
	}
}
