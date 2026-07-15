-- ivr: ck_request_id + ucp_sfl_id для POST body
SELECT
    r.ck_request_id AS "ivrSourceId",
    ('[' || quote_literal(r.ucp_sfl_id::text) || ']') AS "ivrUcpIds"
FROM cmpl.individual_request r
WHERE r.ck_request_id IS NOT NULL
LIMIT ${limit}