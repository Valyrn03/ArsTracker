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

-- changeset liquibase:ability_category_drop_int_type
ALTER TABLE ability_category DROP overarchingType

-- changeset liquibase:ability_category_add_string_type
ALTER TABLE ability_category ADD overarchingType VARCHAR(16)

-- changeset liquibase:ability_category_add_if_categorical
ALTER TABLE ability_category ADD isCategorical INT

-- changeset liquibase:ability_add_specialities
ALTER TABLE ability ADD speciality VARCHAR(36)

-- changeset liquibase:add_book_table
CREATE TABLE book (id VARCHAR(36), name VARCHAR(36), description VARCHAR(512), isSumma INT, level INT, quality INT, field VARCHAR(32), PRIMARY KEY (id))

-- changeset liquibase:add_feature_table
CREATE TABLE feature (id VARCHAR(36), name VARCHAR(36), description VARCHAR(1024), isVirtue INT, isMajor INT, PRIMARY KEY (id))

-- changeset liquibase:add_applied_feature_table
CREATE TABLE applied_feature (player_id VARCHAR(36), feature_id VARCHAR(36), PRIMARY KEY (player_id, feature_id))

-- changeset liquibase:add_general_feature_rule_table
CREATE TABLE feature_rule (feature_id VARCHAR(36), name VARCHAR(36), description VARCHAR(512), PRIMARY KEY (feature_id, name))

-- changeset liquibase:add_ability_feature_rule_table
CREATE TABLE ability_feature_rule (id VARCHAR(36), feature_id VARCHAR(36), ability VARCHAR(36), value INT, PRIMARY KEY (id)) --ability is an FK to ability_category

-- changeset liquibase:add_covenant_feature_table
CREATE TABLE covenant_feature (id VARCHAR(36), name VARCHAR(36), description VARCHAR(512), isBoon INT, isMajor INT, PRIMARY KEY (id))