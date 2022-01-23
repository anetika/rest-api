package com.epam.esm.generation;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;

@SpringBootTest
public class GenerationUtil {
    @Autowired
    private TagDao tagDao;
    @Autowired
    private GiftCertificateDao certificateDao;
    @Autowired
    private UserDao userDao;

    private final Random random = new Random();

    //@Test
    public void generateTags(){
        for (int i = 0; i < 1000; i++) {
            Tag tag = new Tag();
            tag.setName("tag" + i);
            tagDao.save(tag);
        }
    }

    //@Test
    public void generateCertificates(){
        for (int i = 0; i < 10000; i++){
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setName("Certificate" + i);
            giftCertificate.setDescription("Certificate" + i + " description");
            giftCertificate.setDuration(random.nextInt(365));
            giftCertificate.setPrice(new BigDecimal(random.nextInt(1000)));
            giftCertificate.setCreateDate(LocalDateTime.now());
            giftCertificate.setLastUpdateDate(LocalDateTime.now());
            giftCertificate.setTags(new HashSet<>());
            for (int j = 0; j < 10; j++){
                int randomTagId = random.nextInt(1000) + 8;
                giftCertificate.getTags().add(tagDao.findById((long) randomTagId).get());
            }
            certificateDao.save(giftCertificate);
        }
    }

    //@Test
    public void generateUsers() {
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setEmail("user" + i + "@mail.ru");
            user.setFirstName("User" + i);
            user.setLastName("UserLastName" + i);
            userDao.save(user);
        }
    }
}
