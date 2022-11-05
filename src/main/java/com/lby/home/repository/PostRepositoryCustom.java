package com.lby.home.repository;

import com.lby.home.domain.Post;
import com.lby.home.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
