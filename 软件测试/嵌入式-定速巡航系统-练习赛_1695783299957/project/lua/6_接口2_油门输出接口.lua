-- 程序：Output_gas
require("e_checker")

-- lua程序入口函数
function entry()
	-- 此处输入程序代码
	local res = write_msg(channels.Speed, protocols.P_speed,{speed=50})
	res = read_buff(channels.gas, 0, 1000)
	local data = res.value
	local pkg = unpack(protocols.P_gas, ebuff.from_buff(data), true)
	check(pkg.value.header == 0xFF55, "油门输出包包头字段正确", "油门输出包包头字段错误")
	check(pkg.value.cmdType == 0x5, "油门输出包指令类型字段正确", "油门输出包指令类型字段错误")
	check(pkg.value.dataLen == 0x4, "油门输出包数据长度字段正确", "油门输出包数据长度字段错误")
	local checkVal = e_checker.SUM_8(ebuff.from_buff(data),3,48)
	check(pkg.value.checknum == checkVal, "油门输出包校验字段正确", "油门输出包校验字段错误")
	check(pkg.value.tail == 0xFF55, "油门输出包包尾字段正确", "油门输出包包尾字段错误")
	exit()
end
