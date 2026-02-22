/*
 * Define the structure of your table
 * Since the code generator doesn't know what your table
 * needs to contain, some sample SQL is included:

 * create sequence if not exists customer_seq start 1 increment 1;

 * create table if not exists myschema.customer
 (
    id serial primary key,
    resource_id varchar(60) not null unique,
    first_name varchar(32) not  null,
    last_name varchar(32) not null,
    last_modified timestamptz default current_timestamp,
    created_on timestamptz default current_timestamp
)

*/
