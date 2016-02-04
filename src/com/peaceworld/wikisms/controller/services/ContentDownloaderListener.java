package com.peaceworld.wikisms.controller.services;

import com.peaceworld.wikisms.controller.rs.manager.ContentManeger.ContentDownloaderMessages;

public interface ContentDownloaderListener {

	public void finished(ContentDownloaderMessages message);

}
