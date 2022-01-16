package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.translator.Translator;
import com.epam.esm.util.CharsetUtil;
import com.epam.esm.util.HateoasUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class GiftCertificateController {

    private final GiftCertificateService service;
    private final Translator translator;
    private final HateoasUtil hateoasUtil;
    private final CharsetUtil  charsetUtil;

    public GiftCertificateController(GiftCertificateService service, Translator translator, HateoasUtil hateoasUtil, CharsetUtil charsetUtil) {
        this.service = service;
        this.translator = translator;
        this.hateoasUtil = hateoasUtil;
        this.charsetUtil = charsetUtil;
    }

    @PostMapping("/certificates")
    public ResponseEntity<GiftCertificateDto> add(@Valid @RequestBody GiftCertificateDto dto) {
        GiftCertificateDto resultDto = service.add(dto);
        hateoasUtil.attacheCertificateLink(resultDto);
        return new ResponseEntity<>(resultDto, HttpStatus.CREATED);
    }

    @GetMapping("/certificates/{id}")
    public ResponseEntity<GiftCertificateDto> getById(@PathVariable long id) {
        GiftCertificateDto resultDto = service.getById(id);
        hateoasUtil.attacheCertificateLink(resultDto);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @GetMapping("/certificates/findByTags")
    public ResponseEntity<Page<GiftCertificateDto>> getByTags(
            @Valid @RequestBody List<TagDto> dtos,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size) {
        Page<GiftCertificateDto> resultDto = service.getGiftCertificateByTags(dtos, PageRequest.of(page, size));
        resultDto.forEach(hateoasUtil::attacheCertificateLink);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @GetMapping("/certificates")
    public ResponseEntity<List<GiftCertificateDto>> getAll(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size) {
        List<GiftCertificateDto> resultDtos = service.getAll(page, size);
        resultDtos.forEach(hateoasUtil::attacheCertificateLink);
        return new ResponseEntity<>(resultDtos, HttpStatus.OK);
    }

    @DeleteMapping("/certificates/{id}")
    public ResponseEntity<String> deleteById(@PathVariable long id) {
        service.deleteById(id);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(translator.toLocale("certificate_delete"), HttpStatus.OK);
        charsetUtil.changeResponseCharset(responseEntity);
        return responseEntity;
    }

    @DeleteMapping("/certificates")
    public ResponseEntity<String> deleteAll() {
        service.deleteAll();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(translator.toLocale("certificates_delete"), HttpStatus.OK);
        charsetUtil.changeResponseCharset(responseEntity);
        return responseEntity;
    }

    @PutMapping("/certificates/{id}")
    public ResponseEntity<GiftCertificateDto> update(@PathVariable long id, @RequestBody GiftCertificateDto certificateDto) {
        GiftCertificateDto resultDto = service.update(id, certificateDto);
        hateoasUtil.attacheCertificateLink(resultDto);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @PutMapping("/certificates/{id}/updateDuration")
    public ResponseEntity<GiftCertificateDto> updateDuration(@PathVariable long id, @RequestBody int duration) {
        GiftCertificateDto resultDto = service.updateCertificateDuration(id, duration);
        hateoasUtil.attacheCertificateLink(resultDto);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @PutMapping("/certificates/{id}/updatePrice")
    public ResponseEntity<GiftCertificateDto> updatePrice(@PathVariable long id, @RequestBody BigDecimal price) {
        GiftCertificateDto resultDto = service.updateCertificatePrice(id, price);
        hateoasUtil.attacheCertificateLink(resultDto);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }
}
