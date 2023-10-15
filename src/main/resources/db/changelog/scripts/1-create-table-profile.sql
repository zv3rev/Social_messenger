CREATE TABLE profile
(
    id bigserial NOT NULL,
    email text NOT NULL,
    password text NOT NULL,
    nickname text NOT NULL,
    first_name text NOT NULL,
    surname text NOT NULL,
    bio text,
    profile_status text NOT NULL DEFAULT 'UNCONFIRMED'::text,
    allowed_to_send text NOT NULL DEFAULT 'ALL'::text,
    is_friends_list_visible bool NOT NULL DEFAULT true,
    delete_date timestamp without time zone,

    CONSTRAINT profile_id_pk PRIMARY KEY (id),
    CONSTRAINT profile_email_u UNIQUE (email),
    CONSTRAINT profile_nickname_u UNIQUE (nickname)
)
