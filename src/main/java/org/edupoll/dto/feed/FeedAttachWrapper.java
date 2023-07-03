package org.edupoll.dto.feed;

import org.edupoll.entity.FeedAttach;

import lombok.Data;

@Data
public class FeedAttachWrapper {
	private String type;
	private String mediaUrl;

	public FeedAttachWrapper(FeedAttach e) {
		this.type = e.getType();
		this.mediaUrl = e.getMediaUrl();
	}
}
