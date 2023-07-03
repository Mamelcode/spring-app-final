package org.edupoll.service;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.edupoll.dto.feed.FeedWrapper;
import org.edupoll.dto.feed.FeedWriteRequestData;
import org.edupoll.entity.Feed;
import org.edupoll.entity.FeedAttach;
import org.edupoll.entity.User;
import org.edupoll.repository.FeedAttachRepository;
import org.edupoll.repository.FeedRepository;
import org.edupoll.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {

	private final FeedRepository feedRepository;
	
	private final FeedAttachRepository feedAttachRepository;
	
	private final UserRepository userRepository;
	
	@Value("${upload.baseDir}")
	String baseDir;
	@Value("${upload.server}")
	String uploadServer;
	
	@Transactional
	public void createNewFeed(String userEmail, FeedWriteRequestData data) throws IllegalStateException, IOException {		
		User findUser = userRepository.findByEmail(userEmail).get();
		
		Feed feed = new Feed(findUser, data.getDescription());
		
		Feed saveFeed = feedRepository.save(feed);
		
		if(data.getAttaches() != null) {
			for(MultipartFile multi : data.getAttaches()) {
				String emailEncoded = new String(Base64.getEncoder().encode(userEmail.getBytes()));
				File saveDir = new File(baseDir+"/feed/"+saveFeed.getId());
				saveDir.mkdirs();
				String fileName = System.currentTimeMillis()+"";
				fileName += multi.getOriginalFilename().substring(multi.getOriginalFilename().lastIndexOf("."));
				File dest = new File(saveDir, fileName);
				multi.transferTo(dest);
				
				String mediaUrl = uploadServer+"/resource/feed/"+saveFeed.getId()+"/"+fileName;
				String type = multi.getContentType();
				
				FeedAttach attach = new FeedAttach(saveFeed, mediaUrl, type);
				
				feedAttachRepository.save(attach);
			}
		}
	}
	
	@Transactional(readOnly = true)
	public Long size() {
		return feedRepository.count();
	}

	@Transactional(readOnly = true)
	public List<FeedWrapper> allItems(int page) {
		// 10개만 보내게
		Sort sort = Sort.by(Direction.ASC, "id");
		List<Feed> entityList = feedRepository.findAll(PageRequest.of(page-1, 10, sort)).toList();
		
		return entityList.stream().map(e -> new FeedWrapper(e)).toList();
	}
}
