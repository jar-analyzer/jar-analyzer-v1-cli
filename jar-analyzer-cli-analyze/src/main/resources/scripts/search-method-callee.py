# usage: --script search-method-callee --input [callee-method-class-name]|[callee-method-name] --output result.txt
# example: --input java/lang/Runtime|exec
from me.n1ar4.analyze import db_instance
from me.n1ar4.analyze import method_util

print("[python] script-input:  " + str(input))
print("[python] script-output: " + str(output))

param = str(input)
param = param.replace(".", "/")

temp = param.split("|")
callee_class_name = temp[0].strip()
callee_method_name = temp[1].strip()

print("[python] callee class name: " + callee_class_name)
print("[python] callee method name: " + callee_method_name)

callee_method_desc = None
if len(temp) > 2:
    callee_method_desc = temp[2].strip()
    print("[python] callee method desc: " + callee_method_desc)

db = db_instance()
sql = "SELECT caller_class_name, caller_method_name, caller_method_desc FROM method_call_table WHERE callee_class_name = '__input1__' AND callee_method_name = '__input2__'"
if callee_method_desc is not None:
    sql = sql + "AND callee_method_desc = '" + callee_method_desc + "'"
sql = sql.replace("__input1__", callee_class_name)
sql = sql.replace("__input2__", callee_method_name)
result = db.execute_sql(sql)

print("[python] script write output")
file = open(str(output), "w")
file.write("#" * 66 + "\n")
file.write("caller_class_name:" + callee_class_name + "   ")
file.write("caller_method_name:" + callee_method_name + "\n")
file.write("#" * 66 + "\n")
for item in list(result):
    i = dict(item)
    for k, v in i.items():
        if str(k) == 'caller_method_desc':
            value = method_util.desc_to_str(str(v))
            file.write(str(k) + ":" + value + "   ")
            file.write("caller_method_desc_native:" + str(v) + "   ")
        else:
            file.write(str(k) + ":" + str(v) + "   ")
    file.write("\n")
file.close()
print("[python] script run finish")
