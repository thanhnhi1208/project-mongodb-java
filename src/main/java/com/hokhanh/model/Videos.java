package com.hokhanh.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Videos {

	private String _id ;
	private int video_id;
	private String title;
	private String channel_name;
	private List<String> genres;
	private List<String> tags;
	private int views;
	private int likes;
	private int dislikes;
	private LocalDate date;
	
	
	private List<Comments> comments = new ArrayList<>();
}
