package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.dto.TagRequestDto;
import com.kartik.blog_platform_api.dto.TagResponseDto;
import com.kartik.blog_platform_api.entity.Tag;
import com.kartik.blog_platform_api.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagResponseDto createTag(TagRequestDto requestDto) {
        Tag tag = new Tag();
        tag.setName(requestDto.getName());

        Tag savedTag = tagRepository.save(tag);

        return new TagResponseDto(
                savedTag.getId(),
                savedTag.getName()
        );
    }

    public List<TagResponseDto> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tag -> new TagResponseDto(
                        tag.getId(),
                        tag.getName()
                ))
                .toList();
    }
}