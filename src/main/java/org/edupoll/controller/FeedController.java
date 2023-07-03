package org.edupoll.controller;

import java.io.IOException;
import java.util.List;

import org.edupoll.dto.feed.FeedListResponse;
import org.edupoll.dto.feed.FeedWrapper;
import org.edupoll.dto.feed.FeedWriteRequestData;
import org.edupoll.service.FeedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/feed")
public class FeedController {
	
	private final FeedService feedService;
	
	// 전체 글(or 특정조건의) 목록 제공해주는 API
	@GetMapping("/storage")
	public ResponseEntity<?> readAllFeedHandle(@RequestParam(defaultValue = "1") int page) {
		
		Long total=feedService.size();
		List<FeedWrapper> feeds = feedService.allItems(page);
		FeedListResponse response = new FeedListResponse(total, feeds);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	// 특정 글 제공 API -> 공개
	@GetMapping("/detail/{feedId}")
	public ResponseEntity<?> readOneFeedHandle() {
		
		return null;
	}
	
	// 신규 글 등록 API -> 보호
	@PostMapping("/storage")
	public ResponseEntity<?> createNewFeedHandle(@AuthenticationPrincipal String principal,
			FeedWriteRequestData data) throws IllegalStateException, IOException {
		
		log.info("Write : {}, {} ", principal, data);
		
		feedService.createNewFeed(principal, data);
		
		return ResponseEntity.ok("글 등록 완료");
	}
	
	// 특정 글 삭제 API -> 보호
	@DeleteMapping("/delete/{feedId}")
	public ResponseEntity<?> deleteOneFeedHandle() {
		
		return null;
	}
	
	
}
