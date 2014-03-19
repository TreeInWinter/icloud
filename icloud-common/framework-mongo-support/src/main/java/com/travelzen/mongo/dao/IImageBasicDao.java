package com.travelzen.mongo.dao;

import java.io.File;
import java.io.IOException;

import org.bson.types.ObjectId;

import com.travelzen.mongo.entity.ImageEntity;

public interface IImageBasicDao {

	Long getCurrentVersion(String mediaTypeId);

	void removeImage(String mediaTypeId);
	
	void removeImageById(String mediaId);

	ObjectId saveImage(File img, String filename, Long version) throws IOException;

	ImageEntity getImage(String filename);

	ObjectId saveBlankImage(String filename, long currentTimeMillis);

}
