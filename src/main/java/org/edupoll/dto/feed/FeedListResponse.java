package org.edupoll.dto.feed;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedListResponse {
	private Long total;
	private List<FeedWrapper> feeds;
}
