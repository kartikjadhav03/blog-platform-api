
package com.kartik.blog_platform_api;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BlogPlatformApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;



	@Test
	void contextLoads() {
	}


	@Test
	void getAllPosts_shouldReturnOkWithoutAuthentication() throws Exception {

		mockMvc.perform(get("/api/posts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.totalElements").exists());
	}

	@Test
	void createPost_shouldReturnUnauthorizedWithoutToken() throws Exception {

		mockMvc.perform(post("/api/posts"))
				.andExpect(status().isUnauthorized());
	}


	@Test
	void updatePost_shouldReturnUnauthorizedWithoutToken() throws Exception {

		mockMvc.perform(put("/api/posts/1"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void deletePost_shouldReturnUnauthorizedWithoutToken() throws Exception {

		mockMvc.perform(delete("/api/posts/1"))
				.andExpect(status().isUnauthorized());
	}
}
