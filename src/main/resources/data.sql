INSERT INTO loan_products (min_amount, max_amount, min_tenure_months, max_tenure_months, default_interest_rate, penalty_type, penalty_value, is_active)
VALUES
(10000.00, 500000.00, 6, 60, 10.50, 'PERCENT', 2.00, true),
(50000.00, 2000000.00, 12, 120, 8.75, 'FIXED', 500.00, true);

INSERT INTO eligibility_rules (rule_name, rule_key, threshold_value, score_points, is_active)
VALUES
('Income to Loan Ratio Check', 'INCOME_TO_LOAN', 3.00, 50, true),
('Liability to Income Ratio Check', 'LIABILITY_RATIO', 0.40, 30, true);