# usage: --script search-class-member --input [member-name]|[member-type] --output result.txt
# example: --input *|java/lang/String
# example: --input name|*
# example: --input name|java/lang/String
import sys
from me.n1ar4.analyze import db_instance
from me.n1ar4.analyze import member_util

print("[python] script-input:  " + str(input))
print("[python] script-output: " + str(output))

param = str(input)
param = param.replace(".", "/")

member_name = param.split("|")[0].strip()
member_type = param.split("|")[1].strip()

print("[python] member name: " + member_name)
print("[python] member type: " + member_type)

db = db_instance()
sql = "SELECT class_name, member_name, type_class_name, modifiers FROM member_table WHERE "
if member_name == "*" and member_type == "*":
    print("[python] member name and member type are *")
    sys.exit(0)
if member_name != "*":
    sql = sql + "member_name = '" + member_name + "' AND "
if member_type != "*":
    sql = sql + "type_class_name = '" + member_type + "' AND "
sql = sql.strip()
if sql.endswith("AND"):
    sql = sql.rstrip("AND")
result = db.execute_sql(sql)

print("[python] script write output")
file = open(str(output), "w")
for item in list(result):
    i = dict(item)
    for k, v in i.items():
        if str(k) == 'modifiers':
            value = member_util.access_to_str(int(v))
            file.write(str(k) + ":" + value + "   ")
            file.write("modifiers_int:" + str(v) + "   ")
        else:
            file.write(str(k) + ":" + str(v) + "   ")
    file.write("\n")
file.close()
print("[python] script run finish")
