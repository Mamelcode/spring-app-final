package org.edupoll.dto.feed;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedWriteRequestData {

	private String description;
	
	private List<MultipartFile> attaches;
	
}
