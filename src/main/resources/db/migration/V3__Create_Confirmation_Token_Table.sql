create table if not exists confirmation_token (
         id bigserial not null
        ,confirmed_at timestamp
        ,created_at timestamp
        ,expires_at timestamp
        ,token varchar(255)
        ,user_id int8
        ,primary key (id)
        ,CONSTRAINT fk_user
            FOREIGN KEY(user_id)
                REFERENCES site_user(id)
);
