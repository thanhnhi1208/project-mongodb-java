package com.hokhanh.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hokhanh.model.Comments;
import com.hokhanh.model.Videos;
import com.hokhanh.repositories.VideosRepository;

@Service
public class VideosService {

	@Autowired
	private VideosRepository videosRepository;

	public List<Videos> getAllVideos() {
		return videosRepository.getVideos();
	}

	public String insertVideo(Map<String, Object> video) {
		Document document = new Document(video);
		return this.videosRepository.insertVideo(document);
	}

	public String updateVideo(Map<String, Object> video) {
		Document document = new Document(video);
		String id = document.getString("_id");
		document.remove("_id");
		return this.videosRepository.updateVideo(id, document);
	}

	public String deleteVideo(String id) {
		return this.videosRepository.deleteVideo(id);
	}

	public List<Videos> get_all_videos_comment() {
		List<Document> documents = this.videosRepository.get_all_comments_videos();
		List<Videos> videos = new ArrayList<>();

		for (Document document : documents) {
			Videos video = new Videos();
			video.set_id(document.getString("_id"));
			video.setVideo_id(document.getInteger("video_id"));
			video.setTitle(document.getString("title"));
			video.setChannel_name(document.getString("channel_name"));
			List<String> genres = (List<String>) document.get("genres");
			video.setGenres(genres);
			List<String> tags = (List<String>) document.get("tags");
			video.setTags(tags);
			video.setViews(document.getInteger("views"));
			video.setLikes(document.getInteger("likes"));
			video.setDislikes(document.getInteger("dislikes"));

			Date date = document.getDate("date");
			Instant instant = date.toInstant();
			LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
			video.setDate(localDate);

			List<Document> videos_comments = document.getList("videos_comments", Document.class);
			if (videos_comments != null) {
				for (Document d : videos_comments) {
					Comments comments = new Comments();
					comments.setComment_text(d.getString("comment_text"));
					comments.setLikes(d.getInteger("likes"));
					comments.setReplies(d.getInteger("replies"));
					video.getComments().add(comments);
				}
			}
			videos.add(video);

		}
		return videos;
	}

	public String export_videos_comments(String fileName) {
		return this.videosRepository.export_videos_comments(fileName);
	}

	public List<Object> findVideosByName(String channelName) {
		return this.videosRepository.findVideosByName(channelName);
	}

	public Videos getVideoById(String _id) {
		return this.videosRepository.getById(_id);
	}
}
