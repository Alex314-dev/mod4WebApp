CREATE SCHEMA kickindatabase;

CREATE TABLE kickindatabase.activity (
    id integer DEFAULT 0 NOT NULL,
    day_id integer DEFAULT 0,
    event_id integer DEFAULT 0,
    name character varying(255)
);


CREATE TABLE kickindatabase.communication (
    id integer DEFAULT 0 NOT NULL,
    event_id integer DEFAULT 0,
    person_id integer DEFAULT 0,
    organisation_id integer DEFAULT 0,
    senderemail character varying(255),
    subject character varying(255),
    content text,
    priority integer DEFAULT 0,
    send_at timestamp without time zone,
    sender character varying(255)
);


CREATE TABLE kickindatabase.document_email (
    communication_id integer NOT NULL,
    document_id integer NOT NULL
);


CREATE TABLE kickindatabase.documents (
    document_id integer NOT NULL,
    title text,
    content text,
    type text
);


CREATE TABLE kickindatabase.event (
    id integer DEFAULT 0 NOT NULL,
    year integer DEFAULT 0
);


CREATE TABLE kickindatabase.organisation (
    id integer DEFAULT 0 NOT NULL,
    name character varying(255),
    parent_id integer DEFAULT 0
);



CREATE TABLE kickindatabase.organisationactivity (
    id integer DEFAULT 0 NOT NULL,
    organisation_id integer DEFAULT 0,
    activity_id integer DEFAULT 0,
    event_id integer DEFAULT 0,
    organisationperson_id integer DEFAULT 0
);


CREATE TABLE kickindatabase.organisationperson (
    id integer DEFAULT 0 NOT NULL,
    event_id integer DEFAULT 0,
    person_id integer DEFAULT 0,
    organisation_id integer DEFAULT 0
);


CREATE TABLE kickindatabase.person (
    id integer DEFAULT 0 NOT NULL,
    email character varying(255),
    name character varying(255)
);


CREATE TABLE kickindatabase.read_docs (
    document_id integer NOT NULL,
    person_id integer NOT NULL,
    date_time timestamp without time zone
);


CREATE TABLE kickindatabase.read_email (
    communication_id integer NOT NULL,
    person_id integer NOT NULL,
    date_time timestamp without time zone
);


ALTER TABLE ONLY kickindatabase.documents
    ADD CONSTRAINT "Documents_pkey" PRIMARY KEY (document_id);


ALTER TABLE ONLY kickindatabase.document_email
    ADD CONSTRAINT document_email_pkey PRIMARY KEY (communication_id, document_id);


ALTER TABLE ONLY kickindatabase.activity
    ADD CONSTRAINT pk_activity PRIMARY KEY (id);



ALTER TABLE ONLY kickindatabase.communication
    ADD CONSTRAINT pk_communication PRIMARY KEY (id);



ALTER TABLE ONLY kickindatabase.event
    ADD CONSTRAINT pk_event PRIMARY KEY (id);


ALTER TABLE ONLY kickindatabase.organisation
    ADD CONSTRAINT pk_organisation PRIMARY KEY (id);


ALTER TABLE ONLY kickindatabase.organisationactivity
    ADD CONSTRAINT pk_organisationactivity PRIMARY KEY (id);


ALTER TABLE ONLY kickindatabase.organisationperson
    ADD CONSTRAINT pk_organisationperson PRIMARY KEY (id);


ALTER TABLE ONLY kickindatabase.person
    ADD CONSTRAINT pk_person PRIMARY KEY (id);


ALTER TABLE ONLY kickindatabase.read_docs
    ADD CONSTRAINT read_docs_pkey PRIMARY KEY (document_id, person_id);


ALTER TABLE ONLY kickindatabase.read_email
    ADD CONSTRAINT read_email_pkey PRIMARY KEY (communication_id, person_id);


CREATE INDEX activityorganisationactivity ON kickindatabase.organisationactivity USING btree (activity_id);


CREATE INDEX eventactivity ON kickindatabase.activity USING btree (event_id);


CREATE INDEX eventcommunication ON kickindatabase.communication USING btree (event_id);


CREATE INDEX eventorganisationperson ON kickindatabase.organisationperson USING btree (event_id);


CREATE INDEX idx_organisation_id ON kickindatabase.organisationactivity USING btree (organisation_id);


CREATE INDEX organisationcommunication ON kickindatabase.communication USING btree (organisation_id);


CREATE INDEX organisationorganisationactivity ON kickindatabase.organisationactivity USING btree (organisation_id);


CREATE INDEX organisationorganisationperson ON kickindatabase.organisationperson USING btree (organisation_id);


CREATE INDEX personcommunication ON kickindatabase.communication USING btree (person_id);


CREATE INDEX personorganisationperson ON kickindatabase.organisationperson USING btree (person_id);


CREATE UNIQUE INDEX uq_tblreademail_documentid_personid ON kickindatabase.read_email USING btree (communication_id, person_id);


CREATE UNIQUE INDEX uq_tblreadrocs_documentid_personid ON kickindatabase.read_docs USING btree (document_id, person_id);


ALTER TABLE ONLY kickindatabase.organisationactivity
    ADD CONSTRAINT activityorganisationactivity FOREIGN KEY (activity_id) REFERENCES kickindatabase.activity(id) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE ONLY kickindatabase.document_email
    ADD CONSTRAINT communication_id FOREIGN KEY (communication_id) REFERENCES kickindatabase.communication(id);


ALTER TABLE ONLY kickindatabase.document_email
    ADD CONSTRAINT document_id FOREIGN KEY (document_id) REFERENCES kickindatabase.documents(document_id);


ALTER TABLE ONLY kickindatabase.activity
    ADD CONSTRAINT eventactivity FOREIGN KEY (event_id) REFERENCES kickindatabase.event(id) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE ONLY kickindatabase.communication
    ADD CONSTRAINT eventcommunication FOREIGN KEY (event_id) REFERENCES kickindatabase.event(id) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE ONLY kickindatabase.organisationperson
    ADD CONSTRAINT eventorganisationperson FOREIGN KEY (event_id) REFERENCES kickindatabase.event(id) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE ONLY kickindatabase.communication
    ADD CONSTRAINT organisationcommunication FOREIGN KEY (organisation_id) REFERENCES kickindatabase.organisation(id) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE ONLY kickindatabase.organisationactivity
    ADD CONSTRAINT organisationorganisationactivity FOREIGN KEY (organisation_id) REFERENCES kickindatabase.organisation(id) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE ONLY kickindatabase.organisationperson
    ADD CONSTRAINT organisationorganisationperson FOREIGN KEY (organisation_id) REFERENCES kickindatabase.organisation(id) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE ONLY kickindatabase.communication
    ADD CONSTRAINT personcommunication FOREIGN KEY (person_id) REFERENCES kickindatabase.person(id) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE ONLY kickindatabase.organisationperson
    ADD CONSTRAINT personorganisationperson FOREIGN KEY (person_id) REFERENCES kickindatabase.person(id) ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE ONLY kickindatabase.read_docs
    ADD CONSTRAINT read_docs_document_id_fkey FOREIGN KEY (document_id) REFERENCES kickindatabase.documents(document_id);


ALTER TABLE ONLY kickindatabase.read_docs
    ADD CONSTRAINT read_docs_person_id_fkey FOREIGN KEY (person_id) REFERENCES kickindatabase.person(id);


ALTER TABLE ONLY kickindatabase.read_email
    ADD CONSTRAINT read_email_communication_id_fkey FOREIGN KEY (communication_id) REFERENCES kickindatabase.communication(id);


ALTER TABLE ONLY kickindatabase.read_email
    ADD CONSTRAINT read_email_person_id_fkey FOREIGN KEY (person_id) REFERENCES kickindatabase.person(id);



