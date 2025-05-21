-- Drop the old FK constraint (use your actual constraint name)
ALTER TABLE challenge_participants
  DROP CONSTRAINT fkmaxbdxkphx8gcxws0wf9mo2ru;

-- Recreate it with ON DELETE CASCADE
ALTER TABLE challenge_participants
  ADD CONSTRAINT fk_challenge_participants_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE;
