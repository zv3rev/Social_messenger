CREATE TABLE friendship
(
    id bigint NOT NULL,
    sender_id bigint NOT NULL,
    recipient_id bigint NOT NULL,
    request_date timestamp without time zone NOT NULL,
    approved_date timestamp without time zone,
    denied_date timestamp without time zone,

    CONSTRAINT friendship_id_pk PRIMARY KEY (id),
    CONSTRAINT friendship_sender_id_fk FOREIGN KEY (sender_id)
        REFERENCES public.profile (id) MATCH SIMPLE,
    CONSTRAINT friendship_recipient_id_fk FOREIGN KEY (recipient_id)
        REFERENCES public.profile (id) MATCH SIMPLE
)