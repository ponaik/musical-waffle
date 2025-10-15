SELECT e.name AS Employee
FROM Employee e
WHERE EXISTS (
    SELECT 1
    FROM Employee m
    WHERE e.managerId = m.id
      AND e.salary > m.salary
);