INSERT INTO tag
(
    id,
    name
)
VALUES
(
    1,
    'food'
),
(
    2,
    'clothes'
),
(
    3,
    'animals'
),
(
    4,
    'entertainment'
);
INSERT INTO gift_certificate
(
    id,
    name,
    description,
    price,
    duration,
    create_date,
    last_update_date
)
VALUES
(
    1,
    'Restaurant',
    'The best dishes',
    1000,
    14,
    '2005-08-09T18:31:42',
    '2005-08-09T18:31:42'
);

INSERT INTO gift_certificate_tag
(
    certificate_id,
    tag_id
)
VALUES
(
    1,
    1
)