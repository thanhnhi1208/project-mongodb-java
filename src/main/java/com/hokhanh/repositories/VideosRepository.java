package com.hokhanh.repositories;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hokhanh.model.Comments;
import com.hokhanh.model.Videos;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

import javax.swing.*;
import java.awt.*;

@Repository
public class VideosRepository {

	public MongoCollection<Document> getMongoCollection() {
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase mongoDatabase = mongoClient.getDatabase("nhi");
		MongoCollection<Document> mongoCollectionVideos = mongoDatabase.getCollection("videos");
		return mongoCollectionVideos;
	}

	public List<Videos> getVideos() {
		MongoCollection<Document> mongoCollectionVideos = this.getMongoCollection();

		FindIterable<Document> findIterable = mongoCollectionVideos.find();
		List<Videos> videoList = new ArrayList<>();

		for (Document document : findIterable) {
			Videos video = new Videos();
			ObjectId objectId = document.getObjectId("_id");
			video.set_id(objectId.toString());
			video.setVideo_id(document.getInteger("video_id"));
			video.setTitle(document.getString("title"));
			video.setChannel_name(document.getString("channel_name"));

			List<String> genreList = (java.util.List<String>) document.get("genres");
			video.setGenres(genreList);

			List<String> tagList = (java.util.List<String>) document.get("tags");
			video.setTags(tagList);

			video.setViews(document.getInteger("views"));
			video.setLikes(document.getInteger("likes"));
			video.setDislikes(document.getInteger("dislikes"));

			java.util.Date date = document.getDate("date");
			Instant instant = date.toInstant();
			LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
			video.setDate(localDate);


			videoList.add(video);
		}
		return videoList;
	}

	public String insertVideo(Document document) {
		MongoCollection<Document> mongoCollectionVideos = this.getMongoCollection();

		try {
//			String []dateSplit = document.getString("date").split("/");
			
//			Date date = new Date(dateSplit[0], );
//			document.put("date", date);
			mongoCollectionVideos.insertOne(document);
			return "Successfully inserted";
		} catch (Exception e) {
			e.printStackTrace();
			return "Successfully not inserted, try again !";
		}
	}

	public String updateVideo(String id, Document document) {
		MongoCollection<Document> mongoCollectionVideos = this.getMongoCollection();

		try {
			BasicDBObject filter = new BasicDBObject("_id", new ObjectId(id));
			BasicDBObject update = new BasicDBObject("$set", document);
			mongoCollectionVideos.updateOne(filter, update);
			return "Successfully updated";
		} catch (Exception e) {
			e.printStackTrace();
			return "Successfully not updated, try again !";
		}
	}

	public String deleteVideo(String id) {
		MongoCollection<Document> mongoCollectionVideos = this.getMongoCollection();

		try {
			BasicDBObject filter = new BasicDBObject("_id", new ObjectId(id));
			mongoCollectionVideos.deleteOne(filter);
			return "Successfully deleted";
		} catch (Exception e) {
			e.printStackTrace();
			return "Successfully not deleted, try again !";
		}
	}

	public List<Document> get_all_comments_videos() {
		MongoCollection<Document> mongoCollection = this.getMongoCollection();

		Bson lookup = new Document("$lookup", new Document("from", "comments").append("localField", "video_id")
				.append("foreignField", "video_id").append("as", "videos_comments"));

		List<Bson> filters = new ArrayList<>();
		filters.add(lookup);

		AggregateIterable<Document> it = mongoCollection.aggregate(filters);

		List<Document> documents = new ArrayList<>();

		for (Document row : it) {
			row.put("_id", row.getObjectId("_id").toString());

			if (row.containsKey("videos_comments")) {
				List<Document> list = (List<Document>) row.get("videos_comments");

				for (Document document : list) {
					document.remove("_id");
					document.remove("video_id");
				}
			}
			documents.add(row);
		}

		return documents;
	}

	public String export_videos_comments(String fileName) {
		MongoCollection<Document> mongoCollection = this.getMongoCollection();

		try {
			Bson lookup = new Document("$lookup", new Document("from", "comments").append("localField", "video_id")
					.append("foreignField", "video_id").append("as", "videos_comments"));

			Bson lookup2 = new Document("find", new Document());

			List<Bson> filters = new ArrayList<>();
			filters.add(lookup);

			AggregateIterable<Document> it = mongoCollection.aggregate(filters);

			List<Document> documents = new ArrayList<>();

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			for (Document row : it) {
				row.put("_id", row.getObjectId("_id").toString());

				java.util.Date date = (java.util.Date) row.getDate("date");
				String formatDate = simpleDateFormat.format(date);

				row.put("date", formatDate);

				if (row.containsKey("videos_comments")) {
					List<Document> list = (List<Document>) row.get("videos_comments");

					for (Document document : list) {
						document.remove("_id");
						document.remove("video_id");
					}
				}
				documents.add(row);
			}

			String fileName_temp = "C:/Users/ADMIN/OneDrive/Desktop/" + fileName;
			

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Định dạng JSON để đẹp
			try (FileWriter fileWriter = new FileWriter(fileName_temp)) {
				objectMapper.writeValue(fileWriter, documents);

				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "successfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	public List<Object> findVideosByName(String channelName) {
		MongoCollection<Document> mongoCollectionVideos = this.getMongoCollection();

		BasicDBObject basicDBObject = new BasicDBObject("channel_name", channelName);
		FindIterable<Document> findIterable = mongoCollectionVideos.find(basicDBObject);
		List<Object> videoList = new ArrayList<>();

		for (Document document : findIterable) {
			document.put("_id", document.getObjectId("_id").toString());
			videoList.add(document);
		}

		return videoList;
	}

	public Videos getById(String _id) {
		MongoCollection<Document> mongoCollectionComments = this.getMongoCollection();

		ObjectId objectId = new ObjectId(_id);
		BasicDBObject basicDBObject = new BasicDBObject("_id", objectId);
		FindIterable<Document> findIterable = mongoCollectionComments.find(basicDBObject);

		Videos videos = new Videos();
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		for (Document document : findIterable) {
			ObjectId objectId_2 = document.getObjectId("_id");
			videos.set_id(objectId_2.toString());
			videos.setVideo_id(document.getInteger("video_id"));
			videos.setTitle(document.getString("title"));
			videos.setChannel_name(document.getString("channel_name"));
			List<String> genres = (List<String>) document.get("genres");
			videos.setGenres(genres);
			List<String> tags = (List<String>) document.get("tags");
			videos.setTags(tags);
			videos.setViews(document.getInteger("views"));
			videos.setLikes(document.getInteger("likes"));
			videos.setDislikes(document.getInteger("dislikes"));
			
			java.util.Date date = document.getDate("date");
			Instant instant = date.toInstant();
			LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
			videos.setDate(localDate);
			

		}
		return videos;
	}

}
