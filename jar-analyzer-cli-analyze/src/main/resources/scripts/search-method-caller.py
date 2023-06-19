# usage: --script search-method-caller --input [caller-method-class-name]|[caller-method-name]|[optional-desc] --output result.txt
# example: --input java/util/HashMap|get|(Ljava/lang/Object;)Ljava/lang/Object;
from me.n1ar4.analyze import db_instance
from me.n1ar4.analyze import method_util

print("[python] script-input:  " + str(input))
print("[python] script-output: " + str(output))

param = str(input)
param = param.replace(".", "/")

temp = param.split("|")
caller_class_name = temp[0].strip()
caller_method_name = temp[1].strip()

print("[python] caller class name: " + caller_class_name)
print("[python] caller method name: " + caller_method_name)

caller_method_desc = None
if len(temp) > 2:
    caller_method_desc = temp[2].strip()
    print("[python] caller method desc: " + caller_method_desc)

db = db_instance()
sql = "SELECT callee_class_name, callee_method_name, callee_method_desc FROM method_call_table WHERE caller_class_name = '__input1__' AND caller_method_name = '__input2__'"
if caller_method_desc is not None:
    sql = sql + "AND caller_method_desc = '" + caller_method_desc + "'"
sql = sql.replace("__input1__", caller_class_name)
sql = sql.replace("__input2__", caller_method_name)
result = db.execute_sql(sql)

print("[python] script write output")
file = open(str(output), "w")
file.write("#" * 66 + "\n")
file.write("caller_class_name:" + caller_class_name + "   ")
file.write("caller_method_name:" + caller_method_name + "\n")
file.write("#" * 66 + "\n")
for item in list(result):
    i = dict(item)
    for k, v in i.items():
        if str(k) == 'callee_method_desc':
            value = method_util.desc_to_str(str(v))
            file.write(str(k) + ":" + value + "   ")
            file.write("callee_method_desc_native:" + str(v) + "   ")
        else:
            file.write(str(k) + ":" + str(v) + "   ")
    file.write("\n")
file.close()
print("[python] script run finish")
