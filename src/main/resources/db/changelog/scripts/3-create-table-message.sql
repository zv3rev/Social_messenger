CREATE TABLE message
(
    id bigserial NOT NULL,
    sender_id bigint NOT NULL,
    recipient_id bigint NOT NULL,
    send_time timestamp without time zone NOT NULL,
    message_text text NOT NULL,

    CONSTRAINT message_pk PRIMARY KEY (id),
    CONSTRAINT message_sender_recipient_time_u UNIQUE (sender_id, recipient_id, send_time),
    CONSTRAINT message_recipient_id_fk FOREIGN KEY (recipient_id)
        REFERENCES public.profile (id) MATCH SIMPLE,
    CONSTRAINT message_sender_id_fk FOREIGN KEY (sender_id)
        REFERENCES public.profile (id) MATCH SIMPLE
)