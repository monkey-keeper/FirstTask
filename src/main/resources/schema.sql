CREATE TABLE IF NOT EXISTS bankAccount (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    balance decimal CHECK (balance > 0)
);

CREATE TABLE IF NOT EXISTS userAccount (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    firstName varchar(30) NOT NULL,
    middleName varchar(30) NOT NULL,
    lastName varchar(30) NOT NULL,
    birthDate timestamp with time zone
);

CREATE TABLE IF NOT EXISTS transaction (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    value bigint CHECK (value > 0),
    type varchar(10) NOT NULL,
    category varchar(30) NOT NULL,
    createDate timestamp with time zone,
    bankAccount_id bigint REFERENCES bankAccount(id)
);

CREATE TABLE IF NOT EXISTS user_bankAccount (
    user_id bigint REFERENCES userAccount(id),
    bankAccount_id bigint REFERENCES bankAccount(id)
);
