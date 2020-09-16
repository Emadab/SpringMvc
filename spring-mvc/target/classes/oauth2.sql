create table oauth_approvals (
    userId varchar(256),
    clientId varchar(256),
    scope varchar(256),
    status varchar(10),
    expiresAt TIMESTAMP,
    lastModifiedAt timestamp
);

create table oauth_code (
    code varchar(256),
    authentication BLOB
);

create table oauth_refresh_token (
    token_id varchar(256),
    token BLOB,
    authentication BLOB
);

create table oauth_access_token (
    token_id varchar(256),
    token BLOB,
    authentication_id varchar(256) PRIMARY KEY,
    user_name varchar(256),
    client_id varchar(256),
    authentication BLOB,
    refresh_token varchar(256)
);

create table oauth_client_details (
    client_id varchar(256) PRIMARY KEY,
    resource_ids varchar(256),
    client_secret varchar(256),
    scope varchar(256),
    authorized_grant_types varchar(256),
    web_server_redirect_uri varchar(256),
    authorities varchar(256),
    access_token_validity INTEGER,
    refresh_token_validity INTEGER,
    additional_information varchar(1023),
    autoapprove varchar(256)
);

INSERT INTO
oauth_client_details (
    client_id,
    client_secret,
    scope,
    authorized_grant_types,
    authorities,
    access_token_validity,
    refresh_token_validity
)
VALUES
(
    'restapp',
    'restapp',
    'read,write,trust',
    'password,refresh_token',
    'ROLE_CLIENT,ROLE_TRUSTED_CLIENT,ROLE_APP',
    900000,
    2592000
);

INSERT INTO
oauth_client_details (
    client_id,
    client_secret,
    scope,
    authorized_grant_types,
    authorities,
    access_token_validity,
    refresh_token_validity
)
VALUES
(
    'adminapp',
    'adminapp',
    'read,write,trust',
    'password,refresh_token',
    'ROLE_CLIENT,ROLE_TRUSTED_CLIENT,ROLE_APP,AGENT_APP,ADMIN_APP,SUPPORTER_APP',
    900000,
    2592000
);

INSERT INTO
oauth_client_details (
    client_id,
    client_secret,
    scope,
    authorized_grant_types,
    authorities,
    access_token_validity,
    refresh_token_validity
)
VALUES
(
    'agentapp',
    'agentapp',
    'read,write,trust',
    'password,refresh_token',
    'ROLE_CLIENT,ROLE_TRUSTED_CLIENT,ROLE_APP,AGENT_APP',
    900000,
    2592000
);

INSERT INTO
oauth_client_details (
    client_id,
    client_secret,
    scope,
    authorized_grant_types,
    authorities,
    access_token_validity,
    refresh_token_validity
)
VALUES
(
    'supporterapp',
    'supporterapp',
    'read,write,trust',
    'password,refresh_token',
    'ROLE_CLIENT,ROLE_TRUSTED_CLIENT,ROLE_APP,AGENT_APP,SUPPORTER_APP',
    900000,
    2592000
);


commit ;