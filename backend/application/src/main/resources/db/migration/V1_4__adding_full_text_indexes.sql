ALTER TABLE Threads ADD FULLTEXT INDEX IF NOT EXISTS idx_fulltext_title_content(Title, Content);
ALTER TABLE Posts ADD FULLTEXT INDEX IF NOT EXISTS idx_fulltext_content(Content);
ALTER TABLE UserProfiles ADD FULLTEXT INDEX IF NOT EXISTS idx_fulltext_nickname(Nickname);