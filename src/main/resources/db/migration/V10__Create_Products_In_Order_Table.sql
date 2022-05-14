	create table if not exists products_in_order
(
      id bigserial not null
     ,order_quantity int8
     ,price int8
     ,order_id int8
     ,product_id int8
     ,primary key (id)
     ,constraint  fk_order
        foreign key (order_id) references orders(id)
     ,constraint  fk_product
        foreign key (product_id) references product
 )