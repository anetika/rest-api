package com.epam.esm.configuration;

import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.converter.TagConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan("com.epam.esm")
@Profile("prod")
public class ServiceConfiguration {

    @Bean
    public TagConverter tagConverter(){
        return TagConverter.getInstance();
    }

    @Bean
    public GiftCertificateConverter giftCertificateConverter() {return GiftCertificateConverter.getInstance();}

}
