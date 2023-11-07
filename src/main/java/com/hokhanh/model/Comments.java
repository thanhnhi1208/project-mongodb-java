package com.hokhanh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comments {
	private String _id;
	private int video_id;
	private String comment_text;
	private int likes;
	private int replies;
}
