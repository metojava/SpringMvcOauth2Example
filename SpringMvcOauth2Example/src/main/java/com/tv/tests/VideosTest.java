package com.tv.tests;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.config.DaoConfig;
import com.google.common.collect.Lists;
import com.tv.model.Video;
import com.tv.pageablerepos.VideoPagableRepo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DaoConfig.class })
public class VideosTest {

	@Autowired
	VideoPagableRepo repo;

	@Test
	public void getAllVideosTest() {
		List<Video> videos = Lists.newArrayList(repo.findAll());
		videos.forEach(System.out::println);
	}
}
