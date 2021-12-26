CREATE TABLE gift_certificate (
                                    id SERIAL,
                                    name varchar(45) NOT NULL,
                                    description varchar(200) NOT NULL,
                                    price decimal(10,0) NOT NULL,
                                    duration int NOT NULL,
                                    create_date datetime NOT NULL,
                                    last_update_date datetime NOT NULL,
                                    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE tag (
                       id INT NOT NULL AUTO_INCREMENT,
                       name varchar(45) NOT NULL,
                       PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE gift_certificate_tag (
                                        id int NOT NULL AUTO_INCREMENT,
                                        certificate_id int NOT NULL,
                                        tag_id int NOT NULL,
                                        PRIMARY KEY (id),
                                        CONSTRAINT `gift_certificate` FOREIGN KEY (certificate_id) REFERENCES gift_certificate (id) ON DELETE CASCADE ON UPDATE CASCADE,
                                        CONSTRAINT tag FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
