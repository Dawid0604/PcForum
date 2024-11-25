CREATE TABLE SPRING_SESSION (
  PRIMARY_ID char(36) NOT NULL,
  SESSION_ID char(36) NOT NULL,
  CREATION_TIME bigint(20) NOT NULL,
  LAST_ACCESS_TIME bigint(20) NOT NULL,
  MAX_INACTIVE_INTERVAL int(11) NOT NULL,
  EXPIRY_TIME bigint(20) NOT NULL,
  PRINCIPAL_NAME varchar(100) NULL,

  PRIMARY KEY (PRIMARY_ID),
  UNIQUE KEY SPRING_SESSION_IX1 (SESSION_ID),
  KEY SPRING_SESSION_IX2 (EXPIRY_TIME),
  KEY SPRING_SESSION_IX3 (PRINCIPAL_NAME)
);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
  SESSION_PRIMARY_ID char(36) NOT NULL,
  ATTRIBUTE_NAME varchar(200) NOT NULL,
  ATTRIBUTE_BYTES blob NOT NULL,

  PRIMARY KEY (SESSION_PRIMARY_ID,ATTRIBUTE_NAME)
);

CREATE TABLE Users (
  Id bigint(20) NOT NULL AUTO_INCREMENT,
  EncryptedId varchar(128) NULL DEFAULT NULL,
  Username varchar(50) NOT NULL,
  Password varchar(100) NOT NULL,
  Role enum('USER', 'MODERATOR', 'ADMIN') NOT NULL,

  PRIMARY KEY (Id),
  UNIQUE (EncryptedId),
  UNIQUE (Username),
  KEY user_username_ids (Username)
);

CREATE TABLE UserProfileRanks (
  Id bigint(20) NOT NULL AUTO_INCREMENT,
  EncryptedId varchar(128) NULL DEFAULT NULL,
  Name varchar(64) NOT NULL,
  MinPoints mediumint(9) NOT NULL,
  MaxPoints mediumint(9) NOT NULL,

  PRIMARY KEY (Id),
  UNIQUE (EncryptedId),
  UNIQUE (Name)
);

CREATE TABLE UserProfiles (
  Id bigint(20) NOT NULL AUTO_INCREMENT,
  EncryptedId varchar(128) NULL DEFAULT NULL,
  Avatar varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  Background varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  Description longtext NULL,
  CreatedAt timestamp NOT NULL DEFAULT current_timestamp(),
  Nickname varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  UserId bigint(20) NOT NULL,
  LastActivity timestamp NULL NULL,
  IsOnline tinyint(1) NOT NULL DEFAULT 0,
  RankId bigint(20) NOT NULL,

  PRIMARY KEY (Id),
  UNIQUE (EncryptedId),
  UNIQUE (Nickname),
  FOREIGN KEY (UserId) REFERENCES Users(Id),
  FOREIGN KEY (RankId) REFERENCES UserProfileRanks(Id)
);

CREATE TABLE UserProfileObservations (
  Id bigint(20) NOT NULL AUTO_INCREMENT,
  EncryptedId varchar(128) NULL DEFAULT NULL,
  ProfileId bigint(20) NOT NULL,
  ObservedProfileId bigint(20) NOT NULL,
  ObservationDate timestamp NOT NULL DEFAULT current_timestamp(),

  PRIMARY KEY (Id),
  UNIQUE (EncryptedId),
  FOREIGN KEY (ProfileId) REFERENCES UserProfiles(Id),
  FOREIGN KEY (ObservedProfileId) REFERENCES UserProfiles(Id)
);

CREATE TABLE UserProfileVisitors (
  Id bigint(20) NOT NULL AUTO_INCREMENT,
  EncryptedId varchar(128) NULL DEFAULT NULL,
  ProfileId bigint(20) NOT NULL,
  VisitorProfileId bigint(20) NOT NULL,
  FirstVisitDate timestamp NOT NULL DEFAULT current_timestamp(),
  LastVisitDate timestamp NULL NULL,

  PRIMARY KEY (Id),
  UNIQUE (EncryptedId),
  FOREIGN KEY (ProfileId) REFERENCES UserProfiles(Id),
  FOREIGN KEY (VisitorProfileId) REFERENCES UserProfiles(Id)
);

CREATE TABLE ThreadCategories (
  Id bigint(20) NOT NULL AUTO_INCREMENT,
  EncryptedId varchar(128) NULL DEFAULT NULL,
  CategoryLevelPathOne int(11) NOT NULL,
  CategoryLevelPathTwo int(11) NULL,
  CategoryLevelPathThree int(11) NULL,
  Name varchar(64) NOT NULL,
  Description varchar(256) NULL,
  ThumbnailPath varchar(256) NULL,

  PRIMARY KEY (Id),
  UNIQUE (EncryptedId),
  KEY threads_category_path_idx (CategoryLevelPathOne,CategoryLevelPathTwo,CategoryLevelPathThree)
);

CREATE TABLE Threads (
  Id bigint(20) NOT NULL AUTO_INCREMENT,
  EncryptedId varchar(128) NULL DEFAULT NULL,
  Title varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  Content longtext NOT NULL,
  CreatedAt timestamp NOT NULL DEFAULT current_timestamp(),
  LastUpdatedAt timestamp NULL,
  LastActivity timestamp NULL DEFAULT current_timestamp(),
  IsClosed tinyint(1) NOT NULL DEFAULT 0,
  UserProfileId bigint(20) NOT NULL,
  CategoryLevelPathOne int(11) NOT NULL,
  CategoryLevelPathTwo int(11) NULL,
  CategoryLevelPathThree int(11) NULL,

  PRIMARY KEY (Id),
  UNIQUE (EncryptedId),
  FOREIGN KEY (UserProfileId) REFERENCES UserProfiles(Id),
  KEY thread_category_path_idx (CategoryLevelPathOne,CategoryLevelPathTwo,CategoryLevelPathThree),
  KEY thread_category_path_and_order_desc_idx (CategoryLevelPathOne,CategoryLevelPathTwo,CategoryLevelPathThree,CreatedAt DESC)
);

CREATE TABLE Posts (
  Id bigint(20) NOT NULL AUTO_INCREMENT,
  EncryptedId varchar(128) NULL DEFAULT NULL,
  ThreadId bigint(20) NOT NULL,
  UserProfileId bigint(20) NOT NULL,
  Content longtext NOT NULL,
  CreatedAt timestamp NOT NULL DEFAULT current_timestamp(),
  LastUpdatedAt timestamp NULL,

  PRIMARY KEY (Id),
  UNIQUE (EncryptedId),
  FOREIGN KEY (UserProfileId) REFERENCES UserProfiles(Id),
  FOREIGN KEY (ThreadId) REFERENCES Threads(Id),
  KEY post_thread_idx (ThreadId),
  KEY post_thread_created_at_desc_idx (ThreadId,CreatedAt DESC)
);

CREATE TABLE PostReactions (
  Id bigint(20) NOT NULL AUTO_INCREMENT,
  EncryptedId varchar(128) NULL DEFAULT NULL,
  CreatedAt timestamp NOT NULL DEFAULT current_timestamp(),
  PostId bigint(20) NOT NULL,
  UserProfileId bigint(20) NOT NULL,
  UpVote tinyint(1) NOT NULL,
  DownVote tinyint(1) NOT NULL,

  PRIMARY KEY (Id),
  UNIQUE (EncryptedId),
  FOREIGN KEY (UserProfileId) REFERENCES UserProfiles(Id),
  FOREIGN KEY (PostId) REFERENCES Posts(Id),
  KEY post_reaction_up_vote_user_idx (UpVote,UserProfileId),
  KEY post_reaction_down_vote_user_idx (DownVote,UserProfileId)
);