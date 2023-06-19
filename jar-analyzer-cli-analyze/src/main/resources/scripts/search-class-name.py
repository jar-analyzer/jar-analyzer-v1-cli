# usage: --script search-class-name --input [class-name] --output result.txt
# example: --input java/lang/String
from me.n1ar4.analyze import db_instance

print("[python] script-input:  " + str(input))
print("[python] script-output: " + str(output))

param = str(input)
param = param.replace(".", "/")

db = db_instance()
sql = "SELECT class_name,super_class_name,is_interface,jar_name FROM class_table WHERE class_name = '__input__'"
sql = sql.replace("__input__", param)
result = db.execute_sql(sql)

print("[python] script write output")
file = open(str(output), "w")
for item in list(result):
    i = dict(item)
    for k, v in i.items():
        if str(k) == 'is_interface':
            value = None
            if v == 0:
                value = "false"
            else:
                value = "true"
            file.write(str(k) + ":" + value + "   ")
        else:
            file.write(str(k) + ":" + str(v) + "   ")
    file.write("\n")
file.close()
print("[python] script run finish")
