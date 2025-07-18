-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE character (id VARCHAR(36), name VARCHAR(50), campaign_id VARCHAR(36), birth_season INT, character_type INT, intelligence INT, perception INT, strength INT, stamina INT, presence INT, communication INT, dexterity INT, quickness INT, PRIMARY KEY (id))

-- changeset liquibase:2
CREATE TABLE ability_tracker (id VARCHAR(36), player_id VARCHAR(36), ability_id VARCHAR(36), category_id VARCHAR(32), experience INT, PRIMARY KEY (id))

-- changeset liquibase:3
CREATE TABLE ability (id VARCHAR(36), name VARCHAR(32), description VARCHAR (1024), PRIMARY KEY (id))

-- changeset liquibase:4
CREATE TABLE ability_category (name VARCHAR(32), overarchingType INT, PRIMARY KEY (name))

-- changeset liquibase:5
CREATE TABLE campaign (id VARCHAR(36), name VARCHAR(512), current_season INT, PRIMARY KEY (id))