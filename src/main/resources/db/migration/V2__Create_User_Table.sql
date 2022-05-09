create table if not exists site_user (
     id  bigserial not null
    ,email varchar(255)
    ,enabled boolean
    ,first_name varchar(255)
    ,last_name varchar(255)
    ,locked boolean, password varchar(255)
    ,user_role varchar(255)
    ,primary key (id)
)
