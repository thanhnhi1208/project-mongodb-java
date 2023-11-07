package com.hokhanh.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hokhanh.model.Comments;
import com.hokhanh.model.Videos;
import com.hokhanh.services.CommentsService;

@Controller
public class CommentsController {

	@Autowired
	private CommentsService commentsService;

	@GetMapping("/comments")
	public String  getAllComments(Model m) {
	List<Object> commentsList = this.commentsService.getAllComments();
	m.addAttribute("commentList", commentsList);
		return "comments";
	}
	
	@GetMapping("/comments/getById")
	@ResponseBody
	public Comments  getCommentById(String _id ) {
		return this.commentsService.getCommentById(_id);
	}
	
	
	@PostMapping("/comments/addNew")
	public String insertComment(Comments comments) {
		Map<String, Object> map = new HashMap<>();
		map.put("video_id", comments.getVideo_id());
		map.put("comment_text", comments.getComment_text());
		map.put("likes", comments.getLikes());
		map.put("replies", comments.getReplies());
		this.commentsService.insertComment(map);
		return "redirect:/comments";
	}

	@RequestMapping(value =  "/comments/update", method = {RequestMethod.GET, RequestMethod.PUT})
	public String updateComment(Comments comments, @RequestParam("_id") String _id ){
		Map<String, Object> map = new HashMap<>();
		map.put("_id", _id);
		map.put("video_id", comments.getVideo_id());
		map.put("comment_text", comments.getComment_text());
		map.put("likes", comments.getLikes());
		map.put("replies", comments.getReplies());
		
		this.commentsService.updateComment(map);
		return "redirect:/comments";
	}

	@RequestMapping (value = "/comments/delete", method = {RequestMethod.GET, RequestMethod.DELETE})
	public String deleteComment(@RequestParam("_id") String id ){
		this.commentsService.deleteComment(id);
		return "redirect:/comments";
	}
}
