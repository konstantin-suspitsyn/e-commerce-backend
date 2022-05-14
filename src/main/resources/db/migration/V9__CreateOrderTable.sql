create table if not exists orders
(
     id bigserial not null
    ,creation_date timestamp
    ,expiration_date date
    ,order_status int4
    ,session varchar(255)
    ,total_items int8
    ,total_price int8
    ,update_date timestamp
    ,user_id int8
    ,primary key (id)
    ,CONSTRAINT user_id
        foreign key (user_id)
            references site_user(id)
)