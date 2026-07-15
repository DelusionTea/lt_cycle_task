public static FeederBuilder<Object> taskIds = jdbcFeeder(

"jdbc:postgresql://tsled-pprb00544.esrt.sber.ru:5433/cmplnt",

"cmpladmin",

"}D.hL>X4yBQ9n7HOME'Qq2C,CfA}",

"SELECT compliance_request_id, ucp_id, id as taskid FROM cmpl.compliance_task limit 1000")

.circular();





public static FeederBuilder<Object> ucpIds = jdbcFeeder(

"jdbc:postgresql://tsled-pprb00544.esrt.sber.ru:5433/cmplnt",

"cmpladmin",

"}D.hL>X4yBQ9n7HOME'Qq2C,CfA}",

"SELECT ucp_id as ucp_prof FROM cmpl.compliance_organization limit 1000;")

.circular();