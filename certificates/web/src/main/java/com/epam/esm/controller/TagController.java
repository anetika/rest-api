package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import com.epam.esm.translator.Translator;
import com.epam.esm.util.CharsetUtil;
import com.epam.esm.util.HateoasUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TagController {
    private final TagService service;
    private final HateoasUtil hateoasUtil;
    private final Translator translator;
    private final CharsetUtil charsetUtil;

    public TagController(TagService service, HateoasUtil hateoasUtil, Translator translator, CharsetUtil charsetUtil) {
        this.service = service;
        this.hateoasUtil = hateoasUtil;
        this.translator = translator;
        this.charsetUtil = charsetUtil;
    }

    @PostMapping("/tags")
    public ResponseEntity<TagDto> add(@Valid  @RequestBody TagDto tagDto) {
        TagDto resultDto = service.add(tagDto);
        hateoasUtil.attacheTagLink(resultDto);
        return new ResponseEntity<>(resultDto, HttpStatus.CREATED);
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<TagDto> getById(@PathVariable long id) {
        TagDto resultDto = service.getById(id);
        hateoasUtil.attacheTagLink(resultDto);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @GetMapping("/tags")
    public ResponseEntity<Page<TagDto>> getAll(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size) {
        Page<TagDto> resultDtos = service.getAll(PageRequest.of(page, size));
        resultDtos.forEach(hateoasUtil::attacheTagLink);
        return new ResponseEntity<>(resultDtos, HttpStatus.OK);
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<String> deleteById(@PathVariable long id) {
        service.deleteById(id);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(translator.toLocale("tag_delete"), HttpStatus.OK);
        charsetUtil.changeResponseCharset(responseEntity);
        return responseEntity;
    }

    @DeleteMapping("/tags")
    public ResponseEntity<String> deleteAll() {
        service.deleteAll();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(translator.toLocale("tags_delete"), HttpStatus.OK);
        charsetUtil.changeResponseCharset(responseEntity);
        return responseEntity;
    }
}
