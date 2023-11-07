package com.hokhanh.repositories;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.hokhanh.model.Comments;
import com.hokhanh.model.Videos;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Repository
public class CommentsRepository {
	
	public MongoCollection<Document> getMongoCollection() {
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase mongoDatabase = mongoClient.getDatabase("nhi");
		MongoCollection<Document> mongoCollectionComments = mongoDatabase.getCollection("comments");
		return mongoCollectionComments;
	}

	public List<Object> getComments() {
		MongoCollection<Document> mongoCollectionComments = this.getMongoCollection();

		FindIterable<Document> findIterable = mongoCollectionComments.find();
		List<Object> commentList = new ArrayList<>();

		for (Document document : findIterable) {
			Comments comments = new Comments();
			ObjectId objectId = document.getObjectId("_id");
			comments.set_id(objectId.toString());
			comments.setVideo_id(document.getInteger("video_id"));
			comments.setComment_text(document.getString("comment_text"));
			comments.setLikes(document.getInteger("likes"));	
			comments.setReplies(document.getInteger("replies"));
			
			commentList.add(comments);
		}
		return commentList;
	}
	

	public String insertComment(Document document) {
		MongoCollection<Document> mongoCollectionComments = this.getMongoCollection();

		try {
			mongoCollectionComments.insertOne(document);
			return "Successfully inserted";
		} catch (Exception e) {
			e.printStackTrace();
			return "Successfully not inserted, try again !";
		}
	}

	public String updateComment(String id, Document document) {
		MongoCollection<Document> mongoCollectionComments = this.getMongoCollection();

		try {
			BasicDBObject filter = new BasicDBObject("_id", new ObjectId(id));
			BasicDBObject update = new BasicDBObject("$set", document);
			mongoCollectionComments.updateOne(filter, update);
			return "Successfully updated";
		} catch (Exception e) {
			e.printStackTrace();
			return "Successfully not updated, try again !";
		}
	}

	public String deleteComment(String id) {
		MongoCollection<Document> mongoCollectionComments = this.getMongoCollection();

		try {
			BasicDBObject filter = new BasicDBObject("_id", new ObjectId(id));
			mongoCollectionComments.deleteOne(filter);
			return "Successfully deleted";
		} catch (Exception e) {
			e.printStackTrace();
			return "Successfully not deleted, try again !";
		}
	}

	public Comments getCommentById(String id) {
		MongoCollection<Document> mongoCollectionComments = this.getMongoCollection();

		ObjectId objectId = new ObjectId(id);
		BasicDBObject basicDBObject = new BasicDBObject("_id", objectId);
		FindIterable<Document> findIterable = mongoCollectionComments.find(basicDBObject);

		Comments comments = new Comments();
		for (Document document : findIterable) {
			ObjectId objectId_2 = document.getObjectId("_id");
			comments.set_id(objectId_2.toString());
			comments.setVideo_id(document.getInteger("video_id"));
			comments.setComment_text(document.getString("comment_text"));
			comments.setLikes(document.getInteger("likes"));	
			comments.setReplies(document.getInteger("replies"));
			
		}
		return comments;
	}
	
	
}
