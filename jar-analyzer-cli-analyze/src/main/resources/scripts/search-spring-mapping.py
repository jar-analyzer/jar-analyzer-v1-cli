# usage: --script search-spring-mapping --input none --output result.txt
# example: --input none
from me.n1ar4.analyze import db_instance

print("[python] script-input:  " + str(input))
print("[python] script-output: " + str(output))

param = str(input)
param = param.replace(".", "/")

db = db_instance()
sql = "SELECT anno_name, method_name, class_name FROM anno_table WHERE (anno_name LIKE '%org/springframework/web/bind/annotation/GetMapping%' OR anno_name LIKE '%org/springframework/web/bind/annotation/PostMapping%' OR anno_name LIKE '%org/springframework/web/bind/annotation/RequestMapping%') AND class_name NOT LIKE '%org/springframework%'"
result = db.execute_sql(sql)

print("[python] script write output")
file = open(str(output), "w")
for item in list(result):
    i = dict(item)
    for k, v in i.items():
        file.write(str(k) + ":" + str(v) + "   ")
    file.write("\n")
file.close()
print("[python] script run finish")
