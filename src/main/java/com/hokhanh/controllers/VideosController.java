package com.hokhanh.controllers;

import java.text.ParseException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hokhanh.model.Videos;
import com.hokhanh.services.VideosService;



@Controller
public class VideosController{


	@Autowired
	private VideosService videosService;

	@GetMapping("/videos")
	public String getAllVideos(Model m) {
		List<Videos> videoList =  this.videosService.getAllVideos();
		m.addAttribute("videoList", videoList);
		return "Videos";
	}

	@GetMapping("/videos/getById")
	@ResponseBody
	public Videos  getVideoById(String _id ) {
		return this.videosService.getVideoById(_id);
	}
	
	@PostMapping("/videos/addNew")
	public String insertVideos(Videos videos) throws ParseException {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("video_id", videos.getVideo_id());
		map.put("title", videos.getTitle());
		map.put("channel_name", videos.getChannel_name());
		map.put("genres", videos.getGenres());
		map.put("tags", videos.getTags());
		map.put("views", videos.getViews());
		map.put("likes", videos.getLikes());
		map.put("dislikes", videos.getDislikes());
		map.put("date", videos.getDate());
		this.videosService.insertVideo(map);
		return "redirect:/videos";
	}

	@RequestMapping(value =  "/videos/update", method = {RequestMethod.GET, RequestMethod.PUT})
	public String updateVideo(Videos videos, @RequestParam("_id") String id) throws ParseException{
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("_id", id);
		map.put("video_id", videos.getVideo_id());
		map.put("title", videos.getTitle());
		map.put("channel_name", videos.getChannel_name());
		map.put("genres", videos.getGenres());
		map.put("tags", videos.getTags());
		map.put("views", videos.getViews());
		map.put("likes", videos.getLikes());
		map.put("dislikes", videos.getDislikes());
		map.put("date", videos.getDate());
		this.videosService.updateVideo(map);
		return "redirect:/videos";
	}

	@RequestMapping( value =  "/videos/delete", method = {RequestMethod.GET, RequestMethod.DELETE})
	public String deleteVideo(String _id ){
		this.videosService.deleteVideo(_id);
		return "redirect:/videos";
	}
	
//	@GetMapping("/videos/comments")
//	public List<Document> get_all_videos_comments() {
//		return this.videosService.get_all_videos_comment();
//	}
	
	@GetMapping("/videos/comments/export")
	public String export_videos_comments() {
		String fileName = "videoscomments.js";
		this.videosService.export_videos_comments(fileName);
		return "redirect:/index";
	}
	
	@GetMapping("/videos/findByName")
	public String findByChannelName(String channelName) {
		this.videosService.findVideosByName(channelName);
		return "videos";
	}
	
//	@GetMapping("/videos/comments/drawGraph/{fileName}")
//	public String drawGraph(@PathVariable("fileName") String fileName) {
//		String disk_fileName = "C:/Users/DELL/OneDrive/Máy tính/"+fileName;
//		System.out.println(disk_fileName);
//		return this.videosService.drawGraph(disk_fileName);
//	}

	
}
