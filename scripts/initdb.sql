## initialize the database

## STILL NEED TO ADD SOME INDEXES!!

## this script will first clean up the previous database ********

## clear previous database
drop database if exists stockAnalyzer;

## create database
create database stockAnalyzer;

## use database
use stockAnalyzer;

-- stock category table
create table stock_category (
  id	int unsigned not null auto_increment,
  name varchar(255) not null,

  primary key (id),
  UNIQUE key (name)
);

create table stock_exchange (
  id	int unsigned not null auto_increment,
  name varchar(255) not null,

  primary key (id),
  UNIQUE key (name)
);

-- stock info table
create table stock_info (
  id	int unsigned not null auto_increment,
  symbol  varchar(255) not null,
  company_name varchar(255) not null,

  category_id int unsigned not null, foreign key(category_id) references stock_category(id),
  exchange_id int unsigned not null, foreign key(exchange_id) references stock_exchange(id),
  primary key (id),
  UNIQUE key (symbol)
);




