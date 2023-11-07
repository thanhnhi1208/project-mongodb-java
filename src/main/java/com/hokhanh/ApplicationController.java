package com.hokhanh;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hokhanh.model.Videos;
import com.hokhanh.services.VideosService;

@Controller
public class ApplicationController {
	@Autowired
	private VideosService videosService;

	@GetMapping("/index")
	public String index(Model m) {
		List<Videos> videos = this.videosService.get_all_videos_comment();
		m.addAttribute("videoList", videos);
		return "index";
	}
}
