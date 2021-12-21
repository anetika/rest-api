package com.epam.esm.controller;

import com.epam.esm.configuration.Translator;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.ResourceNotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {
    private final GiftCertificateService service;

    private final Translator translator;

    public GiftCertificateController(GiftCertificateService service, Translator translator) {
        this.service = service;
        this.translator = translator;
    }

    @PostMapping
    public ResponseEntity<GiftCertificateDto> add(@RequestBody GiftCertificateDto certificateDto) {
        try {
            certificateDto = service.add(certificateDto);
            return new ResponseEntity<>(certificateDto, HttpStatus.OK);
        } catch(ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GiftCertificateDto> getGiftCertificateById(@PathVariable("id") long id) {
        try {
            GiftCertificateDto certificateDto = service.getById(id);
            return new ResponseEntity<>(certificateDto, HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundServiceException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> getAllGiftCertificates(
            @RequestParam(value = "tag", defaultValue = "", required = false) String tag,
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "sort", defaultValue = "", required = false) String sort
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("tag", tag);
        params.put("search", search);
        params.put("sort", sort);
        try {
            List<GiftCertificateDto> certificateDtoList = service.getAll(params);
            if (certificateDtoList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(certificateDtoList, HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        String message = translator.toLocale("certificate_delete");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(message, HttpStatus.OK);
        try {
            service.delete(id);
        } catch (ServiceException e) {
            message = translator.toLocale("error500_message");
            responseEntity =  new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundServiceException e) {
            message = translator.toLocale("certificate_no_content");
            responseEntity =  new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
        } finally {
            changeResponseCharset(responseEntity);
        }
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        String message = translator.toLocale("certificates_delete");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(message, HttpStatus.OK);
        try {
            service.deleteAll();
        } catch (ServiceException e) {
            message = translator.toLocale("error500_message");
            responseEntity = new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            changeResponseCharset(responseEntity);
        }
        return responseEntity;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<GiftCertificateDto> update(@PathVariable("id") long id, @RequestBody GiftCertificateDto giftCertificateDto) {
        try{
            giftCertificateDto = service.update(id, giftCertificateDto);
            return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
        } catch (ResourceNotFoundServiceException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void changeResponseCharset(ResponseEntity<String> responseEntity){
        HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(responseEntity.getHeaders());
        httpHeaders.add("Content-Type", "text/plain;charset=UTF-8");
    }

}
