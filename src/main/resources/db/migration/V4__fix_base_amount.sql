
-- 1) Back-fill existing NULLs
UPDATE challenge_reward_rules
  SET base_amount = 0.0
  WHERE base_amount IS NULL;

-- 2) Set the DEFAULT so future INSERTs without base_amount get 0.0
ALTER TABLE challenge_reward_rules
  ALTER COLUMN base_amount SET DEFAULT 0.0;

-- 3) Finally, disallow NULLs
ALTER TABLE challenge_reward_rules
  ALTER COLUMN base_amount SET NOT NULL;
