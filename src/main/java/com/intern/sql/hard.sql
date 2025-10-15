SELECT request_at AS "Day",
       ROUND(cancelled::numeric / NULLIF(completed + cancelled, 0), 2) AS "Cancellation Rate"
FROM (
    SELECT t.request_at,
           COUNT(*) FILTER (WHERE status = 'completed') AS completed,
           COUNT(*) FILTER (WHERE status IN ('cancelled_by_driver','cancelled_by_client')) AS cancelled
    FROM Trips t
    INNER JOIN Users clients ON t.client_id = clients.users_id
    INNER JOIN Users drivers ON t.driver_id = drivers.users_id
    WHERE clients.banned = 'No'
        AND drivers.banned = 'No'
        AND t.request_at IN ('2013-10-01', '2013-10-02', '2013-10-03')
    GROUP BY t.request_at
) sub;