create table if not exists product (

     id bigserial not null
    ,active boolean
    ,date_created timestamp
    ,description varchar(255)
    ,image_url varchar(255)
    ,last_updated timestamp
    ,short_name varchar(255)
    ,sku varchar(255)
    ,unit_price int8
    ,units_in_active_stock int8
    ,units_in_stock int8
    ,category_id int8 not null
    ,primary key (id)
    ,CONSTRAINT fk_category
                FOREIGN KEY(category_id)
                    REFERENCES product_category(id)

    )