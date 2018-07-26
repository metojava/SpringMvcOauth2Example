package com.tv.tests;

import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;

import com.example.config.DaoConfig;
import com.tv.model.Video;
import com.tv.pagableservices.VideoPagableService;

public class VideoPagableTest {

	private static final String fieldName = "name";

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
				DaoConfig.class);
		//ctx.refresh();
		VideoPagableService videoPagableService = (VideoPagableService) ctx
				.getBean("videoPagableService", VideoPagableService.class);

		Page<Video> vidsPage = videoPagableService.findAllinRange(0, 5, false,
				fieldName);
		List<Video> videos = vidsPage.getContent();
		for (Iterator iterator = videos.iterator(); iterator.hasNext();) {
			Video video = (Video) iterator.next();
			System.out
					.println(video.getName() + " - " + video.getDescription());
		}
		ctx.close();

	}
}
