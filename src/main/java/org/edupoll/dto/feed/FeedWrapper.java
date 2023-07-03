package org.edupoll.dto.feed;

import java.util.List;

import org.edupoll.dto.user.UserResponseData;
import org.edupoll.entity.Feed;

import lombok.Data;

@Data
public class FeedWrapper {

	private Long id;
	
	private UserResponseData writer;
	
	private String description;
	private Long viewCount;
	
	private List<FeedAttachWrapper> attaches;
	
	public FeedWrapper(Feed e) {
		this.id = e.getId();
		this.description = e.getDescription();
		this.viewCount = e.getViewCount();
		this.writer = new UserResponseData(e.getWriter());
		
		this.attaches = e.getAttaches()
					.stream().map(item->new FeedAttachWrapper(item)).toList();
		
	}
}
