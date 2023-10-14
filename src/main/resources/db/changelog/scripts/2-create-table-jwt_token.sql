CREATE TABLE jwt_token
(
    id bigserial NOT NULL,
    profile_id bigint NOT NULL,
    token text NOT NULL,

    CONSTRAINT jwt_token_pk PRIMARY KEY (id),
    CONSTRAINT jwt_token_profile_id_fk FOREIGN KEY (profile_id)
        REFERENCES public.profile (id) MATCH SIMPLE
)
