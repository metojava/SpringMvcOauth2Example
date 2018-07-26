package com.tv.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tv.model.Video;
import com.tv.pagableservices.VideoPagableService;

@RestController
@RequestMapping("/rest")
public class VideoRestService {

	@Autowired
	VideoPagableService videoPagableService;

	@RequestMapping(path = "/videos", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Video> getAllVideos(Authentication authentication) {
//		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//		System.out.println("Details" + authentication.getDetails().toString());
//		System.out.println("Credentials"
//				+ authentication.getCredentials().toString());
//		System.out.println("authorities -------------------->  : "
//				+ userDetails.getAuthorities());
		return videoPagableService.findAllVideos();
	}
}
