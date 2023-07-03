package org.edupoll.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "feedAttachs")
public class FeedAttach {
	
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id; // 기본키
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feedId")
	private Feed feed; // 피드이름
	
	private String mediaUrl; // 
	private String type; // 
	
	public FeedAttach(Feed feed, String mediaUrl, String type) {
		super();
		this.feed = feed;
		this.mediaUrl = mediaUrl;
		this.type = type;
	}
}
