package org.edupoll.security.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.edupoll.service.JWTService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	private final JWTService jwtService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain)
			throws ServletException, IOException {
		
		// 사용자가 JWT 토큰을 가져오면
		String authorization = req.getHeader("Authorization");
		log.info("Header : {} ", authorization);
		
		if(authorization == null) {
			log.info("헤더 벨류가 없어서 인증을 진행할 수 없습니다.");
			filterChain.doFilter(req, resp);
			return;
		}
		
		// JWT 유효성 검사를 통과하면
		try {
			String email = jwtService.verifyToken(authorization);
			
			// 인증 통과
			Authentication authentication = 
					new UsernamePasswordAuthenticationToken(email, authorization, List.of(new SimpleGrantedAuthority("ROLE_USER")));
			// principal => 인증주체자 : UserDetails 객체
				// @AutenticationPrincipal 에서 나오는 값
			// credential ==> 인증에 사용 됐던 정보
			// authorities ===> 권한 : role에 따른 차단
				// config에 권한이 설정되어있다면 DB에 설정되어있는권한을 꺼내와서 설정해준다.
				// 권한이 맞다면 통과 아니면 포비든(권한없음)
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch (Exception e) {
			// 토큰 오류
			throw new BadCredentialsException("Invalid authentication token");
		}
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		log.info("doFilter : {} ", authentication);
		
		// Authentication을 등록시키면 된다(인증통과상태)
		
		filterChain.doFilter(req, resp);
	}

}
