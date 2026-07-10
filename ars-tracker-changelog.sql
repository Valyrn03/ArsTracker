-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE character (id INTEGER PRIMARY KEY ASC, name VARCHAR(50), campaign_id VARCHAR(36), birth_season INT, character_type INT, intelligence INT, perception INT, strength INT, stamina INT, presence INT, communication INT, dexterity INT, quickness INT)

-- changeset liquibase:2
CREATE TABLE ability_tracker (id INTEGER PRIMARY KEY ASC, player_id VARCHAR(36), ability_id VARCHAR(36), category_id VARCHAR(32), experience INT)

-- changeset liquibase:3
CREATE TABLE ability (id INTEGER PRIMARY KEY ASC, name VARCHAR(32), speciality VARCHAR(36), description VARCHAR (1024))

-- changeset liquibase:4
CREATE TABLE ability_category (name VARCHAR(32), overarchingType VARCHAR(16), isCategorical INT, PRIMARY KEY (name))

-- changeset liquibase:5
CREATE TABLE campaign (name VARCHAR(512), current_season INT, PRIMARY KEY (name))

-- changeset liquibase:add_book_table
CREATE TABLE book (id INTEGER PRIMARY KEY ASC, name VARCHAR(36), description VARCHAR(512), isSumma INT, level INT, quality INT, field VARCHAR(32))

-- changeset liquibase:add_feature_table
CREATE TABLE feature (id INTEGER PRIMARY KEY ASC, name VARCHAR(36), description VARCHAR(1024), isVirtue INT, isMajor INT)

-- changeset liquibase:add_applied_feature_table
CREATE TABLE applied_feature (player_id INTEGER, feature_id INTEGER, PRIMARY KEY (player_id, feature_id))

-- changeset liquibase:add_general_feature_rule_table
CREATE TABLE feature_rule (feature_id INTEGER, name VARCHAR(36), description VARCHAR(512), PRIMARY KEY (feature_id, name))

-- changeset liquibase:add_ability_feature_rule_table
CREATE TABLE ability_feature_rule (id INTEGER PRIMARY KEY ASC, feature_id VARCHAR(36), ability VARCHAR(36), value INT) --ability is an FK to ability_category

-- changeset liquibase:add_covenant_feature_table
CREATE TABLE covenant_feature (id INTEGER PRIMARY KEY ASC, name VARCHAR(36), description VARCHAR(512), isBoon INT, isMajor INT)

-- changeset liquibase:add_applied_covenant_feature_table
CREATE TABLE applied_covenant_feature (covenant_id INTEGER, feature_id INTEGER, PRIMARY KEY(covenant_id, feature_id))

-- changeset liquibase:add_owned_books_table
CREATE TABLE owned_book (covenant_id INTEGER, book_id INTEGER, PRIMARY KEY(covenant_id, book_id))

-- changeset liquibase:add_back_covenant_table
CREATE TABLE covenant (id INTEGER PRIMARY KEY ASC, name VARCHAR(64), tribunal VARCHAR(36), campaign_name VARCHAR(512), establishSeason INT, CrVis INT, InVis INT, MuVis INT, PeVis INT, ReVis INT, AnVis INT, AuVis INT, AqVis INT, CoVis INT, HeVis INT, IgVis INT, ImVis INT, MeVis INT, TeVis INT, ViVis INT)