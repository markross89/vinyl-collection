
-- V1__init_schema.sql

-- USERS
CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       username VARCHAR(60) NOT NULL,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,

                       PRIMARY KEY (id),
                       CONSTRAINT uk_users_username UNIQUE (username)
) ENGINE=InnoDB;


-- ROLES
CREATE TABLE roles (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,

                       PRIMARY KEY (id),
                       CONSTRAINT uk_roles_name UNIQUE (name)
) ENGINE=InnoDB;


-- USER_ROLE
CREATE TABLE user_role (
                           user_id BIGINT NOT NULL,
                           role_id BIGINT NOT NULL,

                           PRIMARY KEY (user_id, role_id),

                           CONSTRAINT fk_user_role_user
                               FOREIGN KEY (user_id) REFERENCES users(id),

                           CONSTRAINT fk_user_role_role
                               FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB;


-- ALBUM
CREATE TABLE album (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       discogs_id BIGINT NOT NULL,
                       artist VARCHAR(255),
                       title VARCHAR(255),
                       uri VARCHAR(255),
                       label VARCHAR(255),
                       catno VARCHAR(255),
                       genre VARCHAR(255),
                       date DATE,

                       PRIMARY KEY (id),
                       CONSTRAINT uk_album_discogs_id UNIQUE (discogs_id)
) ENGINE=InnoDB;


-- ALBUM_USERS
CREATE TABLE album_users (
                             album_id BIGINT NOT NULL,
                             users_id BIGINT NOT NULL,

                             PRIMARY KEY (album_id, users_id),

                             CONSTRAINT fk_album_users_album
                                 FOREIGN KEY (album_id) REFERENCES album(id),

                             CONSTRAINT fk_album_users_user
                                 FOREIGN KEY (users_id) REFERENCES users(id)
) ENGINE=InnoDB;


-- TRACK
CREATE TABLE track (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       discogs_id BIGINT,
                       title VARCHAR(255),
                       position VARCHAR(255),
                       duration VARCHAR(255),
                       artist VARCHAR(255),
                       album VARCHAR(255),
                       label VARCHAR(255),
                       date DATE,
                       album_id BIGINT NOT NULL,

                       PRIMARY KEY (id),

                       CONSTRAINT fk_track_album
                           FOREIGN KEY (album_id) REFERENCES album(id)
) ENGINE=InnoDB;


-- IMAGE
CREATE TABLE image (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       type VARCHAR(255),
                       uri VARCHAR(255),
                       album_id BIGINT NOT NULL,

                       PRIMARY KEY (id),

                       CONSTRAINT fk_image_album
                           FOREIGN KEY (album_id) REFERENCES album(id)
) ENGINE=InnoDB;


-- BOX
CREATE TABLE box (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     name VARCHAR(255) NOT NULL,
                     date DATE,
                     user_id BIGINT NOT NULL,

                     PRIMARY KEY (id),

                     CONSTRAINT fk_box_user
                         FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;


-- BOX_ALBUMS
CREATE TABLE box_albums (
                            box_id BIGINT NOT NULL,
                            album_id BIGINT NOT NULL,

                            PRIMARY KEY (box_id, album_id),

                            CONSTRAINT fk_box_albums_box
                                FOREIGN KEY (box_id) REFERENCES box(id),

                            CONSTRAINT fk_box_albums_album
                                FOREIGN KEY (album_id) REFERENCES album(id)
) ENGINE=InnoDB;


-- TRACKLIST
CREATE TABLE tracklist (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           name VARCHAR(255) NOT NULL,
                           date DATE,
                           user_id BIGINT NOT NULL,

                           PRIMARY KEY (id),

                           CONSTRAINT fk_tracklist_user
                               FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;


-- TRACKLIST_TRACKS
CREATE TABLE tracklist_tracks (
                                  tracklist_id BIGINT NOT NULL,
                                  tracks_id BIGINT NOT NULL,
                                  track_order INT NOT NULL,

                                  PRIMARY KEY (tracklist_id, track_order),

                                  CONSTRAINT fk_tracklist_tracks_tracklist
                                      FOREIGN KEY (tracklist_id) REFERENCES tracklist(id),

                                  CONSTRAINT fk_tracklist_tracks_track
                                      FOREIGN KEY (tracks_id) REFERENCES track(id)
) ENGINE=InnoDB;
