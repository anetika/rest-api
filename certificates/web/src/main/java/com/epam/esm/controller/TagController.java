package com.epam.esm.controller;

import com.epam.esm.configuration.Translator;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ResourceNotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService service;

    private final Translator translator;

    public TagController(TagService service, Translator translator) {
        this.service = service;
        this.translator = translator;
    }

    @PostMapping
    public ResponseEntity<TagDto> add(@RequestBody TagDto dto) {
        try{
            dto = service.add(dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TagDto> getTagById(@PathVariable("id") long id) {
        try {
            TagDto tagDto = service.getById(id);
            return new ResponseEntity<>(tagDto, HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundServiceException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        try {
            List<TagDto> tagDtoList = service.getAll();
            if (tagDtoList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tagDtoList, HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        String message = translator.toLocale("tag_delete");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(message, HttpStatus.OK);
        try {
            service.delete(id);
        } catch (ServiceException e) {
            message = translator.toLocale("error500_message");
            responseEntity =  new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundServiceException e) {
            message = translator.toLocale("error404_message");
            responseEntity = new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } finally {
            changeResponseCharset(responseEntity);
        }
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        String message = translator.toLocale("tags_delete");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(message, HttpStatus.OK);
        try {
            service.deleteAll();
        } catch (ServiceException e) {
            message = translator.toLocale("error500_message");
            responseEntity =  new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            changeResponseCharset(responseEntity);
        }
        return responseEntity;
    }

    private void changeResponseCharset(ResponseEntity<String> responseEntity){
        HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(responseEntity.getHeaders());
        httpHeaders.add("Content-Type", "text/plain;charset=UTF-8");
    }
}
