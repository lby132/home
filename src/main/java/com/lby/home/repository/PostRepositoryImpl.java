package com.lby.home.repository;

import com.lby.home.domain.Post;
import com.lby.home.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.lby.home.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
       return jpaQueryFactory.selectFrom(post)
               .limit(postSearch.getSize())
               .offset(postSearch.getOffset())
               .orderBy(post.id.desc())
               .fetch();
    }
}
