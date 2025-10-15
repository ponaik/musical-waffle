SELECT COALESCE(
    (SELECT salary
    FROM (
        SELECT salary,
           ROW_NUMBER() OVER (ORDER BY salary DESC) AS rn
        FROM (SELECT DISTINCT salary FROM Employee) s
    ) sub
    WHERE rn = 2),
    NULL
) AS "SecondHighestSalary";